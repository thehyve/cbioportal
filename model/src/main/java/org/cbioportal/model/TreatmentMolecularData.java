package org.cbioportal.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class TreatmentMolecularData implements Serializable {

    @NotNull
    private String treatmentId;
    @NotNull
    private String sampleId;
    @NotNull
    private String patientId;
    @NotNull
    private String value;
    
    public String getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}