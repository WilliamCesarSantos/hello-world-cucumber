package br.ada.helloworldcucumber;

import br.ada.helloworldcucumber.model.Movie;
import br.ada.helloworldcucumber.util.DatabaseUtil;
import com.google.gson.Gson;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Assertions;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collection;


public class MovieStepDefinition {

    private Gson gson = new Gson();
    private Movie movie = null;
    private RequestSpecification request = RestAssured.given()
            .baseUri("http://localhost:8080/api")
            .contentType(ContentType.JSON);
    private Response response;

    @Before
    public void setup() {
        String token = TokenUtil.getToken();
        request.header(HttpHeaders.AUTHORIZATION, token);
        request.header(HttpHeaders.ACCEPT_LANGUAGE, "en-US");
    }

    @Given("I have movie registered")
    public void iHaveMovieRegistered(DataTable data) throws SQLException {
        movie = createMovieFromDataTable(data);
        DatabaseUtil.insertMovie(movie);
    }

    @Given("that I don't have the movie registered")
    public void iDontHaveMovieRegistered(DataTable data) throws SQLException {
        movie = createMovieFromDataTable(data);

        Movie found = DatabaseUtil.readMovie(movie.getTitle());
        Assertions.assertNull(found);
    }

    @When("I search the movie by title")
    public void searchTheMovie() {
        response = request.when().get("/movies?title=" + movie.getTitle());
        response.prettyPrint();
    }

    @When("I search the movie by id")
    public void searchById() {
        response = request.when().get("/movies/" + movie.getId());
        response.prettyPrint();
    }

    @When("I register de the movie")
    public void registerMovie() {
        String jsonBody = gson.toJson(movie);
        response = request.body(jsonBody).when().post("/movies");
        response.prettyPrint();
    }

    @When("I apply update on movie")
    public void updateMovie(DataTable data) throws SQLException {
        movie = DatabaseUtil.readMovie(movie.getTitle());
        String title = data.asMap().get("title");
        if (title != null) {
            movie.setTitle(title);
        }
        String genre = data.asMap().get("genre");
        if (genre != null) {
            movie.setGenre(genre);
        }
        String rating = data.asMap().get("rating");
        if (rating != null) {
            movie.setRating(Float.valueOf(rating));
        }
        String jsonBody = gson.toJson(movie);
        response = request.body(jsonBody).when().put("/movies/" + movie.getId());
        response.prettyPrint();
    }

    @Then("I should found {string} movie")
    public void foundMovieByTitle(String title) {
        String found = response.jsonPath().get("[0].title");
        Assertions.assertEquals(title, found);
    }

    @Then("I shouldn't found any movie")
    public void shouldNotFoundAnyMovie() {
        Collection movies = response.jsonPath().get();
        Assertions.assertTrue(movies.isEmpty());
    }

    @Then("found the movie in database")
    public void searchInDatabase() throws SQLException {
        Movie found = DatabaseUtil.readMovie(movie.getTitle());
        Assertions.assertNotNull(found);
    }

    @Then("the movie was not found in database")
    public void searchInDatabase_shouldNotFound() throws SQLException {
        Movie found = DatabaseUtil.readMovie(movie.getTitle());
        Assertions.assertNull(found);
    }

    @And("The response should have status equals {int}")
    public void statusEquals(Integer status) {
        response.then().statusCode(status);
    }

    @And("the rating equals to {float}")
    public void ratingEqualsTo(Float rating) throws SQLException {
        Movie movieInDatabase = DatabaseUtil.readMovie(movie.getTitle());
        Assertions.assertNotNull(movieInDatabase);

        Assertions.assertEquals(rating, movieInDatabase.getRating());
    }

    @And("the response should have rating field equals {float}")
    public void ratingEqualsToInResponse(Float rating) {
        Float ratingUpdated = response.jsonPath().getFloat("rating");
        Assertions.assertEquals(rating, ratingUpdated);
    }

    @And("apply contract validation")
    public void applyContractValidation() throws FileNotFoundException {
        InputStream file = new FileInputStream("src/test/resources/movie-schema.json");
        response.then()
                .body(JsonSchemaValidator.matchesJsonSchema(file));
    }

    @And("apply contract validation on list")
    public void applyContractValidationOnList() throws FileNotFoundException {
        InputStream file = new FileInputStream("src/test/resources/movie-schema.json");
        response.then()
                .body(JsonSchemaValidator.matchesJsonSchema(file));
    }

    private Movie createMovieFromDataTable(DataTable data) {
        Movie movie = new Movie();
        data.asMaps().forEach(it -> {
            String title = it.get("title");
            if (title == null) {
                title = RandomStringUtils.randomAlphabetic(10);
            }
            String genre = it.get("genre");
            if (genre == null) {
                genre = RandomStringUtils.randomAlphabetic(10);
            }
            String ratingString = it.get("rating");
            Float rating = 10.0f;
            if (ratingString != null) {
                rating = Float.valueOf(ratingString);
            }
            movie.setTitle(title);
            movie.setGenre(genre);
            movie.setRating(rating);
        });
        return movie;
    }
}
