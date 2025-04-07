package org.cbioportal.service;

import org.cbioportal.model.NamespaceAttribute;
import org.cbioportal.model.NamespaceData;
import org.cbioportal.model.NamespaceDataCountItem;
import org.cbioportal.web.parameter.NamespaceDataFilter;
import java.util.List;
    
public interface NamespaceDataService {

    List<NamespaceData> fetchNamespaceData(List<String> studyIds, List<String> sampleIds, List<NamespaceDataFilter> namespaceDataFilters);

    List<NamespaceData> fetchNamespaceDataForComparison(List<String> studyIds, List<String> sampleIds, NamespaceAttribute namespaceAttribute, List <String> values);

    List<NamespaceDataCountItem> fetchNamespaceDataCounts(List<String> studyIds, List<String> sampleIds, List<NamespaceAttribute> namespaceAttributes);

}
