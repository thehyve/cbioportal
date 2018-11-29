package org.cbioportal.web;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.cbioportal.model.Treatment;
import org.cbioportal.service.TreatmentService;
import org.cbioportal.service.exception.TreatmentNotFoundException;
import org.cbioportal.web.config.annotation.InternalApi;
import org.cbioportal.web.parameter.HeaderKeyConstants;
import org.cbioportal.web.parameter.PagingConstants;
import org.cbioportal.web.parameter.Projection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@InternalApi
@RestController
@Validated
@Api(tags = "Treatments", description = " ")
public class TreatmentController {

    @Autowired
    private TreatmentService treatmentService;

    @RequestMapping(value = "/treatments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Get all treatments")
    public ResponseEntity<List<Treatment>> getAllTreatments(
        @ApiParam("Level of detail of the response")
        @RequestParam(defaultValue = "SUMMARY") Projection projection,
        @ApiParam("Page size of the result list")
        @Max(Integer.MAX_VALUE)
        @Min(PagingConstants.MIN_PAGE_SIZE)
        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_SIZE) Integer pageSize,
        @ApiParam("Page number of the result list")
        @Min(PagingConstants.MIN_PAGE_NUMBER)
        @RequestParam(defaultValue = PagingConstants.DEFAULT_PAGE_NUMBER) Integer pageNumber) {

        if (projection == Projection.META) {
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HeaderKeyConstants.TOTAL_COUNT, treatmentService.getMetaTreatments().getTotalCount()
                .toString());
            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                treatmentService.getAllTreatments(projection.name(), pageSize, pageNumber), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/treatments/{treatmentId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Get a treatment by stable ID")
    public ResponseEntity<Treatment> getTreatment(
        @ApiParam(required = true, value = "Treatment stable ID e.g. 17-AAG")
        @PathVariable String treatmentId) throws TreatmentNotFoundException {

        return new ResponseEntity<>(treatmentService.getTreatment(treatmentId), HttpStatus.OK);
    }

    @RequestMapping(value = "/treatments/fetch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE)
	    @ApiOperation("Fetch treatments by stable ID")
	    public ResponseEntity<List<Treatment>> fetchTreatments(
	        @ApiParam(required = true, value = "List of treatment stable IDs")
	        @Size(min = 1, max = PagingConstants.MAX_PAGE_SIZE)
	        @RequestBody List<String> treatmentIds) {

	        return new ResponseEntity<>(treatmentService.fetchTreatments(treatmentIds), HttpStatus.OK);
    }

}
