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

import java.util.ArrayList;
import java.util.List;

import org.cbioportal.model.Treatment;
import org.cbioportal.service.TreatmentService;
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
public class TreatmentControllerTest {

    public static final String GENESET_ID_1 = "treatment_id_1";
    private static final Integer INTERNAL_ID_1 = 1;
    private static final String DESCRIPTION_1 = "description 1";
    private static final String REF_LINK_1 = "http://link1";
    public static final String GENESET_ID_2 = "treatment_id_2";
    private static final Integer INTERNAL_ID_2 = 2;
    private static final String DESCRIPTION_2 = "description 2";
    private static final String REF_LINK_2 = "http://link2";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TreatmentService treatmentService;
    private MockMvc mockMvc;

    @Bean
    public TreatmentService treatmentService() {
        return Mockito.mock(TreatmentService.class);
    }

    @Before
    public void setUp() throws Exception {

        Mockito.reset(treatmentService);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void getAllTreatments() throws Exception {

        List<Treatment> treatmentList = createTreatmentList();
        Mockito.when(treatmentService.getAllTreatments(Mockito.anyString(), Mockito.anyInt(),
            Mockito.anyInt())).thenReturn(treatmentList);

        mockMvc.perform(MockMvcRequestBuilders.get("/treatments")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].treatmentId").value(GENESET_ID_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(GENESET_ID_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value(DESCRIPTION_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].refLink").value(REF_LINK_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].treatmentId").value(GENESET_ID_2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(GENESET_ID_2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].description").value(DESCRIPTION_2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].refLink").value(REF_LINK_2));
    }

    @Test
    public void getTreatment() throws Exception {

        Treatment treatment = createTreatmentList().get(0);
        Mockito.when(treatmentService.getTreatment(Mockito.anyString())).thenReturn(treatment);

        //test /treatments/{treatmentId}
        mockMvc.perform(MockMvcRequestBuilders.get("/treatments/test_treatment_id")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.treatmentId").value(GENESET_ID_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(GENESET_ID_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value(DESCRIPTION_1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.refLink").value(REF_LINK_1));
    }

    private List<Treatment> createTreatmentList() {

        List<Treatment> treatmentList = new ArrayList<>();

        Treatment treatment1 = new Treatment();
        treatment1.setInternalId(INTERNAL_ID_1);
        treatment1.setStableId(GENESET_ID_1);
        treatment1.setName(GENESET_ID_1);
        treatment1.setDescription(DESCRIPTION_1);
        treatment1.setRefLink(REF_LINK_1);
        treatmentList.add(treatment1);

        Treatment treatment2 = new Treatment();
        treatment2.setInternalId(INTERNAL_ID_2);
        treatment2.setStableId(GENESET_ID_2);
        treatment2.setName(GENESET_ID_2);
        treatment2.setDescription(DESCRIPTION_2);
        treatment2.setRefLink(REF_LINK_2);
        treatmentList.add(treatment2);

        return treatmentList;
    }
}