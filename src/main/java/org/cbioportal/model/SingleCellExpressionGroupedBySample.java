package org.cbioportal.model;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.validation.constraints.NotNull;

public class SingleCellExpressionGroupedBySample extends UniqueKeyBase {

    @NotNull
    private String molecularProfileId;
    @NotNull
    private String sampleId;
    @NotNull
    private String patientId;
    @NotNull
    private String studyId;
    @NotNull
    private List<SingleCellExpressionGroupedByTissue> singleCellExpressionGroupedByTissues;

    public SingleCellExpressionGroupedBySample(String sampleId, List<SingleCellExpression> singleCellExpressions) {
        // TODO: do a check if all are equal?
        this.sampleId = sampleId;
        this.molecularProfileId = singleCellExpressions.get(0).getMolecularProfileId();
        this.patientId = singleCellExpressions.get(0).getPatientId();
        this.studyId = singleCellExpressions.get(0).getStudyId();

        Map<String, List<SingleCellExpression>> tissueMap = SingleCellExpressionGroupedBySample
            .createSingleCellExpressionGroupedMap(singleCellExpressions, "tissue");
        this.singleCellExpressionGroupedByTissues = tissueMap.entrySet().stream()
            .map(e -> new SingleCellExpressionGroupedByTissue(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    public String getMolecularProfileId() {
        return molecularProfileId;
    }
    public void setMolecularProfileId(String molecularProfileId) {
        this.molecularProfileId = molecularProfileId;
    }
    public String getSampleId() {
        return sampleId;
    }
    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }
    public String getPatientId() {
        return patientId;
    }
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
    public String getStudyId() {
        return studyId;
    }
    public void setStudyId(String studyId) {
        this.studyId = studyId;
    }
    public List<SingleCellExpressionGroupedByTissue> getSingleCellExpressionGroupedByTissues() {
        return singleCellExpressionGroupedByTissues;
    }
    public void setSingleCellExpressionGroupedByTissues(
            List<SingleCellExpressionGroupedByTissue> singleCellExpressionGroupedByTissues) {
        this.singleCellExpressionGroupedByTissues = singleCellExpressionGroupedByTissues;
    }

    class SingleCellExpressionGroupedByTissue {
        @NotNull
        private String tissue;
        @NotNull
        private List<SingleCellExpressionGroupedByCellType> singleCellExpressionGroupedByCellTypes;

        public SingleCellExpressionGroupedByTissue(String tissue,
                List<SingleCellExpression> singleCellExpressions) {
            this.tissue = tissue;

            Map<String, List<SingleCellExpression>> cellTypeMap = SingleCellExpressionGroupedBySample
            .createSingleCellExpressionGroupedMap(singleCellExpressions, "cellType");

            this.singleCellExpressionGroupedByCellTypes = cellTypeMap.entrySet().stream()
                .map(e -> new SingleCellExpressionGroupedByCellType(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
        }
        public String getTissue() {
            return tissue;
        }
        public void setTissue(String tissue) {
            this.tissue = tissue;
        }
        public List<SingleCellExpressionGroupedByCellType> getSingleCellExpressionGroupedByCellTypes() {
            return singleCellExpressionGroupedByCellTypes;
        }
        public void setSingleCellExpressionGroupedByCellTypes(
                List<SingleCellExpressionGroupedByCellType> singleCellExpressionGroupedByCellTypes) {
            this.singleCellExpressionGroupedByCellTypes = singleCellExpressionGroupedByCellTypes;
        }

        class SingleCellExpressionGroupedByCellType {
            @NotNull
            private String cellType;
            @NotNull
            private List<geneExpression> geneExpressions;

            public SingleCellExpressionGroupedByCellType(String cellType,
                    List<SingleCellExpression> singleCellExpressions) {
                this.cellType = cellType;

                this.geneExpressions = singleCellExpressions.stream()
                    .map(e -> new geneExpression(e.getEntrezGeneId(), e.getExpressionValue()))
                    .collect(Collectors.toList());
            }
            public String getCellType() {
                return cellType;
            }
            public void setCellType(String cellType) {
                this.cellType = cellType;
            }
            public List<geneExpression> getGeneExpressions() {
                return geneExpressions;
            }
            public void setGeneExpressions(List<geneExpression> geneExpressions) {
                this.geneExpressions = geneExpressions;
            }

            class geneExpression {
                @NotNull
                private Integer entrezGeneId;
                private Float expressionValue;

                public geneExpression(Integer entrezGeneId, Float expressionValue) {
                    this.entrezGeneId = entrezGeneId;
                    this.expressionValue = expressionValue;
                }

                public Integer getEntrezGeneId() {
                    return entrezGeneId;
                }
                public void setEntrezGeneId(Integer entrezGeneId) {
                    this.entrezGeneId = entrezGeneId;
                }
                public Float getExpressionValue() {
                    return expressionValue;
                }
                public void setExpressionValue(Float expressionValue) {
                    this.expressionValue = expressionValue;
                }
            }
        }
    }

    public static Map<String, List<SingleCellExpression>> createSingleCellExpressionGroupedMap(List<SingleCellExpression> singleCellExpressionData, String sortBy) {
        // singleCellExpressionData NEEDS TO BE SORTED in order for this to work correctly.
        // This is done this way in order to improve efficiency. Sorting is done in the DB query,
        // and indices on the relevant columns increase query speed
        Map<String, List<SingleCellExpression>> singleCellExpressionGroupedBy = new LinkedHashMap<>();
        String currentValue = "";
        String forLoopValue = "";
        List<SingleCellExpression> singleCellExpressionList = new LinkedList<>();
        for (SingleCellExpression singleCellExpression : singleCellExpressionData) {
            switch (sortBy) {
                case "sampleId":
                    forLoopValue = singleCellExpression.getSampleId();
                    break;
                case "tissue":
                    forLoopValue = singleCellExpression.getTissue();
                    break;
                case "cellType":
                    forLoopValue = singleCellExpression.getCellType();
                    break;
                default:
                    throw new RuntimeException("sortBy option " + sortBy + " not supported.");
            }
            if (currentValue.equals("")) {
                currentValue = forLoopValue;
                singleCellExpressionList = new LinkedList<>();
            }
            if (!currentValue.equals(forLoopValue)) {
                singleCellExpressionGroupedBy.put(currentValue, singleCellExpressionList);
                currentValue = forLoopValue;
                singleCellExpressionList = new LinkedList<>();
            }
            singleCellExpressionList.add(singleCellExpression);
        }
        if (!currentValue.equals("")) {
            singleCellExpressionGroupedBy.put(currentValue, singleCellExpressionList);
        }
        return singleCellExpressionGroupedBy;
    }
}