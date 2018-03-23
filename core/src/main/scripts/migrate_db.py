#!/usr/bin/env python2.7

# imports
import os
import sys
import MySQLdb
import argparse
from collections import OrderedDict

# Globals

ERROR_FILE = sys.stderr
OUTPUT_FILE = sys.stdout

DATABASE_HOST = 'db.host'
DATABASE_NAME = 'db.portal_db_name'
DATABASE_USER = 'db.user'
DATABASE_PW = 'db.password'
VERSION_TABLE = 'info'
VERSION_FIELD = 'DB_SCHEMA_VERSION'

class PortalProperties(object):
    """ Properties object class, just has fields for db conn """

    def __init__(self, database_host, database_name, database_user, database_pw):
        # default port:
        self.database_port = 3306
        # if there is a port added to the host name, split and use this one:
        if ':' in database_host:
            host_and_port = database_host.split(':')
            self.database_host = host_and_port[0]
            if self.database_host.strip() == 'localhost':
                print >> ERROR_FILE, "Invalid host config '" + database_host + "' in properties file. If you want to specify a port on local host use '127.0.0.1' instead of 'localhost'"
                sys.exit(1)
            self.database_port = int(host_and_port[1])
        else:
            self.database_host = database_host
        self.database_name = database_name
        self.database_user = database_user
        self.database_pw = database_pw

def get_db_cursor(portal_properties):
    """ Establishes a MySQL connection """

    try:
        
        connection = MySQLdb.connect(host=portal_properties.database_host, 
            port = portal_properties.database_port, 
            user = portal_properties.database_user,
            passwd = portal_properties.database_pw,
            db = portal_properties.database_name)
    except MySQLdb.Error, msg:
        print >> ERROR_FILE, msg
        port_info = ''
        if portal_properties.database_host.strip() != 'localhost':
            # only add port info if host is != localhost (since with localhost apparently sockets are used and not the given port) TODO - perhaps this applies for all names vs ips?
            port_info = " on port " + str(portal_properties.database_port)
        print >> ERROR_FILE, "--> Error connecting to server " + portal_properties.database_host + port_info
        return None

    if connection is not None:
        return connection, connection.cursor()

def get_portal_properties(properties_filename):
    """ Returns a properties object """
    
    properties = {}
    properties_file = open(properties_filename, 'r')

    for line in properties_file:
        line = line.strip()

        # skip line if its blank or a comment
        if len(line) == 0 or line.startswith('#'):
            continue
        
        # store name/value
        property = line.split('=')
        if len(property) < 2:
            print >> ERROR_FILE, 'Skipping invalid entry in property file: ' + line
            continue
        properties[property[0]] = property[1].strip()
    properties_file.close()

    if (DATABASE_HOST not in properties or len(properties[DATABASE_HOST]) == 0 or
        DATABASE_NAME not in properties or len(properties[DATABASE_NAME]) == 0 or
        DATABASE_USER not in properties or len(properties[DATABASE_USER]) == 0 or
        DATABASE_PW not in properties or len(properties[DATABASE_PW]) == 0):
        print >> ERROR_FILE, 'Missing one or more required properties, please check property file'
        return None
    
    # return an instance of PortalProperties
    return PortalProperties(properties[DATABASE_HOST],
                            properties[DATABASE_NAME],
                            properties[DATABASE_USER],
                            properties[DATABASE_PW])

def get_db_version(cursor):
    """ gets the version number of the database """

    # First, see if the version table exists
    version_table_exists = False
    try:
        cursor.execute('select table_name from information_schema.tables')
        for row in cursor.fetchall():
            if VERSION_TABLE == row[0].lower().strip():
                version_table_exists = True
    except MySQLdb.Error, msg:
        print >> ERROR_FILE, msg
        return None
    
    if not version_table_exists:
        return (0,0,0)

    # Now query the table for the version number
    try:
        cursor.execute('select ' + VERSION_FIELD + ' from ' + VERSION_TABLE)
        for row in cursor.fetchall():
            version = tuple(map(int, row[0].strip().split('.')))
    except MySQLdb.Error, msg:
        print >> ERROR_FILE, msg
        return None

    return version

def is_version_larger(version1, version2):
    """ Checks if version 1 is larger than version 2 """

    if version1[0] > version2[0]:
        return True
    if version2[0] > version1[0]:
        return False
    if version1[1] > version2[1]:
        return True
    if version2[1] > version1[1]:
        return False
    if version1[2] > version2[2]:
        return True
    return False

