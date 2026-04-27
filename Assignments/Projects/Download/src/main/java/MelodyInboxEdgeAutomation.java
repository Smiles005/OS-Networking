import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class MelodyInboxEdgeAutomation {

    private static final String DOWNLOAD_DIR = "C:\\Users\\isabe\\Downloads\\Songs For Car\\MelodyDownloads";
    private static final String INPUT_FILE = "src\\main\\resources\\Songs.txt";
    private static final String LOG_FILE = "download_log.txt";

    public static void main(String[] args) {
        // Ensure download directory exists
        try {
            Files.createDirectories(Paths.get(DOWNLOAD_DIR));
        } catch (IOException e) {
            System.err.println("Failed to create download directory: " + e.getMessage());
            return;
        }

        // EdgeOptions setup (Chromium prefs are honored by Edge)
        System.setProperty("webdriver.edge.driver", "C:\\Users\\isabe\\AppData\\Local\\CodeStuff\\msedgedriver.exe");
        EdgeOptions options = new EdgeOptions();

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", DOWNLOAD_DIR);
        prefs.put("download.prompt_for_download", false);
        prefs.put("download.directory_upgrade", true);

        options.setExperimentalOption("prefs", prefs);
        // options.addArguments("--headless=new");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");

        WebDriver driver = new EdgeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
                BufferedWriter log = new BufferedWriter(new FileWriter(LOG_FILE, true))) {

            String query;
            while ((query = br.readLine()) != null) {
                query = query.trim();
                if (query.isEmpty())
                    continue;

                try {
                    driver.get("https://melodyinbox.com/");

                    System.out.println("Page title: " + driver.getTitle());
                    String pageSrc = driver.getPageSource();
                    if (!pageSrc.contains("input") || !pageSrc.contains("search")) {
                        System.out.println(
                                "Page source snippet: " + pageSrc.substring(0, Math.min(1000, pageSrc.length())));
                    }

                    // Try to close common cookie/privacy modals that block interaction
                    String[] cookieSelectors = new String[] {
                            "button[id*='cookie']",
                            "button[id*='accept']",
                            "button[class*='cookie']",
                            "button[class*='accept']",
                            "button[aria-label*='accept']",
                            "button:contains('Accept')",
                            "button:contains('I agree')"
                    };
                    for (String cs : cookieSelectors) {
                        try {
                            for (WebElement btn : driver.findElements(By.cssSelector(cs))) {
                                if (btn.isDisplayed()) {
                                    try {
                                        btn.click();
                                        Thread.sleep(500);
                                    } catch (Exception ignore) {
                                    }
                                }
                            }
                        } catch (Exception ignore) {
                        }
                    }

                    // Search bar: try a list of fallback selectors in case the primary one changes
                    WebElement searchBox = null;
                    String[] selectors = new String[] {
                            "input[type='search']",
                            "input[name='s']",
                            "input[name='q']",
                            "input[placeholder*='Search']",
                            "input#search",
                            ".search input",
                            "input[type='text']"
                    };

                    for (String sel : selectors) {
                        try {
                            searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(sel)));
                            System.out.println("Found search box with selector: " + sel);
                            break;
                        } catch (Exception e) {
                            // try next selector
                        }
                    }
                    if (searchBox == null) {
                        System.out.println("Failed to find search box on page: " + driver.getCurrentUrl());
                        System.out.println("Page title: " + driver.getTitle());
                        System.out.println(pageSrc.substring(0, Math.min(2000, pageSrc.length())));
                        // attempt a screenshot to help debugging
                        try {
                            byte[] img = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                            java.nio.file.Path out = Paths.get("failed_search_" + System.currentTimeMillis() + ".png");
                            Files.write(out, img);
                            System.out.println("Wrote screenshot: " + out.toAbsolutePath());
                        } catch (Exception se) {
                            System.out.println("Could not write screenshot: " + se.getMessage());
                        }
                        throw new RuntimeException("Search box not found using fallback selectors");
                    }
                    searchBox.clear();
                    searchBox.sendKeys(query);
                    // Try to click the first autocomplete suggestion using JS (more reliable than
                    // keyboard)
                    try {
                        Thread.sleep(300);
                        String js = "const selectors = ['[role=listbox] [role=option]','ul[role=listbox] li','div[role=listbox] div','.autocomplete-items div','.autocomplete li','.suggestions li','.suggestion','.downshift-item','.listbox li','#query + div li','#query + ul li'];"
                                + "for (const s of selectors){const el=document.querySelector(s); if(el && el.offsetParent!==null){el.click(); return true;}} return false;";
                        Object res = ((JavascriptExecutor) driver).executeScript(js);
                        boolean clickedSuggestion = res instanceof Boolean && (Boolean) res;
                        if (!clickedSuggestion) {
                            searchBox.sendKeys(Keys.ENTER);
                        }
                    } catch (Exception ignore) {
                        try {
                            searchBox.sendKeys(Keys.ENTER);
                        } catch (Exception ignore2) {
                        }
                    }

                    // Wait briefly for results to load and try multiple result selectors
                    String[] resultSelectors = new String[] {
                            ".result",
                            ".song",
                            ".track",
                            ".download-row",
                            "a[href*='download']",
                            "a[href*='mp3']",
                            "button[class*='download']",
                            ".download-btn",
                            "#results",
                            ".search-results"
                    };

                    boolean clickedDownload = false;
                    for (String rs : resultSelectors) {
                        try {
                            java.util.List<WebElement> els = wait.until(
                                    ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(rs)));
                            System.out.println("Found " + els.size() + " elements for selector: " + rs);
                            for (WebElement el : els) {
                                try {
                                    if (el.isDisplayed() && el.isEnabled()) {
                                        // prefer anchor or button
                                        String tag = el.getTagName();
                                        System.out.println(
                                                "Clicking element tag=" + tag + " text='" + el.getText() + "'");
                                        java.util.Set<String> before = driver.getWindowHandles();
                                        el.click();
                                        Thread.sleep(1000);
                                        java.util.Set<String> after = driver.getWindowHandles();
                                        if (after.size() > before.size()) {
                                            // switch to new window
                                            after.removeAll(before);
                                            String newWin = after.iterator().next();
                                            driver.switchTo().window(newWin);
                                            System.out.println("Switched to new window: " + driver.getCurrentUrl());
                                        }
                                        clickedDownload = true;
                                        break;
                                    }
                                } catch (Exception clickEx) {
                                    // try next element
                                }
                            }
                            if (clickedDownload)
                                break;
                        } catch (Exception e) {
                            // try next selector
                        }
                    }

                    if (!clickedDownload) {
                        System.out.println("No download/result element clicked for query: " + query);
                        try {
                            byte[] img = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                            java.nio.file.Path out = Paths.get("no_download_" + System.currentTimeMillis() + ".png");
                            Files.write(out, img);
                            System.out.println("Wrote screenshot: " + out.toAbsolutePath());
                        } catch (Exception se) {
                            System.out.println("Could not write screenshot: " + se.getMessage());
                        }
                        // also dump links that might point to direct mp3s
                        try {
                            java.util.List<WebElement> links = driver.findElements(By.cssSelector("a[href]"));
                            for (WebElement a : links) {
                                String href = a.getAttribute("href");
                                if (href != null
                                        && (href.contains(".mp3") || href.toLowerCase().contains("download"))) {
                                    System.out.println("Potential link: " + href);
                                }
                            }
                        } catch (Exception ignore) {
                        }
                    }

                    // If we clicked a result, try to find and click the site's download
                    // anchor/button
                    if (clickedDownload) {
                        boolean didDownloadClick = false;
                        // look first within the results container for download anchors
                        String anchorScope = "#results a.download, #results a[data-down], #results a[href$='.mp3'], #results a[href*='download']";
                        java.util.List<WebElement> anchors = driver.findElements(By.cssSelector(
                                anchorScope + ", a.download, a[data-down], a[href$='.mp3'], a[href*='download']"));
                        System.out.println("Found " + anchors.size() + " potential anchors");
                        for (WebElement a : anchors) {
                            try {
                                /*
                                 * if (!a.isDisplayed() || !a.isEnabled())
                                 * continue;
                                 * String text = a.getText() != null ? a.getText().trim() : "";
                                 * if (text.equalsIgnoreCase("Close")) {
                                 * System.out.println("Skipping anchor with text Close");
                                 * continue;
                                 * }
                                 * String dataDown = a.getAttribute("data-down");
                                 * String href = a.getAttribute("href");
                                 * if (text.toLowerCase().contains("download") || dataDown != null || (href !=
                                 * null
                                 * && (href.endsWith(".mp3") || href.toLowerCase().contains("download")))) {
                                 * System.out.println("Clicking download anchor text='" + text + "' data-down='"
                                 * + dataDown + "' href='" + href + "'");
                                 * try {
                                 * if (href != null
                                 * && (href.endsWith(".mp3") || href.toLowerCase().contains("download"))) {
                                 * driver.get(href);
                                 * } else {
                                 * a.click();
                                 * }
                                 * } catch (Exception clickEx) {
                                 * try {
                                 * a.click();
                                 * } catch (Exception ignore) {
                                 * }
                                 * }
                                 * didDownloadClick = true;
                                 * Thread.sleep(1000);
                                 * break;
                                 * } else {
                                 * System.out.println("Anchor skipped (no download-like attributes): text='" +
                                 * text
                                 * + "' href='" + href + "'");
                                 * }
                                 */
                                // Replace the Convert button section (starting around line 245) with this:
                                if (didDownloadClick) {
                                    // Wait for the download interface to fully load
                                    Thread.sleep(2000);

                                    // Try multiple approaches to find and click Convert button
                                    boolean convertClicked = false;

                                    // Approach 1: Look for Convert buttons by class and text
                                    try {
                                        List<WebElement> convertButtons = driver
                                                .findElements(By.xpath("//button[contains(text(),'Convert')]"));
                                        System.out.println("Found " + convertButtons.size() + " Convert buttons");

                                        for (WebElement btn : convertButtons) {
                                            if (btn.isDisplayed() && btn.isEnabled()) {
                                                // Scroll into view first
                                                ((JavascriptExecutor) driver).executeScript(
                                                        "arguments[0].scrollIntoView({block: 'center'});", btn);
                                                Thread.sleep(500);

                                                try {
                                                    btn.click();
                                                    convertClicked = true;
                                                    System.out.println("Successfully clicked Convert button");
                                                    break;
                                                } catch (Exception e) {
                                                    // Try JavaScript click as fallback
                                                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                                            btn);
                                                    convertClicked = true;
                                                    System.out.println("Successfully clicked Convert button via JS");
                                                    break;
                                                }
                                            }
                                        }
                                    } catch (Exception e) {
                                        System.out.println("Approach 1 failed: " + e.getMessage());
                                    }

                                    // Approach 2: Try CSS selector for blue Convert buttons
                                    if (!convertClicked) {
                                        try {
                                            List<WebElement> blueButtons = driver.findElements(By.cssSelector(
                                                    "button[style*='background'], button[class*='btn'], button"));
                                            for (WebElement btn : blueButtons) {
                                                String text = btn.getText();
                                                if (text != null && text.trim().equalsIgnoreCase("Convert")) {
                                                    ((JavascriptExecutor) driver).executeScript(
                                                            "arguments[0].scrollIntoView({block: 'center'});", btn);
                                                    Thread.sleep(500);
                                                    ((JavascriptExecutor) driver).executeScript("arguments[0].click();",
                                                            btn);
                                                    convertClicked = true;
                                                    System.out.println("Clicked Convert button via CSS selector");
                                                    break;
                                                }
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Approach 2 failed: " + e.getMessage());
                                        }
                                    }

                                    // Approach 3: Enhanced JavaScript approach
                                    if (!convertClicked) {
                                        try {
                                            String jsClickConvert = """
                                                    (function(){
                                                        // Try multiple selectors
                                                        const selectors = [
                                                            'button:contains("Convert")',
                                                            'button',
                                                            '[role="button"]',
                                                            '.convert-btn',
                                                            'input[type="button"]'
                                                        ];

                                                        // First try: Find all buttons and check text
                                                        const allButtons = document.querySelectorAll('button');
                                                        for(const btn of allButtons) {
                                                            const text = btn.innerText || btn.textContent || '';
                                                            if(text.trim().toLowerCase() === 'convert') {
                                                                btn.scrollIntoView({block: 'center'});
                                                                btn.click();
                                                                console.log('Clicked button with text: ' + text);
                                                                return true;
                                                            }
                                                        }

                                                        // Second try: Click first visible Convert button in the table
                                                        const rows = document.querySelectorAll('tr');
                                                        for(const row of rows) {
                                                            const btn = row.querySelector('button');
                                                            if(btn && btn.textContent && btn.textContent.includes('Convert')) {
                                                                btn.click();
                                                                return true;
                                                            }
                                                        }

                                                        return false;
                                                    })();
                                                    """;

                                            Boolean result = (Boolean) ((JavascriptExecutor) driver)
                                                    .executeScript(jsClickConvert);
                                            if (Boolean.TRUE.equals(result)) {
                                                convertClicked = true;
                                                System.out.println("Successfully clicked Convert via enhanced JS");
                                            }
                                        } catch (Exception e) {
                                            System.out.println("Approach 3 failed: " + e.getMessage());
                                        }
                                    }

                                    if (convertClicked) {
                                        // Wait for download to start
                                        Thread.sleep(3000);

                                        // Sometimes need to handle a second download button or link
                                        try {
                                            WebElement downloadLink = wait.withTimeout(Duration.ofSeconds(5))
                                                    .until(ExpectedConditions.elementToBeClickable(
                                                            By.xpath("//a[contains(@href, '.mp3')]")));
                                            downloadLink.click();
                                            System.out.println("Clicked final download link");
                                        } catch (Exception e) {
                                            // Download may have started automatically
                                            System.out.println("No additional download link needed or found");
                                        }
                                    } else {
                                        System.out.println("Failed to click any Convert button");
                                        // Take screenshot for debugging
                                        byte[] img = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                                        Path out = Paths.get(
                                                "convert_button_not_clicked_" + System.currentTimeMillis() + ".png");
                                        Files.write(out, img);
                                        System.out.println("Screenshot saved: " + out.toAbsolutePath());
                                    }
                                }
                                // end of Convert button section replacement

                            } catch (Exception ex) {
                                // continue to next anchor
                            }
                        }

                        // Wait for the Convert button or the "Your download is ready" area
                        if (didDownloadClick) {
                            try {
                                // wait for ready text or convert button up to 15s
                                try {
                                    wait.withTimeout(Duration.ofSeconds(15)).until(ExpectedConditions.or(
                                            ExpectedConditions.textToBePresentInElementLocated(
                                                    By.cssSelector("#results"), "Your download is ready"),
                                            ExpectedConditions.elementToBeClickable(
                                                    By.xpath("//button[normalize-space()='Convert']"))));
                                } finally {
                                    wait.withTimeout(Duration.ofSeconds(20));
                                }
                            } catch (Exception ignore) {
                            }

                            WebElement convert = null;
                            try {
                                convert = wait.until(ExpectedConditions
                                        .elementToBeClickable(By.xpath("//button[normalize-space()='Convert']")));
                            } catch (Exception ex) {
                                try {
                                    convert = wait.until(ExpectedConditions.elementToBeClickable(
                                            By.cssSelector("button[class*='convert'], button[class*='Convert']")));
                                } catch (Exception ignore) {
                                }
                            }
                            if (convert != null) {
                                System.out.println("Clicking Convert button (webdriver)");
                                try {
                                    convert.click();
                                } catch (Exception ignore) {
                                    // fall through to JS click fallback
                                }
                                Thread.sleep(1000);
                            } else {
                                System.out.println("No Convert button found via WebDriver; trying JS fallback");
                                try {
                                    String jsClickConvert = "(function(){const buttons=document.querySelectorAll('button'); for(const b of buttons){ if(b.innerText && b.innerText.trim().toLowerCase().startsWith('convert')){ try{ b.scrollIntoView({block:'center'}); b.click(); return true;}catch(e){} } } return false; })();";
                                    Object clicked = ((JavascriptExecutor) driver).executeScript(jsClickConvert);
                                    if (clicked instanceof Boolean && (Boolean) clicked) {
                                        System.out.println("Clicked Convert via JS fallback");
                                        Thread.sleep(1000);
                                    } else {
                                        System.out.println("JS fallback did not find Convert button");
                                        try {
                                            byte[] img = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                                            java.nio.file.Path out = Paths
                                                    .get("no_convert_" + System.currentTimeMillis() + ".png");
                                            Files.write(out, img);
                                            System.out.println("Wrote screenshot: " + out.toAbsolutePath());
                                        } catch (Exception se) {
                                            System.out.println("Could not write screenshot: " + se.getMessage());
                                        }
                                    }
                                } catch (Exception jsEx) {
                                    System.out.println("JS fallback error: " + jsEx.getMessage());
                                }
                                // Check for iframes (may be in the wrong location, but worth a try)
                                List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
                                if (!iframes.isEmpty()) {
                                    System.out.println("Found " + iframes.size() + " iframes");
                                    for (int i = 0; i < iframes.size(); i++) {
                                        driver.switchTo().frame(i);
                                        // Try to find Convert button here
                                        List<WebElement> buttons = driver
                                                .findElements(By.xpath("//button[contains(text(),'Convert')]"));
                                        if (!buttons.isEmpty()) {
                                            System.out.println("Found Convert buttons in iframe " + i);
                                            // Click logic here
                                        }
                                        driver.switchTo().defaultContent();
                                    }
                                }

                            }
                        } else {
                            System.out.println("No suitable download anchor clicked inside results");
                        }
                    }

                    // Wait for download completion
                    boolean success = waitForDownloadToFinish(Paths.get(DOWNLOAD_DIR), 60_000);

                    if (success) {
                        log.write("[SUCCESS] " + query + System.lineSeparator());
                    } else {
                        log.write("[FAILED] Timeout waiting for download: " + query + System.lineSeparator());
                    }

                } catch (Exception e) {
                    log.write("[ERROR] " + query + " — " + e.getMessage() + System.lineSeparator());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    /**
     * Polls the download directory and returns true when no temp download files
     * (like .crdownload or .tmp) are present. Times out after the given millis.
     */
    private static boolean waitForDownloadToFinish(Path downloadDir, long timeoutMillis)
            throws InterruptedException {
        long start = System.currentTimeMillis();

        while (System.currentTimeMillis() - start < timeoutMillis) {
            boolean tempDownloadingExists = false;

            if (Files.exists(downloadDir)) {
                try (Stream<Path> stream = Files.list(downloadDir)) {
                    tempDownloadingExists = stream.anyMatch(path -> path.toString().endsWith(".crdownload") ||
                            path.toString().endsWith(".tmp"));
                } catch (IOException e) {
                    // If listing fails, wait and retry until timeout
                    tempDownloadingExists = true;
                }
            }

            if (!tempDownloadingExists) {
                return true;
            }

            Thread.sleep(500);
        }
        return false;
    }

    private static void waitForDownloadInterface(WebDriver driver, WebDriverWait wait) {
        try {
            // Wait for the table with Convert buttons to be visible
            wait.until(ExpectedConditions
                    .visibilityOfElementLocated(By.cssSelector("table, .download-table, #download-section")));
            Thread.sleep(1000); // Give it a moment to fully render
        } catch (Exception e) {
            System.out.println("Download interface wait timed out, proceeding anyway");
        }
    }

}