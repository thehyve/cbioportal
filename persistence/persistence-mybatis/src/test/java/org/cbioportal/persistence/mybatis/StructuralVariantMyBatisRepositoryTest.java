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
        Assert.assertEquals("7", structuralVariantFirstResult.getGeneticProfileId());
        Assert.assertEquals("1", structuralVariantFirstResult.getStructuralVariantId());
        Assert.assertEquals("15", structuralVariantFirstResult.getSampleIdInternal());
        Assert.assertEquals("207", structuralVariantFirstResult.getSite1EntrezGeneId());
        Assert.assertEquals("AKT1", structuralVariantFirstResult.getSite1HugoSymbol());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite1EnsemblTranscriptId());
        Assert.assertEquals("1", structuralVariantFirstResult.getSite1Exon());
        Assert.assertEquals("11", structuralVariantFirstResult.getSite1Chromosome());
        Assert.assertEquals("10", structuralVariantFirstResult.getSite1Position());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite1Description());
        Assert.assertEquals("208", structuralVariantFirstResult.getSite2EntrezGeneId());
        Assert.assertEquals("AKT2", structuralVariantFirstResult.getSite2HugoSymbol());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite2EnsemblTranscriptId());
        Assert.assertEquals("2", structuralVariantFirstResult.getSite2Exon());
        Assert.assertEquals("12", structuralVariantFirstResult.getSite2Chromosome());
        Assert.assertEquals("20", structuralVariantFirstResult.getSite2Position());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite2Description());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite2EffectOnFrame());
        Assert.assertEquals("no", structuralVariantFirstResult.getDnaSupport());
        Assert.assertEquals("yes", structuralVariantFirstResult.getRnaSupport());
        Assert.assertEquals("NA", structuralVariantFirstResult.getNormalReadCount());
        Assert.assertEquals("100000", structuralVariantFirstResult.getTumorReadCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getNormalVariantCount());
        Assert.assertEquals("100000", structuralVariantFirstResult.getTumorVariantCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getNormalPairedEndReadCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getTumorPairedEndReadCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getNormalSplitReadCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getTumorSplitReadCount());
        Assert.assertEquals("AKT1-AKT2", structuralVariantFirstResult.getAnnotation());
        Assert.assertEquals("NA", structuralVariantFirstResult.getBreakpointType());
        Assert.assertEquals("NA", structuralVariantFirstResult.getCenter());
        Assert.assertEquals("NA", structuralVariantFirstResult.getConnectionType());
        Assert.assertEquals("Fusion", structuralVariantFirstResult.getEventInfo());
        Assert.assertEquals("NA", structuralVariantFirstResult.getVariantClass());
        Assert.assertEquals("NA", structuralVariantFirstResult.getLength());
        Assert.assertEquals("Gain-of-Function", structuralVariantFirstResult.getComments());
        Assert.assertEquals("Genbank:AB209510", structuralVariantFirstResult.getExternalAnnotation());
        Assert.assertEquals("NA", structuralVariantFirstResult.getDriverFilter());
        Assert.assertEquals("NA", structuralVariantFirstResult.getDriverFilterAnn());
        Assert.assertEquals("NA", structuralVariantFirstResult.getDriverTiersFilter());
        Assert.assertEquals("NA", structuralVariantFirstResult.getDriverTiersFilterAnn());

    }

    public void fetchStructuralVariantsWithSampleIdentifiers() throws Exception {

        List<String> geneticProfileStableIds = new ArrayList<String>();
        List<String> hugoGeneSymbols = new ArrayList<String>();
        List<String> studyIds = new ArrayList<String>();
        List<String> sampleIds = new ArrayList<String>();

        geneticProfileStableIds.add("study_tcga_pub_sv");
        hugoGeneSymbols.add("AKT1");
        studyIds.add("acc_tcga");
        sampleIds.add("TCGA-A1-BOSO-01");

        List<StructuralVariant> result = 
                structuralVariantMyBatisRepository.fetchStructuralVariants(geneticProfileStableIds, 
                        hugoGeneSymbols, studyIds, sampleIds);

        Assert.assertEquals(2,  result.size());
        StructuralVariant structuralVariantFirstResult = result.get(0);
        Assert.assertEquals("7", structuralVariantFirstResult.getGeneticProfileId());
        Assert.assertEquals("1", structuralVariantFirstResult.getStructuralVariantId());
        Assert.assertEquals("15", structuralVariantFirstResult.getSampleIdInternal());
        Assert.assertEquals("TCGA-A1-BOSO-01", structuralVariantFirstResult.getSampleId());
        Assert.assertEquals("207", structuralVariantFirstResult.getSite1EntrezGeneId());
        Assert.assertEquals("AKT1", structuralVariantFirstResult.getSite1HugoSymbol());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite1EnsemblTranscriptId());
        Assert.assertEquals("1", structuralVariantFirstResult.getSite1Exon());
        Assert.assertEquals("11", structuralVariantFirstResult.getSite1Chromosome());
        Assert.assertEquals("10", structuralVariantFirstResult.getSite1Position());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite1Description());
        Assert.assertEquals("208", structuralVariantFirstResult.getSite2EntrezGeneId());
        Assert.assertEquals("AKT2", structuralVariantFirstResult.getSite2HugoSymbol());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite2EnsemblTranscriptId());
        Assert.assertEquals("2", structuralVariantFirstResult.getSite2Exon());
        Assert.assertEquals("12", structuralVariantFirstResult.getSite2Chromosome());
        Assert.assertEquals("20", structuralVariantFirstResult.getSite2Position());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite2Description());
        Assert.assertEquals("NA", structuralVariantFirstResult.getSite2EffectOnFrame());
        Assert.assertEquals("no", structuralVariantFirstResult.getDnaSupport());
        Assert.assertEquals("yes", structuralVariantFirstResult.getRnaSupport());
        Assert.assertEquals("NA", structuralVariantFirstResult.getNormalReadCount());
        Assert.assertEquals("100000", structuralVariantFirstResult.getTumorReadCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getNormalVariantCount());
        Assert.assertEquals("100000", structuralVariantFirstResult.getTumorVariantCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getNormalPairedEndReadCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getTumorPairedEndReadCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getNormalSplitReadCount());
        Assert.assertEquals("NA", structuralVariantFirstResult.getTumorSplitReadCount());
        Assert.assertEquals("AKT1-AKT2", structuralVariantFirstResult.getAnnotation());
        Assert.assertEquals("NA", structuralVariantFirstResult.getBreakpointType());
        Assert.assertEquals("NA", structuralVariantFirstResult.getCenter());
        Assert.assertEquals("NA", structuralVariantFirstResult.getConnectionType());
        Assert.assertEquals("Fusion", structuralVariantFirstResult.getEventInfo());
        Assert.assertEquals("NA", structuralVariantFirstResult.getVariantClass());
        Assert.assertEquals("NA", structuralVariantFirstResult.getLength());
        Assert.assertEquals("Gain-of-Function", structuralVariantFirstResult.getComments());
        Assert.assertEquals("Genbank:AB209510", structuralVariantFirstResult.getExternalAnnotation());
        Assert.assertEquals("NA", structuralVariantFirstResult.getDriverFilter());
        Assert.assertEquals("NA", structuralVariantFirstResult.getDriverFilterAnn());
        Assert.assertEquals("NA", structuralVariantFirstResult.getDriverTiersFilter());
        Assert.assertEquals("NA", structuralVariantFirstResult.getDriverTiersFilterAnn());

    }
}
