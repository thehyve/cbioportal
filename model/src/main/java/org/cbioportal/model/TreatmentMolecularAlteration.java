package org.cbioportal.model;

import java.io.Serializable;

public class TreatmentMolecularAlteration extends MolecularAlteration implements Serializable {
    
    private String treatmentId;
    private Float pivotThreshold;
    private String sortOrder;

    /**
     * @return the treatmentId
     */
    public String getTreatmentId() {
        return treatmentId;
    }

    /**
     * @param treatmentId the treatmentId to set
     */
    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }
    
    /**
     * @return the sortOrder
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * @param sortOrder the sortOrder to set
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * @return the pivotThreshold
     */
    public float getPivotThreshold() {
        return pivotThreshold;
    }

    /**
     * @param pivotThreshold the pivotThreshold to set
     */
    public void setPivotThreshold(float pivotThreshold) {
        this.pivotThreshold = pivotThreshold;
    }
}
