package org.cbioportal.model;

import jakarta.validation.constraints.NotNull;

public class SingleCellExpression extends Alteration {

    @NotNull
    private String tissue;
    @NotNull
    private String cellType;
    @NotNull
    private Float expressionValue;

    public String getTissue() {
        return tissue;
    }
    public void setTissue(String tissue) {
        this.tissue = tissue;
    }
    public String getCellType() {
        return cellType;
    }
    public void setCellType(String cellType) {
        this.cellType = cellType;
    }
    public Float getExpressionValue() {
        return expressionValue;
    }
    public void setExpressionValue(Float expressionValue) {
        this.expressionValue = expressionValue;
    }

}