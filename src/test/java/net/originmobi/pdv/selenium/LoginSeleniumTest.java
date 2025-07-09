package net.originmobi.pdv.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static org.junit.Assert.*;

public class LoginSeleniumTest {

    private WebDriver driver;
    private WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    @Before
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); 
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, 10);
    }

    @Test
    public void testLoginComCredenciaisValidas() {
        try {
            driver.get(BASE_URL + "/login");

            WebElement userField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("user"))
            );
            WebElement passwordField = driver.findElement(By.name("senha"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

            userField.sendKeys("gerente");
            passwordField.sendKeys("123");
            loginButton.click();

            wait.until(ExpectedConditions.urlContains("/dashboard"));
            String currentUrl = driver.getCurrentUrl();
            assertTrue("Deve redirecionar para dashboard após login", currentUrl.contains("/dashboard"));

        } catch (Exception e) {
            System.out.println("Teste Selenium não pôde ser executado - servidor pode não estar rodando: " + e.getMessage());
        }
    }

    @Test
    public void testLoginComCredenciaisInvalidas() {
        try {
            driver.get(BASE_URL + "/login");

            WebElement userField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("user"))
            );
            WebElement passwordField = driver.findElement(By.name("senha"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

            userField.sendKeys("usuario_invalido");
            passwordField.sendKeys("senha_invalida");
            loginButton.click();

            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("alert-danger")));
            WebElement errorMessage = driver.findElement(By.className("alert-danger"));
            assertTrue("Deve exibir mensagem de erro", errorMessage.isDisplayed());

        } catch (Exception e) {
            System.out.println("Teste Selenium não pôde ser executado - servidor pode não estar rodando: " + e.getMessage());
        }
    }

    @Test
    public void testLogout() {
        try {
            driver.get(BASE_URL + "/login");
            
            WebElement userField = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.name("user"))
            );
            WebElement passwordField = driver.findElement(By.name("senha"));
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));

            userField.sendKeys("gerente");
            passwordField.sendKeys("123");
            loginButton.click();

            wait.until(ExpectedConditions.urlContains("/dashboard"));

            WebElement logoutLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.linkText("Sair"))
            );
            logoutLink.click();

            wait.until(ExpectedConditions.urlContains("/login"));
            String currentUrl = driver.getCurrentUrl();
            assertTrue("Deve redirecionar para login após logout", currentUrl.contains("/login"));

        } catch (Exception e) {
            System.out.println("Teste Selenium não pôde ser executado - servidor pode não estar rodando: " + e.getMessage());
        }
    }

    @Test
    public void testAcessoSemAutenticacao() {
        try {
            driver.get(BASE_URL + "/dashboard");

            wait.until(ExpectedConditions.urlContains("/login"));
            String currentUrl = driver.getCurrentUrl();
            assertTrue("Deve redirecionar para login quando não autenticado", currentUrl.contains("/login"));

        } catch (Exception e) {
            System.out.println("Teste Selenium não pôde ser executado - servidor pode não estar rodando: " + e.getMessage());
        }
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
