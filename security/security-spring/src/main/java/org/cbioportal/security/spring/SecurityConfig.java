package org.cbioportal.security.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * SecurityConfig
 */
@Configuration
@ComponentScan(basePackages = "org.cbioportal.service.impl")
public class SecurityConfig {

}