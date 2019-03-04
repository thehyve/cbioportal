#!/usr/bin/env bash

set -e # fail when error occurs
set -u # unset variables throw error
set -o pipefail # pipes fail when partial command fails
# shopt -s failglob # empty globs throw error

CURRENT_DIR=$PWD
TEST_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )" # get location of this script file
cd $TEST_HOME

# exit when no screenshots have been configured
SCREENSHOTS= python3 -c "import yaml;print(yaml.load(open('screenshots.yaml'))['screenshot']['names'])"
if [[ "$SCREENSHOTS" == "None" ]]; then
    echo No screenshots configured for local database end-to-end screenshot tests. Exiting...
    exit
fi

mkdir -p /tmp/mysql_end-to-end_test

MYSQL_DATA_DIR=/tmp/mysql_end-to-end_test
MYSQL_HOST_NAME="cbiodb"
MYSQL_USER_NAME="cbio_user"
MYSQL_PASSWORD="cbio_pass"
MYSQL_DATABASE="endtoend_local_cbiodb"

curl https://raw.githubusercontent.com/cBioPortal/cbioportal/v2.0.0/db-scripts/src/main/resources/cgds.sql > cgds.sql
curl https://raw.githubusercontent.com/cBioPortal/datahub/master/seedDB/seed-cbioportal_hg19_v2.7.3.sql.gz > seed.sql.gz

docker network create endtoendlocaldb_default || true

# create local database container with cbioportal db and seed data
docker run -d \
    --name=$MYSQL_HOST_NAME \
    --net=endtoendlocaldb_default \
    -e MYSQL_ROOT_PASSWORD=$MYSQL_USER_NAME \
    -e MYSQL_USER=$MYSQL_USER_NAME \
    -e MYSQL_PASSWORD=$MYSQL_PASSWORD \
    -e MYSQL_DATABASE=$MYSQL_DATABASE \
    -p 127.0.0.1:3306:3306 \
    -v "$MYSQL_DATA_DIR:/var/lib/mysql/" \
    -v "$PORTAL_HOME/test/end-to-end_local_db/cgds.sql:/docker-entrypoint-initdb.d/cgds.sql:ro" \
    -v "$PORTAL_HOME/test/end-to-end_local_db/seed.sql.gz:/docker-entrypoint-initdb.d/seed_part1.sql.gz:ro" \
    mysql:5.7

while ! docker run --rm --net=endtoendlocaldb_default mysql:5.7 mysqladmin ping -u $MYSQL_USER_NAME -p$MYSQL_PASSWORD -h$MYSQL_HOST_NAME --silent; do
    echo Waiting for cbioportal database to initialize...
    sleep 10
done

docker stop $MYSQL_HOST_NAME && docker rm $MYSQL_HOST_NAME

echo Building docker containers ...
docker-compose build

# Note: --force-recreate flag is needed for selenium browser nodes to start correctly
# (see: https://github.com/SeleniumHQ/docker-selenium/issues/91#issuecomment-167519978)
docker-compose up -d --force-recreate

# migrate database schema to most recent version
echo Migrating database schema to most recent version ...
docker-compose run --rm \
    -v "$PORTAL_HOME/test/end-to-end_local_db/runtime-config/portal.properties:/cbioportal/portal.properties:ro" \
    cbioportal \
    python3 /cbioportal/core/src/main/scripts/migrate_db.py -y -p /cbioportal/portal.properties -s /cbioportal/db-scripts/src/main/resources/migration.sql

# sleep 5s

# import all test studies into local database
for DIR in $PORTAL_HOME/test/end-to-end_local_db/studies/*/ ; do
    docker-compose run --rm \
        --name=cbioportal-importer \
        -v "$PORTAL_HOME/test/end-to-end_local_db/runtime-config/portal.properties:/cbioportal/portal.properties:ro" \
        -v "$DIR:/study:ro" \
        -v "$DIR:/outdir" \
        cbioportal \
        python3 /cbioportal/core/src/main/scripts/importer/metaImport.py \
        --url_server http://cbioportal:8080 \
        --study_directory /study \
        --override_warning
done

# # spot visual regression by comparing screenshots in the repo with
# # screenshots of this portal loaded with the data from the amazon db
cd $PORTAL_HOME
bash test/end-to-end_local_db/test_make_screenshots.sh test/end-to-end_local_db/screenshots.yml

# stop cbioportal and selenium grid containers
cd $TEST_HOME
docker-compose stop

# return to working directory
cd $CURRENT_DIR