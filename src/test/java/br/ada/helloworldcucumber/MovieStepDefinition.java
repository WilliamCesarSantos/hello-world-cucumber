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
        driver = new ChromeDriver(
                new ChromeOptions().addArguments("--remote-allow-origins=*")
        );
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
        driver.get("http://localhost:8080/app/movies/create");

        driver.findElement(By.id("title")).sendKeys(title);
        driver.findElement(By.id("genre")).sendKeys(genre);
        driver.findElement(By.id("rating")).sendKeys(String.valueOf(rating));
        driver.findElement(By.xpath("//form/button")).click();
    }

    @Then("The movie {string} is list")
    public void listMovie(String title) {
        driver.get("http://localhost:8080/app/movies");
        WebElement element = driver.findElement(
                By.xpath("//td[text()='" + title + "']")
        );
        Assertions.assertNotNull(element);
    }

    @Then("The {string} did not registered")
    public void movieDidntRegistered(String title) {
        driver.get("http://localhost:8080/app/movies");
        assertThrows(NoSuchElementException.class, () ->
                driver.findElement(
                        By.xpath("//td[text()='" + title + "']")
                ));
    }

}
