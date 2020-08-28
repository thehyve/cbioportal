package org.cbioportal.web.parameter;

import io.swagger.annotations.ApiModelProperty;
import org.cbioportal.model.MutationEventType;

import java.util.List;

import javax.validation.constraints.Size;

public class MolecularProfileCasesGroupAndAlterationTypeFilter {


    private AlterationEventTypeFilter alterationEventTypes;
    @Size(min = 1, max = PagingConstants.MAX_PAGE_SIZE)
    @ApiModelProperty(required = true)
    private List<MolecularProfileCasesGroupFilter> MolecularProfileCasesGroupFilter;
 

    public List<MolecularProfileCasesGroupFilter> getMolecularProfileCasesGroupFilter() {
        return MolecularProfileCasesGroupFilter;
    }

    public void setMolecularProfileCasesGroupFilter(
            List<MolecularProfileCasesGroupFilter> molecularProfileCasesGroupFilter) {
                MolecularProfileCasesGroupFilter = molecularProfileCasesGroupFilter;
    }

    public AlterationEventTypeFilter getAlterationEventTypes() {
        return alterationEventTypes;
    }

    public void setAlterationEventTypes(AlterationEventTypeFilter alterationEventTypes) {
        this.alterationEventTypes = alterationEventTypes;
    }
}
