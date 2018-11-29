package org.cbioportal.service;

import java.util.List;

import org.cbioportal.model.TreatmentMolecularData;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.service.exception.SampleListNotFoundException;

public interface TreatmentDataService {
    List<TreatmentMolecularData> fetchTreatmentData(String geneticProfileId, List<String> sampleIds, List<String> treatmentIds) throws MolecularProfileNotFoundException;
	List<TreatmentMolecularData> fetchTreatmentData(String geneticProfileId, String sampleListId, List<String> treatmentIds) throws MolecularProfileNotFoundException, SampleListNotFoundException;
}