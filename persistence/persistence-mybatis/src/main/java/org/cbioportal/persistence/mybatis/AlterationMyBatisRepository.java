package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.AlterationCountByGene;
import org.cbioportal.persistence.AlterationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlterationMyBatisRepository implements AlterationRepository {

    @Autowired
    private AlterationMapper alterationMapper;

	@Override
	public List<AlterationCountByGene> getSampleCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
			List<String> sampleIds, List<Integer> entrezGeneIds, List<String> alterations) {
        
        return alterationMapper.getSampleCountInMultipleMolecularProfiles(molecularProfileIds, sampleIds, entrezGeneIds, null, alterations);
    }
    
    @Override
    public List<AlterationCountByGene> getPatientCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                                List<String> patientIds,
                                                                                List<Integer> entrezGeneIds,
                                                                                List<String> alterations ) {

        return alterationMapper.getPatientCountInMultipleMolecularProfiles(molecularProfileIds, patientIds, entrezGeneIds,
            null, alterations);
    }
}
