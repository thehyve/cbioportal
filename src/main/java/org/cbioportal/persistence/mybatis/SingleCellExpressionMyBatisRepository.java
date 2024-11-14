package org.cbioportal.persistence.mybatis;

import java.util.List;

import org.cbioportal.model.SingleCellExpression;
import org.cbioportal.persistence.SingleCellExpressionRepository;
import org.cbioportal.persistence.mybatis.util.PaginationCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class SingleCellExpressionMyBatisRepository implements SingleCellExpressionRepository {

    @Autowired
    private SingleCellExpressionMapper singleCellExpressionMapper;

    @Override
    public List<SingleCellExpression> fetchSingleCellExpressionInMolecularProfile(
            String molecularProfileId, String sampleListId, List<Integer> entrezGeneIds, Integer pageSize, Integer pageNumber,
            String sortBy, String direction) {

        List<SingleCellExpression> singleCellExpressions = singleCellExpressionMapper
                .fetchSingleCellExpressionInMolecularProfile(molecularProfileId,
                        sampleListId, entrezGeneIds, pageSize, PaginationCalculator.offset(pageSize, pageNumber), sortBy, direction);
        return singleCellExpressions;
    }
}
