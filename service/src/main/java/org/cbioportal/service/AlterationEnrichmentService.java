package org.cbioportal.service;

import java.util.List;
import java.util.Map;

import org.cbioportal.model.AlterationCountByGene;
import org.cbioportal.model.AlterationEnrichment;
import org.cbioportal.model.MolecularProfileCaseIdentifier;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;

public interface AlterationEnrichmentService {

    Map<String, List<AlterationCountByGene>> getAlterationCountsbyEntrezGeneIdAndGroup(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        List<String> alterationEnrichmentEventType, String enrichmentType);

    List<AlterationEnrichment> getAlterationEnrichments(
            Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets, List<String> alterationEnrichmentEventType, String enrichmentType)
            throws MolecularProfileNotFoundException;
}
