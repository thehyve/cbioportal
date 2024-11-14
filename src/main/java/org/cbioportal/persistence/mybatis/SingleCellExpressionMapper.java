package org.cbioportal.persistence.mybatis;

import org.cbioportal.model.SingleCellExpression;

import java.util.List;

public interface SingleCellExpressionMapper {

    public List<SingleCellExpression> fetchSingleCellExpressionInMolecularProfile(
            String molecularProfileId, String sampleListId, List<Integer> entrezGeneIds, Integer limit, Integer offset,
            String sortBy, String direction);

}
