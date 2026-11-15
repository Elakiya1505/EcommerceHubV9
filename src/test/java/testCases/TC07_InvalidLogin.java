package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;

public class TC07_InvalidLogin extends BaseClass {

    @Test(
        groups = { "regression", "negative" },
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testInvalidLogin() {
        log.info("===== TC07_InvalidLogin STARTED =====");

        try {
            HomePage homePage = new HomePage(getDriver());
            log.info("Opening login flow from home page");
            homePage.clickMyAccount();
            homePage.goToLogin();

            LoginPage loginPage = new LoginPage(getDriver());
            log.info("Attempting invalid login with wrong credentials");
            loginPage.loginWith("invaliduser@test.com", "wrongpassword");

            boolean loginErrorVisible = loginPage.isLoginErrorDisplayed();
            log.info("Login error visible: {}", loginErrorVisible);

            String errorText = loginPage.getLoginErrorText();
            log.info("Error text captured: {}", errorText);

            Assert.assertTrue(loginErrorVisible, "Login error was not displayed for invalid credentials");
            Assert.assertTrue(
                errorText.toLowerCase().contains("warning")
                    || errorText.toLowerCase().contains("no match")
                    || errorText.toLowerCase().contains("invalid"),
                "Unexpected invalid login message: " + errorText
            );

            String currentUrl = getDriver().getCurrentUrl();
            log.info("Current URL after invalid login: {}", currentUrl);
            Assert.assertTrue(
                currentUrl.contains("cloudberrystore.services") || currentUrl.contains("route"),
                "User unexpectedly navigated away from login after invalid credentials"
            );

        } catch (Exception e) {
            log.error("Unexpected exception in TC07_InvalidLogin", e);
            String screenshotPath = captureScreen("TC07_InvalidLogin_EXCEPTION");
            log.info("Screenshot captured: {}", screenshotPath);
            throw e;
        } finally {
            log.info("===== TC07_InvalidLogin FINISHED =====");
        }
    }
}
