package org.cbioportal.security.spring;

import org.cbioportal.security.spring.authentication.RestAuthenticationEntryPoint;
import org.cbioportal.utils.config.annotation.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@Order(SecurityProperties.BASIC_AUTH_ORDER - 2)
@ConditionalOnProperty(name = "authenticate", havingValue = {"saml"})
public class ApiSecurityConfig extends WebSecurityConfigurerAdapter {

    // Add security filter chains that handle calls to the API endpoints.
    // Different chains are added for the '/api' and legacy '/webservice.do' paths.
    // Both are able to handle API tokens provided in the request.
    // see: "Creating and Customizing Filter Chains" @ https://spring.io/guides/topicals/spring-security-architecture
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
                .sessionFixation().none()
            .and()
                // add ExceptionTranslationFilter to the chain
                .exceptionHandling()                    
                .authenticationEntryPoint(restAuthenticationEntryPoint())
            .and()
            .antMatcher("/api/**")
                .authorizeRequests()
                    .antMatchers("/api/swagger-resources/**",
                         "/api/swagger-ui.html",
                         "/api/api-docs",
                         "/api/health",
                         "/api/cache/**").permitAll()
                    .anyRequest()
                        .authenticated();
    }

    @Bean
    public RestAuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }
 
}
