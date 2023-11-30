package com.testcontainers.products;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;

@TestConfiguration(proxyBeanMethods = false)
public class ContainersConfig {

    static String KEYCLOAK_IMAGE = "quay.io/keycloak/keycloak:23.0.1";
    static String realmImportFile = "/keycloaktcdemo-realm.json";
    static String realmName = "keycloaktcdemo";

    @Bean
    KeycloakContainer keycloak(DynamicPropertyRegistry registry) {
        var keycloak = new KeycloakContainer(KEYCLOAK_IMAGE)
            .withRealmImportFile(realmImportFile);
        registry.add(
            "spring.security.oauth2.resourceserver.jwt.issuer-uri",
            () -> keycloak.getAuthServerUrl() + "/realms/" + realmName
        );
        return keycloak;
    }
}
