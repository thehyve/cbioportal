package org.cbioportal.security.spring.authentication.token.oauth2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * OAuth2DataAccessTokenServiceImpl
 */
@RunWith(SpringRunner.class)
public class OAuth2DataAccessTokenServiceImplTest {

    @Autowired
    OAuth2DataAccessTokenServiceImpl service;

    @Test
    public void getUsername() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX25hbWUiOiJKb2huIERvZSJ9.gcmTPf4gVU01ZxPiRMmx1Br_uaJ5ro45QFJl04q5fDY";
        assertEquals(service.getUsername(token), "John Doe");
    }

    @Test
    public void createAuthenticationRequest() {
        String token = "test";
        assertEquals(service.createAuthenticationRequest(token).getCredentials(), "test");
    }

    @Test
    public void getExpiration() {
        assertNull(service.getExpiration("token"));
    }

}