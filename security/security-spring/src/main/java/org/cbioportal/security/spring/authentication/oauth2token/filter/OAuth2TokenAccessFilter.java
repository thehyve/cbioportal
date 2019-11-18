package org.cbioportal.security.spring.authentication.oauth2token.filter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;

import org.cbioportal.security.spring.authentication.oauth2token.TokenRefreshRestTemplate;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

public class OAuth2TokenAccessFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${dat.oauth2.jwkUrl}")
    private String jwkUrl;

    @Value("${dat.oauth2.issuer}")
    private String issuer;

    @Value("${dat.oauth2.clientId}")
    private String clientId;

    @Autowired
    TokenRefreshRestTemplate tokenRefreshRestTemplate;

    public OAuth2TokenAccessFilter(final String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationSuccessHandler(customRedirectHandler());
    }

    @Override
    @Autowired
    public void setAuthenticationManager(final AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // get the offline token provided by the user (script)
        final String offlineToken = extractHeaderToken(request);

        if (offlineToken == null) {
            logger.error("Exception", "No token was found in request header");
            throw new BadCredentialsException("No offlineToken was passed");
        }

        // request an access token from the OAuth2 identity provider
        final String accessToken = tokenRefreshRestTemplate.getAccessToken(offlineToken);

        // extract the key-id for public key that signed the JWT
        final String kid = JwtHelper.headers(accessToken).get("kid");

        Set<GrantedAuthority> authorities = null;
        String userName = null;
        try {

            // validate token (using public key of the AOuth2 identity provider)
            final Jwt tokenDecoded = JwtHelper.decodeAndVerify(accessToken, verifier(kid));

            // get claims from token
            final String claims = tokenDecoded.getClaims();
            final JsonNode claimsMap = new ObjectMapper().readTree(claims);

            hasValidIssuer(claimsMap);
            hasValidClientId(claimsMap);

            userName = extractUserName(claimsMap);
            authorities = extractAuthorities(claimsMap);

        } catch (final MalformedURLException e) {
            logger.error("Exception", "Malformed URL for token endpoint (value encountered: '" + "')");
            throw e;
        } catch (final JwkException e) {
            logger.error("Exception", "Invalid JWT signature");
            throw new BadCredentialsException("Invalid JWT signature");
        } catch (final Exception e) {
            e.printStackTrace();
            throw e;
        }

        return new UsernamePasswordAuthenticationToken(userName, "", authorities);
    }

    private AuthenticationSuccessHandler customRedirectHandler() {
        return new SavedRequestAwareAuthenticationSuccessHandler() {
            @Override
            protected String determineTargetUrl(final HttpServletRequest request, final HttpServletResponse response) {
                return request.getServletPath();
            }

            @Override
            public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws ServletException, IOException {
                request.getRequestDispatcher(request.getServletPath()).forward(request, response);
            }
        };
    }

    private Set<GrantedAuthority> extractAuthorities(final JsonNode claimsMap) {
        Set<GrantedAuthority> authorities;
        final Iterable<JsonNode> roles = () -> claimsMap.get("resource_access").get("cbioportal").get("roles")
                .getElements();
        authorities = StreamSupport.stream(roles.spliterator(), false).map(role -> role.toString().replaceAll("\"", ""))
                .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toSet());
        return authorities;
    }

    private String extractUserName(final JsonNode claimsMap) {
        String userName;
        userName = claimsMap.get("user_name").asText();
        return userName;
    }

    private void hasValidIssuer(final JsonNode claimsMap) throws BadCredentialsException {
        if (! claimsMap.get("iss").asText().equals(issuer)) {
            throw new BadCredentialsException("Wrong Issuer found in token");
        }
    }

    private void hasValidClientId(final JsonNode claimsMap) throws BadCredentialsException {
        if (! claimsMap.get("aud").asText().equals(clientId)) {
            throw new BadCredentialsException("Wrong clientId found in token");
        }
    }

    protected String extractHeaderToken(final ServletRequest request) {
        final String authorizationHeader = ((HttpServletRequest) request).getHeader("Authorization");
        if (!StringUtils.isEmpty(authorizationHeader)) {
            if ((authorizationHeader.toLowerCase().startsWith("Bearer".toLowerCase()))) {
                return authorizationHeader.substring("Bearer".length()).trim();
            }
        }
        return null;
    }

    private RsaVerifier verifier(final String kid) throws MalformedURLException, JwkException {
        final JwkProvider provider = new UrlJwkProvider(new URL(jwkUrl));
        final Jwk jwk = provider.get(kid);
        final RSAPublicKey publicKey = (RSAPublicKey) jwk.getPublicKey();
        return new RsaVerifier(publicKey, "SHA512withRSA");
    }

}
