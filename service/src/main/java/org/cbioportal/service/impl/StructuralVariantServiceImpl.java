package org.cbioportal.service.impl;

import java.util.List;

import org.cbioportal.model.StructuralVariant;
import org.cbioportal.persistence.StructuralVariantRepository;
import org.cbioportal.service.StructuralVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;


@Service
public class StructuralVariantServiceImpl implements StructuralVariantService {

    @Autowired
    private StructuralVariantRepository structuralVariantRepository;
    
    @Override
    public List<StructuralVariant> fetchStructuralVariants(List<String> geneticProfileStableIds, 
            List<String> hugoGeneSymbols, List<String> studyIds, List<String> sampleIds) {
        
        return structuralVariantRepository.fetchStructuralVariants(geneticProfileStableIds, hugoGeneSymbols,
                studyIds, sampleIds);
    }
}
