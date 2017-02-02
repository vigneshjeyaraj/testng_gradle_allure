package utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class TestUtils
{
    private static Logger LOGGER = LoggerFactory.getLogger(TestUtils.class);

    /**
     * Method to populate Text field.
     * @param driver The current WebDriver
     * @param locator A locator used to find the field
     * @param value The value with which to populate the field
     */
    public static void populateField(WebDriver driver, By locator, String value)
    {
        if (StringUtils.isNotEmpty(value))
        {
            //clearing the TextBox values before entering the new value
            driver.findElement(locator).clear();
            driver.findElement(locator).sendKeys(value);

            //validating if the entered text is correct or re-entering the values again.
            String retrievedText = driver.findElement(locator).getAttribute("value");
            if (!retrievedText.equalsIgnoreCase(value))
            {
                driver.findElement(locator).sendKeys(Keys.chord(Keys.CONTROL, "a"), value);
            }
        }
    }

    /**
     * Method to select the value from a list box using its value.
     * @param element The element to select from
     * @param value The value to select
     */
    public static void selectlistvalue(WebElement element, String value)
    {
        if (value != null && !value.isEmpty())
        {
            Select sel = new Select(element);
            sel.selectByValue(value);
        }
        else
        {
            LOGGER.debug("The value for the WebElement {} is {}", element, value);
        }
    }

    /**
     * Method to select Radio Button based on its value.
     * @param driver The current WebDriver
     * @param locator The locator to use to find the button
     * @param value The value to select
     */
    public static void selectRadioButton(WebDriver driver, By locator, String value)
    {
        List<WebElement> select = driver.findElements(locator);
        for (WebElement radio : select)
        {
            if (radio.getAttribute("value").equalsIgnoreCase(value))
            {
                radio.click();
            }
        }
    }

    /**
     * Method to select Check-box based on its value.
     * @param driver The current WebDriver
     * @param locator The locator used to find the checkboxes
     * @param values The values to select
     */
    public static void selectCheckboxes(WebDriver driver, By locator, String... values)
    {
        List<WebElement> elements = driver.findElements(locator);

        for (String check : values)
        {
            for (WebElement chk : elements)
            {
                if (chk.getAttribute("value").equalsIgnoreCase(check))
                {
                    chk.click();
                }
            }
        }
    }

    /**
     * Method to verify a particular text exists in a page.
     * @param driver The current WebDriver
     * @param textToVerify The text to verify
     * @return true if the text is on the page, false otherwise
     */
    public static boolean isTextPresent(WebDriver driver, String textToVerify)
    {
        textToVerify = textToVerify.replace(" ", "\\s*");
        String pageSource = driver.getPageSource();
        String[] pageSourceLines = pageSource.trim().split("\\n");
        String pageSourceWithoutNewlines = "";

        for (String pageSourceLine : pageSourceLines)
        {
            pageSourceWithoutNewlines += pageSourceLine + " ";
        }

        pageSourceWithoutNewlines = pageSourceWithoutNewlines.trim();

        Pattern p = Pattern.compile(textToVerify);
        Matcher m = p.matcher(pageSourceWithoutNewlines);

        return m.find();
    }

    /**
     * Method to take a screen shot of a particular page.
     * @param driver The current WebDriver
     * @param screenShotPath The path in which to place the screen shot
     */

    public void takeScreenshot(WebDriver driver,String screenShotPath)
    {
        if (driver == null)
        {
            LOGGER.error("Report.driver is not initialized!");
            return;
        }

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        try
        {
            FileUtils.copyFile(scrFile, new File(screenShotPath), true);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing screenshot to file", e);
        }
    }
    /**
     * Method to check for the page to Load.
     * @param driver The current WebDriver
     */
    public static void waitForPageLoad(WebDriver driver)
    {
        WebDriverWait wait = new WebDriverWait(driver,90, 1000);
        boolean pageLoadDone = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("loading-mask")));

        if (!pageLoadDone)
        {
            LOGGER.info(
                    "The loading mask on page still appeared and the page was not loaded successfully. Increase the time-out");
        }
    }

    /**
     * Method to wait for an Element on a Page until the element is Present.
     * @param driver The current WebDriver
     * @param locator The locator used to find the element
     * @return The element
     */
    public static WebElement waitForElement(WebDriver driver, By locator)
    {
        WebDriverWait wait = new WebDriverWait(driver,60, 500);
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Method to get the Current Date and Time in the format "yyyy/MM/dd HH:mm:ss".
     * @return Current Date/Time as yyyy/MM/dd HH:mm:ss
     */
    public static String getCurrentDateTime()
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        //get current date time with Calendar()
        Calendar cal = Calendar.getInstance();

        return dateFormat.format(cal.getTime());
    }

    /**
     * Method to Navigate to the bottom of page for Infinite scroll and then Navigate to Top of page.
     * @throws InterruptedException
     */
    public static void scrollToBottomThenTop(WebDriver driver) throws InterruptedException
    {
        //code to scroll to the bottom of the page using JavaScript.
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Long contentHeight = (Long)js.executeScript("return " +
                "(document.body.clientHeight-(window.pageYOffset + window.innerHeight))");
        LOGGER.debug("Content height = {}", contentHeight);

        boolean pageBottomReached = false;
        int tryCount = 0;
        while (!pageBottomReached && tryCount < 100)
        {
            tryCount++;
            js.executeScript("javascript:window.onload=toBottom();"+
                    "function toBottom(){" +
                    "window.scrollTo(0,Math.max(document.documentElement.scrollHeight," +
                    "document.body.scrollHeight,document.documentElement.clientHeight));" +
                    "}");
            try
            {
                WebDriverWait loadingSpinnerWait = new WebDriverWait(driver, 3, 500);
                pageBottomReached = loadingSpinnerWait.until(
                        ExpectedConditions.invisibilityOfElementLocated(
                                By.id("search-results-loading-ind")));
            }
            catch (TimeoutException e)
            {
                // Do nothing here.  We just want to not have the timeout cause the tests to fail.
            }
            //Thread.sleep(30000);
            contentHeight = (Long)js.executeScript("return " +
                    "(document.body.clientHeight-(window.pageYOffset + window.innerHeight))");
            LOGGER.debug("Content height = {}", contentHeight);
        }

        //with javascript scroll reached the bottom of page.Additional Check to validate we reached the bottom.
        if (driver.findElement(By.xpath(".//div[@id='search-results-loading-ind' and contains(@class, 'ng-hide')]")).isEnabled())
        {
            LOGGER.debug("Success: Scrolled to the Bottom of Myorders page");
        }
        else
        {
            LOGGER.error("Fail: Not able to Scroll to the Bottom of MyOrders page");
        }

       // backToTop(driver);
    }

    public static void backToTop(WebDriver driver, By locator) throws InterruptedException
    {
        if (driver.findElement(locator).isDisplayed())
        {
            driver.findElement(locator).click();
            LOGGER.debug("In TestUtils.backToTop() - clicked on back to top button.");
            Thread.sleep(2000);
        }
        else
        {
            LOGGER.error("In TestUtils.backToTop() - back to top button not displayed.");
        }
    }

    /**
     * Method to scroll to the Bottom of the Page.
     * @param driver The current WebDriver
     * @throws InterruptedException
     */
    public static void scrollDown(WebDriver driver) throws InterruptedException
    {
        //code to scroll to the bottom of the page using JavaScript.
        JavascriptExecutor js = (JavascriptExecutor) driver;

        while ((Long)js.executeScript("return " +
                "(document.body.clientHeight-(window.pageYOffset + window.innerHeight))")>0)
        {
            js.executeScript("javascript:window.onload=toBottom();"+
                    "function toBottom(){" +
                    "window.scrollTo(0,Math.max(document.documentElement.scrollHeight," +
                    "document.body.scrollHeight,document.documentElement.clientHeight));" +
                    "}");
            Thread.sleep(1000);
        }
    }

    public static List<File> filesOnFolder(String folderPath) {
        List<File> allFiles = null;
        try{
            allFiles = Files.walk(Paths.get(folderPath))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return allFiles;
    }
}

