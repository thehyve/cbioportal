package org.cbioportal.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cbioportal.model.AlterationCountByGene;
import org.cbioportal.model.AlterationEnrichment;
import org.cbioportal.model.MolecularProfileCaseIdentifier;
import org.cbioportal.service.AlterationEnrichmentService;
import org.cbioportal.service.AlterationService;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.service.util.AlterationEnrichmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlterationEnrichmentServiceImpl implements AlterationEnrichmentService {

    @Autowired
    private AlterationService alterationService;
    @Autowired
    private AlterationEnrichmentUtil<AlterationCountByGene> alterationEnrichmentUtil;

    @Override
    public Map<String, List<AlterationCountByGene>> getAlterationCountsbyEntrezGeneIdAndGroup(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        List<String> alterationEnrichmentEventType,
        String enrichmentType) {
        return molecularProfileCaseSets
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                    entry -> entry.getKey(),
                    entry -> { //set value of each group to list of MutationCountByGene
                        List<String> molecularProfileIds = new ArrayList<>();
                        List<String> sampleIds = new ArrayList<>();

                        entry.getValue().forEach(molecularProfileCase -> {
                            molecularProfileIds.add(molecularProfileCase.getMolecularProfileId());
                            sampleIds.add(molecularProfileCase.getCaseId());
                        });

                        if (enrichmentType.equals("SAMPLE")) {
                            return alterationService
                                    .getSampleCountInMultipleMolecularProfiles(molecularProfileIds,
                                            sampleIds,
                                            null,
                                            true,
                                            true,
                                            alterationEnrichmentEventType);
                        } else {
                            return alterationService
                                    .getPatientCountInMultipleMolecularProfiles(molecularProfileIds,
                                            sampleIds,
                                            null,
                                            true,
                                            true,
                                            alterationEnrichmentEventType);
                        }
                    }));
    }

    @Override
    public List<AlterationEnrichment> getAlterationEnrichments(
            Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets, List<String> alterationEnrichmentEventType,
            String enrichmentType)
            throws MolecularProfileNotFoundException {

        Map<String, List<AlterationCountByGene>> alterationCountsbyEntrezGeneIdAndGroup = getAlterationCountsbyEntrezGeneIdAndGroup(
            molecularProfileCaseSets, alterationEnrichmentEventType, enrichmentType);

        return alterationEnrichmentUtil.createAlterationEnrichments(alterationCountsbyEntrezGeneIdAndGroup,
                molecularProfileCaseSets, enrichmentType);
    }
}
