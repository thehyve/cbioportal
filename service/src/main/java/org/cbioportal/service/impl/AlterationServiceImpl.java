package org.cbioportal.service.impl;

import org.cbioportal.model.*;
import org.cbioportal.persistence.AlterationRepository;
import org.cbioportal.service.*;
import org.cbioportal.service.util.AlterationEnrichmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AlterationServiceImpl implements AlterationService {

    @Autowired
    private AlterationRepository alterationRepository;
    @Autowired
    private AlterationEnrichmentUtil<AlterationCountByGene> alterationEnrichmentUtil;

    @Override
	public List<AlterationCountByGene> getSampleCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
			List<String> sampleIds, List<Integer> entrezGeneIds, boolean includeFrequency,
            boolean includeMissingAlterationsFromGenePanel, List<String> alterations) {
        
        List<AlterationCountByGene> alterationCountByGenes;
        if (molecularProfileIds.isEmpty()) {
            alterationCountByGenes = Collections.emptyList();
        } else {
            alterationCountByGenes = alterationRepository.getSampleCountInMultipleMolecularProfiles(
                molecularProfileIds, sampleIds, entrezGeneIds, alterations);
            if (includeFrequency) {
                alterationEnrichmentUtil.includeFrequencyForSamples(molecularProfileIds, sampleIds, alterationCountByGenes, includeMissingAlterationsFromGenePanel);
            }
        }

        return alterationCountByGenes;
    }
    
    @Override
    public List<AlterationCountByGene> getPatientCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                                List<String> patientIds,
                                                                                List<Integer> entrezGeneIds,
                                                                                boolean includeFrequency,
                                                                                boolean includeMissingAlterationsFromGenePanel,
                                                                                List<String> alterations) {
        
        List<AlterationCountByGene> alterationCountByGenes;
        if (molecularProfileIds.isEmpty()) {
            alterationCountByGenes = Collections.emptyList();
        } else {
            alterationCountByGenes = alterationRepository.getPatientCountInMultipleMolecularProfiles(molecularProfileIds, patientIds,
                    entrezGeneIds, alterations);
            if (includeFrequency) {
                alterationEnrichmentUtil.includeFrequencyForPatients(molecularProfileIds, patientIds, alterationCountByGenes, includeMissingAlterationsFromGenePanel);
            }
        }

        return alterationCountByGenes;
    }
}
