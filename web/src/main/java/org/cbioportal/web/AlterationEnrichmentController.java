package org.cbioportal.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.ArrayList;
import java.util.Collection;

import org.cbioportal.model.AlterationEnrichment;
import org.cbioportal.model.MolecularProfileCaseIdentifier;
import org.cbioportal.service.AlterationEnrichmentService;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.web.config.annotation.InternalApi;
import org.cbioportal.web.parameter.AlterationEnrichmentEventType;
import org.cbioportal.web.parameter.EnrichmentType;
import org.cbioportal.web.parameter.MolecularProfileCasesGroupAndAlterationTypeFilter;
import org.cbioportal.web.parameter.MolecularProfileCasesGroupFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import springfox.documentation.annotations.ApiIgnore;

@InternalApi
@RestController
@Validated
@Api(tags = "Alteration Enrichments", description = " ")
public class AlterationEnrichmentController {

    @Autowired
    private AlterationEnrichmentService alterationEnrichmentService;

    @PreAuthorize("hasPermission(#involvedCancerStudies, 'Collection<CancerStudyId>', 'read')")
    @RequestMapping(value = "/alteration-enrichments/fetch",
        method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("Fetch alteration enrichments in a molecular profile")
    public ResponseEntity<List<AlterationEnrichment>> fetchAlterationEnrichments(
        @ApiIgnore // prevent reference to this attribute in the swagger-ui interface
        @RequestAttribute(required = false, value = "involvedCancerStudies") Collection<String> involvedCancerStudies,
        @ApiIgnore // prevent reference to this attribute in the swagger-ui interface. this attribute is needed for the @PreAuthorize tag above.
        @Valid @RequestAttribute(required = false, value = "interceptedMolecularProfileCasesGroupFilters") List<MolecularProfileCasesGroupFilter> interceptedMolecularProfileCasesGroupFilters,
        @ApiIgnore // prevent reference to this attribute in the swagger-ui interface. this attribute is needed for the @PreAuthorize tag above.
        @Valid @RequestAttribute(required = false, value = "alterationEnrichmentEventTypes") List<AlterationEnrichmentEventType> alterationEnrichmentEventTypes,
        @ApiParam("Type of the enrichment e.g. SAMPLE or PATIENT")
        @RequestParam(defaultValue = "SAMPLE") EnrichmentType enrichmentType,
        @ApiParam(required = true, value = "List of groups containing sample identifiers and list of Alteration Types")
        @Valid @RequestBody(required = false)MolecularProfileCasesGroupAndAlterationTypeFilter groupsAndAlterationTypes) throws MolecularProfileNotFoundException {

        Map<String, List<MolecularProfileCaseIdentifier>> groupCaseIdentifierSet = interceptedMolecularProfileCasesGroupFilters.stream()
                .collect(Collectors.toMap(MolecularProfileCasesGroupFilter::getName,
                        MolecularProfileCasesGroupFilter::getMolecularProfileCaseIdentifiers));

        List<String> alterationTypes = new ArrayList<String>();
        for (AlterationEnrichmentEventType alterationEnrichmentEventType : alterationEnrichmentEventTypes) {
            for (String eventType : alterationEnrichmentEventType.getAlterationTypes()) {
                alterationTypes.add(eventType);
            }
        }

        return new ResponseEntity<>(
                alterationEnrichmentService.getAlterationEnrichments(groupCaseIdentifierSet, alterationTypes, enrichmentType.name()),
                HttpStatus.OK);
    }
}

