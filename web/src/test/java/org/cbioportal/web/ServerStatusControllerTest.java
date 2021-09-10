package org.cbioportal.web;

import org.cbioportal.service.ServerStatusService;
import org.cbioportal.service.impl.ServerStatusServiceImpl;
import org.cbioportal.web.config.TestConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest
@ContextConfiguration(classes = {CacheStatsController.class, TestConfig.class})
public class ServerStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Bean
    public static ServerStatusService serverStatusService() {
        ServerStatusService serverStatusServiceMock = Mockito.mock(ServerStatusService.class);
        return serverStatusServiceMock;
    }
}
