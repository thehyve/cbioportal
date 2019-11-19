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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbioportal.model.DataAccessToken;
import org.cbioportal.service.DataAccessTokenService;
import org.cbioportal.service.exception.InvalidDataAccessTokenException;
import org.cbioportal.service.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component("oauth2")
public class OAuth2DataAccessTokenServiceImpl implements DataAccessTokenService {

    @Autowired
    private JwtUtils jwtUtils;

    private static final Log LOG = LogFactory.getLog(OAuth2DataAccessTokenServiceImpl.class);

    @Override
    public DataAccessToken createDataAccessToken(String username) {
        throw new UnsupportedOperationException("this implementation of OAuth2 tokens does not allow creation of t");
    }

    @Override
    public DataAccessToken createDataAccessToken(String username, boolean allowRevocationOfOtherTokens) {
        throw new UnsupportedOperationException("this implementation of (pure) JWT Data Access Tokens does not allow retrieval of stored tokens");
    }

    @Override
    public List<DataAccessToken> getAllDataAccessTokens(String username) {
        throw new UnsupportedOperationException("this implementation of (pure) JWT Data Access Tokens does not allow retrieval of stored tokens");
    }

    @Override
    public DataAccessToken getDataAccessToken(String username) {
        throw new UnsupportedOperationException("this implementation of (pure) JWT Data Access Tokens does not allow retrieval of stored tokens");
    }

    @Override
    public DataAccessToken getDataAccessTokenInfo(String token) {
        throw new UnsupportedOperationException("this implementation of (pure) JWT Data Access Tokens does not allow this operation");
    }

    @Override
    public void revokeAllDataAccessTokens(String username) {
        throw new UnsupportedOperationException("this implementation of (pure) JWT Data Access Tokens does not allow revocation of tokens");
    }

    @Override
    public void revokeDataAccessToken(String token) {
        throw new UnsupportedOperationException("this implementation of (pure) JWT Data Access Tokens does not allow revocation of tokens");
    }

    @Override
    public Boolean isValid(String token) {
        return Boolean.FALSE;
    }

    @Override
    public String getUsername(String token) {
        return null;
    }

    @Override
    public Date getExpiration(String token) {
        try {
            return jwtUtils.extractExpirationDate(token);
        } catch (InvalidDataAccessTokenException idate) {
            return null;
        }
    }

}
