package org.cbioportal.service;

import org.cbioportal.model.*;
import org.cbioportal.model.util.Select;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;

import java.util.List;
import java.util.Map;

public interface AlterationEnrichmentService {

    List<AlterationEnrichment> getAlterationEnrichments(
        Map<String, List<MolecularProfileCaseIdentifier>> molecularProfileCaseSets,
        final Select<MutationEventType> mutationEventTypes,
        final Select<CopyNumberAlterationEventType> cnaEventTypes,
        EnrichmentScope enrichmentScope,
        boolean searchFusions,
        boolean exludeVUS,
        List<String> selectedTiers,
        boolean excludeGermline)
        throws MolecularProfileNotFoundException;
}
