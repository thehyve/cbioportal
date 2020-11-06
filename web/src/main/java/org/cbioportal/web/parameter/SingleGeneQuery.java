package org.cbioportal.web.parameter;

import org.cbioportal.model.AlterationType;
import org.cbioportal.model.CNA;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

public class SingleGeneQuery implements Serializable {

    @NotNull
    private String hugoGeneSymbol;
    @NotNull
    private AlterationType alterationType;
    private List<CNA> cnaTypes;

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

    public List<CNA> getCnaTypes() {
        return cnaTypes;
    }

    public void setCnaTypes(List<CNA> cnaTypes) {
        this.cnaTypes = cnaTypes;
    }
    
}
