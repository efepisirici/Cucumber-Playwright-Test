package main;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/desktop", 
        glue= {"steps"},
        plugin = {"pretty", "html:target/cucumber.html"},
        monochrome = true
)
public class RunCucumberTest {

}

