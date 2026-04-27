import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.nio.file.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MelodyInboxEdgeAutomation {

    private static final String DOWNLOAD_DIR = "C:\\Users\\isabe\\Downloads\\Songs For Car\\MelodyDownloads";
    private static final String INPUT_FILE = "songs.txt";
    private static final String LOG_FILE = "download_log.txt";

    public static void main(String[] args) {

        // Force Edge to use your custom download folder + run headless
        EdgeOptions options = new EdgeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", DOWNLOAD_DIR);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);

        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new EdgeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
             BufferedWriter log = new BufferedWriter(new FileWriter(LOG_FILE, true))) {

            String query;

            while ((query = br.readLine()) != null) {
                if (query.trim().isEmpty()) continue;

                try {
                    driver.get("https://melodyinbox.com/");

                    // Search bar
                    WebElement searchBox = wait.until(
                            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[type='search']"))
                    );
                    searchBox.clear();
                    searchBox.sendKeys(query);

                    // Search button
                    WebElement searchBtn = wait.until(
                            ExpectedConditions.elementToBeClickable(By.cssSelector("button[type='submit']"))
                    );
                    searchBtn.click();

                    // Download button
                    WebElement downloadBtn = wait.until(
                            ExpectedConditions.elementToBeClickable(By.cssSelector(".download-btn"))
                    );
                    downloadBtn.click();

                    // Convert button
                    WebElement convertBtn = wait.until(
                            ExpectedConditions.elementToBeClickable(By.cssSelector(".convert-btn"))
                    );
                    convertBtn.click();

                    // Detect download completion
                    boolean success = waitForDownloadToFinish();

                    if (success) {
                        log.write("[SUCCESS] " + query + "\n");
                    } else {
                        log.write("[FAILED] Timeout waiting for download: " + query + "\n");
                    }

                } catch (Exception e) {
                    log.write("[ERROR] " + query + " — " + e.getMessage() + "\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    // Detects when a new file appears in the download folder
    private static boolean waitForDownloadToFinish() throws InterruptedException {
        long start = System.currentTimeMillis();
        long timeout = 30000; // 30 seconds

        while (System.currentTimeMillis() - start < timeout) {
            boolean downloading = Files.exists(Paths.get(DOWNLOAD_DIR))
                    && Files.list(Paths.get(DOWNLOAD_DIR))
                    .anyMatch(path -> path.toString().endsWith(".crdownload"));

            if (!downloading) {
                return true; // No temp files → download complete
            }

            Thread.sleep(500);
        }
        return false;
    }
}
