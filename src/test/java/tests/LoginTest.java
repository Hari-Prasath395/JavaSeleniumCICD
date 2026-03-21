package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

/**
 * LoginTest: verifies login functionality against sample OrangeHRM demo
 */
public class LoginTest extends BaseTest {

    @Test(description = "Valid login should navigate to dashboard")
    public void validLoginTest() {
        String url = config.getProperty("url");
        String username = config.getProperty("username");
        String password = config.getProperty("password");

        LoginPage login = new LoginPage(driver);
        login.open(url);
        test.info("Opened URL: " + url);
        login.login(username, password);
        test.info("Performed login with user: " + username);

        boolean dashboardVisible = login.isDashboardDisplayed();
        Assert.assertTrue(dashboardVisible, "Dashboard should be visible after successful login");
        test.pass("Dashboard displayed - login successful");
    }

    @Test(description = "Invalid login shows error message")
    public void invalidLoginTest() {
        String url = config.getProperty("url");

        LoginPage login = new LoginPage(driver);
        login.open(url);
        test.info("Opened URL: " + url);
        login.login("baduser", "badpass");
        test.info("Attempted login with invalid credentials");

        String err = login.getErrorMessage();
        Assert.assertFalse(err.isEmpty(), "Error message should be displayed for invalid login");
        test.pass("Error message displayed: " + err);
    }
}
