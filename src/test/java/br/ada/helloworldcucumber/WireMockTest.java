package br.ada.helloworldcucumber;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Rule;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

public class WireMockTest {

    private static final Integer PORT= 8089;
    private static final String APPLICATION_JSON = "application/json";

    private static final RequestSpecification request = RestAssured.given()
            .baseUri("http://localhost:"+ PORT +"/api")
            .contentType(ContentType.JSON);

    @Rule
    public WireMockRule wireMock = new WireMockRule(PORT);

    @Test
    public void findMovieWithWireMock(){
        wireMockGetMovieStub();

        Response response = request.when().get("/movies");

        assertEquals(200, response.getStatusCode());
    }

    // Teste de criaçao de movie com Erro
    @Test
    public void createMovieWithError() {
        wireMockCreateMovieError();

        Response response = request.body(" {\"id\": 1, \"title\": \"Deadpool\",\"genre\": \"Comedia\",\"raiting\": 11 }")
                .when().post("/movies");

        assertEquals(400, response.getStatusCode());
    }

    // Teste de criaçao de movie com Sucesso
    @Test
    public void createMovieWithSucess() {
        // Simulaçao da API (Create Movie)
        wireMockCreateMovie();

        Response response = request.body("""
                                {
                                    "title": "Deadpool",
                                    "genre": "Comedia",
                                    "raiting": 9
                                }
                                """).when().post("/movies");

        assertEquals(201, response.getStatusCode());
    }

    // Teste de buscar o movie atraves do ID com Erro
    @Test
    public void getMovieWithIdWithError(){
        wireMockGetMovieStubWithError();

        Response response = request.when().get("/movies/1");

        assertEquals(404, response.getStatusCode());
    }

// Stubs (Simulações de API)
    private void wireMockGetMovieStub() {
        stubFor(get(urlPathMatching("/api/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody("""
                                {
                                    "id": 1,
                                    "title": "Deadpool",
                                    "genre": "Comedia",
                                    "raiting": 9
                                }
                                """)
                )
        );
    }

    private void wireMockGetMovieStubWithError(){
        stubFor(get(urlPathMatching("/api/.*"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody("""
                                {
                                  "message": "Not found",
                                  "code": "NOT_FOUND"
                                }
                                """
                        )
                )

        );
    }

    private void wireMockCreateMovieError() {
        stubFor(post(urlPathMatching("/api/.*"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody(
                                """
                                    {
                                      "errors": [
                                        {
                                          "rating": "must be less than or equal to 10"
                                        }
                                      ],
                                      "code": "BAD_REQUEST"
                                    }
                                """
                        )
                )

        );
    }

    private void wireMockCreateMovie(){
        stubFor(post(urlPathMatching("/api/.*"))
                .willReturn(aResponse()
                        .withStatus(201)
                        .withHeader("Content-Type", APPLICATION_JSON)
                        .withBody("""
                                {
                                    "id": 1,
                                    "title": "Deadpool",
                                    "genre": "Comedia",
                                    "raiting": 9
                                }
                                """)
                ));
    }
}
