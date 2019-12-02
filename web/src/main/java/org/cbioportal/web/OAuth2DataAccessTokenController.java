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

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cbioportal.model.DataAccessToken;
import org.cbioportal.service.DataAccessTokenService;
import org.cbioportal.service.exception.DataAccessTokenProhibitedUserException;
import org.cbioportal.web.config.annotation.InternalApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@InternalApi
@RestController
@Validated
@Api(tags = "Data Access Tokens", description = " ")
@Profile("dat.oauth2")
public class OAuth2DataAccessTokenController {
    
    @Value("${dat.oauth2.userAuthorizationUri}")
    private String userAuthorizationUri;
    
    @Value("${dat.oauth2.clientId}")
    private String clientId;
    
    @Autowired
    private DataAccessTokenService tokenService;
    private String authorizationUrl;
    private String fileName;

    private final String REDIRECT_PATH = "/api/data-access-token/oauth2";
    
    // This is the entrypoint for the cBioPortal frontend to download a single user token
    // The user is redirected to retrieve an access code (later used by the backend to 
    // request an offline token).
    @RequestMapping("/data-access-token")
    public ResponseEntity<String> downloadDataAccessToken(final Authentication authentication, final HttpServletRequest request, final HttpServletResponse response,
        @ApiParam(required = false, value = "file_name", defaultValue = "token.txt") @PathVariable final String fileName) throws IOException {

        // The authorization url contains the redirect url as a url param
        // The redirect url is resolved using the base url of the server to
        // remove the need for an extra entry in portal.properties.
        if (authorizationUrl == null) {
            String url = request.getRequestURL().toString();
            String baseURL = url.substring(0, url.length() - request.getRequestURI().length()) + request.getContextPath();
            String redirectUri = baseURL + REDIRECT_PATH;
            authorizationUrl = String.format("%s?response_type=code&client_id=%s&redirect_uri=%s", userAuthorizationUri, clientId, redirectUri);
        }

        this.fileName = fileName;

        // redirect to authentication endpoint
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Location", authorizationUrl);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);

    }

    // retrieve and trigger download OAuth2 offline token
    @RequestMapping("/data-access-token/oauth2")
    public ResponseEntity<String> downloadOAuth2DataAccessToken(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        
        final String accessCode = request.getParameter("code");
        final DataAccessToken offlineToken = tokenService.createDataAccessToken(accessCode);

        if (offlineToken == null) {
            throw new DataAccessTokenProhibitedUserException();
        }

        // add header to trigger download of the token by the browser
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        
        return new ResponseEntity<>(offlineToken.toString(), HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/data-access-tokens", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataAccessToken> createDataAccessToken(final Authentication authentication,
    @RequestParam(required = false) final Boolean myAllowRevocationOfOtherTokens) throws HttpClientErrorException {
        throw new UnsupportedOperationException("this endpoint is does not apply to OAuth2 data access token method.");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/data-access-tokens")
    public ResponseEntity<List<DataAccessToken>> getAllDataAccessTokens(final HttpServletRequest request,
    final Authentication authentication) {
        throw new UnsupportedOperationException("this endpoint is does not apply to OAuth2 data access token method.");
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/data-access-tokens/{token}")
    public ResponseEntity<DataAccessToken> getDataAccessToken(
        @ApiParam(required = true, value = "token") @PathVariable final String token) {
        throw new UnsupportedOperationException("this endpoint is does not apply to OAuth2 data access token method.");
    
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/data-access-tokens")
    public void revokeAllDataAccessTokens(final Authentication authentication) {
        throw new UnsupportedOperationException("this endpoint is does not apply to OAuth2 data access token method.");
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/data-access-tokens/{token}")
    public void revokeDataAccessToken(@ApiParam(required = true, value = "token") @PathVariable final String token) {
        throw new UnsupportedOperationException("this endpoint is does not apply to OAuth2 data access token method.");
    }

}
