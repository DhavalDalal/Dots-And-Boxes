import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true,
        plugin = "html:target/html-report",
        features = "src/test/features/",
        tags = "~@todo")
public class RunAllCucumberSpecs {
}
