package org.cbioportal.web.parameter;

import java.util.List;

import javax.validation.constraints.Size;

public class MolecularProfileCasesGroupAndAlterationTypeFilter {

    @Size(min = 1, max = PagingConstants.MAX_PAGE_SIZE)
    private List<AlterationEnrichmentEventType> AlterationEnrichmentEventTypes;
    private List<MolecularProfileCasesGroupFilter> MolecularProfileCasesGroupFilter;
 

    public List<MolecularProfileCasesGroupFilter> getMolecularProfileCasesGroupFilter() {
        return MolecularProfileCasesGroupFilter;
    }

    public void setMolecularProfileCasesGroupFilter(
            List<MolecularProfileCasesGroupFilter> molecularProfileCasesGroupFilter) {
                MolecularProfileCasesGroupFilter = molecularProfileCasesGroupFilter;
    }

    public List<AlterationEnrichmentEventType> getAlterationEnrichmentEventTypes() {
        return AlterationEnrichmentEventTypes;
    }

    public void setAlterationEnrichmentEventTypes(
            List<AlterationEnrichmentEventType> alterationEnrichmentEventTypes) {
                AlterationEnrichmentEventTypes = alterationEnrichmentEventTypes;
    }

}
