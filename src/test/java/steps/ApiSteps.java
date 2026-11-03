package steps;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import utils.ApiClient;
import utils.ConfigReader;

public class ApiSteps {

    private final TestContext ctx;

    public ApiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @When("I log in through the API with the default test user")
    public void apiLogin() {
        ctx.apiResponse = ApiClient.login(ConfigReader.get("username"), ConfigReader.get("password"));
        if (ctx.apiResponse.statusCode() == 200) {
            ctx.customerId = ctx.apiResponse.jsonPath().getInt("id");
        }
    }

    @Then("the API response status should be {int}")
    public void statusIs(int expected) {
        assertEquals(ctx.apiResponse.statusCode(), expected,
                "Unexpected status. Response body: " + ctx.apiResponse.asString());
    }

    @Then("the API should return a customer id")
    public void hasCustomerId() {
        assertTrue(ctx.customerId > 0, "No customer id in the login response");
    }

    @When("I fetch the accounts through the API")
    public void fetchAccounts() {
        ctx.apiResponse = ApiClient.accounts(ctx.customerId);
        assertEquals(ctx.apiResponse.statusCode(), 200,
                "Accounts call failed. Customer id " + ctx.customerId
                        + ". Response body: " + ctx.apiResponse.asString());
        List<Integer> ids = ctx.apiResponse.jsonPath().getList("id");
        ctx.apiAccountIds = ids.stream().map(String::valueOf).collect(Collectors.toList());
    }

    @Then("the UI account ids should match the API account ids")
    public void uiMatchesApi() {
        assertFalse(ctx.apiAccountIds.isEmpty(), "API returned no accounts");
        assertEquals(new HashSet<>(ctx.uiAccountIds), new HashSet<>(ctx.apiAccountIds),
                "Accounts shown in the UI differ from the API");
    }
}
