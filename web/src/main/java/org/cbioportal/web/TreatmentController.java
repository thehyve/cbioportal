/*
 * Copyright (c) 2019 The Hyve B.V.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF MERCHANTABILITY OR FITNESS
 * FOR A PARTICULAR PURPOSE. The software and documentation provided hereunder
 * is on an "as is" basis, and Memorial Sloan-Kettering Cancer Center has no
 * obligations to provide maintenance, support, updates, enhancements or
 * modifications. In no event shall Memorial Sloan-Kettering Cancer Center be
 * liable to any party for direct, indirect, special, incidental or
 * consequential damages, including lost profits, arising out of the use of this
 * software and its documentation, even if Memorial Sloan-Kettering Cancer
 * Center has been advised of the possibility of such damage.
 */

/*
 * This file is part of cBioPortal.
 *
 * cBioPortal is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
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
