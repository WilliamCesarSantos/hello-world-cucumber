package br.ada.helloworldcucumber;

import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MovieStepDefinition {

    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
    }

    @AfterAll
    public static void destroy() {
        driver.quit();
    }

    @Given("The movie {string} was not registered")
    public void movieWasNotRegistered(String title) {
        driver.get("http://localhost:8080/app/movies");
        assertThrows(NoSuchElementException.class, () ->
                driver.findElement(
                        By.xpath("//td[text()='" + title + "']")
                ));
    }

    @When("Create {string},{string},{int} movie")
    public void createNewMovie(String title, String genre, int rating) {

    }

    @Then("The movie {string} is list")
    public void listMovie(String title) {

    }

}
