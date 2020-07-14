package org.cbioportal.persistence;

import org.cbioportal.model.AlterationCountByGene;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface AlterationRepository {

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    List<AlterationCountByGene> getSampleCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                            List<String> sampleIds,
                                                                            List<Integer> entrezGeneIds,
                                                                            List<String> alterations);

    @Cacheable(cacheNames = "GeneralRepositoryCache", condition = "@cacheEnabledConfig.getEnabled()")
    List<AlterationCountByGene> getPatientCountInMultipleMolecularProfiles(List<String> molecularProfileIds,
                                                                            List<String> patientIds,
                                                                            List<Integer> entrezGeneIds,
                                                                            List<String> alterations);
}
