package steps;

import io.restassured.response.Response;
import java.util.List;
import org.openqa.selenium.WebDriver;

// One new instance per scenario, shared by all step classes (PicoContainer).
public class TestContext {
    public WebDriver driver;
    public Response apiResponse;
    public int customerId;
    public List<String> apiAccountIds;
    public List<String> uiAccountIds;
}
