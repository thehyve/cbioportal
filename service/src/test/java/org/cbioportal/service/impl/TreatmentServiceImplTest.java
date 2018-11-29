package org.cbioportal.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.cbioportal.model.Treatment;
import org.cbioportal.model.meta.BaseMeta;
import org.cbioportal.persistence.TreatmentRepository;
import org.cbioportal.service.MolecularProfileService;
import org.cbioportal.service.SampleService;
import org.cbioportal.service.exception.TreatmentNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TreatmentServiceImplTest extends BaseServiceImplTest {

    public static final String TREATMENT_ID_1 = "treatment_id_1";
    private static final int INTERNAL_ID_1 = 1;
    public static final String TREATMENT_ID_2 = "treatment_id_2";
    private static final int INTERNAL_ID_2 = 2;

    @InjectMocks
    private TreatmentServiceImpl treatmentService;

    @Mock
    private TreatmentRepository treatmentRepository;
    @Mock
    private SampleService sampleService;
    @Mock
    private MolecularProfileService geneticProfileService;

    @Test
    public void getAllTreatments() {

        List<Treatment> treatmentList = createTreatmentList();
        Mockito.when(treatmentRepository.getAllTreatments(PROJECTION, PAGE_SIZE, PAGE_NUMBER))
            .thenReturn(treatmentList);

        List<Treatment> result = treatmentService.getAllTreatments(PROJECTION, PAGE_SIZE, PAGE_NUMBER);

        Assert.assertEquals(treatmentList, result);
    }

    @Test
    public void getMetaTreatments() {

        BaseMeta expectedBaseMeta = new BaseMeta();
        Mockito.when(treatmentRepository.getMetaTreatments()).thenReturn(expectedBaseMeta);
        BaseMeta result = treatmentService.getMetaTreatments();

        Assert.assertEquals(expectedBaseMeta, result);
    }

    @Test
    public void getTreatment() throws TreatmentNotFoundException {

        Treatment treatment = createTreatmentList().get(0);
        Mockito.when(treatmentRepository.getTreatmentByStableId(TREATMENT_ID_1))
            .thenReturn(treatment);

        Treatment result = treatmentService.getTreatment(TREATMENT_ID_1);
        Assert.assertEquals(treatment, result);
    }
    
    @Test(expected = TreatmentNotFoundException.class)
    public void getTreatmentByStableIdNotFound() throws TreatmentNotFoundException {

        Treatment treatment = createTreatmentList().get(0);
        Mockito.when(treatmentRepository.getTreatmentByStableId(TREATMENT_ID_1))
            .thenReturn(treatment);
        //expect TreatmentNotFoundException here:
        treatmentService.getTreatment("wrongId");
    }

    private List<Treatment> createTreatmentList() {

        List<Treatment> treatmentList = new ArrayList<>();

        Treatment treatment1 = new Treatment();
        treatment1.setInternalId(INTERNAL_ID_1);
        treatment1.setStableId(TREATMENT_ID_1);
        treatmentList.add(treatment1);

        Treatment treatment2 = new Treatment();
        treatment2.setInternalId(INTERNAL_ID_2);
        treatment2.setStableId(TREATMENT_ID_2);
        treatmentList.add(treatment2);
        return treatmentList;
    }

}