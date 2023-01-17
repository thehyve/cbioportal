package org.cbioportal.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cbioportal.service.CustomDataService;
import org.cbioportal.service.util.CustomDataSession;
import org.cbioportal.service.util.SessionServiceRequestHandler;
import org.cbioportal.session_service.domain.SessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class CustomDataServiceImpl implements CustomDataService {
    @Autowired
    private SessionServiceRequestHandler sessionServiceRequestHandler;
    
    @Autowired
    private ObjectMapper sessionServiceObjectMapper;
    
    @Override
    public List<CustomDataSession> getCustomDataSessions(List<String> customAttributeIds) {
        List<CompletableFuture<CustomDataSession>> postFutures = customAttributeIds.stream()
            .map(attributeId -> CompletableFuture.supplyAsync(() -> {
                try {
                    String customDataSessionJson = sessionServiceRequestHandler.getSessionDataJson(
                        SessionType.custom_data,
                        attributeId
                    );
                    return sessionServiceObjectMapper.readValue(customDataSessionJson, CustomDataSession.class);
                } catch (Exception e) {
                    return null;
                }
            })).collect(Collectors.toList());

        CompletableFuture.allOf(postFutures.toArray(new CompletableFuture[postFutures.size()])).join();

        List<CustomDataSession> customDataSessions = postFutures
            .stream()
            .map(CompletableFuture::join)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        return customDataSessions;
    }
}
