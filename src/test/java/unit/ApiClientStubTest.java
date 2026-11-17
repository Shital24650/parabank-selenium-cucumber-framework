package unit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.testng.Assert.assertEquals;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.response.Response;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.ApiClient;

/** Tests ApiClient against a stubbed ParaBank, so it needs no network and no shared public site. */
public class ApiClientStubTest {

    private WireMockServer server;

    @BeforeClass
    public void start() {
        server = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        server.start();
        System.setProperty("baseUrl", "http://localhost:" + server.port());
    }

    @AfterClass(alwaysRun = true)
    public void stop() {
        System.clearProperty("baseUrl");
        server.stop();
    }

    @Test
    public void loginSendsTheExpectedRequestAndReturnsTheCustomer() {
        server.stubFor(get(urlEqualTo("/services/bank/login/john/demo"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"id\":12212,\"firstName\":\"John\"}")));

        Response response = ApiClient.login("john", "demo");

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getInt("id"), 12212);
        server.verify(getRequestedFor(urlEqualTo("/services/bank/login/john/demo"))
                .withHeader("Accept", equalTo("application/json")));
    }

    @Test
    public void invalidLoginReturns400() {
        server.stubFor(get(urlEqualTo("/services/bank/login/bad/bad"))
                .willReturn(aResponse().withStatus(400).withBody("Invalid username and/or password")));

        assertEquals(ApiClient.login("bad", "bad").statusCode(), 400);
    }

    @Test
    public void accountsReturnsTheCustomersAccounts() {
        server.stubFor(get(urlEqualTo("/services/bank/customers/12212/accounts"))
                .willReturn(aResponse().withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("[{\"id\":13344},{\"id\":13455}]")));

        Response response = ApiClient.accounts(12212);

        assertEquals(response.statusCode(), 200);
        assertEquals(response.jsonPath().getList("id").size(), 2);
    }

    @Test
    public void serverErrorIsSurfacedAsAStatusCode() {
        server.stubFor(get(urlEqualTo("/services/bank/customers/500/accounts"))
                .willReturn(aResponse().withStatus(500)));

        assertEquals(ApiClient.accounts(500).statusCode(), 500);
    }
}
