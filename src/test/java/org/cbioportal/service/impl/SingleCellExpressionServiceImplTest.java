package org.cbioportal.service.impl;

import java.util.List;

import org.cbioportal.model.SingleCellExpression;
import org.cbioportal.persistence.SingleCellExpressionRepository;
import org.cbioportal.utils.SingleCellExpressionTestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SingleCellExpressionServiceImplTest {

    @InjectMocks
    private SingleCellExpressionServiceImpl singleCellExpressionService;
    @Mock
    private SingleCellExpressionRepository singleCellExpressionRepository;


    @Test
    public void fetchSingleCellExpressionInMolecularProfile() throws Exception {
        // List<SingleCellExpression> testSingleCellExpressionList = SingleCellExpressionTestUtils.createTestSingleCellExpressionList();
        // Mockito.when(singleCellExpressionRepository.fetchSingleCellExpressionInMolecularProfile(
        //     Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
        //     .thenReturn(testSingleCellExpressionList);
        //
        // List<SingleCellExpression> result = singleCellExpressionService.fetchSingleCellExpressionInMolecularProfile(SingleCellExpressionTestUtils.TEST_MOLECULAR_PROFILE_ID_1, SingleCellExpressionTestUtils.TEST_SAMPLE_LIST_ID,
        //     List.of(SingleCellExpressionTestUtils.TEST_ENTREZ_GENE_ID_1), 1000, 0);
        //
        //
        // Assert.assertEquals(testSingleCellExpressionList, result);
    }

    @Test
    public void fetchSingleCellExpressionInMolecularProfileGroupedBySample() throws Exception {
        // TODO
    }
}
