package org.cbioportal.service;

import java.util.List;

import org.cbioportal.model.Treatment;
import org.cbioportal.model.meta.BaseMeta;
import org.cbioportal.service.exception.TreatmentNotFoundException;

public interface TreatmentService {
    List<Treatment> getAllTreatments(String projection, Integer pageSize, Integer pageNumber);
    BaseMeta getMetaTreatments();
    Treatment getTreatment(String treatmentId) throws TreatmentNotFoundException;
    List<Treatment> fetchTreatments(List<String> treatmentIds);
}