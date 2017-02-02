package functional.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Step;
import utils.TestUtils;

public class PostMessagePage {
    private WebDriver driver;
    /**
     * Locators
     */
    public By txtXMLMessage = By.id("dataForm:xmlString");
    public By txtResponseXML = By.id("dataForm:resultString");
    public By btnReset = By.id("dataForm:resetCmdId");
    public By btnChooseFile = By.id("dataForm:uploadedFileID");
    public By btnSend = By.id("dataForm:postMessageCmdId");
    /**
     * Constructor and Page level assertions to ensure the page loaded successfully.
     * @param driver
     */
    public PostMessagePage(WebDriver driver)
    {
        this.driver = driver;
        assert(this.driver != null);
    }

    /**
     * Method to Post the XML in WMS.
     * @param xmlData
     */
    @Step("Posting the {0} XML")
    public void postXMLData(String xmlData) {
        WebElement btnChoose  = driver.findElement(btnChooseFile);
        ((RemoteWebElement) btnChoose).setFileDetector(new LocalFileDetector());
        btnChoose.sendKeys(xmlData);
        driver.findElement(btnSend).click();
    }

    /**
     * Method to verify the POST XMl was success.
     */

    @Step("Verify the XML POST was Successful")
    public void verifyPOSTXML () {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebDriverWait wait = new WebDriverWait(driver, 120, 500);
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(txtResponseXML)));
        wait.until(ExpectedConditions.presenceOfElementLocated(btnReset));

    }
}
