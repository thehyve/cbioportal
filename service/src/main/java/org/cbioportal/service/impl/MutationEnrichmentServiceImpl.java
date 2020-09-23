package org.cbioportal.service.impl;

import org.cbioportal.model.*;
import org.cbioportal.service.AlterationCountService;
import org.cbioportal.service.MutationEnrichmentService;
import org.cbioportal.service.util.AlterationEnrichmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MutationEnrichmentServiceImpl implements MutationEnrichmentService {

    @Autowired
    private AlterationCountService alterationCountService;
    @Autowired
    private AlterationEnrichmentUtil<AlterationCountByGene> alterationEnrichmentUtil;

    @Override
    public List<AlterationEnrichment> getMutationEnrichments(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        EnrichmentScope enrichmentScope,
        boolean exludeVUS,
        List<String> selectedTiers,
        boolean excludeGermline) {

        Map<String, List<AlterationCountByGene>> mutationCountsbyEntrezGeneIdAndGroup = getMutationCountsbyEntrezGeneIdAndGroup(
            molecularProfileCaseSets, enrichmentScope, exludeVUS, selectedTiers, excludeGermline);

        return alterationEnrichmentUtil.createAlterationEnrichments(mutationCountsbyEntrezGeneIdAndGroup,
            molecularProfileCaseSets);
    }

    public Map<String, List<AlterationCountByGene>> getMutationCountsbyEntrezGeneIdAndGroup(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        EnrichmentScope enrichmentScope,
        boolean exludeVUS,
        List<String> selectedTiers,
        boolean excludeGermline) {
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

                    if (enrichmentScope.name().equals("SAMPLE")) {
                        return alterationCountService.getSampleMutationCounts(
                            entry.getValue(),
                            null,
                            true,
                            true,
                            null,
                            exludeVUS,
                            selectedTiers,
                            excludeGermline);
                    } else {
                        return alterationCountService
                            .getPatientMutationCounts(
                                entry.getValue(),
                                null,
                                true,
                                true,
                                null,
                                exludeVUS,
                                selectedTiers,
                                excludeGermline);
                    }
                }));
    }
}
