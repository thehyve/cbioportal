package org.cbioportal.model;

import java.util.List;

public class AnnotationSourceFilter {
    
    private boolean exludeVus;
    private List<String> selectedTiers;

    public boolean exludeVus() {
        return exludeVus;
    }

    public void setExludeVus(boolean exludeVus) {
        this.exludeVus = exludeVus;
    }

    public List<String> getSelectedTiers() {
        return selectedTiers;
    }

    public void setSelectedTiers(List<String> selectedTiers) {
        this.selectedTiers = selectedTiers;
    }
}
