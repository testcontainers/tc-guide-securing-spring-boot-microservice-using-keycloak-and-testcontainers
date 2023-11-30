package com.testcontainers.products.api;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static java.util.Collections.singletonList;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.testcontainers.products.ContainersConfig;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(ContainersConfig.class)
class ProductControllerTests {

    static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
    static final String CLIENT_ID = "product-service";
    static final String CLIENT_SECRET = "jTJJqdzeCSt3DmypfHZa42vX8U9rQKZ9";

    @LocalServerPort
    private int port;

    @Autowired
    OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void shouldGetProductsWithoutAuthToken() {
        when().get("/api/products").then().statusCode(200);
    }

    @Test
    void shouldGetUnauthorizedWhenCreateProductWithoutAuthToken() {
        given()
            .contentType("application/json")
            .body(
                """
                    {
                        "title": "New Product",
                        "description": "Brand New Product"
                    }
                """
            )
            .when()
            .post("/api/products")
            .then()
            .statusCode(401);
    }

    @Test
    void shouldCreateProductWithAuthToken() {
        String token = getToken();

        given()
            .header("Authorization", "Bearer " + token)
            .contentType("application/json")
            .body(
                """
                    {
                        "title": "New Product",
                        "description": "Brand New Product"
                    }
                """
            )
            .when()
            .post("/api/products")
            .then()
            .statusCode(201);
    }

    private String getToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("grant_type", singletonList(GRANT_TYPE_CLIENT_CREDENTIALS));
        map.put("client_id", singletonList(CLIENT_ID));
        map.put("client_secret", singletonList(CLIENT_SECRET));

        String authServerUrl =
            oAuth2ResourceServerProperties.getJwt().getIssuerUri() +
            "/protocol/openid-connect/token";

        var request = new HttpEntity<>(map, httpHeaders);
        KeyCloakToken token = restTemplate.postForObject(
            authServerUrl,
            request,
            KeyCloakToken.class
        );

        assert token != null;
        return token.accessToken();
    }

    record KeyCloakToken(@JsonProperty("access_token") String accessToken) {}
}
