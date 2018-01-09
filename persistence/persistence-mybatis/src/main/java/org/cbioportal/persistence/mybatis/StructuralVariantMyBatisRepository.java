package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.StructuralVariant;
import org.cbioportal.persistence.StructuralVariantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StructuralVariantMyBatisRepository implements StructuralVariantRepository {

    @Autowired
    private StructuralVariantMapper structuralVariantmapper;
    
    @Override
    public List<StructuralVariant> fetchStructuralVariants(List<String> molecularProfileIds, 
            List<String> hugoGeneSymbols, List<String> sampleIds) {

        return structuralVariantmapper.fetchStructuralVariants(molecularProfileIds, 
                hugoGeneSymbols, sampleIds);
    }
}
