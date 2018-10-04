/*
 * Copyright (c) 2018 The Hyve B.V.
 * This code is licensed under the GNU Affero General Public License (AGPL),
 * version 3, or (at your option) any later version.
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

package org.cbioportal.security.spring.authentication.keycloak;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cbioportal.security.spring.authentication.PortalUserDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService for integration with Keycloak's OpenID.
 *
 * @author Pieter Lukasse
 */
@Primary
@Service
public class OpenIDPortalUserDetailsService
    implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    // logger
    private static final Log log = LogFactory.getLog(OpenIDPortalUserDetailsService.class);
    @Value("${saml.idp.metadata.attribute.email:}") 
    private String IDP_METADATA_EMAIL_ATTR_NAME; //TODO - use idp in property name

    @Value("${saml.idp.metadata.attribute.role:}")
    private String IDP_METADATA_ROLE_ATTR_NAME;

    /**
     * Constructor.
     *
     */
    public OpenIDPortalUserDetailsService() {
    }
          

    /**
     * Implementation of {@code UserDetailsService}.
     * We only need this to satisfy the {@code RememberMeServices} requirements.
     */
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        throw new UnsupportedOperationException();
    }

    /**
     * Implementation of {@code AuthenticationUserDetailsService}
     * which allows full access to the submitted {@code Authentication} object.
     * Used by the OpenIDAuthenticationProvider.
     */
    public UserDetails loadUserDetails(OpenIDAuthenticationToken credential) throws UsernameNotFoundException {

        PortalUserDetails userDetailsObj = null;
        String userId = null;
        List<String> userRoles = new ArrayList<String>();
        
        // get userid and roles: iterate over attributes searching for "email" and "roles":
        for (OpenIDAttribute cAttribute : credential.getAttributes()) {
            String attrName = cAttribute.getName();
            log.debug("loadUserByOpenID_Keycloak(), parsing attribute -" + attrName);
            
            //email as user id:
            if (userId == null && attrName.equals(IDP_METADATA_EMAIL_ATTR_NAME)) {
                userId = cAttribute.getValues().get(0);
                log.debug("loadUserByOpenID_Keycloak(), found: " + userId);
            }
            //TODO: iterate over roles. Hardcoding brca_tcga for now:
            userRoles.add("brca_tcga");
        }
        

        try {
            
            if (userId == null) {

                String errorMsg = "loadUserByOpenID_Keycloak(), can not instantiate PortalUserDetails from OpenID token."
                    + " Expected 'email' attribute was not found or has no values. ";
                log.error(errorMsg);
                throw new Exception(errorMsg);
            }

            log.debug("loadUserByOpenID_Keycloak(), IDP successfully authenticated user, userid: " + userId);

            //add granted authorities:
            if (userRoles.size() > 0) userDetailsObj = new PortalUserDetails(userId,
                AuthorityUtils.createAuthorityList(userRoles.toArray(new String[userRoles.size()])));
            else
                userDetailsObj = new PortalUserDetails(userId, AuthorityUtils.createAuthorityList(new String[0]));
            userDetailsObj.setEmail(userId);
            userDetailsObj.setName(userId);
            return userDetailsObj;
        }
        catch (Exception e) {
            throw new RuntimeException("Error occurs during authentication: " + e.getMessage());
        }
    }
}
