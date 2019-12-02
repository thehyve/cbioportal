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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cbioportal.model.DataAccessToken;
import org.cbioportal.service.DataAccessTokenService;
import org.cbioportal.service.DataAccessTokenServiceFactory;
import org.cbioportal.service.exception.DataAccessTokenNoUserIdentityException;
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
@Profile("!oauth2")
public class DataAccessTokenController {
    
    private final List<String> SUPPORTED_DAT_METHODS = Arrays.asList("uuid", "jwt", "oauth2", "none");
    
    @Value("${dat.method:none}")
    private String datMethod;

    @Autowired
    private DataAccessTokenServiceFactory dataAccessTokenServiceFactory;
    
    private DataAccessTokenService tokenService;

    @Value("${dat.uuid_revoke_other_tokens:false}")
    private Boolean allowRevocationOfOtherTokens;
    
    // fields for OAuth2 token data access 
    @Value("${dat.oauth2.userAuthorizationUri:}")
    private String oauth2UserAuthorizationUri;

    @Value("${dat.oauth2.redirectUri:}")
    private String oauth2RedirectUri;

    @Value("${dat.oauth2.clientId:}")

    private String oauth2ClientId;
    private String oauth2AuthorizationUrl;
    private String oauth2TokenDownloadFileName;

    @PostConstruct
    public void postConstruct() {
        if (SUPPORTED_DAT_METHODS.contains(datMethod)) {

            this.tokenService = this.dataAccessTokenServiceFactory.getDataAccessTokenService(this.datMethod);

            // FIXME: compose url using 3rd party lib <-- dangerous because of link forgery
            if (datMethod.equals("oauth2")) {
                oauth2AuthorizationUrl = String.format("%s?response_type=code&client_id=%s&redirect_uri=%s", oauth2UserAuthorizationUri, oauth2ClientId, oauth2RedirectUri);
            }

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
    @RequestParam(required = false) Boolean myAllowRevocationOfOtherTokens) throws HttpClientErrorException {
        // TODO: front end reads backend config and passes AppConfig.serverConfig.dat_uuid_revoke_other_tokens  
        // right back to the backend. This makes very little sense. I keep the allowRevocationOfOtherTokens
        // in the parameter list but no longer use it
        String userName = getAuthenticatedUser(authentication);
        DataAccessToken token = createDataAccessToken(userName, myAllowRevocationOfOtherTokens);
        if (token == null) {
            return new ResponseEntity<>(token, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }
    
    @RequestMapping(method = RequestMethod.GET, value = "/data-access-tokens")
    public ResponseEntity<List<DataAccessToken>> getAllDataAccessTokens(HttpServletRequest request,
    Authentication authentication) {
        String userName = getAuthenticatedUser(authentication);
        List<DataAccessToken> allDataAccessTokens = tokenService.getAllDataAccessTokens(userName);
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
    public void revokeDataAccessToken(@ApiParam(required = true, value = "token") @PathVariable String token) {
        tokenService.revokeDataAccessToken(token);
    }

    // this is the entrypoint for the cBioPortal frontend to download a single user token
    @RequestMapping("/data-access-token")
    public ResponseEntity<String> downloadDataAccessToken(Authentication authentication, HttpServletRequest request, HttpServletResponse response,
        @RequestParam(required = false, value = "file_name", defaultValue = "token.txt") @PathVariable String fileName) throws IOException {

        if (datMethod.equals("oauth2")) {

            oauth2TokenDownloadFileName = fileName;

            // for oauth2 redirect to authenticatoin endpoint
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", oauth2AuthorizationUrl);
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } else {
            // for other methods add header to trigger download of the token by the browser
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            String userName = getAuthenticatedUser(authentication);
            DataAccessToken token = createDataAccessToken(userName, allowRevocationOfOtherTokens);
            if (token == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(token.toString(), HttpStatus.CREATED);
        }

    }

    // retrieve and trigger download OAuth2 offline token
    @RequestMapping("/data-access-token/oauth2")
    public ResponseEntity<String> downloadOAuth2DataAccessToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String accessCode = request.getParameter("code");
        DataAccessToken offlineToken = tokenService.createDataAccessToken(accessCode);

        if (offlineToken == null) {
            throw new DataAccessTokenProhibitedUserException();
        }

        // add header to trigger download of the token by the browser
        response.setHeader("Content-Disposition", "attachment; filename=" + oauth2TokenDownloadFileName);
        
        return new ResponseEntity<>(offlineToken.toString(), HttpStatus.OK);
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

    private DataAccessToken createDataAccessToken(String userName, Boolean myAllowRevocationOfOtherTokens) {
        DataAccessToken token;
        if (myAllowRevocationOfOtherTokens != null) {
            token = tokenService.createDataAccessToken(userName, myAllowRevocationOfOtherTokens);
        }  else {
            token = tokenService.createDataAccessToken(userName);
        }
        return token;
    }

}
