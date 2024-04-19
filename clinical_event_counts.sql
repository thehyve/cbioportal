-- This is example code to get Clinical Event counts using pure SQL
-- Could replace JAVA code tied to the /api/clinical-event-type-counts/fetch endpoint
-- Authors: Robert Sheridan, Matthijs Pon

WITH select_all_events_from_studies AS -- Keeps this table IN memory, saves overhead?
(
    SELECT
        sample.stable_id AS stableId,
        clinical_event.event_type AS eventType,
        patient.stable_id AS patientId,
        cancer_study.cancer_study_identifier AS cancerStudyId
    FROM
        sample
    Inner Join patient ON sample.patient_id = patient.internal_id
    Inner join clinical_event ON clinical_event.patient_id = patient.internal_id
    Inner Join cancer_study ON cancer_study.cancer_study_id = patient.cancer_study_id
    WHERE cancer_study.cancer_study_identifier IN ('brca_akt1_genie_2019','erbb2_genie_public','crc_public_genie_bpc',
                                                   'nsclc_public_genie_bpc','mbc_genie_2020')
),
    studyViewFilter AS -- TEMP representation of full studyviewfilter, this is only the clinical event part
(
SELECT
    DISTINCT stableId AS uniqueSampleKey,
    patientId AS uniquePatientKey
FROM
    select_all_events_from_studies WHERE eventType IN ('Treatment', 'LAB_TEST') -- OR
INTERSECT -- AND
SELECT
    DISTINCT stableId AS UniqueSampleKey,
    patientId AS uniquePatientKey
FROM
    select_all_events_from_studies WHERE eventType IN ('Pathology') -- OR
)
SELECT
    eventType,
    count(DISTINCT patientId) AS count  -- Counts are on patient level
FROM
    select_all_events_from_studies
WHERE
    stableId IN (SELECT uniqueSampleKey FROM studyViewFilter)
GROUP BY eventType
ORDER BY count DESC;
