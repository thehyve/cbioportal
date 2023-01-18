package org.cbioportal.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.cbioportal.service.ClinicalDataService;
import org.cbioportal.service.CustomDataService;
import org.cbioportal.service.PatientService;
import org.cbioportal.web.parameter.ClinicalDataFilter;
import org.cbioportal.service.util.CustomDataSession;
import org.cbioportal.web.parameter.SampleIdentifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomDataFilterApplier extends ClinicalDataEqualityFilterApplier {

    @Autowired
    public CustomDataFilterApplier(PatientService patientService, ClinicalDataService clinicalDataService,
            StudyViewFilterUtil studyViewFilterUtil) {
        super(patientService, clinicalDataService, studyViewFilterUtil);
    }

    @Autowired
    private CustomDataService customDataService;

    @Override
    public List<SampleIdentifier> apply(List<SampleIdentifier> sampleIdentifiers,
            List<ClinicalDataFilter> customDataFilters, Boolean negateFilters) {
        if (!customDataFilters.isEmpty() && !sampleIdentifiers.isEmpty()) {

            final List<String> attributeIds = customDataFilters.stream()
                .map(customDataFilter -> customDataFilter.getAttributeId())
                .collect(Collectors.toList());

            final Map<String, CustomDataSession> customDataSessions = customDataService.getCustomDataSessions(attributeIds);

            Map<String, CustomDataSession> customDataSessionById = customDataSessions.values().stream()
                .collect(Collectors.toMap(CustomDataSession::getId, Function.identity()));

            MultiKeyMap clinicalDataMap = new MultiKeyMap();
            customDataSessionById.values().stream()
                .forEach(customDataSession -> {
                    customDataSession.getData().getData().forEach(datum -> {
                        String value = datum.getValue().toUpperCase();
                        if (value.equals("NAN") || value.equals("N/A")) {
                            value = "NA";
                        }
                        clinicalDataMap.put(datum.getStudyId(), datum.getSampleId(), customDataSession.getId(), value);
                    });
                });

            List<SampleIdentifier> newSampleIdentifiers = new ArrayList<>();

            sampleIdentifiers.forEach(sampleIdentifier -> {
                int count = apply(customDataFilters, clinicalDataMap,
                        sampleIdentifier.getSampleId(), sampleIdentifier.getStudyId(), negateFilters);

                if (count == customDataFilters.size()) {
                    newSampleIdentifiers.add(sampleIdentifier);
                }
            });

            return newSampleIdentifiers;
        }
        return sampleIdentifiers;
    }

}
