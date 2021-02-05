package org.cbioportal.model;

import org.cbioportal.model.util.Select;

import java.io.Serializable;
import java.util.List;

public class GeneFilterQuery extends AlterationFilter implements Serializable {
    
    private String hugoGeneSymbol;
    private Integer entrezGeneId;
    private List<CNA> alterations;

    private boolean includeUnknownTier;

    public GeneFilterQuery() {}

    public GeneFilterQuery(String hugoGeneSymbol,
                           Integer entrezGeneId,
                           List<CNA> alterations,
                           boolean includeDriver,
                           boolean includeVUS,
                           boolean includeUnknownOncogenicity,
                           Select<String> selectedTiers,
                           boolean includeUnknownTier,
                           boolean includeGermline,
                           boolean includeSomatic,
                           boolean includeUnknownStatus) {
        super(includeDriver, includeVUS, includeUnknownOncogenicity, includeGermline, includeSomatic, includeUnknownStatus, selectedTiers, includeUnknownTier);
        this.hugoGeneSymbol = hugoGeneSymbol;
        this.entrezGeneId = entrezGeneId;
        this.alterations = alterations;
    }

    public String getHugoGeneSymbol() {
        return hugoGeneSymbol;
    }

    public void setHugoGeneSymbol(String hugoGeneSymbol) {
        this.hugoGeneSymbol = hugoGeneSymbol;
    }

    public Integer getEntrezGeneId() {
        return entrezGeneId;
    }

    public void setEntrezGeneId(int entrezGeneId) {
        this.entrezGeneId = entrezGeneId;
    }

    public List<CNA> getAlterations() {
        return alterations;
    }

    public void setAlterations(List<CNA> alterations) {
        this.alterations = alterations;
    }

    public boolean getIncludeUnknownTier() {
        return includeUnknownTier;
    }

    public void setIncludeUnknownTier(boolean includeUnknownTier) {
        this.includeUnknownTier = includeUnknownTier;
    }
    
}
