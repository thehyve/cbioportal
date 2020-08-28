package org.cbioportal.model;

public class MutationAnnotationSourceFilter extends AnnotationSourceFilter {
    
    private boolean excludeGermline;

    public boolean excludeGermline() {
        return excludeGermline;
    }

    public void setExcludeGermline(boolean excludeGermline) {
        this.excludeGermline = excludeGermline;
    }
    
}
