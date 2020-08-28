package org.cbioportal.service.impl;

import org.cbioportal.model.*;
import org.cbioportal.service.AlterationCountService;
import org.cbioportal.service.CopyNumberEnrichmentService;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.service.util.AlterationEnrichmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CopyNumberEnrichmentServiceImpl implements CopyNumberEnrichmentService {

    @Autowired
    private AlterationCountService alterationCountService;
    @Autowired
    private AlterationEnrichmentUtil<CopyNumberCountByGene> alterationEnrichmentUtil;

    @Override
    public List<AlterationEnrichment> getCopyNumberEnrichments(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        CopyNumberAlterationEventType copyNumberEventType,
        EnrichmentScope enrichmentScope,
        boolean exludeVUS,
        List<String> selectedTiers) throws MolecularProfileNotFoundException {

        Map<String, List<CopyNumberCountByGene>> copyNumberCountByGeneAndGroup = getCopyNumberCountByGeneAndGroup(
            molecularProfileCaseSets,
            copyNumberEventType,
            enrichmentScope,
            exludeVUS,
            selectedTiers);

        return alterationEnrichmentUtil
            .createAlterationEnrichments(
                copyNumberCountByGeneAndGroup,
                molecularProfileCaseSets);
    }

    public Map<String, List<CopyNumberCountByGene>> getCopyNumberCountByGeneAndGroup(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        CopyNumberAlterationEventType copyNumberEventType,
        EnrichmentScope enrichmentScope,
        boolean exludeVUS,
        List<String> selectedTiers) {
        return molecularProfileCaseSets
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey(),
                entry -> { //set value of each group to list of CopyNumberCountByGene

                    List<String> molecularProfileIds = new ArrayList<>();
                    List<String> sampleIds = new ArrayList<>();

                    entry.getValue().forEach(molecularProfileCase -> {
                        molecularProfileIds.add(molecularProfileCase.getMolecularProfileId());
                        sampleIds.add(molecularProfileCase.getCaseId());
                    });

                    List<CopyNumberAlterationEventType> cnaTypes = Arrays.asList(copyNumberEventType);

                    if (enrichmentScope.name().equals("SAMPLE")) {
                        return alterationCountService.getSampleCnaCounts(
                            entry.getValue(),
                            null,
                            true,
                            true,
                            cnaTypes,
                            exludeVUS,
                            selectedTiers);
                    } else {
                        return alterationCountService.getPatientCnaCounts(
                            entry.getValue(),
                            null,
                            true,
                            true,
                            cnaTypes,
                            exludeVUS,
                            selectedTiers);
                    }
                }));
    }
    
}
