package org.cbioportal.persistence;

import java.util.List;

import org.cbioportal.model.Treatment;
import org.cbioportal.model.meta.BaseMeta;

public interface TreatmentRepository {
    List<Treatment> getAllTreatments(String projection, Integer pageSize, Integer pageNumber);
    BaseMeta getMetaTreatments();
	Treatment getTreatmentByStableId(String treatmentId);
    List<Treatment> fetchTreatments(List<String> treatmentIds);
}