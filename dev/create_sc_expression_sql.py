#!/usr/bin/env python
"""
Create a SQL script to insert single_cell_expression data into the database
"""

import json
import pandas as pd

# STUDY_ID = msk_spectrum_tme_2022
cancer_study_id = 1
# THIS DOESNT MATTER, JUST A HIGH NUMBER?
genetic_profile_id = 10000


create_table_statement = """
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
"""

add_genetic_profile = f"""
DELETE FROM genetic_profile WHERE STABLE_ID = "SINGLE_CELL_EXPRESSION";
INSERT INTO genetic_profile (
    GENETIC_PROFILE_ID, STABLE_ID, CANCER_STUDY_ID, GENETIC_ALTERATION_TYPE,
    DATATYPE, NAME, DESCRIPTION, SHOW_PROFILE_IN_ANALYSIS_TAB
) VALUES (
     {genetic_profile_id}, "SINGLE_CELL_EXPRESSION", {cancer_study_id}, "SINGLE_CELL_EXPRESSION",
    "single_cell_expression", "Single Cell Expression", "Single Cell Expression", 1
);
"""

insert_data_start = """
INSERT INTO single_cell_expression (
    GENETIC_PROFILE_ID, SAMPLE_ID, TISSUE, CELL_TYPE, ENTREZ_GENE_ID, EXPRESSION_VALUE
) VALUES (
"""

value_split = "\n), (\n"

def create_sample_map():
    """SQL
    SELECT sample.INTERNAL_ID, sample.STABLE_ID FROM sample 
    INNER JOIN patient ON sample.PATIENT_ID = patient.INTERNAL_ID 
    INNER JOIN cancer_study ON patient.CANCER_STUDY_ID = cancer_study.CANCER_STUDY_ID 
    WHERE cancer_study.CANCER_STUDY_IDENTIFIER = "msk_spectrum_tme_2022";
    """
    data = pd.read_csv("sample_map.tsv", skiprows=0, sep="\t")
    data = data.dropna(how="any")
    return dict(zip(data["STABLE_ID"], data["INTERNAL_ID"]))


def create_gene_map():
    """SQL:
    select ENTREZ_GENE_ID, HUGO_GENE_SYMBOL from gene;
    """
    data = pd.read_csv("gene_map.tsv", skiprows=0, sep="\t")
    data = data.dropna(how="any")
    return dict(zip(data["HUGO_GENE_SYMBOL"], data["ENTREZ_GENE_ID"]))


def create_data_sql() -> str:
    sample_map = create_sample_map()
    gene_map = create_gene_map()
    with open("sc-expression-msk-spectrum.json") as f:
        data = json.loads(f.read())

    sql = """"""
    first = True
    for sample_id in data.keys():
        mapped_sample_id = sample_map.get(sample_id)
        if mapped_sample_id is None:
            continue
        for tissue in data[sample_id].keys():
            for cell_type in data[sample_id][tissue].keys():
                for gene, value in data[sample_id][tissue][cell_type].items():
                    mapped_gene_id = gene_map.get(gene)
                    if mapped_gene_id is None:
                        continue
                    if not first:
                        sql += "\n), (\n"
                    if pd.isna(value):
                        value = "NULL"
                    sql += f"{genetic_profile_id}, {mapped_sample_id}, \"{tissue}\", \"{cell_type}\", {mapped_gene_id}, {value} "
                    first = False
    sql += "\n);"
    print(sql)
    return sql

def create_sql_file():
    with open("single_cell_expression.sql", "w+") as f:
        f.write(create_table_statement + "\n")
        f.write(add_genetic_profile + "\n")
        f.write(insert_data_start)
        f.write(create_data_sql())

create_sql_file()
