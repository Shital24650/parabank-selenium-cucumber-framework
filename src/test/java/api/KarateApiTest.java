package api;

import com.intuit.karate.Results;
import com.intuit.karate.Runner;
import org.testng.Assert;
import org.testng.annotations.Test;

public class KarateApiTest {

    @Test
    public void runKarateFeatures() {
        Results results = Runner.path("classpath:karate").parallel(1);
        Assert.assertEquals(results.getFailCount(), 0, results.getErrorMessages());
    }
}
