package br.ada.helloworldcucumber;

import br.ada.helloworldcucumber.model.Movie;
import br.ada.helloworldcucumber.util.DatabaseUtil;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;


public class MovieStepDefinition {

    private Movie movie = null;
    private RequestSpecification request;
    private Response response;

    @Given("I have {string} movie registered")
    public void iHaveMovieRegistered(String title) throws SQLException {
        movie = new Movie();
        movie.setTitle(title);
        movie.setGenre(RandomStringUtils.random(10));
        movie.setRating(9.0f);
        DatabaseUtil.insertMovie(movie);

        request = RestAssured.given()
                .contentType(ContentType.JSON);
    }

    @Given("that I don't have the {string} movie registered")
    public void iDontHaveMovieRegistered(String title) throws SQLException {
        movie = new Movie();
        movie.setTitle(title);

        Movie found = DatabaseUtil.readMovie(title);
        Assertions.assertNull(found);

        request = RestAssured.given()
                .contentType(ContentType.JSON);
    }

    @When("I search the movie by title")
    public void searchTheMovie() {
        response = request.when().get("http://localhost:8080/api/movies?title=" + movie.getTitle());
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

    @And("The response should have status equals {int}")
    public void statusEquals(Integer status) {
        response.then().statusCode(status);
    }

}
