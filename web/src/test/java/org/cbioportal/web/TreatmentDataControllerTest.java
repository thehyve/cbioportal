package org.cbioportal.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.cbioportal.model.TreatmentMolecularData;
import org.cbioportal.service.TreatmentDataService;
import org.cbioportal.web.parameter.TreatmentDataFilterCriteria;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/applicationContext-web.xml")
@Configuration
public class TreatmentDataControllerTest {

    private static final String PROF_ID = "test_prof_id";
    private static final String SAMPLE_LIST_ID = "test_sample_list_id";
    public static final String TREATMENT_STABLE_ID_1 = "treatment_id_1";
    private static final String SAMPLE_STABLE_ID_1 = "test_sample_stable_id_1";
    private static final String VALUE_1 = "0.845";
    public static final String TREATMENT_STABLE_ID_2 = "treatment_id_2";
    private static final String VALUE_2 = "-0.457";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TreatmentDataService treatmentDataService;
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public TreatmentDataService treatmentDataService() {
        return Mockito.mock(TreatmentDataService.class);
    }
    
    @Before
    public void setUp() throws Exception {

        Mockito.reset(treatmentDataService);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    @Test
    public void fetchGeneticDataItems() throws Exception {

        List<TreatmentMolecularData> treatmentDataItems = createTreatmentDataItemsList();
        Mockito.when(treatmentDataService.fetchTreatmentData(Mockito.anyString(), Mockito.anyListOf(String.class),
            Mockito.anyListOf(String.class))).thenReturn(treatmentDataItems);

        TreatmentDataFilterCriteria treatmentDataFilterCriteria = new TreatmentDataFilterCriteria();
        treatmentDataFilterCriteria.setTreatmentIds(Arrays.asList(TREATMENT_STABLE_ID_1, TREATMENT_STABLE_ID_2));

        mockMvc.perform(MockMvcRequestBuilders.post("/genetic-profiles/" + PROF_ID + "/treatment-genetic-data/fetch")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(treatmentDataFilterCriteria)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].geneticProfileId").value(PROF_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].treatmentId").value(TREATMENT_STABLE_ID_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sampleId").value(SAMPLE_STABLE_ID_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].value").value(VALUE_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].geneticProfileId").value(PROF_ID))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].treatmentId").value(TREATMENT_STABLE_ID_2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sampleId").value(SAMPLE_STABLE_ID_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].value").value(VALUE_2));

        //testing the sampleListId route:
        List<TreatmentMolecularData> treatmentDataItems2 = createTreatmentDataItemsList();
        treatmentDataItems2.addAll(createTreatmentDataItemsList());//duplicate, just to make it different from response above
        Mockito.when(treatmentDataService.fetchTreatmentData(Mockito.anyString(), Mockito.anyString(),
                Mockito.anyListOf(String.class))).thenReturn(treatmentDataItems2);
        //set sampleListId to ensure the fetchTreatmentData variant above is called:
        treatmentDataFilterCriteria.setSampleListId(SAMPLE_LIST_ID);

        mockMvc.perform(MockMvcRequestBuilders.post("/genetic-profiles/" + PROF_ID + "/treatment-genetic-data/fetch")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(treatmentDataFilterCriteria)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(4)));
    }

    private List<TreatmentMolecularData> createTreatmentDataItemsList() {

        List<TreatmentMolecularData> treatmentDataItems = new ArrayList<>();

        TreatmentMolecularData gsItem1 = new TreatmentMolecularData();
        gsItem1.setTreatmentId(TREATMENT_STABLE_ID_1);
        gsItem1.setMolecularProfileId(PROF_ID);
        gsItem1.setSampleId(SAMPLE_STABLE_ID_1);
        gsItem1.setValue(VALUE_1);
        treatmentDataItems.add(gsItem1);

        TreatmentMolecularData gsItem2 = new TreatmentMolecularData();
        gsItem2.setTreatmentId(TREATMENT_STABLE_ID_2);
        gsItem2.setMolecularProfileId(PROF_ID);
        gsItem2.setSampleId(SAMPLE_STABLE_ID_1);
        gsItem2.setValue(VALUE_2);
        treatmentDataItems.add(gsItem2);

        return treatmentDataItems;
    }

}