package org.cbioportal.web.util;

import org.cbioportal.model.AlterationFilter;
import org.mockito.ArgumentMatcher;

public class AlterationFilterMockitoArgumentMatcher implements ArgumentMatcher<AlterationFilter> {
    private String checkWhatMutation;
    private String checkWhatCna;

    public AlterationFilterMockitoArgumentMatcher(String checkWhatMutation, String checkWhatCna) {
        this.checkWhatMutation = checkWhatMutation;
        this.checkWhatCna = checkWhatCna;
    }

    @Override
    public boolean matches(AlterationFilter filter) {
        boolean correctMutation;
        boolean correctCna;
        switch (checkWhatMutation) {
            case "ALL":
                correctMutation = filter.getMutationTypeSelect().hasAll();
                break;
            case "EMPTY":
                correctMutation = filter.getMutationTypeSelect().hasNone();
                break;
            case "SOME":
                correctMutation = filter.getMutationTypeSelect().hasValues();
                break;
            default:
                correctMutation = false;
        }
        switch (checkWhatCna) {
            case "ALL":
                correctCna = filter.getCnaTypeSelect().hasAll();
                break;
            case "EMPTY":
                correctCna = filter.getCnaTypeSelect().hasNone();
                break;
            case "SOME":
                correctCna = filter.getCnaTypeSelect().hasValues();
                break;
            default:
                correctCna = false;
        }
        return correctMutation && correctCna;
    }
}
