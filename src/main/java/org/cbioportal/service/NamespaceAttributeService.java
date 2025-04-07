package org.cbioportal.service;

import org.cbioportal.model.NamespaceAttribute;
import org.cbioportal.model.NamespaceAttributeCount;

import java.util.List;

public interface NamespaceAttributeService {

    List<NamespaceAttribute> fetchNamespaceAttributes(List<String> studyIds);

    List<NamespaceAttributeCount> fetchNamespaceAttributeCountsBySampleIds(List<String> studyIds, List<String> sampleIds, List<NamespaceAttribute> namespaceAttributes);
}
