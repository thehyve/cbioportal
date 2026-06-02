package org.cbioportal.web.mixin;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class NamespaceDataCountMixin {

    @JsonIgnore
    private String outerKey;
    @JsonIgnore
    private String innerKey;
}

