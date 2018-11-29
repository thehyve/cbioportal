package org.cbioportal.service;

import java.util.List;

import org.cbioportal.model.TreatmentMolecularDataProfile;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.service.exception.SampleListNotFoundException;

public interface TreatmentDataService {
    TreatmentMolecularDataProfile fetchTreatmentData(String geneticProfileId, List<String> sampleIds, List<String> treatmentIds) throws MolecularProfileNotFoundException;
	TreatmentMolecularDataProfile fetchTreatmentData(String geneticProfileId, String sampleListId, List<String> treatmentIds) throws MolecularProfileNotFoundException, SampleListNotFoundException;
}