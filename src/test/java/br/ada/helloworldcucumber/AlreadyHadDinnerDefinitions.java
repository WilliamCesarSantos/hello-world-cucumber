package br.ada.helloworldcucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class AlreadyHadDinnerDefinitions {

    private String dessert;

    @Given("After eat my dinner")
    public void afterDinner() {

    }

    @When("I find my dessert")
    public void findMyDessert() {
    }

    @And("I found {string}")
    public void foundDessert(String dessert) {
        this.dessert = dessert;
        System.out.println("I will eat " + dessert);
    }

    @Then("I aet {string}")
    public void iAetIceCream(String dessert) {
        Assertions.assertEquals(dessert, this.dessert);
    }

}
