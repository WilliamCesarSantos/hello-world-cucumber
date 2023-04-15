package br.ada.helloworldcucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.CoreMatchers;


public class MovieStepDefinition {

    private RequestSpecification request;
    private Response response;

    @Given("The movie {string} was not registered")
    public void movieWasNotRegistered(String title) {

    }

    @When("Create {string},{string},{int} movie")
    public void createNewMovie(String title, String genre, int rating) {
        request = io.restassured.RestAssured.given()
                .body("{\"title\": \"" + title + "\", \"genre\": \"" + genre + "\", \"rating\": " + rating + "}")
                .contentType(ContentType.JSON);
        response = request.when().post("http://localhost:8080/api/movies");
    }

    @Then("The movie {string} is list")
    public void listMovie(String title) {

    }

    @Then("The movie {string} did registered")
    public void movieDidRegistered(String title) {
        response.then().statusCode(201)
                .body(CoreMatchers.containsString(title));
    }

    @Then("Response has status code {int}")
    public void responseStatusCodeIs(int code) {
        response.then().statusCode(code);
    }

    @Then("The {string} did not registered")
    public void movieDidntRegistered(String title) {

    }

}
