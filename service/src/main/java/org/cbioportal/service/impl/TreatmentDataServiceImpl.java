/*
 * Copyright (c) 2016 The Hyve B.V.
 * This code is licensed under the GNU Affero General Public License (AGPL),
 * version 3, or (at your option) any later version.
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

package org.cbioportal.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.cbioportal.model.MolecularProfile;
import org.cbioportal.model.Sample;
import org.cbioportal.model.TreatmentMolecularAlteration;
import org.cbioportal.model.TreatmentMolecularData;
import org.cbioportal.model.TreatmentMolecularDataProfile;
import org.cbioportal.persistence.MolecularDataRepository;
import org.cbioportal.service.MolecularProfileService;
import org.cbioportal.service.SampleListService;
import org.cbioportal.service.SampleService;
import org.cbioportal.service.TreatmentDataService;
import org.cbioportal.service.exception.MolecularProfileNotFoundException;
import org.cbioportal.service.exception.SampleListNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TreatmentDataServiceImpl implements TreatmentDataService {

    @Autowired
    private MolecularDataRepository molecularDataRepository;
    @Autowired
    private SampleService sampleService;
    @Autowired
    private MolecularProfileService molecularProfileService;
    @Autowired
    private SampleListService sampleListService;

    public TreatmentMolecularDataProfile fetchTreatmentData(String geneticProfileId, List<String> sampleIds, List<String> treatmentIds)
            throws MolecularProfileNotFoundException {

        //validate (throws exception if profile is not found):
        MolecularProfile molecularProfile = molecularProfileService.getMolecularProfile(geneticProfileId);

        // get samples in molecular profile
        String commaSeparatedSampleIdsOfGeneticProfile = molecularDataRepository
            .getCommaSeparatedSampleIdsOfMolecularProfile(geneticProfileId);
        if (commaSeparatedSampleIdsOfGeneticProfile == null) {
        	// no data, return empty profile:
            return new TreatmentMolecularDataProfile();
        }
        List<Integer> internalSampleIds = Arrays.stream(commaSeparatedSampleIdsOfGeneticProfile.split(","))
            .mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());

        List<Sample> samples;
        if (sampleIds == null) {
            samples = sampleService.getSamplesByInternalIds(internalSampleIds);
        } else {
            List<String> studyIds = new ArrayList<>();
            sampleIds.forEach(s -> studyIds.add(molecularProfile.getCancerStudyIdentifier()));
            samples = sampleService.fetchSamples(studyIds, sampleIds, "ID");
        }

        // get values of molecular profile
        List<TreatmentMolecularAlteration> treatmentAlterations = molecularDataRepository.getTreatmentMolecularAlterations(geneticProfileId,
                treatmentIds, "SUMMARY");

        TreatmentMolecularDataProfile treatmentProfile = new TreatmentMolecularDataProfile();

        // iterate over values and only keep the values of requested samples 
        for (Sample sample : samples) {

            int indexOfSampleId = internalSampleIds.indexOf(sample.getInternalId());
            
            if (indexOfSampleId != -1) {
                for (TreatmentMolecularAlteration treatmentAlteration : treatmentAlterations) {

                    treatmentProfile.setStudyId(sample.getCancerStudyIdentifier());
                    treatmentProfile.setMolecularProfileId(geneticProfileId);
                    treatmentProfile.setPivotThreshold(treatmentAlteration.getPivotThreshold());
                    treatmentProfile.setSortOrder(treatmentAlteration.getSortOrder());
                    
                    TreatmentMolecularData treatmentData = new TreatmentMolecularData();
                    
                    treatmentData.setSampleId(sample.getStableId());
                    treatmentData.setPatientId(sample.getPatientStableId());
                    treatmentData.setTreatmentId(treatmentAlteration.getTreatmentId());
                    treatmentData.setValue(treatmentAlteration.getSplitValues()[indexOfSampleId]);
                   
                    treatmentProfile.addDataPoint(treatmentData);
                }
            }
        }
        
        return treatmentProfile;
    }

    public TreatmentMolecularDataProfile fetchTreatmentData(String geneticProfileId, String sampleListId, List<String> treatmentIds) 
            throws MolecularProfileNotFoundException, SampleListNotFoundException {

        //get list of samples for given sampleListId:
        List<String> sampleIds = sampleListService.getAllSampleIdsInSampleList(sampleListId);
        return fetchTreatmentData(geneticProfileId, sampleIds, treatmentIds);
    }
}