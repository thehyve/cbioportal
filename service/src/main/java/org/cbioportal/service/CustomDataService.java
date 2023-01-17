package org.cbioportal.service;

import org.cbioportal.service.util.CustomDataSession;

import java.util.List;

public interface CustomDataService {
    List<CustomDataSession> getCustomDataSessions(List<String> attributes);
}
