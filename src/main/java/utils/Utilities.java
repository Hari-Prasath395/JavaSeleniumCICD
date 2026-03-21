package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilities: helper methods like screenshot capture
 */
public class Utilities {
    private static final String SCREENSHOT_DIR = "screenshots";

    public static String takeScreenshot(WebDriver driver, String name) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path dstDir = Path.of(SCREENSHOT_DIR);
            if (!Files.exists(dstDir)) {
                Files.createDirectories(dstDir);
            }
            String filename = name + "_" + timestamp + ".png";
            Path dst = dstDir.resolve(filename);
            Files.copy(src.toPath(), dst, StandardCopyOption.REPLACE_EXISTING);
            return dst.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
