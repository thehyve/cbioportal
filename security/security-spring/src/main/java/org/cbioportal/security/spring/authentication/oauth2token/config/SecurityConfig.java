package org.cbioportal.security.spring.authentication.oauth2token.config;

import org.cbioportal.security.spring.authentication.oauth2token.filter.OAuth2TokenRetrievalFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    public static class OAuth2TokenConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired
        OAuth2TokenRetrievalFilter oAuth2TokenRetrievalFilter;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                .antMatcher("/token/**")
                .addFilterAfter(new OAuth2ClientContextFilter(), AbstractPreAuthenticatedProcessingFilter.class)
                .addFilterAfter(oAuth2TokenRetrievalFilter, OAuth2ClientContextFilter.class)
                .httpBasic().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/token/login"))
                .and()
                .authorizeRequests()
                    .antMatchers("/token").fullyAuthenticated()
                .and()
                .authorizeRequests()
                    .antMatchers("/token/login").permitAll();
            ;
        }

    }

}
