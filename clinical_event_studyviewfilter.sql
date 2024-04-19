-- This is example code to filter a sample list using clinical event filters
-- Should become part of a bigger 'StudyViewFilter' SQL statement, which performs the full filtering.
-- Authors: Robert Sheridan, Matthijs Pon

WITH select_all_events_from_study as -- Keeps this table in memory, saves overhead?
(
    SELECT
        sample.stable_id as stableId,
        clinical_event.event_type as eventType,
        patient.stable_id as patientId,
        cancer_study.cancer_study_identifier as cancerStudyId
    FROM
        sample
    Inner Join patient on sample.patient_id = patient.internal_id
    Inner join clinical_event on clinical_event.patient_id = patient.internal_id
    Inner Join cancer_study on cancer_study.cancer_study_id = patient.cancer_study_id
    WHERE cancer_study.cancer_study_identifier in ('brca_akt1_genie_2019','erbb2_genie_public','crc_public_genie_bpc',
                                                   'nsclc_public_genie_bpc','mbc_genie_2020')
)
SELECT
    DISTINCT stableId as uniqueSampleKey,
    patientId as uniquePatientKey,
    cancerStudyId
FROM
    select_all_events_from_study WHERE eventType in ('Treatment', 'LAB_TEST') -- OR filter
INTERSECT -- AND filter
SELECT
    DISTINCT stableId as UniqueSampleKey,
    patientId as uniquePatientKey,
    cancerStudyId
FROM
    select_all_events_from_study WHERE eventType in ('Pathology'); -- OR filter