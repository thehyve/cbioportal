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

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.cbioportal.model.DataAccessToken;
import org.cbioportal.service.DataAccessTokenService;
import org.cbioportal.service.DataAccessTokenServiceFactory;
import org.cbioportal.service.exception.DataAccessTokenNoUserIdentityException;
import org.cbioportal.service.exception.DataAccessTokenProhibitedUserException;
import org.cbioportal.web.config.annotation.InternalApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class DataAccessTokenController {

    private final List<String> SUPPORTED_DAT_METHODS = Arrays.asList("uuid", "jwt", "oauth2", "none");
    
    @Value("${dat.method:none}") // default value is none
    private String datMethod;
    
    @Autowired
    private DataAccessTokenServiceFactory dataAccessTokenServiceFactory;
    
    private DataAccessTokenService tokenService;

    @Value("${dat.oauth2.clientId}")
    private String clientId;

    @Value("${dat.oauth2.clientSecret}")
    private String clientSecret;

    @Value("${dat.oauth2.accessTokenUri}")
    private String accessTokenUri;

    @Value("${dat.oauth2.frontChanelAuthorizationUri}")
    private String userAuthorizationUri;

    @Value("${dat.oauth2.redirectUri}")
    private String redirectUri;

    private String oauth2TokenDownloadEndpointUrl = "https://localhost/api/oauth2-token-download";
    
    @PostConstruct
    public void postConstruct() {
        if (SUPPORTED_DAT_METHODS.contains(datMethod)) {
            this.tokenService = this.dataAccessTokenServiceFactory.getDataAccessTokenService(this.datMethod);
        } else {
            throw new RuntimeException("Specified data access token method, " + datMethod + " is not supported");
        }
    }

    @Value("${dat.unauth_users:anonymousUser}")
    private String[] USERS_WHO_CANNOT_USE_TOKENS;
    private Set<String> usersWhoCannotUseTokenSet;

    @Autowired
    private void initializeUsersWhoCannotUseTokenSet() {
        usersWhoCannotUseTokenSet = new HashSet<>(Arrays.asList(USERS_WHO_CANNOT_USE_TOKENS));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/data-access-tokens", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataAccessToken> createDataAccessToken(Authentication authentication,
                                    @RequestParam(required = false) Boolean allowRevocationOfOtherTokens) throws HttpClientErrorException {

        DataAccessToken createdToken;
        if (allowRevocationOfOtherTokens != null) {
            createdToken = tokenService.createDataAccessToken(getAuthenticatedUser(authentication), allowRevocationOfOtherTokens);
        }
        else {
            createdToken = tokenService.createDataAccessToken(getAuthenticatedUser(authentication));
        }
        if (createdToken == null) {
            return new ResponseEntity<>(new DataAccessToken(null), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(createdToken, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/data-access-tokens")
    public ResponseEntity<List<DataAccessToken>> getAllDataAccessTokens(HttpServletRequest request, Authentication authentication) {

        String serverUrl = request.getRequestURI();

        if (datMethod.equals("oauth2")) {
            
            String url = userAuthorizationUri + "?response_type=token&client_id=" + clientId + "&redirect_uri=" + oauth2TokenDownloadEndpointUrl;
            
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", url);
            
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        
        List<DataAccessToken> allDataAccessTokens = tokenService.getAllDataAccessTokens(getAuthenticatedUser(authentication));
        return new ResponseEntity<>(allDataAccessTokens, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/data-access-tokens/{token}")
    public ResponseEntity<DataAccessToken> getDataAccessToken(
            @ApiParam(required = true, value = "token") @PathVariable String token) {
        DataAccessToken dataAccessToken = tokenService.getDataAccessTokenInfo(token);
        return new ResponseEntity<>(dataAccessToken, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/data-access-tokens")
    public void revokeAllDataAccessTokens(Authentication authentication) {
        tokenService.revokeAllDataAccessTokens(getAuthenticatedUser(authentication));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/data-access-tokens/{token}")
    public void revokeDataAccessToken(
            @ApiParam(required = true, value = "token") @PathVariable String token) {
        tokenService.revokeDataAccessToken(token);
    }

    @RequestMapping("/oauth2-token-download")
    public void getOAuth2DataAccessToken(HttpServletRequest request) {
        int i = 1;
    }

    private String getAuthenticatedUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new DataAccessTokenNoUserIdentityException();
        }
        String username = authentication.getName();
        if (usersWhoCannotUseTokenSet.contains(username)) {
            throw new DataAccessTokenProhibitedUserException();
        }
        return username;
    }
}
