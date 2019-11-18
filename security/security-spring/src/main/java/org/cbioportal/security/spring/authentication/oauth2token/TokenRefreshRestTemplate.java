package org.cbioportal.security.spring.authentication.oauth2token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class TokenRefreshRestTemplate extends RestTemplate {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    OAuth2ProtectedResourceDetails resourceDetails;

    public TokenRefreshRestTemplate(OAuth2ProtectedResourceDetails tokenRefreshResourceDetails) {
        resourceDetails = tokenRefreshResourceDetails;
    }

    public String getAccessToken(String offline_token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "refresh_token");
        map.add("client_id", resourceDetails.getClientId());
        map.add("client_secret", resourceDetails.getClientSecret());
        map.add("refresh_token", offline_token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<String> response = postForEntity(
            resourceDetails.getAccessTokenUri(), request , String.class);

        Pattern pattern = Pattern.compile("access_token\":\"([^\"]+)");
        String body = response.getBody();
        Matcher matcher = pattern.matcher(body);
        String accessToken = null;
        if (matcher.find()) {
            accessToken = matcher.group(1);
            logger.debug("Received response from token endpoint:\n" + accessToken);
        }
        return accessToken;
    }

}
