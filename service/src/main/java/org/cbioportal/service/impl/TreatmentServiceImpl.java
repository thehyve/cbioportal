package org.cbioportal.service.impl;

import java.util.List;

import org.cbioportal.model.Treatment;
import org.cbioportal.model.meta.BaseMeta;
import org.cbioportal.persistence.TreatmentRepository;
import org.cbioportal.service.TreatmentService;
import org.cbioportal.service.exception.TreatmentNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentServiceImpl implements TreatmentService {
	
	
	@Autowired
	private TreatmentRepository treatmentRepository;
	
	
	@Override
	public List<Treatment> getAllTreatments(String projection, Integer pageSize, Integer pageNumber) {
		List<Treatment> treatmentList = treatmentRepository.getAllTreatments(projection, pageSize, pageNumber);
		return treatmentList;
	}
	
	@Override
	public BaseMeta getMetaTreatments() {
		return treatmentRepository.getMetaTreatments();
	}
	
	@Override
	public Treatment getTreatment(String treatmentId) throws TreatmentNotFoundException {
		
		Treatment treatment = treatmentRepository.getTreatmentByStableId(treatmentId);
		
		if (treatment == null) {
			throw new TreatmentNotFoundException(treatmentId);
		}

		return treatment;
	}
	
	@Override
	public List<Treatment> fetchTreatments(List<String> treatmentIds) {
		return treatmentRepository.fetchTreatments(treatmentIds);
	}
}