package com.placement.platform.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("AI Placement Platform API")
                        .version("1.0.0")
                        .description("Production-ready API documentation for the AI Placement Platform backend services.")
                        .contact(new Contact()
                                .name("Antigravity Dev Team")
                                .email("dev@placementplatform.com")
                                .url("https://placementplatform.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication", new SecurityScheme()
                                .name("Bearer Authentication")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .description("JWT Bearer token authentication")));
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("Authentication")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi profileApi() {
        return GroupedOpenApi.builder()
                .group("Profile")
                .pathsToMatch("/api/profile/**")
                .pathsToExclude("/api/profile/skills/**", "/api/profile/companies/**")
                .build();
    }

    @Bean
    public GroupedOpenApi resumeApi() {
        return GroupedOpenApi.builder()
                .group("Resume")
                .pathsToMatch("/api/resume/**")
                .build();
    }

    @Bean
    public GroupedOpenApi skillsApi() {
        return GroupedOpenApi.builder()
                .group("Skills")
                .pathsToMatch("/api/profile/skills/**")
                .build();
    }

    @Bean
    public GroupedOpenApi companiesApi() {
        return GroupedOpenApi.builder()
                .group("Companies")
                .pathsToMatch("/api/companies/**", "/api/company/**", "/api/profile/companies/**")
                .build();
    }

    @Bean
    public GroupedOpenApi interviewApi() {
        return GroupedOpenApi.builder()
                .group("Interview")
                .pathsToMatch("/api/interview/**")
                .build();
    }

    @Bean
    public GroupedOpenApi placementApi() {
        return GroupedOpenApi.builder()
                .group("Placement")
                .pathsToMatch("/api/dashboard/**")
                .build();
    }

    @Bean
    public GroupedOpenApi jobsApi() {
        return GroupedOpenApi.builder()
                .group("Jobs")
                .pathsToMatch("/api/jobs/**")
                .pathsToExclude("/api/jobs/sync")
                .build();
    }

    @Bean
    public GroupedOpenApi careerIntelligenceApi() {
        return GroupedOpenApi.builder()
                .group("Career Intelligence")
                .pathsToMatch("/api/career/**")
                .build();
    }

    @Bean
    public GroupedOpenApi premiumApi() {
        return GroupedOpenApi.builder()
                .group("Premium")
                .pathsToMatch("/api/premium/**")
                .build();
    }

    @Bean
    public GroupedOpenApi administrationApi() {
        return GroupedOpenApi.builder()
                .group("Administration")
                .pathsToMatch("/api/subscription/**", "/api/jobs/sync")
                .build();
    }
}
