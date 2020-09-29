package org.cbioportal.service.impl;

import org.cbioportal.model.*;
import org.cbioportal.model.util.Select;
import org.cbioportal.service.AlterationEnrichmentService;
import org.cbioportal.service.AlterationCountService;
import org.cbioportal.service.util.AlterationEnrichmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AlterationEnrichmentServiceImpl implements AlterationEnrichmentService {

    @Autowired
    private AlterationCountService alterationCountService;
    @Autowired
    private AlterationEnrichmentUtil<AlterationCountByGene> alterationEnrichmentUtil;

    @Override
    public List<AlterationEnrichment> getAlterationEnrichments(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets, final Select<MutationEventType> mutationEventTypes,
        final Select<CopyNumberAlterationEventType> cnaEventTypes, EnrichmentScope enrichmentScope, boolean searchFusions, boolean excludeVUS, List<String> selectedTiers, boolean excludeGermline) {

        Map<String, List<AlterationCountByGene>> alterationCountsbyEntrezGeneIdAndGroup = getAlterationCountsbyEntrezGeneIdAndGroup(
            molecularProfileCaseSets, mutationEventTypes, cnaEventTypes, enrichmentScope, searchFusions, excludeVUS, selectedTiers, excludeGermline);

        return alterationEnrichmentUtil.createAlterationEnrichments(alterationCountsbyEntrezGeneIdAndGroup,
                molecularProfileCaseSets);
    }

    public Map<String, List<AlterationCountByGene>> getAlterationCountsbyEntrezGeneIdAndGroup(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        Select<MutationEventType> mutationEventTypes,
        Select<CopyNumberAlterationEventType> cnaEventTypes,
        EnrichmentScope enrichmentType,
        boolean searchFusions,
        boolean exludeVUS,
        List<String> selectedTiers,
        boolean excludeGermline) {
        return molecularProfileCaseSets
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey(), // group name
                entry -> {               // group counts

                    if (enrichmentType.equals(EnrichmentScope.SAMPLE)) {
                        return alterationCountService
                            .getSampleAlterationCounts(
                                entry.getValue(),
                                null,
                                true,
                                true,
                                mutationEventTypes,
                                cnaEventTypes,
                                searchFusions,
                                exludeVUS,
                                selectedTiers,
                                excludeGermline);
                    } else {
                        return alterationCountService
                            .getPatientAlterationCounts(
                                entry.getValue(),
                                null,
                                true,
                                true,
                                mutationEventTypes,
                                cnaEventTypes,
                                searchFusions,
                                exludeVUS,
                                selectedTiers,
                                excludeGermline);
                    }
                }));
    }
}
