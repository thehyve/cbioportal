package org.cbioportal.service;

import java.util.List;
import java.util.Map;

import org.cbioportal.model.AlterationEnrichment;
import org.cbioportal.model.MolecularProfileCaseIdentifier;
import org.cbioportal.model.MutationCountByGene;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;

public interface MutationEnrichmentService {

    Map<String, List<MutationCountByGene>> getmutationCountsbyEntrezGeneIdAndGroup(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        String enrichmentType);

    List<AlterationEnrichment> getMutationEnrichments(
            Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets, String enrichmentType)
            throws MolecularProfileNotFoundException;
}
