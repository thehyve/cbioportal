package org.cbioportal.web.parameter;

import org.cbioportal.model.CopyNumberAlterationEventType;
import org.cbioportal.model.MutationEventType;

import java.util.List;

public class AlterationEventTypeFilter {
    
    private List<MutationEventType> mutationEventTypes;
    private List<CopyNumberAlterationEventType> copyNumberAlterationEventTypes;

    public List<MutationEventType> getMutationEventTypes() {
        return mutationEventTypes;
    }

    public void setMutationEventTypes(List<MutationEventType> mutationEventTypes) {
        this.mutationEventTypes = mutationEventTypes;
    }

    public List<CopyNumberAlterationEventType> getCopyNumberAlterationEventTypes() {
        return copyNumberAlterationEventTypes;
    }

    public void setCopyNumberAlterationEventTypes(List<CopyNumberAlterationEventType> copyNumberAlterationEventTypes) {
        this.copyNumberAlterationEventTypes = copyNumberAlterationEventTypes;
    }
}
