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
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;

import java.sql.SQLException;


public class MovieStepDefinition {

    private Movie movie = null;
    private RequestSpecification request;
    private Response response;

    @Given("I have {string} movie registered")
    public void iHaveMovieRegistered(String title) throws SQLException {
        movie = new Movie();
        movie.setTitle(title);
        movie.setGenre("Ação");
        movie.setRating(9.0f);
        DatabaseUtil.insertMovie(movie);

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

    @And("The response should have status equals {int}")
    public void statusEquals(Integer status) {
        response.then().statusCode(status);
    }

}
