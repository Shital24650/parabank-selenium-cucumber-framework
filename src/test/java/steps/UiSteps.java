package steps;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AccountsPage;
import pages.LoginPage;
import utils.ConfigReader;

public class UiSteps {

    private final TestContext ctx;

    public UiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("I open the ParaBank home page")
    public void openHome() {
        new LoginPage(ctx.driver).open();
    }

    @When("I log in with the default test user")
    public void loginDefault() {
        new LoginPage(ctx.driver).login(ConfigReader.get("username"), ConfigReader.get("password"));
    }

    @When("I log in with username {string} and password {string}")
    public void loginWith(String username, String password) {
        new LoginPage(ctx.driver).login(username, password);
    }

    @Then("I should see the accounts overview")
    public void seeOverview() {
        assertTrue(new LoginPage(ctx.driver).isAccountsOverviewDisplayed(),
                "Accounts Overview page was not displayed after login");
    }

    @Then("I should not be logged in")
    public void notLoggedIn() {
        LoginPage page = new LoginPage(ctx.driver);
        page.waitForLoginResult();
        assertFalse(page.isLoggedIn(), "User appears logged in with invalid credentials");
    }

    @When("I read the account ids shown in the UI")
    public void readUiAccounts() {
        ctx.uiAccountIds = new AccountsPage(ctx.driver).getAccountIds();
    }
}
