/*
 * Copyright (c) 2018 Memorial Sloan-Kettering Cancer Center.
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

package org.cbioportal.service.impl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbioportal.model.DataAccessToken;
import org.cbioportal.service.DataAccessTokenService;
import org.cbioportal.service.exception.InvalidDataAccessTokenException;
import org.cbioportal.service.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// import com.auth0.jwk.Jwk;
// import com.auth0.jwk.JwkException;
// import com.auth0.jwk.JwkProvider;
// import com.auth0.jwk.UrlJwkProvider;

@Service
@Component("oauth2")
public class OAuth2DataAccessTokenServiceImpl implements DataAccessTokenService {

    @Autowired
    private JwtUtils jwtUtils;

    private static final Log LOG = LogFactory.getLog(OAuth2DataAccessTokenServiceImpl.class);

    @Override
    public DataAccessToken createDataAccessToken(final String username) {
        throw new UnsupportedOperationException("this implementation of OAuth2 tokens does not allow creation of t");
    }

    @Override
    public DataAccessToken createDataAccessToken(final String username, final boolean allowRevocationOfOtherTokens) {
        throw new UnsupportedOperationException(
                "this implementation of (pure) JWT Data Access Tokens does not allow retrieval of stored tokens");
    }

    @Override
    public List<DataAccessToken> getAllDataAccessTokens(final String username) {
        throw new UnsupportedOperationException(
                "this implementation of (pure) JWT Data Access Tokens does not allow retrieval of stored tokens");
    }

    @Override
    public DataAccessToken getDataAccessToken(final String username) {
        throw new UnsupportedOperationException(
                "this implementation of (pure) JWT Data Access Tokens does not allow retrieval of stored tokens");
    }

    @Override
    public DataAccessToken getDataAccessTokenInfo(final String token) {
        throw new UnsupportedOperationException(
                "this implementation of (pure) JWT Data Access Tokens does not allow this operation");
    }

    @Override
    public void revokeAllDataAccessTokens(final String username) {
        throw new UnsupportedOperationException(
                "this implementation of (pure) JWT Data Access Tokens does not allow revocation of tokens");
    }

    @Override
    public void revokeDataAccessToken(final String token) {
        throw new UnsupportedOperationException(
                "this implementation of (pure) JWT Data Access Tokens does not allow revocation of tokens");
    }

    @Override
    public Boolean isValid(final String token) {
        return Boolean.FALSE;
    }

    @Override
    public String getUsername(final String token) {
        return null;
    }

    @Override
    public Date getExpiration(final String token) {
        try {
            return jwtUtils.extractExpirationDate(token);
        } catch (final InvalidDataAccessTokenException idate) {
            return null;
        }
    }

    @Override
    // TODO: extract Authorities
    public Set<GrantedAuthority> getAuthorities(final String token) {
        
        // final Jwt tokenDecoded = JwtHelper.decodeAndVerify(accessToken, verifier(kid));

        // // get claims from token
        // final String claims = tokenDecoded.getClaims();
        // final JsonNode claimsMap = new ObjectMapper().readTree(claims);

        // Map<String, Object> resourceAccessClaims = (Map<String, Object>) body.get("resource_access");
        // Map<String, Object> cbioportalClaims = (Map<String, Object>) resourceAccessClaims.get("cbioportal");
        // List<String> authorities = (List<String>) cbioportalClaims.get("roles");

        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        // final Iterable<JsonNode> roles = () -> claimsMap.get("resource_access").get("cbioportal").get("roles")
        //         .getElements();
        // authorities = StreamSupport.stream(roles.spliterator(), false).map(role -> role.toString().replaceAll("\"", ""))
        //         .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
        return authorities;
    }

    /**
	 * Removes signature for JWT parsing.
	 *
	 * @param jws Signed JWT
	 * @return JWT unsigned
	 */
	private String removeSignature(String jws) {
		int i = jws.lastIndexOf('.');
		return jws.substring(0, i + 1);
	}

}
