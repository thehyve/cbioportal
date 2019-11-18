package org.cbioportal.security.spring.authentication.oauth2token.config;

import java.util.Arrays;

import org.cbioportal.security.spring.authentication.oauth2token.TokenRefreshRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableOAuth2Client
public class OAuth2Config {

    @Value("${dat.oauth2.clientId}")
    private String clientId;

    @Value("${dat.oauth2.clientSecret}")
    private String clientSecret;

    @Value("${dat.oauth2.accessTokenUri}")
    private String accessTokenUri;

    @Value("${dat.oauth2.userAuthorizationUri}")
    private String userAuthorizationUri;

    @Value("${dat.oauth2.redirectUri}")
    private String redirectUri;

    @Bean
    public OAuth2ProtectedResourceDetails resourceDetails() {
        final AuthorizationCodeResourceDetails details = new AuthorizationCodeResourceDetails();
        details.setClientId(clientId);
        details.setClientSecret(clientSecret);
        details.setAccessTokenUri(accessTokenUri);
        details.setUserAuthorizationUri(userAuthorizationUri);
        details.setScope(Arrays.asList("offline_access"));
        details.setPreEstablishedRedirectUri(redirectUri);
        details.setUseCurrentUri(true);
        return details;
    }

    @Bean
    public OAuth2RestTemplate restTemplate(final OAuth2ClientContext clientContext) {
        final OAuth2RestTemplate template = new OAuth2RestTemplate(resourceDetails(), clientContext);
        return template;
    }

    @Bean
    public TokenRefreshRestTemplate refreshTokenRestTemplate() {
        final TokenRefreshRestTemplate template = new TokenRefreshRestTemplate(resourceDetails());
        return template;
    }

}
