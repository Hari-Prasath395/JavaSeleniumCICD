package base;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import utils.ExtentManager;
import utils.Utilities;

import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Properties;

/**
 * BaseTest: handles WebDriver setup/teardown and Extent reporting (moved to test sources)
 */
public class BaseTest {
    protected WebDriver driver;
    protected ExtentTest test;
    protected Properties config;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) throws Exception {
        // load configuration
        config = new Properties();
        try (FileInputStream fis = new FileInputStream("src/test/resources/config.properties")) {
            config.load(fis);
        }

        // Create extent test early so we can report setup failures
        test = ExtentManager.createTest(method.getDeclaringClass().getSimpleName() + " - " + method.getName());
        test.info("Starting test: " + method.getName());

        // Browser setup - Chrome
        try {
            // Clear any explicit webdriver.chrome.driver set earlier (e.g. by WebDriverManager cache)
            // so Selenium Manager (bundled with Selenium 4.14+) can resolve the matching driver.
            System.clearProperty("webdriver.chrome.driver");
            ChromeOptions options = new ChromeOptions();
            // uncomment if you want headless runs
            // options.addArguments("--headless=new");
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            test.info("Launched Chrome browser");
        } catch (Exception e) {
            // ensure any setup exception is recorded in the report before rethrowing
            try {
                if (test != null) {
                    test.fail("Driver setup failed: " + e.getMessage());
                    test.fail(e);
                }
            } catch (Exception ignored) {
            }
            throw e;
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            // make sure test instance exists
            if (test == null) {
                // create a fallback test to record results
                String name = "UnknownTest";
                try {
                    name = result.getMethod().getConstructorOrMethod().getName();
                } catch (Exception ignored) {
                }
                test = ExtentManager.createTest(name);
            }

            if (result.getStatus() == ITestResult.FAILURE) {
                String path = "";
                try {
                    path = Utilities.takeScreenshot(driver, result.getName());
                } catch (Exception sce) {
                    // ignore screenshot failure
                }
                test.fail(result.getThrowable());
                if (!path.isEmpty()) {
                    try {
                        test.addScreenCaptureFromPath(path);
                    } catch (Exception ignored) {
                    }
                }
            } else if (result.getStatus() == ITestResult.SKIP) {
                test.skip("Test skipped: " + result.getName());
            } else {
                test.pass("Test passed: " + result.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (driver != null) {
                try {
                    driver.quit();
                } catch (Exception ignored) {
                }
            }
            ExtentManager.flushReports();
        }
    }
}