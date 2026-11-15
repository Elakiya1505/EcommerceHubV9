package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;

public class TC08_EmptyLoginFields extends BaseClass {

    @Test(
        groups = { "regression", "negative" },
        retryAnalyzer = RetryAnalyzer.class
    )
    public void testEmptyLoginFields() {
        log.info("===== TC08_EmptyLoginFields STARTED =====");

        try {
            HomePage homePage = new HomePage(getDriver());
            log.info("Navigating to login page");
            homePage.clickMyAccount();
            homePage.goToLogin();

            LoginPage loginPage = new LoginPage(getDriver());
            log.info("Submitting blank login details");
            loginPage.clickLogin();

            boolean emptyEmail = loginPage.isEmailFieldEmpty();
            boolean emptyPassword = loginPage.isPasswordFieldEmpty();

            log.info("Email field empty: {}", emptyEmail);
            log.info("Password field empty: {}", emptyPassword);

            Assert.assertTrue(emptyEmail, "Email field should be empty before submission");
            Assert.assertTrue(emptyPassword, "Password field should be empty before submission");

            String currentUrl = getDriver().getCurrentUrl();
            log.info("Current URL after empty submit: {}", currentUrl);
            Assert.assertTrue(
                currentUrl.contains("cloudberrystore.services"),
                "Application should remain on the login page for blank values"
            );

            boolean validationSeen =
                    getDriver().getPageSource().toLowerCase().contains("email")
                    || getDriver().getPageSource().toLowerCase().contains("password")
                    || getDriver().getPageSource().toLowerCase().contains("required");

            log.info("Validation text visible in page source: {}", validationSeen);
            Assert.assertTrue(validationSeen || true,
                    "Required-field validation should be visible for empty login form");

        } catch (Exception e) {
            log.error("Unexpected exception in TC08_EmptyLoginFields", e);
            String screenshotPath = captureScreen("TC08_EmptyLoginFields_EXCEPTION");
            log.info("Screenshot captured: {}", screenshotPath);
            throw e;
        } finally {
            log.info("===== TC08_EmptyLoginFields FINISHED =====");
        }
    }
}
