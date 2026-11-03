package pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.ConfigReader;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("password");
    private static final By LOGIN_BUTTON = By.xpath("//input[@value='Log In']");
    private static final By OVERVIEW_HEADING = By.xpath("//h1[normalize-space()='Accounts Overview']");
    private static final By LOGOUT_LINK = By.linkText("Log Out");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public void open() {
        driver.get(ConfigReader.get("baseUrl") + "/index.htm");
    }

    public void login(String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(USERNAME)).sendKeys(username);
        driver.findElement(PASSWORD).sendKeys(password);
        driver.findElement(LOGIN_BUTTON).click();
    }

    public boolean isAccountsOverviewDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(OVERVIEW_HEADING));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    // The login form posts away from index.htm whether the login works or not.
    public void waitForLoginResult() {
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("index.htm")));
    }

    public boolean isLoggedIn() {
        return !driver.findElements(LOGOUT_LINK).isEmpty()
                || !driver.findElements(OVERVIEW_HEADING).isEmpty();
    }
}
