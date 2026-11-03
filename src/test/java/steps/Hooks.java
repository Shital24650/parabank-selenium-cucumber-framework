package steps;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.DriverFactory;

public class Hooks {

    private final TestContext ctx;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    // API-only scenarios (@api) do not start a browser.
    @Before("not @api")
    public void startBrowser() {
        ctx.driver = DriverFactory.init();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (ctx.driver != null) {
            if (scenario.isFailed()) {
                byte[] shot = ((TakesScreenshot) ctx.driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(shot, "image/png", "failure screenshot");
            }
            DriverFactory.quit();
        }
    }
}
