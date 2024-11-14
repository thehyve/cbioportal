DROP TABLE IF EXISTS single_cell_expression;
CREATE TABLE IF NOT EXISTS single_cell_expression (
    GENETIC_PROFILE_ID int NOT NULL,
    SAMPLE_ID int NOT NULL,
    TISSUE varchar(255) NOT NULL,
    CELL_TYPE varchar(255) NOT NULL,
    ENTREZ_GENE_ID int NOT NULL,
    EXPRESSION_VALUE float,
    FOREIGN KEY(GENETIC_PROFILE_ID) REFERENCES genetic_profile(GENETIC_PROFILE_ID),
    FOREIGN KEY(SAMPLE_ID) REFERENCES sample(INTERNAL_ID),
    FOREIGN KEY(ENTREZ_GENE_ID) REFERENCES gene(ENTREZ_GENE_ID)
);

DELETE FROM genetic_profile WHERE STABLE_ID = "single_cell_expression";
INSERT INTO genetic_profile (
    GENETIC_PROFILE_ID, STABLE_ID, CANCER_STUDY_ID, GENETIC_ALTERATION_TYPE,
    DATATYPE, NAME, DESCRIPTION, SHOW_PROFILE_IN_ANALYSIS_TAB
) VALUES (
    2, "SINGLE_CELL_EXPRESSION", 1, "single_cell_expression",
    "single_cell_expression", "Single Cell Expression", "Test Single Cell Expression", 1
);

DELETE FROM gene WHERE ENTREZ_GENE_ID = 100;
INSERT INTO gene (
    ENTREZ_GENE_ID, HUGO_GENE_SYMBOL, GENETIC_ENTITY_ID
) VALUES (
    100, "TEST_GENE", 1
);

INSERT INTO single_cell_expression (
    GENETIC_PROFILE_ID, SAMPLE_ID, TISSUE, CELL_TYPE, ENTREZ_GENE_ID, EXPRESSION_VALUE
) VALUES (
    2, 1, "omentum", "B cell", 7157, 1.34
), (
    2, 2, "omentum", "D cell", 7157, 4.24
), (
    2, 2, "omentum", "D cell", 100, -1.24
), (
    2, 2, "awesome tissue", "D cell", 7157, 8.24
), (
    2, 2, "awesome tissue", "D cell", 100, NULL
);


/* command:
mysql -hlocalhost -P3306 --protocol=tcp --default-auth=mysql_native_password --get-server-public-key -ucbio -pP@ssword1 cbioportal < dev/test.sql 
*/
