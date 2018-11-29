package org.cbioportal.web.mixin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TreatmentMolecularDataProfileMixin {

    @JsonProperty("geneticProfileId")
    private String molecularProfileId;
}