package org.cbioportal.persistence.mybatis;

import java.util.List;

import org.cbioportal.model.Treatment;
import org.cbioportal.model.meta.BaseMeta;

public interface TreatmentMapper {

    List<Treatment> getTreatments(String projection,
                        Integer limit,
                        Integer offset,
                        String sortBy,
                        String direction);

    BaseMeta getMetaTreatments();

    Treatment getTreatmentByInternalId(Integer internalId,
            String projection);
    
    Treatment getTreatmentByStableId(String treatmentId,
                               String projection);

    List<Treatment> fetchTreatments(List<String> treatmentIds);
    
}
