package utils;

import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DriverUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(DriverUtils.class);
    private WebDriver driver;
    Properties properties = new Properties();
    private static final int NUM_SECONDS_TO_WAIT = 30;


    public void initializeDriver() throws MalformedURLException {
        loadConfig();
        String browserName = System.getProperty("browser");
        String gridMode = System.getProperty("gridMode");
        String baseUrl = this.properties.getProperty("URL");
        String gridUrl = this.properties.getProperty("GridHubUrl");
        URL hubUrl = new URL(gridUrl);


        if (browserName == null || browserName.isEmpty()) {
            browserName = "chrome";
        }

        if (gridMode == null || gridMode.isEmpty()) {
            gridMode = "OFF";
        }

        if(browserName.contentEquals("firefox")) {
            FirefoxProfile firefoxProfile = new FirefoxProfile();
            firefoxProfile.setPreference("network.proxy.type", Proxy.ProxyType.AUTODETECT.ordinal());
            firefoxProfile.setEnableNativeEvents(true);
            DesiredCapabilities capability = DesiredCapabilities.firefox();
            capability.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
            if ("ON".equalsIgnoreCase(gridMode)) {
                driver = new RemoteWebDriver(hubUrl, capability);
            } else {
                driver = new FirefoxDriver(capability);
            }
        } else if (browserName.contentEquals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            DesiredCapabilities capability = DesiredCapabilities.chrome();
            capability.setBrowserName("chrome");
            options.addArguments("--window-size=1280x1024");
            capability.setCapability(ChromeOptions.CAPABILITY, options);
            if ("ON".equalsIgnoreCase(gridMode)) {
                driver = new RemoteWebDriver(hubUrl, capability);
            } else {
                System.setProperty("webdriver.chrome.driver", this.properties.getProperty("chromeDriverPath"));
                driver = new ChromeDriver(capability);
            }
        } else if (browserName.contentEquals("ie")) {
            DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
            capability.setCapability("nativeEvents", false);
            capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
            if ("ON".equalsIgnoreCase(gridMode)) {
                driver = new RemoteWebDriver(hubUrl, capability);
            } else {
                driver = new InternetExplorerDriver(capability);
            }
        }
        driver.get(baseUrl);
        driver.manage().timeouts().implicitlyWait(NUM_SECONDS_TO_WAIT, TimeUnit.SECONDS);

        driver.manage().window().maximize();

    }

    public WebDriver getDriver()
    {
        return driver;
    }
    /**
     * Method to load the Configuration file.
     */
    public void loadConfig() {
        try
        {
            InputStream stream = this.getClass().getClassLoader().getResourceAsStream("functional/config.Properties");
            properties.load(stream);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        assert !properties.isEmpty();
    }

    public String getPropertyValue(final String propertyName)
    {
        String propertyValue = properties.getProperty(propertyName);

        if (propertyValue != null)
        {
            propertyValue = propertyValue.trim();
        }
        else
        {
            LOGGER.debug("The value for property {} is not present", propertyName);
        }

        return propertyValue;
    }
}
