package org.cbioportal.service.impl;

import java.util.List;

import org.cbioportal.model.StructuralVariant;
import org.cbioportal.persistence.StructuralVariantRepository;
import org.cbioportal.service.StructuralVariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StructuralVariantServiceImpl implements StructuralVariantService {

    @Autowired
    private StructuralVariantRepository structuralVariantRepository;
    
    @Override
    public List<StructuralVariant> fetchStructuralVariants(List<String> molecularProfileIds, 
            List<String> hugoGeneSymbols,  List<String> sampleIds) {
        
        return structuralVariantRepository.fetchStructuralVariants(molecularProfileIds, hugoGeneSymbols, sampleIds);
    }
}
