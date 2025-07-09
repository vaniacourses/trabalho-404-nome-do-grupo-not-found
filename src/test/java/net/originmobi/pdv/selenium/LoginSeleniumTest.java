package net.originmobi.pdv.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.originmobi.pdv.PdvApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PdvApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LoginSeleniumTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    
    @LocalServerPort
    private int port;
    
    private String getBaseUrl() {
        return "http://localhost:" + port;
    }

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
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, 60);
        
        // Aguarda um pouco para garantir que a aplicação esteja totalmente inicializada
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        driver.get(getBaseUrl() + "/login");
    }

    @Test
    public void testLoginSimples() {
        try {
            // Aguarda até que a página de login esteja disponível
            wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.name("user")),
                ExpectedConditions.presenceOfElementLocated(By.id("user")),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[name='user']"))
            ));

            WebElement userField = driver.findElement(By.name("user"));
            WebElement passwordField = driver.findElement(By.name("senha"));
            
            userField.clear();
            userField.sendKeys("gerente");
            passwordField.clear();
            passwordField.sendKeys("123");
            
            WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
            loginButton.click();
            
            // Aguarda o redirecionamento após login bem-sucedido
            wait.until(ExpectedConditions.not(
                ExpectedConditions.urlContains("/login")
            ));
            
            System.out.println("Login realizado. URL atual: " + driver.getCurrentUrl());
            assertTrue("Deveria ter redirecionado da página de login", 
                      driver.getCurrentUrl().contains("/dashboard") || !driver.getCurrentUrl().contains("/login"));
                      
        } catch (Exception e) {
            System.err.println("Erro durante o teste de login: " + e.getMessage());
            System.err.println("URL atual: " + driver.getCurrentUrl());
            System.err.println("Título da página: " + driver.getTitle());
            System.err.println("Source da página: " + driver.getPageSource().substring(0, Math.min(500, driver.getPageSource().length())));
            throw e;
        }
    }

    @Test
    public void testAplicacaoEstaAcessivel() {
        // Testa se a aplicação está acessível
        driver.get(getBaseUrl());
        
        // Aguarda até que a página carregue (pode ser redirecionada para login)
        wait.until(ExpectedConditions.or(
            ExpectedConditions.urlContains("/login"),
            ExpectedConditions.urlContains("/dashboard"),
            ExpectedConditions.presenceOfElementLocated(By.tagName("body"))
        ));
        
        String currentUrl = driver.getCurrentUrl();
        System.out.println("Aplicação acessível em: " + currentUrl);
        System.out.println("Título da página: " + driver.getTitle());
        
        // Verifica se não há erros de conexão
        assertFalse("Não deveria ter erro de conexão", 
                   driver.getPageSource().contains("ERR_CONNECTION_REFUSED"));
        assertFalse("Não deveria ter erro de conexão", 
                   driver.getTitle().contains("not available"));
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
