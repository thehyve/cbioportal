package org.cbioportal.web.parameter;

import java.util.Arrays;
import java.util.List;

public enum AlterationEnrichmentEventType {

    HOMDEL("-2"),
    AMP("2"),
    De_novo_Start("De_novo_Start"),
    Frame_Shift_Del("Frame_Shift_Del"),
    Frame_Shift_Ins("Frame_Shift_Ins"),
    Indel("Indel"),
    In_Frame_Del("In_Frame_Del"), 
    In_Frame_Ins("In_Frame_Ins"),
    Missense("Missense"),
    Missense_Mutation("Missense_Mutation"), 
    Nonsense_Mutation("Nonsense_Mutation"), 
    Nonstop_Mutation("Nonstop_Mutation"), 
    Splice_Site("Splice_Site"), 
    Stop_Codon_Del("Stop_Codon_Del"),
    Translation_Start_Site("Translation_Start_Site");

    private List<String> alterationTypes;

    AlterationEnrichmentEventType(String... alterationTypes) {

        this.alterationTypes = Arrays.asList(alterationTypes);
    }

    public List<String> getAlterationTypes() {
        return alterationTypes;
    }
}
