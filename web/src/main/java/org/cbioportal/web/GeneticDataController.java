package org.cbioportal.web;

import java.util.List;

import org.cbioportal.model.Gene;
import org.cbioportal.model.GeneticData;
import org.cbioportal.service.GeneticDataService;
import org.cbioportal.web.exception.PageSizeTooBigException;
import org.cbioportal.web.parameter.HeaderKeyConstants;
import org.cbioportal.web.parameter.PagingConstants;
import org.cbioportal.web.parameter.Projection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = "Genetic Data", description = " ")
public class GeneticDataController {

	@Autowired
    private GeneticDataService geneticDataService;
	
    @RequestMapping(value = "/studies/{studyId}/samples/{sampleId}/genetic-data", method = RequestMethod.GET)
    @ApiOperation("Get all genetic data of a sample in a study")
    public ResponseEntity<List<GeneticData>> getAllGeneticDataInSampleInStudy(@PathVariable String studyId,
                                                                                               @PathVariable String sampleId,
                                                                                               @RequestParam String geneticProfileId,
                                                                                               @RequestParam(defaultValue = "SUMMARY") Projection projection,
                                                                                               @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
                                                                                               @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber) {

        throw new UnsupportedOperationException();
    }

    @RequestMapping(value = "/studies/{studyId}/patients/{patientId}/genetic-data", method = RequestMethod.GET)
    @ApiOperation("Get all genetic data of a patient in a study")
    public ResponseEntity<List<GeneticData>> getAllGeneticDataInPatientInStudy(@PathVariable String studyId,
                                                                                                @PathVariable String patientId,
                                                                                                @RequestParam String geneticProfileId,
                                                                                                @RequestParam(defaultValue = "SUMMARY") Projection projection,
                                                                                                @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
                                                                                                @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber) {

        throw new UnsupportedOperationException();
    }

    @RequestMapping(value = "/genetic-profiles/{geneticProfileId}/genetic-data", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Get all genetic data in a genetic profile")
    public ResponseEntity<List<GeneticData>> getAllGeneticDataInGeneticProfile(
    		@ApiParam(required = true, value = "Genetic profile ID, e.g. brca_tcga_mrna")
    		@PathVariable String geneticProfileId,
    		@ApiParam("Level of detail of the response, e.g. META, SUMMARY or DETAILED")
    		@RequestParam(defaultValue = "SUMMARY") Projection projection,
    		@ApiParam("Page size of the result list")
	        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_SIZE) Integer pageSize,  //TODO implement paging
	        @ApiParam("Page number of the result list")
	        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber) {

    	if (projection == Projection.META) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HeaderKeyConstants.TOTAL_COUNT, geneticDataService.getMetaGeneticDataInGeneticProfile(geneticProfileId)
                    .getTotalCount().toString());
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
            		geneticDataService.getAllGeneticDataInGeneticProfile(geneticProfileId, projection.name(), pageSize, pageNumber), HttpStatus.OK);
        }
    }
    
    
    @RequestMapping(value = "/genetic-profiles/{geneticProfileId}/fetch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Fetch genetic data items by profile Id, gene ids and sample ids")
    public ResponseEntity<List<GeneticData>> fetchGeneticDataItems(
    		@ApiParam(required = true, value = "Genetic profile ID, e.g. brca_tcga_mrna")
    		@PathVariable String geneticProfileId,
            @ApiParam(required = true, value = "The list of identifiers for the genetic entities of interest. "
            		+ "If these are genes: list of Entrez Gene IDs and/or Hugo Gene Symbols. If these are gene sets: list of gene set identifiers")
            @RequestBody List<String> geneticEntityIds,
            @ApiParam(required = false, value = "Full list of samples or patients to query")
            @RequestBody List<String> caseIds, 
            @ApiParam(required = false, value = "Identifier of pre-defined case list with samples to query ")
            @RequestBody String caseListId) throws PageSizeTooBigException {

        //TODO 
    	return null;
    }
    
    
    

    @RequestMapping(value = "/genetic-data/query", method = RequestMethod.POST)
    @ApiOperation("Query genetic data by example")
    public ResponseEntity<List<GeneticData>> queryGeneticDataByExample(@RequestParam(defaultValue = "SUMMARY") Projection projection,
                                                                                        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
                                                                                        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber,
                                                                                        @RequestBody GeneticData exampleGenericData) {

        throw new UnsupportedOperationException();
    }
}
