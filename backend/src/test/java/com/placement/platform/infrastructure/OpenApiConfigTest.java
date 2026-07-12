package com.placement.platform.infrastructure;

import com.placement.platform.config.OpenApiConfig;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OpenApiConfigTest {

    @Test
    public void testOpenApiBeans() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();
        assertNotNull(openAPI);
        assertEquals("AI Placement Platform API", openAPI.getInfo().getTitle());

        GroupedOpenApi authApi = config.authApi();
        assertNotNull(authApi);
        assertEquals("Authentication", authApi.getGroup());
    }
}
