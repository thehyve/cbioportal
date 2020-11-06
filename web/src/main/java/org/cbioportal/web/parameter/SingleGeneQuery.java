package org.cbioportal.web.parameter;

import org.cbioportal.model.CNA;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class SingleGeneQuery implements Serializable {

    @NotNull
    private String hugoGeneSymbol;
    private List<CNA> cnaTypes;

    public String getHugoGeneSymbol() {
        return hugoGeneSymbol;
    }

    public void setHugoGeneSymbol(String hugoGeneSymbol) {
        this.hugoGeneSymbol = hugoGeneSymbol;
    }

    public List<CNA> getCnaTypes() {
        return cnaTypes;
    }

    public void setCnaTypes(List<CNA> cnaTypes) {
        this.cnaTypes = cnaTypes;
    }
    
}
