package org.cbioportal.service;

import org.cbioportal.model.AlterationCountByGene;

import java.util.List;

public interface AlterationService {
    
    List<AlterationCountByGene> getSampleCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                        List<String> sampleIds,
                                                                        List<Integer> entrezGeneIds,
                                                                        boolean includeFrequency,
                                                                        boolean includeMissingAlterationsFromGenePanel,
                                                                        List<String> alterations);

    List<AlterationCountByGene> getPatientCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                        List<String> patientIds,
                                                                        List<Integer> entrezGeneIds,
                                                                        boolean includeFrequency,
                                                                        boolean includeMissingAlterationsFromGenePanel,
                                                                        List<String> alterations);
}
