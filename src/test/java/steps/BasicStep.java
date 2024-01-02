package steps;

import com.microsoft.playwright.*;


public class BasicStep {

    public enum ClientType {
        desktop, mobile
    }

    protected Browser browser;
    private BrowserContext context;
    protected Page page;

    public void setup(String browserTypeAsString, ClientType clientType) {
        BrowserType browserType = null;
        switch (browserTypeAsString) {
            case "Firefox":
                browserType = Playwright
                        .create()
                        .firefox();
                break;
            case "Chromium":
                browserType = Playwright
                        .create()
                        .chromium();
                break;
            case "Webkit":
                browserType = Playwright
                        .create()
                        .webkit();
                break;

        }
        if (browserType == null) {
            throw new IllegalArgumentException("Could not launch a browser for type " + browserTypeAsString);
        }
        browser = browserType.launch(new BrowserType.LaunchOptions().setHeadless(false));

        if (clientType.equals(ClientType.mobile)) {
            this.page = newPage(this.context = newMobileBrowserContext());
        } else {
            this.page = newPage(this.context = newDesktopBrowserContext());
        }
        page.setDefaultNavigationTimeout(60000); // 60 seconds
        page.setDefaultTimeout(15000); // 15 seconds


    }

    protected Page newPage(BrowserContext browserContext) {
        return browserContext.newPage();
    }

    protected BrowserContext newDesktopBrowserContext() {
        return browser.newContext(new Browser.NewContextOptions().setViewportSize(800, 600));
    }
//iphone 14 pro max
    protected BrowserContext newMobileBrowserContext() {
        return browser.newContext(new Browser.NewContextOptions().setViewportSize(430, 932).setDeviceScaleFactor(2));
    }

}
