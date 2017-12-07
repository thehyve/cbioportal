package org.cbioportal.service;

import org.cbioportal.model.StructuralVariant;

import java.util.List;

public interface StructuralVariantService {
    
    List<StructuralVariant> fetchStructuralVariants();
}