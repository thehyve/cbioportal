package org.cbioportal.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    private final CustomDataService customDataService;

    @Autowired
    private ClinicalDataEqualityFilterApplier clinicalDataEqualityFilterApplier;
    @Autowired
    private ClinicalDataIntervalFilterApplier clinicalDataIntervalFilterApplier;
    
    @Autowired
    public CustomDataFilterApplier(
        PatientService patientService, 
        ClinicalDataService clinicalDataService,
        StudyViewFilterUtil studyViewFilterUtil, 
        CustomDataService customDataService
    ) {
        super(patientService, clinicalDataService, studyViewFilterUtil);
        this.customDataService = customDataService;
    }

    @Override
    public List<SampleIdentifier> apply(List<SampleIdentifier> sampleIdentifiers,
            List<ClinicalDataFilter> customDataFilters, Boolean negateFilters) {
        if (customDataFilters.isEmpty() || sampleIdentifiers.isEmpty()) {
            return sampleIdentifiers;
        }

        final List<String> attributeIds = customDataFilters.stream()
            .map(customDataFilter -> customDataFilter.getAttributeId())
            .collect(Collectors.toList());

        final Map<String, CustomDataSession> customDataSessions = customDataService.getCustomDataSessions(attributeIds);

        Map<String, CustomDataSession> customDataSessionById = customDataSessions.values().stream()
            .collect(Collectors.toMap(CustomDataSession::getId, Function.identity()));

        // Custom data entry by key1=studyId; key2=sampleId and key3=sessionId:
        MultiKeyMap<String, String> customDataMap = new MultiKeyMap<>();

        customDataSessionById.values().forEach(customDataSession -> {
            customDataSession.getData().getData().forEach(datum -> {
                String value = datum.getValue().toUpperCase();
                if (value.equals("NAN") || value.equals("N/A")) {
                    value = "NA";
                }
                customDataMap.put(datum.getStudyId(), datum.getSampleId(), customDataSession.getId(), value);
            });
        });

        return filterCustomData(
            customDataFilters, 
            negateFilters, 
            sampleIdentifiers, 
            customDataSessionById,
            customDataMap
        );
    }
    
    private List<SampleIdentifier> filterCustomData(
        List<ClinicalDataFilter> customDataFilters,
        Boolean negateFilters,
        List<SampleIdentifier> sampleIdentifiers,
        Map<String, CustomDataSession> customDataSessionById,
        MultiKeyMap<String, String> clinicalDataMap
    ) {
        List<ClinicalDataFilter> customDataEqualityFilters = new ArrayList<>();
        List<ClinicalDataFilter> customDataIntervalFilters = new ArrayList<>();
        
        customDataFilters.forEach(filter -> {
            String attributeId = filter.getAttributeId();
            if (!customDataSessionById.containsKey(attributeId)) {
                return;
            }
            if (customDataSessionById
                .get(attributeId)
                .getData()
                .getDatatype()
                .equals("STRING")
            ) {
                customDataEqualityFilters.add(filter);
            } else {
                customDataIntervalFilters.add(filter);
            }
        });

        List<SampleIdentifier> filtered = new ArrayList<>();
        sampleIdentifiers.forEach(sampleIdentifier -> {
            int customEqualityFilterCount = clinicalDataEqualityFilterApplier.apply(customDataEqualityFilters, clinicalDataMap,
                sampleIdentifier.getSampleId(), sampleIdentifier.getStudyId(), negateFilters);
            int customIntervalFilterCount = clinicalDataIntervalFilterApplier.apply(customDataIntervalFilters, clinicalDataMap,
                sampleIdentifier.getSampleId(), sampleIdentifier.getStudyId(), negateFilters);
            if (customEqualityFilterCount == customDataEqualityFilters.size() 
                && customIntervalFilterCount == customDataIntervalFilters.size()
            ) {
                filtered.add(sampleIdentifier);
            }
        });
        
        return filtered;
    }
    
}
