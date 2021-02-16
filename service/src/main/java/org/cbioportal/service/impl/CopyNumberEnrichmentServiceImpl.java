package org.cbioportal.service.impl;

import org.cbioportal.model.AlterationEnrichment;
import org.cbioportal.model.AlterationFilter;
import org.cbioportal.model.CopyNumberCountByGene;
import org.cbioportal.model.EnrichmentType;
import org.cbioportal.model.MolecularProfileCaseIdentifier;
import org.cbioportal.model.util.Select;
import org.cbioportal.service.AlterationCountService;
import org.cbioportal.service.CopyNumberEnrichmentService;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.service.util.AlterationEnrichmentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        EnrichmentType enrichmentType,
        AlterationFilter alterationFilter) throws MolecularProfileNotFoundException {

        Map<String, List<CopyNumberCountByGene>> copyNumberCountByGeneAndGroup = getCopyNumberCountByGeneAndGroup(
            molecularProfileCaseSets,
            enrichmentType,
            alterationFilter);

        return alterationEnrichmentUtil
            .createAlterationEnrichments(
                copyNumberCountByGeneAndGroup,
                molecularProfileCaseSets);
    }

    public Map<String, List<CopyNumberCountByGene>> getCopyNumberCountByGeneAndGroup(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        EnrichmentType enrichmentType,
        AlterationFilter alterationFilter) {
        return molecularProfileCaseSets
            .entrySet()
            .stream()
            .collect(Collectors.toMap(
                entry -> entry.getKey(),
                entry -> { //set value of each group to list of CopyNumberCountByGene

                    if (enrichmentType.name().equals("SAMPLE")) {
                        return alterationCountService.getSampleCnaCounts(
                            entry.getValue(),
                            Select.all(),
                            true,
                            true,
                            alterationFilter);
                    } else {
                        return alterationCountService.getPatientCnaCounts(
                            entry.getValue(),
                            Select.all(),
                            true,
                            true,
                            alterationFilter);
                    }
                }));
    }
    
}
