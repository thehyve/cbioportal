package org.cbioportal.utils;

import java.util.ArrayList;
import java.util.List;

import org.cbioportal.model.SingleCellExpression;

public final class SingleCellExpressionTestUtils {

    public static final String TEST_SAMPLE_LIST_ID = "test_sample_list_id";
    public static final String TEST_MOLECULAR_PROFILE_ID_1 = "test_molecular_profile_id_1";
    public static final String TEST_SAMPLE_ID_1 = "test_sample_id_1";
    public static final String TEST_PATIENT_ID_1 = "test_patient_id_1";
    public static final Integer TEST_ENTREZ_GENE_ID_1 = 1;
    public static final String TEST_STUDY_ID_1 = "test_study_id_1";
    public static final String TEST_TISSUE_1 = "test_tissue_1";
    public static final String TEST_CELL_TYPE_1 = "test_cell_type_1";
    public static final Float TEST_EXPRESSION_VALUE_1 = 1.1f;
    public static final String TEST_MOLECULAR_PROFILE_ID_2 = "test_molecular_profile_id_2";
    public static final String TEST_SAMPLE_ID_2 = "test_sample_id_2";
    public static final String TEST_PATIENT_ID_2 = "test_patient_id_2";
    public static final Integer TEST_ENTREZ_GENE_ID_2 = 2;
    public static final String TEST_STUDY_ID_2 = "test_study_id_2";
    public static final String TEST_TISSUE_2 = "test_tissue_2";
    public static final String TEST_CELL_TYPE_2 = "test_cell_type_2";
    public static final Float TEST_EXPRESSION_VALUE_2 = 2.2f;

    public static List<SingleCellExpression> createTestSingleCellExpressionList() {
        List<SingleCellExpression> singleCellExpressionList = new ArrayList<>();
        SingleCellExpression singleCellExpression1 = new SingleCellExpression();
        singleCellExpression1.setMolecularProfileId(TEST_MOLECULAR_PROFILE_ID_1);
        singleCellExpression1.setSampleId(TEST_SAMPLE_ID_1);
        singleCellExpression1.setPatientId(TEST_PATIENT_ID_1);
        singleCellExpression1.setEntrezGeneId(TEST_ENTREZ_GENE_ID_1);
        singleCellExpression1.setStudyId(TEST_STUDY_ID_1);
        singleCellExpression1.setTissue(TEST_TISSUE_1);
        singleCellExpression1.setCellType(TEST_CELL_TYPE_1);
        singleCellExpression1.setExpressionValue(TEST_EXPRESSION_VALUE_1);
        singleCellExpressionList.add(singleCellExpression1);
        SingleCellExpression singleCellExpression2 = new SingleCellExpression();
        singleCellExpression2.setMolecularProfileId(TEST_MOLECULAR_PROFILE_ID_2);
        singleCellExpression2.setSampleId(TEST_SAMPLE_ID_2);
        singleCellExpression2.setPatientId(TEST_PATIENT_ID_2);
        singleCellExpression2.setEntrezGeneId(TEST_ENTREZ_GENE_ID_2);
        singleCellExpression2.setStudyId(TEST_STUDY_ID_2);
        singleCellExpression2.setTissue(TEST_TISSUE_2);
        singleCellExpression2.setCellType(TEST_CELL_TYPE_2);
        singleCellExpression2.setExpressionValue(TEST_EXPRESSION_VALUE_2);
        singleCellExpressionList.add(singleCellExpression2);
        return singleCellExpressionList;
    }
}
