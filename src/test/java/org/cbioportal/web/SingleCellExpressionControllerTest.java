package org.cbioportal.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Sin;
import org.cbioportal.model.SingleCellExpression;
import org.cbioportal.service.SingleCellExpressionService;
import org.cbioportal.utils.SingleCellExpressionTestUtils;
import org.cbioportal.web.config.TestConfig;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {SingleCellExpressionController.class, TestConfig.class})
public class SingleCellExpressionControllerTest {

    @MockBean
    private SingleCellExpressionService singleCellExpressionService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    public void fetchSingleCellExpressionDefaultProjection() throws Exception {

    //     Mockito.when(singleCellExpressionService.fetchSingleCellExpressionInMolecularProfile(
    //         Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
    //         .thenReturn(SingleCellExpressionTestUtils.createTestSingleCellExpressionList());
    //
    //     mockMvc.perform(MockMvcRequestBuilders.get("/api/molecular-profiles/test_molecular_profile_id/single-cell-expression")
    //         .param("sampleListId", SingleCellExpressionTestUtils.TEST_SAMPLE_LIST_ID)
    //         .accept(MediaType.APPLICATION_JSON))
    //         .andExpect(MockMvcResultMatchers.status().isOk())
    //         .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].molecularProfileId") .value(SingleCellExpressionTestUtils.TEST_MOLECULAR_PROFILE_ID_1))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].sampleId").value(SingleCellExpressionTestUtils.TEST_SAMPLE_ID_1))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].patientId").value(SingleCellExpressionTestUtils.TEST_PATIENT_ID_1))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].entrezGeneId").value(SingleCellExpressionTestUtils.TEST_ENTREZ_GENE_ID_1))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].studyId").value(SingleCellExpressionTestUtils.TEST_STUDY_ID_1))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].tissue").value(SingleCellExpressionTestUtils.TEST_TISSUE_1))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].cellType").value(SingleCellExpressionTestUtils.TEST_CELL_TYPE_1))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].expressionValue").value(SingleCellExpressionTestUtils.TEST_EXPRESSION_VALUE_1))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene").doesNotExist())
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].molecularProfileId") .value(SingleCellExpressionTestUtils.TEST_MOLECULAR_PROFILE_ID_2))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].sampleId").value(SingleCellExpressionTestUtils.TEST_SAMPLE_ID_2))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].patientId").value(SingleCellExpressionTestUtils.TEST_PATIENT_ID_2))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].entrezGeneId").value(SingleCellExpressionTestUtils.TEST_ENTREZ_GENE_ID_2))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].studyId").value(SingleCellExpressionTestUtils.TEST_STUDY_ID_2))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].tissue").value(SingleCellExpressionTestUtils.TEST_TISSUE_2))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].cellType").value(SingleCellExpressionTestUtils.TEST_CELL_TYPE_2))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].expressionValue").value(SingleCellExpressionTestUtils.TEST_EXPRESSION_VALUE_2))
    //         .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene").doesNotExist());
    }

    @Test
    @WithMockUser
    public void fetchSingleCellExpressionGroupedBySampleDefaultProjection() throws Exception {

    }
}
