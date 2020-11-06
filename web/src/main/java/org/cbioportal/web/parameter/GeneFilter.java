package org.cbioportal.web.parameter;

import java.io.Serializable;
import java.util.*;
import javax.validation.constraints.NotNull;

import org.cbioportal.model.AlterationType;
import org.cbioportal.model.CNA;

public class GeneFilter implements Serializable {

    @NotNull
    private Set<String> molecularProfileIds;
    private List<List<SingleGeneQuery>> geneQueries;

    public class SingleGeneQuery implements Serializable {

        @NotNull
        private String hugoGeneSymbol;
        @NotNull
        private AlterationType alterationType;
        private CNA[] cnaTypes;
        private boolean excludeVUS;
        private boolean excludeGermline;
        private String[] selectedTiers;

        public String getHugoGeneSymbol() {
            return hugoGeneSymbol;
        }

        public void setHugoGeneSymbol(String hugoGeneSymbol) {
            this.hugoGeneSymbol = hugoGeneSymbol;
        }

        public AlterationType getAlterationType() {
            return alterationType;
        }

        public void setAlterationType(AlterationType alterationType) {
            this.alterationType = alterationType;
        }

        public boolean isExcludeVUS() {
            return excludeVUS;
        }

        public void setExcludeVUS(boolean excludeVUS) {
            this.excludeVUS = excludeVUS;
        }

        public boolean isExcludeGermline() {
            return excludeGermline;
        }

        public void setExcludeGermline(boolean excludeGermline) {
            this.excludeGermline = excludeGermline;
        }

        public String[] getSelectedTiers() {
            return selectedTiers;
        }

        public void setSelectedTiers(String[] selectedTiers) {
            this.selectedTiers = selectedTiers;
        }

        public CNA[] getCnaTypes() {
            return cnaTypes;
        }

        public void setCnaTypes(CNA[] cnaTypes) {
            this.cnaTypes = cnaTypes;
        }
    }

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
}