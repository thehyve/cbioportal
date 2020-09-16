package org.cbioportal.service.impl;

import org.cbioportal.model.AlterationDriverAnnotation;
import org.cbioportal.model.CustomDriverAnnotationReport;
import org.cbioportal.persistence.AlterationDriverAnnotationRepository;
import org.cbioportal.service.AlterationDriverAnnotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlterationDriverAnnotationServiceImpl implements AlterationDriverAnnotationService {
    
    @Autowired
    private AlterationDriverAnnotationRepository alterationDriverAnnotationRepository;
    
    public CustomDriverAnnotationReport getCustomDriverAnnotationProps(List<String> molecularProfileIds) {
        
        List<AlterationDriverAnnotation> rows = alterationDriverAnnotationRepository
            .getAlterationDriverAnnotations(molecularProfileIds);
        
        Set<String> tiers = rows.stream()
            .filter(d -> d.getDriverTiersFilter() != null)
            .map(d -> d.getDriverTiersFilter())
            .collect(Collectors.toSet());
        boolean hasBinary = !tiers.isEmpty() || rows.stream()
            .anyMatch(d -> "Putative_Driver".equals(d.getDriverFilter()) ||
                "Putative_Passenger".equals(d.getDriverFilter()));
        
        return new CustomDriverAnnotationReport(hasBinary, tiers);
    }
}