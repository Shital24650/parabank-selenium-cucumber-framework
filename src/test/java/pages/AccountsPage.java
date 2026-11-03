package pages;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AccountsPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // The "Total" row has no link, so only real account rows match.
    private static final By ACCOUNT_LINKS =
            By.cssSelector("#accountTable tbody tr td:first-child a");

    public AccountsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    public List<String> getAccountIds() {
        // The table is filled by a background call, so wait for the first account link.
        wait.until(ExpectedConditions.presenceOfElementLocated(ACCOUNT_LINKS));
        return driver.findElements(ACCOUNT_LINKS).stream()
                .map(WebElement::getText)
                .map(String::trim)
                .collect(Collectors.toList());
    }
}