def check_database_empty(cursor):
    sql_statement = 'SELECT count(*) FROM cancer_study;'
    try:
        cursor.execute(sql_statement)
    except MySQLdb.Error, msg:
        print >> ERROR_FILE, msg
        sys.exit(1)
    number_of_studies = int(cursor.fetchall()[0][0])
    if number_of_studies == 0:
        return True
    else:
        return False


def ask_reference_genome():
    selection_message = ("\nPlease enter a reference genome or enter 'n' to abort:\n"
                         'hg19 (default)\n'
                         'hg38 (only use this when using a database with hg38 gene lengths and cytobands)\n'
                         'mm10 (only use this when using a database with mm10 gene lengths and cytobands)\n')
    response = raw_input('This migration includes a step that links all current genes in the database to 1 reference '
                         'genome. This can not be undone!\n' +
                         selection_message).strip()
    while response.lower() not in ['', 'n', 'hg19', 'hg38', 'mm10']:
        response = raw_input(selection_message).strip()
    if response.lower() == 'n':
        print "Aborted migration."
        sys.exit()
    elif response.lower() == 'hg38':
        return 'GRCh38'
    elif response.lower() == 'mm10':
        return 'GRCm38'
    else:
        return 'GRCh37'


def verify_reference_genome(reference_genomes, proposed_reference_genome):
    response = raw_input('\nFound the following reference genomes in NCBI column of mutation_event:\n'
                         '%s\n'
                         'Is linking all genes to %s correct? (y/n)\n' % (", ".join(reference_genomes),
                                                                          proposed_reference_genome))
    while response.lower() not in ['y', 'n']:
        response = raw_input('Found the following reference genomes in NCBI column of mutation_event:\n'
                             '%s\n'
                             'Is linking all genes to %s correct? (y/n)\n').strip()
    if response.lower() == 'n':
        print("DB migration aborted")
        sys.exit()
    else:
        return True


def find_reference_genome(cursor):
    """
    In the migration to 2.7.0, the genes in the database are linked to a specific reference genome. This functions finds
    the reference genome of the used database in the mutation table.
    """

    # First check if the database is empty. This will result in GRCh37
    if check_database_empty(cursor):
        print 'This database contains no studies.'
        reference_genome = ask_reference_genome()
        return reference_genome

    # If the database is not empty, check the mutation_event table for NCBI Build
    sql_statement = 'SELECT distinct(NCBI_BUILD) FROM mutation_event;'
    try:
        cursor.execute(sql_statement)
    except MySQLdb.Error, msg:
        print >> ERROR_FILE, msg
        sys.exit(1)
    query_result = cursor.fetchall()
    reference_genomes_original = set(['na' if x[0] is None else x[0] for x in query_result])
    reference_genomes = set(['na' if x[0] is None else x[0].lower() for x in query_result])

    # When a database contains data but no reference genomes, ask the user for reference genome.
    if len(reference_genomes) == 0:
        print 'Cannot infer reference genome from mutation_event table.'
        reference_genome = ask_reference_genome()
        return reference_genome

    # When a database contains only NA values, ask the user for reference genome.
    elif len(reference_genomes) == 1 and list(reference_genomes)[0] == 'na':
        print 'Cannot infer reference genome from mutation_event table.'
        reference_genome = ask_reference_genome()
        return reference_genome

    # Check if reference genome can be inferred
    elif reference_genomes.issubset({'37', 'grch37', 'hg19'}):
        return 'GRCh37'
    elif reference_genomes.issubset({'grch38', 'hg38'}):
        return 'GRCh38'
    elif reference_genomes.issubset({'grcm38', 'mm10'}):
        return 'GRCm38'

    # Check if reference genome can be inferred but database also contains NA values. Verify with the user
    elif reference_genomes.issubset({'37', 'grch37', 'hg19', 'na'}):
        if verify_reference_genome(reference_genomes_original, 'hg19'):
            return 'GRCh37'
    elif reference_genomes.issubset({'grch38', 'hg38', 'na'}):
        verify_reference_genome(reference_genomes_original, 'hg38')
        return 'GRCh38'
    elif reference_genomes.issubset({'grcm38', 'mm10', 'na'}):
        verify_reference_genome(reference_genomes_original, 'mm10')
        return 'GRCm38'

    # Return an error in case of unknown or multiple reference genomes.
    else:
        print '\bFound unknown or multiple reference genomes in mutation_event table in database:\n%s\n\n' \
              'Please start from new seed database, migrate database and reload studies.'\
              % ", ".join(reference_genomes_original)
        sys.exit(1)


