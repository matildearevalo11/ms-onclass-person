package com.pragma.powerup.infrastructure.constants;

public final class ApiPaths {
    public static final String LOGIN = "/auth/login";
    public static final String PERSONS = "/persons";
    public static final String OPENAPI = "/openapi.yaml";
    public static final String OPENAPI_FILE = "openapi.yaml";

    private ApiPaths() {
    }

    public static String[] documentation() {
        return new String[] {OPENAPI, "/v3/api-docs", "/v3/api-docs/swagger-config", "/webjars/swagger-ui/**",
                "/swagger-ui/**", "/swagger-ui.html"};
    }
}
