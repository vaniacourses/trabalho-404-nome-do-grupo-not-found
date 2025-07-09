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

import java.time.Duration;

import static org.junit.Assert.*;

public class LoginSeleniumTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    private static final String BASE_URL = "http://localhost:8080";

    @Before
    public void setUp() {
        driver = WebDriverManager.chromedriver().create();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        driver.manage().timeouts().setScriptTimeout(60, java.util.concurrent.TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 600);
        driver.get(BASE_URL + "/login");
    }

    @Test
    public void testLoginSimples() {
        WebElement userField = wait.until(
            ExpectedConditions.presenceOfElementLocated(By.name("user"))
        );
        WebElement passwordField = driver.findElement(By.name("senha"));
        userField.clear();
        userField.sendKeys("gerente");
        passwordField.clear();
        passwordField.sendKeys("123");
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();
        // Espera redirecionamento para dashboard ou página pós-login
        wait.until(ExpectedConditions.not(
            ExpectedConditions.urlContains("/login")
        ));
        System.out.println("Login realizado. URL atual: " + driver.getCurrentUrl());
        assertTrue(driver.getCurrentUrl().contains("/dashboard") || !driver.getCurrentUrl().contains("/login"));
    }

    // ...outros testes removidos para deixar apenas o login simples

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
