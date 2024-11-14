package org.cbioportal.service;

import java.util.List;

import org.cbioportal.service.exception.MolecularProfileNotFoundException;

import org.cbioportal.model.SingleCellExpression;
import org.cbioportal.model.SingleCellExpressionGroupedBySample;

public interface SingleCellExpressionService {

    List<SingleCellExpression> fetchSingleCellExpressionInMolecularProfile(
            String molecularProfileId, String sampleListId,
            List<Integer> entrezGeneIds, Integer pageSize, Integer pageNumber,
            String sortBy, String direction)
            throws MolecularProfileNotFoundException;

    List<SingleCellExpressionGroupedBySample> fetchSingleCellExpressionInMolecularProfileGroupedBySample(
            String molecularProfileId, String sampleListId,
            List<Integer> entrezGeneIds, Integer pageSize, Integer pageNumber)
            throws MolecularProfileNotFoundException;
}