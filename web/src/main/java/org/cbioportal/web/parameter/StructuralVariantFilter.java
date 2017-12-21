package org.cbioportal.web.parameter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import java.util.List;

public class StructuralVariantFilter {
    
    @Size(min=1, max = PagingConstants.MAX_PAGE_SIZE)
    private List<String> geneticProfileStableIds;
    private List<String> hugoGeneSymbols;
    @Size(min = 1, max = PagingConstants.MAX_PAGE_SIZE)
    private List<SampleIdentifier> sampleIdentifiers;
    
    @AssertTrue
    private boolean isGeneticProfileStableIdsAndHugoGeneSymbolsPresent() {
        return geneticProfileStableIds != null && hugoGeneSymbols != null;
    }
    
    public List<String> getGeneticProfileStableIds(){
        return geneticProfileStableIds;
    }
    
    public void setGeneticProfileStableIds(List<String> geneticProfileStableIds) {
        this.geneticProfileStableIds = geneticProfileStableIds;
    }
    
    public List<String> getHugoGeneSymbols(){
        return hugoGeneSymbols;
    }
    
    public void setHugoGeneSymbols(List<String> hugoGeneSymbols) {
        this.hugoGeneSymbols = hugoGeneSymbols;
    }
    
    public List<SampleIdentifier> getSampleIdentifiers(){
        return sampleIdentifiers;
    }
    
    public void setSampleIdentifiers(List<SampleIdentifier> sampleIdentifiers) {
        this.sampleIdentifiers = sampleIdentifiers;
    }
} 