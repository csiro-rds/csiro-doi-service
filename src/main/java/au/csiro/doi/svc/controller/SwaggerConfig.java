package au.csiro.doi.svc.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;

/*
 * #%L
 * CSIRO RPR
 * %%
 * Copyright (C) 2010 - 2016 Commonwealth Scientific and Industrial Research Organisation (CSIRO) ABN 41 687 119 230.
 * %%
 * Licensed under the CSIRO Open Source License Agreement (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License in the LICENSE file.
 * #L%
 */

/**
 * Swagger configuration for all web end points
 * 
 * Copyright 2016, CSIRO Australia
 * All rights reserved.
 * 
 * @author      pul052 on 11 Aug 2016
 * @version     $Revision$  $Date$
 */
@Configuration
@ComponentScan("au.csiro.doi.svc.controller")
public class SwaggerConfig
{
    /**
     * @return a Docket
     */
    @Bean
    public Docket api()
    {
        //basic auth
        List<SecurityScheme> authSchemes = new ArrayList<SecurityScheme>();
        authSchemes.add(new BasicAuth("basicAuth"));
        //authSchemes.add(apiKey());
        
        return new Docket(DocumentationType.SWAGGER_2)
                .securitySchemes(authSchemes)
                .securityContexts(getSecurityContexts())
                .select()
                .apis(RequestHandlerSelectors.basePackage("au.csiro.doi.svc.controller"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());
    }
    
    /**
     * @return the UiConfiguration
     */
    @Bean
    public UiConfiguration uiConfig() 
    {
        return UiConfigurationBuilder
                .builder()
                .operationsSorter(OperationsSorter.METHOD)
                .build();
    }
    
    private ApiKey apiKey()
    {
        return new ApiKey("Authorization", "api_key", "header");
    }

    private List<SecurityContext> getSecurityContexts()
    {
        List<SecurityContext> securityContexts = new ArrayList<SecurityContext>();
        securityContexts.add(SecurityContext.builder().securityReferences(getSecurityReferences())
                .forPaths(PathSelectors.any()).build());
        return securityContexts;
    }
    
    private List<SecurityReference> getSecurityReferences()
    {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> secRefs = new ArrayList<SecurityReference>();
        secRefs.add(new SecurityReference("basicAuth", authorizationScopes));
        secRefs.add(new SecurityReference("Authorization", authorizationScopes));
        return secRefs;
    }
    
    @SuppressWarnings("deprecation")
    private ApiInfo apiInfo() 
    {
        ApiInfo apiInfo = new ApiInfo(
          "DOI Service REST API",
          "Swagger API Documentation for the DOI Service.\r\n" + 
          "These endpoints are primarily used for minting and updating DOI.",
          "",//API TOS
          "",
          "researchdatasupport@csiro.au",
          "",//License CSIRO Only
          "");//API license URL
        return apiInfo;
    }    
}
