package org.cbioportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.cbioportal.model.util.Select;

import java.io.Serializable;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class AlterationFilter extends MutationStatusAlterationFilter implements Serializable {
    
    private Map<MutationEventType, Boolean> mutationEventTypes;
    private Map<CNA, Boolean> copyNumberAlterationEventTypes;
    
    @JsonIgnore
    private Select<MutationEventType> mutationTypeSelect;
    @JsonIgnore
    private Select<CNA> cnaTypeSelect;

    public AlterationFilter() {}

    public AlterationFilter(Select<MutationEventType> mutationEventTypes,
                            Select<CNA> cnaEventTypes,
                            boolean includeDriver,
                            boolean includeVUS,
                            boolean includeUnknownOncogenicity,
                            boolean includeGermline,
                            boolean includeSomatic,
                            boolean includeUnknownStatus,
                            Select<String> tiersSelect,
                            boolean includeUnknownTier) {
        super(includeDriver, includeVUS, includeUnknownOncogenicity, includeGermline, includeSomatic, includeUnknownStatus, tiersSelect, includeUnknownTier);
        this.mutationTypeSelect = mutationEventTypes;
        this.cnaTypeSelect = cnaEventTypes;
    }

    public void setMutationEventTypes(Map<MutationEventType, Boolean> selectedTypes) {
        this.mutationEventTypes = selectedTypes;
        if (selectedTypes == null)
            this.mutationTypeSelect = Select.all();
        else
            this.mutationTypeSelect = Select.byValues(
                selectedTypes.entrySet().stream()
                    .filter(e -> e.getValue())
                    .map(e -> e.getKey()));
        if (selectedTypes.entrySet().stream().allMatch(e -> e.getValue()))
            this.mutationTypeSelect.hasAll(true);
    }

    public Map<MutationEventType, Boolean> getMutationEventTypes() {
        return mutationEventTypes;
    }

    @JsonIgnore
    public Select<MutationEventType> getMutationTypeSelect() {
        if (this.mutationTypeSelect == null)
            return Select.all();
        return this.mutationTypeSelect;
    }

    @JsonIgnore
    public void setMutationTypeSelect(Select<MutationEventType> typeSelect) {
        this.mutationTypeSelect = typeSelect;
    }
    
    public void setCopyNumberAlterationEventTypes(Map<CNA, Boolean> selectedTypes) {
        this.copyNumberAlterationEventTypes = selectedTypes;
        if (selectedTypes == null)
            this.cnaTypeSelect = Select.all();
        else
            this.cnaTypeSelect = Select.byValues(
                selectedTypes.entrySet().stream()
                    .filter(e -> e.getValue())
                    .map(e -> e.getKey()));
        if (selectedTypes.entrySet().stream().allMatch(e -> e.getValue()))
            this.cnaTypeSelect.hasAll(true);
    }

    @JsonIgnore
    public Select<CNA> getCnaTypeSelect() {
        if (this.cnaTypeSelect == null)
            return Select.all();
        return this.cnaTypeSelect;
    }

    public Map<CNA, Boolean> getCopyNumberAlterationEventTypes() {
        return copyNumberAlterationEventTypes;
    }

    @JsonIgnore
    public void setCnaTypeSelect(Select<CNA> typeSelect) {
        this.cnaTypeSelect = typeSelect;
    }
}
