package org.cbioportal.web.parameter.sort;

public enum SingleCellExpressionSortBy {

    geneticProfileId("geneticProfileId"),
    sampleId("sampleId"),
    tissue("tissue"),
    cellType("cellType"),
    entrezGeneId("entrezGeneId"),
    expressionValue("expressionValue");

    private String originalValue;

    SingleCellExpressionSortBy(String originalValue) {
        this.originalValue = originalValue;
    }

    public String getOriginalValue() {
        return originalValue;
    }
}
