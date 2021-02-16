package org.cbioportal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.cbioportal.model.util.Select;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class MutationStatusAlterationFilter extends DriverAnnotationAlterationFilter implements Serializable {
    
    // Default behavior of filter options is to include everything
    private boolean includeGermline = true;
    private boolean includeSomatic = true;
    private boolean includeUnknownStatus = true;
    
    public MutationStatusAlterationFilter() {}

    public MutationStatusAlterationFilter(
                            boolean includeDriver,
                            boolean includeVUS,
                            boolean includeUnknownOncogenicity,
                            boolean includeGermline,
                            boolean includeSomatic,
                            boolean includeUnknownStatus,
                            Select<String> tiersSelect,
                            boolean includeUnknownTier) {
        super(includeDriver, includeVUS, includeUnknownOncogenicity, tiersSelect, includeUnknownTier);
        this.includeGermline = includeGermline;
        this.includeSomatic = includeSomatic;
        this.includeUnknownStatus = includeUnknownStatus;
    }

    public boolean getIncludeGermline() {
        return includeGermline;
    }

    public void setIncludeGermline(boolean includeGermline) {
        this.includeGermline = includeGermline;
    }

    public boolean getIncludeSomatic() {
        return includeSomatic;
    }

    public void setIncludeSomatic(boolean includeSomatic) {
        this.includeSomatic = includeSomatic;
    }

    public boolean getIncludeUnknownStatus() {
        return includeUnknownStatus;
    }

    public void setIncludeUnknownStatus(boolean includeUnknownStatus) {
        this.includeUnknownStatus = includeUnknownStatus;
    }
    
}
