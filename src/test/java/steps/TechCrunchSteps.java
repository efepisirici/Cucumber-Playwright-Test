package steps;

import com.microsoft.playwright.options.LoadState;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import page.TechCrunchPage;

public class TechCrunchSteps extends BasicStep {

    private TechCrunchPage techCrunchPage;

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            // Take a screenshot and attach it to the scenario
            byte[] screenshot = page.screenshot();
            scenario.attach(screenshot, "image/png", "screenshot");
        }

        // Close the page/browser if necessary
        if (page != null) {
            page.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (page != null) {
            page.close();
        }
    }


    @Given("I load the page with {string} for {string} usage")
    public void i_load_the_page_with_for_desktop(String browserType, String device) {
        if (device.equals("desktop")) {
            setup(browserType, ClientType.desktop);
        } else {
            setup(browserType, ClientType.mobile);
        }
    }

    @And("I navigate to the TechCrunch website")
    public void iNavigateToTheTechCrunchWebsite() {
        //DOMLoaded
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        this.techCrunchPage = new TechCrunchPage(page);
        techCrunchPage.navigateToTechCrunch();
        //images etc. loaded
        page.waitForLoadState(LoadState.LOAD);
    }


    @Then("each latest article should have an author")
    public void eachArticleShouldHaveAnAuthor() {
        Assert.assertTrue("Not every latest article has an author", techCrunchPage.isAuthorPresentInLatestArticles());
    }


    @Then("each featured article should have an author")
    public void eachFeaturedArticleShouldHaveAnAuthor() {
        Assert.assertTrue("Not every featured article has an author", techCrunchPage.isAuthorPresentInEachMainArticle());

    }

    @When("I click on Load More Button")
    public void iClickOnLoadMoreButton() {
        techCrunchPage.clickOnLoadMore();
    }


    @Then("main featured article should have an image")
    public void mainFeaturedArticleShouldHaveAnImage() {
        techCrunchPage.  isAuthorPresentPicturesInMainArticles();
    }

    @Then("each latest article should have an image")
    public void eachLatestArticleShouldHaveAnImage() {
        techCrunchPage.  isAuthorPresentPicturesInLatestArticles();
    }

    @When("navigate to latest article")
    public void clickOnLatestArticle() {
        techCrunchPage.clickOnAuthorPresentInLatestArticles();

    }
}
