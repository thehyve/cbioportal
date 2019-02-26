#!/usr/bin/env bash

set -e # fail when error occurs
set -u # unset variables throw error
set -o pipefail # pipes fail when partial command fails
shopt -s failglob # empty globs throw error

export PORTAL_HOME=$PWD

cd test/end-to-end_local_db
curl https://raw.githubusercontent.com/cBioPortal/cbioportal/v2.0.0/db-scripts/src/main/resources/cgds.sql > cgds.sql
curl https://raw.githubusercontent.com/cBioPortal/datahub/master/seedDB/seed-cbioportal_hg19_v2.7.3.sql.gz > seed.sql.gz

# create local database container with cbioportal db and seed data
docker network create endtoendlocaldb_default
docker run -d \
    --name=cbiodb_temp \
    --net=endtoendlocaldb_default \
    -e MYSQL_ROOT_PASSWORD=cbio_pass \
    -e MYSQL_USER=cbio_user \
    -e MYSQL_PASSWORD=cbio_pass \
    -e MYSQL_DATABASE=endtoend_local_cbiodb \
    -p 127.0.0.1:3306:3306 \
    -v "$PWD/mysql_data:/var/lib/mysql/" \
    -v "$PWD/cgds.sql:/docker-entrypoint-initdb.d/cgds.sql:ro" \
    -v "$PWD/seed.sql.gz:/docker-entrypoint-initdb.d/seed_part1.sql.gz:ro" \
    mysql:5.7

while ! docker run --rm --net=end-to-end_local_db mysql:5.7 mysqladmin ping -u cbio_user -pcbio_pass -h cbiodb --silent; do
echo Waiting for cbioportal database to initialize ...
sleep 10
done

echo Building docker containers ...
docker-compose build

# migrate database schema to most recent version
echo Migrating database schema to most recent version ...
docker-compose run --rm -d cbioportal \
-v "$PWD/runtime-config/portal.properties:/cbioportal/portal.properties:ro" \
migrate_db.py -p /cbioportal/portal.properties -s /cbioportal/db-scripts/src/main/resources/migration.sql

sleep 30s

docker stop cbiodb_temp
docker-compose up -d

# import all test studies into local database
for DIR in studies/*/ ; do
    docker-compose run --rm \
        -v "$DIR:/study:ro" \
        -v "$PWD/runtime-config/portal.properties:/cbioportal/portal.properties:ro" \
        -v "$DIR:/outdir" \
        cbioportal \
        python3 /cbioportal/core/src/main/scripts/importer/metaImport.py \
        --url_server http://cbioportal:8080 \
        --study_directory /study \
        --override_warning
done

# docker network rm end-to-end_local_db

# sleep 30s
cd $PORTAL_HOME
# spot visual regression by comparing screenshots in the repo with
# screenshots of this portal loaded with the data from the amazon db
bash test/end-to-end_local_db/test_make_screenshots.sh test/end-to-end_local_db/screenshots.yml