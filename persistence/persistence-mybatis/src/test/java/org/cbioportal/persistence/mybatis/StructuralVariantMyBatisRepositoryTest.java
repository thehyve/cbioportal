package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.StructuralVariant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/testContextDatabase.xml")
@Configurable
public class StructuralVariantMyBatisRepositoryTest {

    @Autowired
    StructuralVariantMyBatisRepository structuralVariantMyBatisRepository;
    
    @Test
    public void fetchStructuralVariantsNoSampleIdentifiers() throws Exception {

        List<String> molecularProfileIds = new ArrayList<String>();
        List<String> hugoGeneSymbols = new ArrayList<String>();
        List<String> sampleIds = new ArrayList<String>();

        molecularProfileIds.add("study_tcga_pub_sv");
        hugoGeneSymbols.add("KIAA1549");

        List<StructuralVariant> result = 
                structuralVariantMyBatisRepository.fetchStructuralVariants(molecularProfileIds, 
                        hugoGeneSymbols, sampleIds);

        Assert.assertEquals(2,  result.size());
        StructuralVariant structuralVariantFirstResult = result.get(0);
        Assert.assertEquals((int) 7, structuralVariantFirstResult.getGeneticProfileId());
        Assert.assertEquals(1, structuralVariantFirstResult.getStructuralVariantId());
        Assert.assertEquals((int) 1, structuralVariantFirstResult.getSampleIdInternal());
        Assert.assertEquals((String) "TCGA-A1-A0SB-01", structuralVariantFirstResult.getSampleId());
        Assert.assertEquals((String) "TCGA-A1-A0SB", structuralVariantFirstResult.getPatientId());
        Assert.assertEquals((String) "study_tcga_pub", structuralVariantFirstResult.getStudyId());
        Assert.assertEquals((Long) 57670L, structuralVariantFirstResult.getSite1EntrezGeneId());
        Assert.assertEquals("KIAA1549", structuralVariantFirstResult.getSite1HugoSymbol());
        Assert.assertEquals("ENST00000242365", structuralVariantFirstResult.getSite1EnsemblTranscriptId());
        Assert.assertEquals((Integer) 15, structuralVariantFirstResult.getSite1Exon());
        Assert.assertEquals("7", structuralVariantFirstResult.getSite1Chromosome());
        Assert.assertEquals((Integer) 138536968, structuralVariantFirstResult.getSite1Position());
        Assert.assertEquals("KIAA1549-BRAF.K16B10.COSF509_1", structuralVariantFirstResult.getSite1Description());
        Assert.assertEquals((Long)673L, structuralVariantFirstResult.getSite2EntrezGeneId());
        Assert.assertEquals("BRAF", structuralVariantFirstResult.getSite2HugoSymbol());
        Assert.assertEquals("ENST00000288602", structuralVariantFirstResult.getSite2EnsemblTranscriptId());
        Assert.assertEquals((Integer) 10, structuralVariantFirstResult.getSite2Exon());
        Assert.assertEquals("7", structuralVariantFirstResult.getSite2Chromosome());
        Assert.assertEquals((Integer) 140482957, structuralVariantFirstResult.getSite2Position());
        Assert.assertEquals("KIAA1549-BRAF.K16B10.COSF509_2", structuralVariantFirstResult.getSite2Description());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite2EffectOnFrame());
        Assert.assertEquals("GRCh37", structuralVariantFirstResult.getNcbiBuild());
        Assert.assertEquals("no", structuralVariantFirstResult.getDnaSupport());
        Assert.assertEquals("yes", structuralVariantFirstResult.getRnaSupport());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalReadCount());
        Assert.assertEquals((Integer) 100000, structuralVariantFirstResult.getTumorReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalVariantCount());
        Assert.assertEquals((Integer) 90000, structuralVariantFirstResult.getTumorVariantCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalPairedEndReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getTumorPairedEndReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalSplitReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getTumorSplitReadCount());
        Assert.assertEquals("KIAA1549-BRAF.K16B10.COSF509", structuralVariantFirstResult.getAnnotation());
        Assert.assertEquals(null, structuralVariantFirstResult.getBreakpointType());
        Assert.assertEquals(null, structuralVariantFirstResult.getCenter());
        Assert.assertEquals(null, structuralVariantFirstResult.getConnectionType());
        Assert.assertEquals("Fusion", structuralVariantFirstResult.getEventInfo());
        Assert.assertEquals(null, structuralVariantFirstResult.getVariantClass());
        Assert.assertEquals(null, structuralVariantFirstResult.getLength());
        Assert.assertEquals("Gain-of-Function", structuralVariantFirstResult.getComments());
        Assert.assertEquals("COSMIC:COSF509", structuralVariantFirstResult.getExternalAnnotation());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverFilter());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverFilterAnn());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverTiersFilter());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverTiersFilterAnn());

    }

    @Test
    public void fetchStructuralVariantsWithSampleIdentifiers() throws Exception {

        List<String> molecularProfileIds = new ArrayList<String>();
        List<String> hugoGeneSymbols = new ArrayList<String>();
        List<String> sampleIds = new ArrayList<String>();

        molecularProfileIds.add("study_tcga_pub_sv");
        hugoGeneSymbols.add("KIAA1549");
        sampleIds.add("TCGA-A1-A0SB-01");

        List<StructuralVariant> result = 
                structuralVariantMyBatisRepository.fetchStructuralVariants(molecularProfileIds, 
                        hugoGeneSymbols, sampleIds);

        Assert.assertEquals(1,  result.size());
        StructuralVariant structuralVariantFirstResult = result.get(0);
        Assert.assertEquals((int) 7, structuralVariantFirstResult.getGeneticProfileId());
        Assert.assertEquals(1, structuralVariantFirstResult.getStructuralVariantId());
        Assert.assertEquals((int) 1, structuralVariantFirstResult.getSampleIdInternal());
        Assert.assertEquals((String) "TCGA-A1-A0SB-01", structuralVariantFirstResult.getSampleId());
        Assert.assertEquals((String) "TCGA-A1-A0SB", structuralVariantFirstResult.getPatientId());
        Assert.assertEquals((String) "study_tcga_pub", structuralVariantFirstResult.getStudyId());
        Assert.assertEquals((Long) 57670L, structuralVariantFirstResult.getSite1EntrezGeneId());
        Assert.assertEquals("KIAA1549", structuralVariantFirstResult.getSite1HugoSymbol());
        Assert.assertEquals("ENST00000242365", structuralVariantFirstResult.getSite1EnsemblTranscriptId());
        Assert.assertEquals((Integer) 15, structuralVariantFirstResult.getSite1Exon());
        Assert.assertEquals("7", structuralVariantFirstResult.getSite1Chromosome());
        Assert.assertEquals((Integer) 138536968, structuralVariantFirstResult.getSite1Position());
        Assert.assertEquals("KIAA1549-BRAF.K16B10.COSF509_1", structuralVariantFirstResult.getSite1Description());
        Assert.assertEquals((Long)673L, structuralVariantFirstResult.getSite2EntrezGeneId());
        Assert.assertEquals("BRAF", structuralVariantFirstResult.getSite2HugoSymbol());
        Assert.assertEquals("ENST00000288602", structuralVariantFirstResult.getSite2EnsemblTranscriptId());
        Assert.assertEquals((Integer) 10, structuralVariantFirstResult.getSite2Exon());
        Assert.assertEquals("7", structuralVariantFirstResult.getSite2Chromosome());
        Assert.assertEquals((Integer) 140482957, structuralVariantFirstResult.getSite2Position());
        Assert.assertEquals("KIAA1549-BRAF.K16B10.COSF509_2", structuralVariantFirstResult.getSite2Description());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite2EffectOnFrame());
        Assert.assertEquals("GRCh37", structuralVariantFirstResult.getNcbiBuild());
        Assert.assertEquals("no", structuralVariantFirstResult.getDnaSupport());
        Assert.assertEquals("yes", structuralVariantFirstResult.getRnaSupport());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalReadCount());
        Assert.assertEquals((Integer) 100000, structuralVariantFirstResult.getTumorReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalVariantCount());
        Assert.assertEquals((Integer) 90000, structuralVariantFirstResult.getTumorVariantCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalPairedEndReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getTumorPairedEndReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalSplitReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getTumorSplitReadCount());
        Assert.assertEquals("KIAA1549-BRAF.K16B10.COSF509", structuralVariantFirstResult.getAnnotation());
        Assert.assertEquals(null, structuralVariantFirstResult.getBreakpointType());
        Assert.assertEquals(null, structuralVariantFirstResult.getCenter());
        Assert.assertEquals(null, structuralVariantFirstResult.getConnectionType());
        Assert.assertEquals("Fusion", structuralVariantFirstResult.getEventInfo());
        Assert.assertEquals(null, structuralVariantFirstResult.getVariantClass());
        Assert.assertEquals(null, structuralVariantFirstResult.getLength());
        Assert.assertEquals("Gain-of-Function", structuralVariantFirstResult.getComments());
        Assert.assertEquals("COSMIC:COSF509", structuralVariantFirstResult.getExternalAnnotation());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverFilter());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverFilterAnn());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverTiersFilter());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverTiersFilterAnn());

    }
}
