package org.cbioportal.persistence;

import org.cbioportal.model.StructuralVariant;

import java.util.List;

public interface StructuralVariantRepository {

    List<StructuralVariant> fetchStructuralVariants(List<String> geneticProfileStableIds, 
            List<String> hugoGeneSymbols, List<String> studyIds, List<String> sampleIds);
}
