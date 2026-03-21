package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentManager: creates and provides ExtentReports instance
 */
public class ExtentManager {
    private static ExtentReports extent;

    private static synchronized void init() {
        if (extent == null) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportsDir = "reports";
            String fileName = "ExtentReport_" + timestamp + ".html";
            Path reportPath = Path.of(reportsDir, fileName);
            try {
                if (!reportPath.getParent().toFile().exists()) {
                    reportPath.getParent().toFile().mkdirs();
                }
            } catch (Exception ignored) {
            }
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath.toString());
            extent = new ExtentReports();
            extent.attachReporter(spark);
        }
    }

    public static ExtentTest createTest(String name) {
        init();
        return extent.createTest(name);
    }

    public static ExtentReports getExtent() {
        init();
        return extent;
    }

    public static void flushReports() {
        if (extent != null) {
            extent.flush();
        }
    }
}
