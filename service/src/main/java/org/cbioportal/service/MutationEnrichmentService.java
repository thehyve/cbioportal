package org.cbioportal.service;

import org.cbioportal.model.AlterationCountByGene;
import org.cbioportal.model.AlterationEnrichment;
import org.cbioportal.model.EnrichmentScope;
import org.cbioportal.model.MolecularProfileCaseIdentifier;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;

import java.util.List;
import java.util.Map;

public interface MutationEnrichmentService {

    List<AlterationEnrichment> getMutationEnrichments(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        EnrichmentScope enrichmentScope,
        boolean exludeVUS,
        List<String> selectedTiers,
        boolean excludeGermline)
        throws MolecularProfileNotFoundException;
}
