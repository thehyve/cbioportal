package org.cbioportal.service.impl;

import org.cbioportal.model.StructuralVariant;
import org.cbioportal.persistence.StructuralVariantRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StructuralVariantServiceImplTest extends BaseServiceImplTest {

    @InjectMocks
    private StructuralVariantServiceImpl structuralVariantService;
    
    @Mock
    private StructuralVariantRepository structuralVariantRepository;
    
    @Test
    public void getStructuralVariants() throws Exception {

        List<StructuralVariant> expectedStructuralVariantList = new ArrayList<>();
        StructuralVariant sampleStructuralVariant = new StructuralVariant();
        expectedStructuralVariantList.add(sampleStructuralVariant);
        
        List<String> molecularProfileIds = new ArrayList<>();
        List<Integer> entrezGeneIds = new ArrayList<>();
        molecularProfileIds.add("genetic_profile_id");
        entrezGeneIds.add(ENTREZ_GENE_ID_1);

        Mockito.when(structuralVariantRepository.fetchStructuralVariants(molecularProfileIds, 
                entrezGeneIds, null))
            .thenReturn(expectedStructuralVariantList);

        List<StructuralVariant> result = structuralVariantService.fetchStructuralVariants(molecularProfileIds, 
                entrezGeneIds, null);

        Assert.assertEquals(expectedStructuralVariantList, result);
    }
}
