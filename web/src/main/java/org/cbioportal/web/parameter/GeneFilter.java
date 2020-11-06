package org.cbioportal.web.parameter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

public class GeneFilter implements Serializable {

    @NotNull
    private Set<String> molecularProfileIds;
    private List<List<SingleGeneQuery>> geneQueries;
    private boolean excludeVUS;
    private boolean excludeGermline;
    private List<String> selectedTiers;

    public Set<String> getMolecularProfileIds() {
        return molecularProfileIds;
    }

    public void setMolecularProfileIds(Set<String> molecularProfileIds) {
        this.molecularProfileIds = molecularProfileIds;
    }

    public List<List<SingleGeneQuery>> getGeneQueries() {
        return geneQueries;
    }

    public void setGeneQueries(List<List<SingleGeneQuery>> geneQueries) {
        this.geneQueries = geneQueries;
    }

    public boolean getExcludeVUS() {
        return excludeVUS;
    }

    public void setExcludeVUS(boolean excludeVUS) {
        this.excludeVUS = excludeVUS;
    }

    public boolean getExcludeGermline() {
        return excludeGermline;
    }

    public void setExcludeGermline(boolean excludeGermline) {
        this.excludeGermline = excludeGermline;
    }

    public List<String> getSelectedTiers() {
        return selectedTiers;
    }

    public void setSelectedTiers(List<String> selectedTiers) {
        this.selectedTiers = selectedTiers;
    }
    
}