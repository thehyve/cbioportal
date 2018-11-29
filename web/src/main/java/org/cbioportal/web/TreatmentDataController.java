package org.cbioportal.web;

import org.cbioportal.model.TreatmentMolecularDataProfile;
import org.cbioportal.service.TreatmentDataService;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.service.exception.SampleListNotFoundException;
import org.cbioportal.web.config.annotation.InternalApi;
import org.cbioportal.web.parameter.TreatmentDataFilterCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@InternalApi
@RestController
@Validated
@Api(tags = "Treatment response values", description = " ")
public class TreatmentDataController {

	@Autowired
	private TreatmentDataService treatmentDataService;

	@PreAuthorize("hasPermission(#geneticProfileId, 'GeneticProfile', 'read')")
    @RequestMapping(value = "/genetic-profiles/{geneticProfileId}/treatment-genetic-data/fetch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Fetch treatment \"genetic data\" items (treatment response values) by profile Id, treatment Ids and sample Ids")
    public ResponseEntity<TreatmentMolecularDataProfile> fetchGeneticDataItems(
            @ApiParam(required = true, value = "Genetic profile ID, e.g. study_es_0_treatment_ic50")
            @PathVariable String geneticProfileId,
            @ApiParam(required = true, value = "Search criteria to return the values for a given set of samples and treatment items. "
            		+ "treatmentIds: The list of identifiers (STABLE_ID) for the treatments of interest, e.g. 17-AAG. "
            		+ "Use one of these if you want to specify a subset of samples:"
            		+ "(1) sampleListId: Identifier of pre-defined sample list with samples to query, e.g. brca_tcga_all " 
            		+ "or (2) sampleIds: custom list of samples or patients to query, e.g. TCGA-BH-A1EO-01, TCGA-AR-A1AR-01")
            @RequestBody TreatmentDataFilterCriteria treatmentDataFilterCriteria) throws MolecularProfileNotFoundException, SampleListNotFoundException {

				if (treatmentDataFilterCriteria.getSampleListId() != null && treatmentDataFilterCriteria.getSampleListId().trim().length() > 0) {
					return new ResponseEntity<>(
							treatmentDataService.fetchTreatmentData(geneticProfileId, treatmentDataFilterCriteria.getSampleListId(), 
									treatmentDataFilterCriteria.getTreatmentIds()), HttpStatus.OK);
				} else {
					return new ResponseEntity<>(
							treatmentDataService.fetchTreatmentData(geneticProfileId, treatmentDataFilterCriteria.getSampleIds(), 
									treatmentDataFilterCriteria.getTreatmentIds()), HttpStatus.OK);
				}
			}

}