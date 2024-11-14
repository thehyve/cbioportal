package org.cbioportal.persistence;

import java.util.List;

import org.cbioportal.model.SingleCellExpression;
import org.springframework.cache.annotation.Cacheable;

public interface SingleCellExpressionRepository {

    @Cacheable(cacheResolver = "generalRepositoryCacheResolver", condition = "@cacheEnabledConfig.getEnabled()")
    public List<SingleCellExpression> fetchSingleCellExpressionInMolecularProfile(
            String molecularProfileId, String sampleListId, List<Integer> entrezGeneIds, Integer pageSize,
            Integer pageNumber, String sortBy, String direction);

}
