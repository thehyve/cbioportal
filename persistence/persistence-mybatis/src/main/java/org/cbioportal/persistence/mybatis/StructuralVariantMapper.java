package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.StructuralVariant;

import java.util.List;

public interface StructuralVariantMapper {

    List<StructuralVariant> fetchStructuralVariants(List<String> geneticProfileStableIds, 
            List<String> hugoGeneSymbols, List<String> studyIds, List<String> sampleIds);
}
