package org.cbioportal.web.util;

import org.cbioportal.model.ClinicalAttribute;
import org.cbioportal.model.ClinicalData;
import org.cbioportal.model.ClinicalDataBin;
import org.cbioportal.service.ClinicalAttributeService;
import org.cbioportal.service.CustomDataService;
import org.cbioportal.service.PatientService;
import org.cbioportal.service.util.ClinicalAttributeUtil;
import org.cbioportal.service.util.CustomAttributeWithData;
import org.cbioportal.service.util.CustomDataSession;
import org.cbioportal.service.util.CustomDataValue;
import org.cbioportal.web.parameter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ClinicalDataBinUtil {

    @Autowired
    private StudyViewFilterApplier studyViewFilterApplier;
    @Autowired
    private ClinicalDataFetcher clinicalDataFetcher;
    @Autowired
    private DataBinner dataBinner;
    @Autowired
    private StudyViewFilterUtil studyViewFilterUtil;
    @Autowired
    private ClinicalAttributeService clinicalAttributeService;
    @Autowired
    private ClinicalAttributeUtil clinicalAttributeUtil;
    @Autowired
    private PatientService patientService;
    @Autowired
    private CustomDataService customDataService;

    public StudyViewFilter removeSelfFromFilter(ClinicalDataBinCountFilter dataBinCountFilter) {
        List<ClinicalDataBinFilter> attributes = dataBinCountFilter.getAttributes();
        StudyViewFilter studyViewFilter = dataBinCountFilter.getStudyViewFilter();

        if (attributes.size() == 1) {
            studyViewFilterUtil.removeSelfFromFilter(attributes.get(0).getAttributeId(), studyViewFilter);
        }

        return studyViewFilter;
    }

    public List<ClinicalDataBin> fetchClinicalDataBinCounts(
        DataBinMethod dataBinMethod,
        ClinicalDataBinCountFilter dataBinCountFilter
    ) {
        return this.fetchClinicalDataBinCounts(
            dataBinMethod,
            dataBinCountFilter,
            // by default call the method to remove self from filter
            true
        );
    }

    public List<ClinicalDataBin> fetchClinicalDataBinCounts(
        DataBinMethod dataBinMethod,
        ClinicalDataBinCountFilter dataBinCountFilter,
        boolean shouldRemoveSelfFromFilter
    ) {
        List<ClinicalDataBinFilter> attributes = dataBinCountFilter.getAttributes();
        StudyViewFilter studyViewFilter = dataBinCountFilter.getStudyViewFilter();

        if (shouldRemoveSelfFromFilter) {
            studyViewFilter = removeSelfFromFilter(dataBinCountFilter);
        }

        // get attribute ids
        List<String> attributeIds = attributes.stream().map(ClinicalDataBinFilter::getAttributeId).collect(Collectors.toList());

        // filter only by study id and sample identifiers, ignore rest
        List<SampleIdentifier> unfilteredSampleIdentifiers = filterByStudyAndSample(studyViewFilter);

        List<String> unfilteredStudyIds = new ArrayList<>();
        List<String> unfilteredSampleIds = new ArrayList<>();
        List<String> unfilteredPatientIds = new ArrayList<>();
        List<String> studyIdsOfUnfilteredPatients = new ArrayList<>();
        List<String> unfilteredUniqueSampleKeys = new ArrayList<>();
        List<String> unfilteredUniquePatientKeys = new ArrayList<>();
        List<String> unfilteredSampleAttributeIds = new ArrayList<>();
        List<String> unfilteredPatientAttributeIds = new ArrayList<>();
        // patient attributes which are also sample attributes in other studies
        List<String> unfilteredConflictingPatientAttributeIds = new ArrayList<>();

        populateIdLists(
            // input
            unfilteredSampleIdentifiers,
            attributeIds,

            // output
            unfilteredStudyIds,
            unfilteredSampleIds,
            unfilteredPatientIds,
            studyIdsOfUnfilteredPatients,
            unfilteredUniqueSampleKeys,
            unfilteredUniquePatientKeys,
            unfilteredSampleAttributeIds,
            unfilteredPatientAttributeIds,
            unfilteredConflictingPatientAttributeIds
        );

        Map<String, ClinicalDataType> attributeDatatypeMap = constructAttributeDataMap(
            unfilteredSampleAttributeIds,
            unfilteredPatientAttributeIds,
            unfilteredConflictingPatientAttributeIds
        );

        List<ClinicalData> unfilteredClinicalDataForSamples = clinicalDataFetcher.fetchClinicalDataForSamples(
            unfilteredStudyIds,
            unfilteredSampleIds,
            new ArrayList<>(unfilteredSampleAttributeIds)
        );

        List<ClinicalData> unfilteredClinicalDataForPatients = clinicalDataFetcher.fetchClinicalDataForPatients(
            studyIdsOfUnfilteredPatients,
            unfilteredPatientIds,
            new ArrayList<>(unfilteredPatientAttributeIds)
        );

        List<ClinicalData> unfilteredClinicalDataForConflictingPatientAttributes = clinicalDataFetcher.fetchClinicalDataForConflictingPatientAttributes(
            studyIdsOfUnfilteredPatients,
            unfilteredPatientIds,
            new ArrayList<>(unfilteredConflictingPatientAttributeIds)
        );

        List<ClinicalData> unfilteredClinicalData = Stream.of(
            unfilteredClinicalDataForSamples,
            unfilteredClinicalDataForPatients,
            unfilteredClinicalDataForConflictingPatientAttributes
        ).flatMap(Collection::stream).collect(Collectors.toList());

        // if filters are practically the same no need to re-apply them
        List<SampleIdentifier> filteredSampleIdentifiers =
            studyViewFilterUtil.shouldSkipFilterForClinicalDataBins(studyViewFilter) ?
                unfilteredSampleIdentifiers : studyViewFilterApplier.apply(studyViewFilter);

        List<String> filteredUniqueSampleKeys;
        List<String> filteredUniquePatientKeys;
        List<ClinicalData> filteredClinicalData;

        // if filtered and unfiltered samples are exactly the same, no need to fetch clinical data again
        if (filteredSampleIdentifiers.equals(unfilteredSampleIdentifiers)) {
            filteredUniqueSampleKeys = unfilteredUniqueSampleKeys;
            filteredUniquePatientKeys = unfilteredUniquePatientKeys;
            filteredClinicalData = unfilteredClinicalData;
        } else {
            List<String> filteredStudyIds = new ArrayList<>();
            List<String> filteredSampleIds = new ArrayList<>();
            List<String> filteredPatientIds = new ArrayList<>();
            List<String> studyIdsOfFilteredPatients = new ArrayList<>();
            filteredUniqueSampleKeys = new ArrayList<>();
            filteredUniquePatientKeys = new ArrayList<>();
            List<String> filteredSampleAttributeIds = new ArrayList<>();
            List<String> filteredPatientAttributeIds = new ArrayList<>();
            // patient attributes which are also sample attributes in other studies
            List<String> filteredConflictingPatientAttributeIds = new ArrayList<>();

            populateIdLists(
                // input
                filteredSampleIdentifiers,
                attributeIds,

                // output
                filteredStudyIds,
                filteredSampleIds,
                filteredPatientIds,
                studyIdsOfFilteredPatients,
                filteredUniqueSampleKeys,
                filteredUniquePatientKeys,
                filteredSampleAttributeIds,
                filteredPatientAttributeIds,
                filteredConflictingPatientAttributeIds
            );

            filteredClinicalData = studyViewFilterUtil.filterClinicalData(
                unfilteredClinicalDataForSamples,
                unfilteredClinicalDataForPatients,
                unfilteredClinicalDataForConflictingPatientAttributes,
                filteredStudyIds,
                filteredSampleIds,
                studyIdsOfFilteredPatients,
                filteredPatientIds,
                filteredSampleAttributeIds,
                filteredPatientAttributeIds,
                filteredConflictingPatientAttributeIds
            );
        }

        Map<String, List<ClinicalData>> unfilteredClinicalDataByAttributeId =
            unfilteredClinicalData.stream().collect(Collectors.groupingBy(ClinicalData::getAttrId));

        Map<String, List<ClinicalData>> filteredClinicalDataByAttributeId =
            filteredClinicalData.stream().collect(Collectors.groupingBy(ClinicalData::getAttrId));

        List<ClinicalDataBin> clinicalDataBins = Collections.emptyList();

        if (dataBinMethod == DataBinMethod.STATIC) {
            if (!unfilteredSampleIdentifiers.isEmpty() && !unfilteredClinicalData.isEmpty()) {
                clinicalDataBins = calculateStaticDataBins(
                    attributes,
                    attributeDatatypeMap,
                    unfilteredClinicalDataByAttributeId,
                    filteredClinicalDataByAttributeId,
                    unfilteredUniqueSampleKeys,
                    unfilteredUniquePatientKeys,
                    filteredUniqueSampleKeys,
                    filteredUniquePatientKeys
                );
            }
        } else { // dataBinMethod == DataBinMethod.DYNAMIC
            if (!filteredClinicalData.isEmpty()) {
                clinicalDataBins = calculateDynamicDataBins(
                    attributes,
                    attributeDatatypeMap,
                    filteredClinicalDataByAttributeId,
                    filteredUniqueSampleKeys,
                    filteredUniquePatientKeys
                );
            }
        }

        return clinicalDataBins;
    }


    public List<ClinicalDataBin> fetchCustomDataBinCounts(
        DataBinMethod dataBinMethod,
        ClinicalDataBinCountFilter dataBinCountFilter,
        boolean shouldRemoveSelfFromFilter
    ) {

        List<ClinicalDataBinFilter> attributes = dataBinCountFilter.getAttributes();
        StudyViewFilter studyViewFilter = dataBinCountFilter.getStudyViewFilter();

        if (shouldRemoveSelfFromFilter) {
            studyViewFilter = removeSelfFromFilter(dataBinCountFilter);
        }

        List<String> attributeIds = attributes.stream()
            .map(ClinicalDataBinFilter::getAttributeId).collect(Collectors.toList());

        Map<String, CustomDataSession> customDataSessions = customDataService.getCustomDataSessions(attributeIds);

        // FIXME translate CustomDataSession to ClinicalData collections
        // Proper way to fix this would be:
        // 1. Store ClinicalData objects in Session Service for custom data (instead of CustomDataValue)
        // 2. Create interface for ClinicalData/CustomDataValue that is accepted by binning functions.
        Map<String, List<ClinicalData>> filteredClinicalDataByAttributeId = customDataSessions.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry
                    .getValue()
                    .getData()
                    .getData()
                    .stream()
                    .map(mapCustomToClinicalData(entry))
                    .collect(Collectors.toList())
            ));

        // Stop if all clinical data is empty.
        List<ClinicalDataBin> clinicalDataBins = Collections.emptyList();
        if (filteredClinicalDataByAttributeId.entrySet().stream()
            .allMatch(entry -> entry.getValue() == null)
        ) {
            return clinicalDataBins;
        }

        // filter only by study id and sample identifiers, ignore rest
        List<SampleIdentifier> unfilteredSampleIdentifiers = filterByStudyAndSample(studyViewFilter);

        List<String> unfilteredStudyIds = new ArrayList<>();
        List<String> unfilteredSampleIds = new ArrayList<>();
        List<String> unfilteredPatientIds = new ArrayList<>();
        List<String> studyIdsOfUnfilteredPatients = new ArrayList<>();
        List<String> unfilteredUniqueSampleKeys = new ArrayList<>();
        List<String> unfilteredUniquePatientKeys = new ArrayList<>();
        List<String> unfilteredSampleAttributeIds = new ArrayList<>();
        List<String> unfilteredPatientAttributeIds = new ArrayList<>();
        // patient attributes which are also sample attributes in other studies
        List<String> unfilteredConflictingPatientAttributeIds = new ArrayList<>();

        Map<String, ClinicalDataType> attributeDatatypeMap = customDataSessions.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> entry.getValue().getData().getPatientAttribute() ? ClinicalDataType.PATIENT : ClinicalDataType.SAMPLE
            ));

        populateIdLists(
            // input
            unfilteredSampleIdentifiers,
            attributeIds,

            // output
            unfilteredStudyIds,
            unfilteredSampleIds,
            unfilteredPatientIds,
            studyIdsOfUnfilteredPatients,
            unfilteredUniqueSampleKeys,
            unfilteredUniquePatientKeys,
            unfilteredSampleAttributeIds, // 0
            unfilteredPatientAttributeIds, // 0
            unfilteredConflictingPatientAttributeIds // 0
        );

        List<ClinicalData> unfilteredClinicalDataForPatients = filteredClinicalDataByAttributeId
            .values()
            .stream()
            .filter(e -> e.get(0).getClinicalAttribute().getPatientAttribute())
            .flatMap(List::stream)
            .collect(Collectors.toList());

        List<ClinicalData> unfilteredClinicalDataForSamples = filteredClinicalDataByAttributeId
            .values()
            .stream()
            .filter(e -> !e.get(0).getClinicalAttribute().getPatientAttribute())
            .flatMap(List::stream)
            .collect(Collectors.toList());

        List<ClinicalData> unfilteredClinicalDataForConflictingPatientAttributes = clinicalDataFetcher.fetchClinicalDataForConflictingPatientAttributes(
            studyIdsOfUnfilteredPatients,
            unfilteredPatientIds,
            new ArrayList<>(unfilteredConflictingPatientAttributeIds)
        );

        List<ClinicalData> unfilteredClinicalData = Stream.of(
            unfilteredClinicalDataForSamples,
            unfilteredClinicalDataForPatients,
            unfilteredClinicalDataForConflictingPatientAttributes
        ).flatMap(Collection::stream).collect(Collectors.toList());

        // if filters are practically the same no need to re-apply them
        List<SampleIdentifier> filteredSampleIdentifiers =
            studyViewFilterUtil.shouldSkipFilterForClinicalDataBins(studyViewFilter) ?
                unfilteredSampleIdentifiers : studyViewFilterApplier.apply(studyViewFilter);

        List<String> filteredUniqueSampleKeys;
        List<String> filteredUniquePatientKeys;

        filteredUniqueSampleKeys = unfilteredUniqueSampleKeys;
        filteredUniquePatientKeys = unfilteredUniquePatientKeys;

        Map<String, List<ClinicalData>> unfilteredClinicalDataByAttributeId =
            unfilteredClinicalData.stream().collect(Collectors.groupingBy(ClinicalData::getAttrId));

        if (dataBinMethod == DataBinMethod.STATIC) {
            if (!unfilteredSampleIdentifiers.isEmpty() && !unfilteredClinicalData.isEmpty()) {
                clinicalDataBins = calculateStaticDataBins(
                    attributes,
                    attributeDatatypeMap,
                    unfilteredClinicalDataByAttributeId,
                    filteredClinicalDataByAttributeId,
                    unfilteredUniqueSampleKeys,
                    unfilteredUniquePatientKeys,
                    filteredUniqueSampleKeys,
                    filteredUniquePatientKeys
                );
            }
        } else { // dataBinMethod == DataBinMethod.DYNAMIC
            throw new UnsupportedOperationException("Dynamic DataBinMethod not implemented for custom clinical data");
        }

        return clinicalDataBins;

    }

    private Function<CustomDataValue, ClinicalData> mapCustomToClinicalData(Map.Entry<String, CustomDataSession> entry) {
        return customDataValue -> {
            final String attributeId = entry.getKey();
            final CustomAttributeWithData customAttributeWithData = entry.getValue().getData();

            final ClinicalData clinicalDatum = new ClinicalData();
            clinicalDatum.setStudyId(customDataValue.getStudyId());
            clinicalDatum.setSampleId(customDataValue.getSampleId());
            clinicalDatum.setPatientId(customDataValue.getPatientId());
            clinicalDatum.setAttrId(attributeId);
            clinicalDatum.setAttrValue(customDataValue.getValue());

            final ClinicalAttribute clinicalAttribute = new ClinicalAttribute();
            clinicalAttribute.setDisplayName(customAttributeWithData.getDisplayName());
            clinicalAttribute.setDescription(customAttributeWithData.getDescription());
            clinicalAttribute.setPriority(customAttributeWithData.getPriority());
            clinicalAttribute.setPatientAttribute(customAttributeWithData.getPatientAttribute());
            clinicalAttribute.setAttrId(attributeId);
            clinicalAttribute.setDatatype(customAttributeWithData.getDatatype());

            clinicalDatum.setClinicalAttribute(clinicalAttribute);

            return clinicalDatum;
        };
    }

    public void populateIdLists(
        // input lists
        List<SampleIdentifier> sampleIdentifiers,
        List<String> attributeIds,
        // lists to get populated
        List<String> studyIds,
        List<String> sampleIds,
        List<String> patientIds,
        List<String> studyIdsOfPatients,
        List<String> uniqueSampleKeys,
        List<String> uniquePatientKeys,
        List<String> sampleAttributeIds,
        List<String> patientAttributeIds,
        List<String> conflictingPatientAttributeIds
    ) {
        studyViewFilterUtil.extractStudyAndSampleIds(
            sampleIdentifiers,
            studyIds,
            sampleIds
        );

        patientService.getPatientsOfSamples(studyIds, sampleIds).stream().forEach(patient -> {
            patientIds.add(patient.getStableId());
            studyIdsOfPatients.add(patient.getCancerStudyIdentifier());
        });

        uniqueSampleKeys.addAll(studyViewFilterApplier.getUniqkeyKeys(studyIds, sampleIds));
        uniquePatientKeys.addAll(studyViewFilterApplier.getUniqkeyKeys(studyIdsOfPatients, patientIds));

        if (attributeIds != null) {
            List<ClinicalAttribute> clinicalAttributes = clinicalAttributeService
                .getClinicalAttributesByStudyIdsAndAttributeIds(studyIds, attributeIds);

            clinicalAttributeUtil.extractCategorizedClinicalAttributes(
                clinicalAttributes,
                sampleAttributeIds,
                patientAttributeIds,
                conflictingPatientAttributeIds
            );
        }
    }

    public List<ClinicalDataBin> calculateStaticDataBins(
        List<ClinicalDataBinFilter> attributes, // 4 
        Map<String, ClinicalDataType> attributeDatatypeMap, // 4
        Map<String, List<ClinicalData>> unfilteredClinicalDataByAttributeId, // 4
        Map<String, List<ClinicalData>> filteredClinicalDataByAttributeId, // 4
        List<String> unfilteredUniqueSampleKeys, // 845
        List<String> unfilteredUniquePatientKeys, // 841
        List<String> filteredUniqueSampleKeys, // 845
        List<String> filteredUniquePatientKeys //841
    ) {
        List<ClinicalDataBin> clinicalDataBins = new ArrayList<>();

        for (ClinicalDataBinFilter attribute : attributes) {
            if (attributeDatatypeMap.containsKey(attribute.getAttributeId())) {
                ClinicalDataType clinicalDataType = attributeDatatypeMap.get(attribute.getAttributeId());
                List<String> filteredIds = clinicalDataType == ClinicalDataType.PATIENT ? filteredUniquePatientKeys
                    : filteredUniqueSampleKeys;
                List<String> unfilteredIds = clinicalDataType == ClinicalDataType.PATIENT
                    ? unfilteredUniquePatientKeys
                    : unfilteredUniqueSampleKeys;

                List<ClinicalDataBin> dataBins = dataBinner
                    .calculateClinicalDataBins(attribute, clinicalDataType,
                        filteredClinicalDataByAttributeId.getOrDefault(attribute.getAttributeId(),
                            Collections.emptyList()),
                        unfilteredClinicalDataByAttributeId.getOrDefault(attribute.getAttributeId(),
                            Collections.emptyList()),
                        filteredIds, unfilteredIds)
                    .stream()
                    .map(dataBin -> studyViewFilterUtil.dataBinToClinicalDataBin(attribute, dataBin))
                    .collect(Collectors.toList());

                clinicalDataBins.addAll(dataBins);
            }
        }

        return clinicalDataBins;
    }

    public List<ClinicalDataBin> calculateDynamicDataBins(
        List<ClinicalDataBinFilter> attributes,
        Map<String, ClinicalDataType> attributeDatatypeMap,
        Map<String, List<ClinicalData>> filteredClinicalDataByAttributeId,
        List<String> filteredUniqueSampleKeys,
        List<String> filteredUniquePatientKeys
    ) {
        List<ClinicalDataBin> clinicalDataBins = new ArrayList<>();

        for (ClinicalDataBinFilter attribute : attributes) {

            // if there is clinical data for requested attribute
            if (attributeDatatypeMap.containsKey(attribute.getAttributeId())) {
                ClinicalDataType clinicalDataType = attributeDatatypeMap.get(attribute.getAttributeId());
                List<String> filteredIds = clinicalDataType == ClinicalDataType.PATIENT
                    ? filteredUniquePatientKeys
                    : filteredUniqueSampleKeys;

                List<ClinicalDataBin> dataBins = dataBinner
                    .calculateDataBins(attribute, clinicalDataType,
                        filteredClinicalDataByAttributeId.getOrDefault(attribute.getAttributeId(),
                            Collections.emptyList()),
                        filteredIds)
                    .stream()
                    .map(dataBin -> studyViewFilterUtil.dataBinToClinicalDataBin(attribute, dataBin))
                    .collect(Collectors.toList());
                clinicalDataBins.addAll(dataBins);
            }
        }

        return clinicalDataBins;
    }

    public Map<String, ClinicalDataType> constructAttributeDataMap(
        List<String> sampleAttributeIds,
        List<String> patientAttributeIds,
        List<String> conflictingPatientAttributeIds
    ) {
        Map<String, ClinicalDataType> attributeDatatypeMap = new HashMap<>();

        sampleAttributeIds.forEach(attribute -> {
            attributeDatatypeMap.put(attribute, ClinicalDataType.SAMPLE);
        });
        patientAttributeIds.forEach(attribute -> {
            attributeDatatypeMap.put(attribute, ClinicalDataType.PATIENT);
        });
        conflictingPatientAttributeIds.forEach(attribute -> {
            attributeDatatypeMap.put(attribute, ClinicalDataType.SAMPLE);
        });

        return attributeDatatypeMap;
    }

    public List<SampleIdentifier> filterByStudyAndSample(
        StudyViewFilter studyViewFilter
    ) {
        StudyViewFilter filter = null;

        // only filter by study id and sample identifiers
        if (studyViewFilter != null) {
            filter = new StudyViewFilter();
            filter.setStudyIds(studyViewFilter.getStudyIds());
            filter.setSampleIdentifiers(studyViewFilter.getSampleIdentifiers());
        }

        return studyViewFilterApplier.apply(filter);
    }
}
