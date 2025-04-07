package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.NamespaceAttribute;
import org.cbioportal.model.NamespaceAttributeCount;
import org.cbioportal.model.NamespaceDataCount;
import org.cbioportal.model.NamespaceData;

import java.util.List;

public interface NamespaceMapper{

    List<NamespaceAttribute> getNamespaceOuterKey(List<String> studyIds);

    List<NamespaceAttribute> getNamespaceInnerKey(String outerKey, List<String> studyIds);

    List<NamespaceAttributeCount> getNamespaceAttributeCountsBySampleIds(List<String> studyIds, List<String> sampleIds, List<NamespaceAttribute> namespaceAttributes);

    List<NamespaceDataCount> getNamespaceDataCounts(List<String> studyIds, List<String> sampleIds, String outerKey, String innerKey);

    List<NamespaceData> getNamespaceData(List<String> studyIds, List<String> sampleIds, String outerKey, String innerKey);

    List<NamespaceData> getNamespaceDataForComparison(List<String> studyIds, List<String> sampleIds, String outerKey, String innerKey, String value);

}
