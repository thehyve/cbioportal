package org.cbioportal.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.cbioportal.service.SingleCellExpressionService;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.cbioportal.model.SingleCellExpression;
import org.cbioportal.model.SingleCellExpressionGroupedBySample;
import org.cbioportal.persistence.SingleCellExpressionRepository;

@Service
public class SingleCellExpressionServiceImpl implements SingleCellExpressionService {

    @Autowired
    private SingleCellExpressionRepository singleCellExpressionRepository;

    @Override
    public List<SingleCellExpression> fetchSingleCellExpressionInMolecularProfile(
            String molecularProfileId, String sampleListId,
            List<Integer> entrezGeneIds, Integer pageSize, Integer pageNumber,
            String sortBy, String direction)
            throws MolecularProfileNotFoundException {

        return singleCellExpressionRepository.fetchSingleCellExpressionInMolecularProfile(
                molecularProfileId, sampleListId, entrezGeneIds, pageSize, pageNumber, sortBy, direction);
    }

    @Override
    public List<SingleCellExpressionGroupedBySample> fetchSingleCellExpressionInMolecularProfileGroupedBySample(
            String molecularProfileId, String sampleListId, List<Integer> entrezGeneIds,
            Integer pageSize, Integer pageNumber)
            throws MolecularProfileNotFoundException {

        List<SingleCellExpression> singleCellExpressionData = fetchSingleCellExpressionInMolecularProfile(
                molecularProfileId, sampleListId, entrezGeneIds, pageSize, pageNumber, "sampleId ASC, tissue ASC, cellType", "ASC");

        Map<String, List<SingleCellExpression>> singleCellExpressionGroupedBySamples = 
            SingleCellExpressionGroupedBySample.createSingleCellExpressionGroupedMap(singleCellExpressionData, "sampleId");

        return singleCellExpressionGroupedBySamples.entrySet().stream()
                .map(e -> new SingleCellExpressionGroupedBySample(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
    }

}