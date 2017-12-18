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

        List<String> geneticProfileStableIds = new ArrayList<String>();
        List<String> hugoGeneSymbols = new ArrayList<String>();
        List<String> studyIds = new ArrayList<String>();
        List<String> sampleIds = new ArrayList<String>();

        geneticProfileStableIds.add("study_tcga_pub_sv");
        hugoGeneSymbols.add("AKT1");

        List<StructuralVariant> result = 
                structuralVariantMyBatisRepository.fetchStructuralVariants(geneticProfileStableIds, 
                        hugoGeneSymbols, studyIds, sampleIds);

        Assert.assertEquals(2,  result.size());
        StructuralVariant structuralVariantFirstResult = result.get(0);
        Assert.assertEquals((int) 7, structuralVariantFirstResult.getGeneticProfileId());
        Assert.assertEquals(1, structuralVariantFirstResult.getStructuralVariantId());
        Assert.assertEquals((int) 15, structuralVariantFirstResult.getSampleIdInternal());
        Assert.assertEquals((String) "TCGA-A1-B0SO-01", structuralVariantFirstResult.getSampleId());
        Assert.assertEquals((Long) 207L, structuralVariantFirstResult.getSite1EntrezGeneId());
        Assert.assertEquals("AKT1", structuralVariantFirstResult.getSite1HugoSymbol());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite1EnsemblTranscriptId());
        Assert.assertEquals((Integer) 1, structuralVariantFirstResult.getSite1Exon());
        Assert.assertEquals("11", structuralVariantFirstResult.getSite1Chromosome());
        Assert.assertEquals((Integer) 10, structuralVariantFirstResult.getSite1Position());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite1Description());
        Assert.assertEquals((Long)208L, structuralVariantFirstResult.getSite2EntrezGeneId());
        Assert.assertEquals("AKT2", structuralVariantFirstResult.getSite2HugoSymbol());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite2EnsemblTranscriptId());
        Assert.assertEquals((Integer) 2, structuralVariantFirstResult.getSite2Exon());
        Assert.assertEquals("12", structuralVariantFirstResult.getSite2Chromosome());
        Assert.assertEquals((Integer) 20, structuralVariantFirstResult.getSite2Position());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite2Description());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite2EffectOnFrame());
        Assert.assertEquals("no", structuralVariantFirstResult.getDnaSupport());
        Assert.assertEquals("yes", structuralVariantFirstResult.getRnaSupport());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalReadCount());
        Assert.assertEquals((Integer) 100000, structuralVariantFirstResult.getTumorReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalVariantCount());
        Assert.assertEquals((Integer) 100000, structuralVariantFirstResult.getTumorVariantCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalPairedEndReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getTumorPairedEndReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalSplitReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getTumorSplitReadCount());
        Assert.assertEquals("AKT1-AKT2", structuralVariantFirstResult.getAnnotation());
        Assert.assertEquals(null, structuralVariantFirstResult.getBreakpointType());
        Assert.assertEquals(null, structuralVariantFirstResult.getCenter());
        Assert.assertEquals(null, structuralVariantFirstResult.getConnectionType());
        Assert.assertEquals("Fusion", structuralVariantFirstResult.getEventInfo());
        Assert.assertEquals(null, structuralVariantFirstResult.getVariantClass());
        Assert.assertEquals(null, structuralVariantFirstResult.getLength());
        Assert.assertEquals("Gain-of-Function", structuralVariantFirstResult.getComments());
        Assert.assertEquals("Genbank:AB209510", structuralVariantFirstResult.getExternalAnnotation());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverFilter());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverFilterAnn());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverTiersFilter());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverTiersFilterAnn());

    }

    @Test
    public void fetchStructuralVariantsWithSampleIdentifiers() throws Exception {

        List<String> geneticProfileStableIds = new ArrayList<String>();
        List<String> hugoGeneSymbols = new ArrayList<String>();
        List<String> studyIds = new ArrayList<String>();
        List<String> sampleIds = new ArrayList<String>();

        geneticProfileStableIds.add("study_tcga_pub_sv");
        hugoGeneSymbols.add("AKT1");
        studyIds.add("acc_tcga");
        sampleIds.add("TCGA-A1-B0SO-01");

        List<StructuralVariant> result = 
                structuralVariantMyBatisRepository.fetchStructuralVariants(geneticProfileStableIds, 
                        hugoGeneSymbols, studyIds, sampleIds);

        Assert.assertEquals(1,  result.size());
        StructuralVariant structuralVariantFirstResult = result.get(0);
        Assert.assertEquals((int) 7, structuralVariantFirstResult.getGeneticProfileId());
        Assert.assertEquals(1, structuralVariantFirstResult.getStructuralVariantId());
        Assert.assertEquals((int) 15, structuralVariantFirstResult.getSampleIdInternal());
        Assert.assertEquals((String) "TCGA-A1-B0SO-01", structuralVariantFirstResult.getSampleId());
        Assert.assertEquals((Long) 207L, structuralVariantFirstResult.getSite1EntrezGeneId());
        Assert.assertEquals("AKT1", structuralVariantFirstResult.getSite1HugoSymbol());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite1EnsemblTranscriptId());
        Assert.assertEquals((Integer) 1, structuralVariantFirstResult.getSite1Exon());
        Assert.assertEquals("11", structuralVariantFirstResult.getSite1Chromosome());
        Assert.assertEquals((Integer) 10, structuralVariantFirstResult.getSite1Position());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite1Description());
        Assert.assertEquals((Long)208L, structuralVariantFirstResult.getSite2EntrezGeneId());
        Assert.assertEquals("AKT2", structuralVariantFirstResult.getSite2HugoSymbol());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite2EnsemblTranscriptId());
        Assert.assertEquals((Integer) 2, structuralVariantFirstResult.getSite2Exon());
        Assert.assertEquals("12", structuralVariantFirstResult.getSite2Chromosome());
        Assert.assertEquals((Integer) 20, structuralVariantFirstResult.getSite2Position());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite2Description());
        Assert.assertEquals(null, structuralVariantFirstResult.getSite2EffectOnFrame());
        Assert.assertEquals("no", structuralVariantFirstResult.getDnaSupport());
        Assert.assertEquals("yes", structuralVariantFirstResult.getRnaSupport());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalReadCount());
        Assert.assertEquals((Integer) 100000, structuralVariantFirstResult.getTumorReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalVariantCount());
        Assert.assertEquals((Integer) 100000, structuralVariantFirstResult.getTumorVariantCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalPairedEndReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getTumorPairedEndReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getNormalSplitReadCount());
        Assert.assertEquals(null, structuralVariantFirstResult.getTumorSplitReadCount());
        Assert.assertEquals("AKT1-AKT2", structuralVariantFirstResult.getAnnotation());
        Assert.assertEquals(null, structuralVariantFirstResult.getBreakpointType());
        Assert.assertEquals(null, structuralVariantFirstResult.getCenter());
        Assert.assertEquals(null, structuralVariantFirstResult.getConnectionType());
        Assert.assertEquals("Fusion", structuralVariantFirstResult.getEventInfo());
        Assert.assertEquals(null, structuralVariantFirstResult.getVariantClass());
        Assert.assertEquals(null, structuralVariantFirstResult.getLength());
        Assert.assertEquals("Gain-of-Function", structuralVariantFirstResult.getComments());
        Assert.assertEquals("Genbank:AB209510", structuralVariantFirstResult.getExternalAnnotation());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverFilter());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverFilterAnn());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverTiersFilter());
        Assert.assertEquals(null, structuralVariantFirstResult.getDriverTiersFilterAnn());

    }
}
