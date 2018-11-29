package org.cbioportal.persistence.mybatis;

import java.util.List;

import org.cbioportal.model.Treatment;
import org.cbioportal.model.meta.BaseMeta;
import org.cbioportal.persistence.PersistenceConstants;
import org.cbioportal.persistence.TreatmentRepository;
import org.cbioportal.persistence.mybatis.util.OffsetCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class TreatmentMyBatisRepository implements TreatmentRepository {

    @Autowired
    TreatmentMapper treatmentMapper;
    @Autowired
    private OffsetCalculator offsetCalculator;

	@Override
	public List<Treatment> getAllTreatments(String projection, Integer pageSize, Integer pageNumber) {
		return treatmentMapper.getTreatments(projection, pageSize, offsetCalculator.calculate(pageSize, pageNumber), "STABLE_ID", "ASC");
	}

	@Override
	public BaseMeta getMetaTreatments() {
		return treatmentMapper.getMetaTreatments();
	}

	@Override
	public Treatment getTreatmentByStableId(String treatmentId) {
		return treatmentMapper.getTreatmentByStableId(treatmentId, PersistenceConstants.DETAILED_PROJECTION);
	}
	
	@Override
	public List<Treatment> fetchTreatments(List<String> treatmentIds) {
		return treatmentMapper.fetchTreatments(treatmentIds);
	}

}