def run_migration(db_version, sql_filename, connection, cursor):
    """

        Goes through the sql and runs lines based on the version numbers. SQL version should be stated as follows:

        ##version: 1.0.0
        INSERT INTO ...

        ##version: 1.1.0
        CREATE TABLE ...
    
    """
    
    sql_file = open(sql_filename, 'r')
    sql_version = (0,0,0)
    run_line = False
    statements = OrderedDict()
    statement = ''
    reference_genome = ''
    for line in sql_file:
        if line.startswith('##'):
            sql_version = tuple(map(int, line.split(':')[1].strip().split('.')))
            run_line = is_version_larger(sql_version, db_version)

            # In case of upgrade to 2.7.0, check if the database is hg19 by looking in the database, or verify with the user.
            if run_line and line.split(':')[1].strip() == '2.7.0':
                reference_genome = find_reference_genome(cursor)
                reference_genome_dict = {'GRCh37': 'hg19',
                                         'GRCh38': 'hg38',
                                         'GRCm38': 'mm10'}
                print "Selected reference genome: %s" % reference_genome_dict[reference_genome]
            continue

        # skip blank lines
        if len(line.strip()) < 1:
            continue
        # skip comments
        if line.startswith('#'):
            continue
        # skip sql comments
        if line.startswith('--') and len(line) > 2 and line[2].isspace():
            continue
        # Add reference genome variable
        if line.strip() == "(SELECT REFERENCE_GENOME_ID FROM reference_genome WHERE BUILD_NAME='?')":
            line = line.replace('?', reference_genome)
        # only execute sql line if the last version seen in the file is greater than the db_version
        if run_line:
            line = line.strip()
            statement = statement + ' ' + line
            if line.endswith(';'):
                if sql_version not in statements:
                    statements[sql_version] = [statement]
                else:
                    statements[sql_version].append(statement)
                statement = ''
    if len(statements.items()) > 0:
        run_statements(statements, connection, cursor)
    else:
        print 'Everything up to date, nothing to migrate.'
    
def run_statements(statements, connection, cursor):
    try:
        cursor.execute('SET autocommit=0;')
    except MySQLdb.Error, msg:
        print >> ERROR_FILE, msg
        sys.exit(1)

    for version,statement_list in statements.iteritems():
        print >> OUTPUT_FILE, 'Running statements for version: ' + '.'.join(map(str,version))
        for statement in statement_list:
            print >> OUTPUT_FILE, '\tExecuting statement: ' + statement.strip()
            try:
                cursor.execute(statement.strip())
            except MySQLdb.Error, msg:
                print >> ERROR_FILE, msg
                sys.exit(1)
        connection.commit();

def warn_user():
    """
    warn the user before the script runs, give them a chance to
    back up their database if desired
    """
    response = raw_input('WARNING: This script will alter your database! Be sure to back up your data before running.\nContinue running DB migration? (y/n) ').strip()
    while response.lower() != 'y' and response.lower() != 'n':
        response = raw_input('Did not recognize response.\nContinue running DB migration? (y/n) ').strip()
    if response.lower() == 'n':
        sys.exit()

def usage():
    print >> OUTPUT_FILE, 'migrate_db.py --properties-file [portal properties file] --sql [sql migration file]'

def main():
    """ main function to run mysql migration """
    
    parser = argparse.ArgumentParser(description='cBioPortal DB migration script')
    parser.add_argument('-p', '--properties-file', type=str, required=True,
                        help='Path to portal.properties file')
    parser.add_argument('-s', '--sql',type=str, required=True,
                        help='Path to official migration.sql script.')
    parser = parser.parse_args()

    properties_filename = parser.properties_file
    sql_filename = parser.sql

    # check existence of properties file
    if not os.path.exists(properties_filename):
        print >> ERROR_FILE, 'properties file ' + properties_filename + ' cannot be found'
        usage()
        sys.exit(2)
    if not os.path.exists(sql_filename):
        print >> ERROR_FILE, 'sql file ' + sql_filename + ' cannot be found'
        usage()
        sys.exit(2)

    warn_user()
    
    # set up - get properties and db cursor
    portal_properties = get_portal_properties(properties_filename)
    connection, cursor = get_db_cursor(portal_properties)

    if cursor is None:
        print >> ERROR_FILE, 'failure connecting to sql database'
        sys.exit(1)

    # execute - get the database version and run the migration
    db_version = get_db_version(cursor)
    run_migration(db_version, sql_filename, connection, cursor)
    connection.close();
    print 'Finished.'
    

# do main
if __name__ == '__main__':
    main()
