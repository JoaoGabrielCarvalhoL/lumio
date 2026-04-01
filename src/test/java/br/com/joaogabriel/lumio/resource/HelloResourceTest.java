package br.com.joaogabriel.lumio.resource;


import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
class HelloResourceTest {
	
	@ConfigProperty(name = "quarkus.oidc.enabled")
    String oidcEnabled;

    @Test
    void test() {
        System.out.println("OIDC ENABLED = " + oidcEnabled);
    }

    @Test
    void testHello() {
        RestAssured
                .given()
                .when().get("/hello")
                .then()
                .statusCode(200)
                .body(Matchers.is("Hello"));
    }
}
