package org.cbioportal.model;

import java.io.Serializable;

public class TreatmentMolecularData extends MolecularData implements Serializable {

    private String treatmentId;
    private String sortOrder;
    private Float pivotThreshold;
    
    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

	public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
	}

	public void setPivotThreshold(Float pivotThreshold) {
        this.pivotThreshold = pivotThreshold;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public Float getPivotThreshold() {
        return pivotThreshold;
    }

}