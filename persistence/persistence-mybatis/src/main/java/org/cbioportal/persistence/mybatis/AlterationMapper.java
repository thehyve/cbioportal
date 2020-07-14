package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.AlterationCountByGene;

import java.util.List;

public interface AlterationMapper {

    List<AlterationCountByGene> getSampleCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                        List<String> sampleIds,
                                                                        List<Integer> entrezGeneIds,
                                                                        Boolean snpOnly,
                                                                        List<String> alterations);
    
    List<AlterationCountByGene> getPatientCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                        List<String> patientIds,
                                                                        List<Integer> entrezGeneIds,
                                                                        Boolean snpOnly,
                                                                        List<String> alterations);
}
