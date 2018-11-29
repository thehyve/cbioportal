package org.cbioportal.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

public class TreatmentMolecularDataProfile implements Serializable {

    @NotNull
    private String cancerStudyIdentifier;
    @NotNull
    private String molecularProfileId;
    @NotNull
    private String sortOrder;
    @NotNull
    private Float pivotThreshold;
    @NotNull
    private List<TreatmentMolecularData> data = new ArrayList<TreatmentMolecularData>();


	public void setStudyId(String cancerStudyIdentifier) {
        this.cancerStudyIdentifier = cancerStudyIdentifier;
	}
    
	public String getStudyId() {
        return this.cancerStudyIdentifier;
	}
    
    public void setMolecularProfileId(String geneticProfileId) {
        this.molecularProfileId = geneticProfileId;
    }

    public String getMolecularProfileId() {
        return this.molecularProfileId;
    }
    
    public void addDataPoint(TreatmentMolecularData dataPoint) {
        this.data.add(dataPoint);
    }

    public void setDataPoints(List<TreatmentMolecularData> dataPoints) {
        this.data = dataPoints;
    }

    public List<TreatmentMolecularData> getDataPoints() {
        return this.data;
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