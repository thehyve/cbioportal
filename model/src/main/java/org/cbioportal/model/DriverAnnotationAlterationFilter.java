package org.cbioportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.cbioportal.model.util.Select;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class DriverAnnotationAlterationFilter implements Serializable {
    
    // Default behavior of filter options is to include everything
    private boolean includeDriver = true;
    private boolean includeVUS = true;
    private boolean includeUnknownOncogenicity = true;
    private Map<String,Boolean> selectedTiers = new HashMap<>();
    private boolean includeUnknownTier = true;
    
    @JsonIgnore
    private Select<String> tiersSelect;

    public DriverAnnotationAlterationFilter() {}

    public DriverAnnotationAlterationFilter(boolean includeDriver,
                                            boolean includeVUS,
                                            boolean includeUnknownOncogenicity,
                                            Select<String> tiersSelect,
                                            boolean includeUnknownTier) {
        this.includeDriver = includeDriver;
        this.includeVUS = includeVUS;
        this.includeUnknownOncogenicity = includeUnknownOncogenicity;
        this.tiersSelect = tiersSelect;
        this.includeUnknownTier = includeUnknownTier;
    }

    public boolean getIncludeDriver() {
        return includeDriver;
    }

    public void setIncludeDriver(boolean includeDriver) {
        this.includeDriver = includeDriver;
    }

    public boolean getIncludeVUS() {
        return includeVUS;
    }

    public void setIncludeVUS(boolean includeVUS) {
        this.includeVUS = includeVUS;
    }

    public boolean getIncludeUnknownOncogenicity() {
        return includeUnknownOncogenicity;
    }

    public void setIncludeUnknownOncogenicity(boolean includeUnknownOncogenicity) {
        this.includeUnknownOncogenicity = includeUnknownOncogenicity;
    }
    
    public boolean getIncludeUnknownTier() {
        return includeUnknownTier;
    }

    public void setIncludeUnknownTier(boolean includeUnknownTier) {
        this.includeUnknownTier = includeUnknownTier;
    }

    public void setSelectedTiers(Map<String, Boolean> selectedTiers) {
        if (selectedTiers == null)
            this.tiersSelect = Select.all();
        else
            this.tiersSelect = Select.byValues(
                selectedTiers.entrySet().stream()
                    .filter(e -> e.getValue())
                    .map(e -> e.getKey()));
        if (selectedTiers.entrySet().stream().allMatch(e -> e.getValue()))
            this.tiersSelect.hasAll(true);
    }

    @JsonIgnore
    public Select<String> getSelectedTiers() {
        if (tiersSelect == null)
            return Select.all();
        return tiersSelect;
    }

    @JsonIgnore
    public void setTiersSelect(Select<String> tiersSelect) {
        this.tiersSelect = tiersSelect;
    }
    
}
