package org.cbioportal.web;

import java.util.Collection;
import java.util.List;

import org.cbioportal.model.SingleCellExpression;
import org.cbioportal.model.SingleCellExpressionGroupedBySample;
import org.cbioportal.service.SingleCellExpressionService;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.web.config.PublicApiTags;
import org.cbioportal.web.config.annotation.PublicApi;
import org.cbioportal.web.parameter.Direction;
import org.cbioportal.web.parameter.HeaderKeyConstants;
import org.cbioportal.web.parameter.PagingConstants;
import org.cbioportal.web.parameter.Projection;
import org.cbioportal.web.parameter.sort.SingleCellExpressionSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@PublicApi
@RestController()
@RequestMapping("/api")
@Validated
@Tag(name = "Single Cell Expression", description = " ")
public class SingleCellExpressionController {

    @Autowired
    private SingleCellExpressionService singleCellExpressionService;

    @PreAuthorize("hasPermission(#molecularProfileId, 'MolecularProfileId', T(org.cbioportal.utils.security.AccessLevel).READ)")
    @RequestMapping(value = "/molecular-profiles/{molecularProfileId}/single-cell-expression", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get single-cell expression in a molecular profile by Sample List ID")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = SingleCellExpression.class))))
    public ResponseEntity<List<SingleCellExpression>> fetchSingleCellExpression(
        @Parameter(required = true, description = "Molecular Profile ID e.g. acc_tcga_mutations")
        @PathVariable String molecularProfileId,
        @Parameter(required = true, description = "Sample List ID e.g. acc_tcga_all")
        @RequestParam String sampleListId,
        @Parameter(description = "Entrez Gene IDS")
        @RequestParam(required = false) List<Integer> entrezGeneIds,
        @Parameter(description = "Level of detail of the response")
        @RequestParam(defaultValue = "SUMMARY") Projection projection,
        @Parameter(description = "Page size of the result list")
        @Max(PagingConstants.MAX_PAGE_SIZE)
        @Min(PagingConstants.MIN_PAGE_SIZE)
        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
        @Parameter(description = "Page number of the result list")
        @Min(PagingConstants.MIN_PAGE_NUMBER)
        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
        @Parameter(description = "Name of the property that the result list is sorted by")
        @RequestParam(required = false) SingleCellExpressionSortBy sortBy,
        @Parameter(description = "Direction of the sort")
        @RequestParam(defaultValue = "ASC") Direction direction) throws MolecularProfileNotFoundException {
        return new ResponseEntity<>(
            singleCellExpressionService.fetchSingleCellExpressionInMolecularProfile(molecularProfileId, sampleListId,
                entrezGeneIds, pageSize, pageNumber, sortBy.getOriginalValue(), direction.name())
        , HttpStatus.OK);
    }

    @PreAuthorize("hasPermission(#molecularProfileId, 'MolecularProfileId', T(org.cbioportal.utils.security.AccessLevel).READ)")
    @RequestMapping(value = "/molecular-profiles/{molecularProfileId}/single-cell-expression-grouped", method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get single-cell expression in a molecular profile by Sample List ID, grouped by sample, tissue and cell type")
    @ApiResponse(responseCode = "200", description = "OK",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = SingleCellExpressionGroupedBySample.class))))
    public ResponseEntity<List<SingleCellExpressionGroupedBySample>> fetchSingleCellExpressionGroupedBySample(
        @Parameter(required = true, description = "Molecular Profile ID e.g. acc_tcga_mutations")
        @PathVariable String molecularProfileId,
        @Parameter(required = true, description = "Sample List ID e.g. acc_tcga_all")
        @RequestParam String sampleListId,
        @Parameter(description = "Entrez Gene IDS")
        @RequestParam(required = false) List<Integer> entrezGeneIds,
        @Parameter(description = "Level of detail of the response")
        @RequestParam(defaultValue = "SUMMARY") Projection projection,
        @Parameter(description = "Page size of the result list")
        @Max(PagingConstants.MAX_PAGE_SIZE)
        @Min(PagingConstants.MIN_PAGE_SIZE)
        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
        @Parameter(description = "Page number of the result list")
        @Min(PagingConstants.MIN_PAGE_NUMBER)
        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber)
        throws MolecularProfileNotFoundException {
        return new ResponseEntity<>(
            singleCellExpressionService.fetchSingleCellExpressionInMolecularProfileGroupedBySample(
                molecularProfileId, sampleListId, entrezGeneIds, pageSize, pageNumber),
            HttpStatus.OK);
    }

    // TODO: add other endpoints from mutations controller?
}