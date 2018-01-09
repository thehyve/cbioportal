package org.cbioportal.web.parameter;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;
import java.util.List;

public class StructuralVariantFilter {
    
    @Size(min=1, max = PagingConstants.MAX_PAGE_SIZE)
    private List<String> molecularProfileIds;
    private List<String> hugoGeneSymbols;
    @Size(min = 1, max = PagingConstants.MAX_PAGE_SIZE)
    private List<SampleMolecularIdentifier> sampleMolecularIdentifiers;
    
    @AssertTrue
    private boolean isEitherMolecularProfileIdsOrSampleMolecularIdentifiersPresent() {
        return molecularProfileIds != null ^ sampleMolecularIdentifiers != null;
    }
    
    public List<String> getMolecularProfileIds() {
        return molecularProfileIds;
    }

    public void setMolecularProfileIds(List<String> molecularProfileIds) {
        this.molecularProfileIds = molecularProfileIds;
    }
    
    public List<String> getHugoGeneSymbols(){
        return hugoGeneSymbols;
    }
    
    public void setHugoGeneSymbols(List<String> hugoGeneSymbols) {
        this.hugoGeneSymbols = hugoGeneSymbols;
    }
    
    public List<SampleMolecularIdentifier> getSampleMolecularIdentifiers(){
        return sampleMolecularIdentifiers;
    }
    
    public void setSampleMolecularIdentifiers(List<SampleMolecularIdentifier> sampleMolecularIdentifiers) {
        this.sampleMolecularIdentifiers = sampleMolecularIdentifiers;
    }
} 