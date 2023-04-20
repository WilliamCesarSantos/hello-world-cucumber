package br.ada.helloworldcucumber;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;

public class TokenUtil {

    public static String getToken() {
        //Com o uso do docker
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .when().post("http://localhost:8282/api/token");
        response.prettyPrint();
        return response.jsonPath().get("access_token");
    }

    public static String getTokenWithoutDocker() {
        return RandomStringUtils.randomAlphanumeric(150);
    }

}
