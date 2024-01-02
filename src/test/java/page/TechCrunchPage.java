package page;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TechCrunchPage {

    private Page page;

    public TechCrunchPage(Page page) {
        this.page = page;
    }

    public void navigateToTechCrunch() {
        page.navigate("https://techcrunch.com/");
    }

    public int getLocatorCount(String locator) {
        return page.querySelectorAll(locator).size();
    }

    public int getMainArticleCount() {
        assertEquals(getLocatorCount(".fi-main-block__title"), 1);
        String locator = ".mini-view .mini-view__item";
        return getLocatorCount(locator);
    }

    public boolean isAuthorPresentInEachMainArticle() {
        String locator = ".fi-main-block__byline";
        assertBylineDetails(locator);
        return getLocatorCount(locator) == getMainArticleCount() + 1;
    }

    public int isLatestArticlePresented() {
        return getLocatorCount(".post-block__header");
    }

    public boolean isAuthorPresentInLatestArticles() {
        String locator = ".river-byline__authors";
        assertBylineDetails(locator);
        return getLocatorCount(locator) == isLatestArticlePresented();
    }

    public void assertBylineDetails(String locator) {
        List<ElementHandle> bylineElements = page.locator(locator).elementHandles();
        for (ElementHandle byline : bylineElements) {
            List<ElementHandle> spanElements = byline.querySelectorAll("span");
            for (ElementHandle span : spanElements) {
                ElementHandle link = span.querySelector("a");
                String href = link.getAttribute("href");
                String text = link.innerText();
                Assert.assertNotNull("Href attribute is missing", href);
                Assert.assertFalse("Href attribute is empty", href.isEmpty());
                Assert.assertFalse("Author name is empty", text.isEmpty());
            }
        }
    }

    public void clickOnLoadMore() {
        page.click(".load-more");
    }

    public void assertImagesDetails(String locator) {
        List<ElementHandle> footerElements = page.locator(locator).elementHandles();
        for (ElementHandle footer : footerElements) {
            // Check if the figure element exists within the footer
            ElementHandle figure = footer.querySelector("figure.post-block__media");
            Assert.assertNotNull("Figure element is missing in the footer", figure);

            // Check if the img element exists within the figure
            ElementHandle img = figure.querySelector("img");
            Assert.assertNotNull("Image element is missing in the figure", img);

            // Assert that the src attribute of the img element is not empty
            String src = img.getAttribute("src");
            Assert.assertNotNull("Src attribute is missing in the image", src);
            Assert.assertFalse("Src attribute is empty in the image", src.isEmpty());

            // Additional checks can be added here, e.g., alt text, srcset, etc.
        }
    }


    public boolean isAuthorPresentPicturesInLatestArticles() {
        String locator = "footer.post-block__footer";
        assertImagesDetails(locator);
        return getLocatorCount(locator) == isLatestArticlePresented();
    }

    public boolean isAuthorPresentPicturesInMainArticles() {
        String locator = ".feature-island-main-block .post-block__title__link :nth-of-type(2)";
        assertImagesDetails(locator);
        return getLocatorCount(locator) == 1;
    }

    public void clickOnAuthorPresentInLatestArticles() {
        String locator = ".post-block--unread :nth-of-type(1)";
        String latestArticle = latestArticleInfo();
        page.click(locator);
        assertArticleDetails(latestArticle);
    }

    public String latestArticleInfo() {
        JSONObject headerDetails = new JSONObject();
        try {
            // Fetch primary category link details
            ElementHandle primaryCategoryLink = page.querySelector("div.article__primary-category a.article__primary-category__link");
            if (primaryCategoryLink != null) {
                headerDetails.put("primaryCategoryLink", primaryCategoryLink.getAttribute("href"));
                headerDetails.put("primaryCategoryText", primaryCategoryLink.innerText());
            }
            // Fetch and add post title details
            ElementHandle titleLink = page.querySelector("h2.post-block__title a.post-block__title__link");
            if (titleLink != null) {
                headerDetails.put("titleLink", titleLink.getAttribute("href"));
                headerDetails.put("titleText", titleLink.innerText());
            }
            // Fetch and add author details
            ElementHandle author = page.querySelector("span.river-byline__authors a");
            if (author != null) {
                headerDetails.put("authorLink", author.getAttribute("href"));
                headerDetails.put("authorName", author.innerText());
            }
            // Fetch and add date-time details
            ElementHandle dateTime = page.querySelector("time.river-byline__full-date-time");
            if (dateTime != null) {
                headerDetails.put("dateTime", dateTime.getAttribute("datetime"));
                headerDetails.put("displayedDateTime", dateTime.innerText());
            }
        } catch (Exception ignored) {
        }
        return headerDetails.toString();
    }
    public void assertArticleDetails(String headerJson) {
        JSONObject headerDetails = null;
        try {
            headerDetails = new JSONObject(headerJson);

        // Assert the article title
        String expectedTitle = headerDetails.getString("titleText");
        String actualTitle = page.querySelector("h1.article__title").textContent();
        Assert.assertEquals("Article title does not match", expectedTitle, actualTitle);

        // Assert the author name
        String expectedAuthor = headerDetails.getString("authorName");
        String actualAuthor = page.querySelector("div.article__byline a").innerText();
        Assert.assertEquals("Article author does not match", expectedAuthor, actualAuthor);

        // Assert the date-time
        String expectedDateTime = headerDetails.getString("dateTime");
        String actualDateTime = page.querySelector("time.full-date-time").getAttribute("datetime");
        Assert.assertEquals("Article date-time does not match", expectedDateTime, actualDateTime);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
