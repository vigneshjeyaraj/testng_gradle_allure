package functional.pages;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Step;
import utils.TestUtils;

public class WavesPage extends BasePage {
    private WebDriver driver;
    /**
     * Locators
     */
    public By txtWaveNumber = By.cssSelector("[type='text'][id='dataForm:listView:filterId:field0value1']");
    public By btnView = By.cssSelector("[type='button'][id*='View']");
    public By btnMore = By.cssSelector("[type='button'][id*='moreButton']");
    public By btnRefresh = By.cssSelector("input[id$='dataTable_rfsh_but']");

    public By tblWaves = By.cssSelector("[id='dataForm:listView:dataTable_body'] > tbody > tr");
    public By lblWaveNumber = By.cssSelector("span[id$='shipWaveNbr']");
    public By lblStatus = By.cssSelector("span[id$='dataTable:0:c0012']");
    public By chkWaveNum = By.cssSelector("[type='checkbox'][id^='checkAll_c0_dataForm']");

    //Elements inside More
    public By popAction = By.cssSelector("#main > div[id^='rmbuttons']");
    public By popActionList = By.cssSelector("ul.fotop > li");
    public By lnkTasks = By.cssSelector("a[id*='Tasks']");
    /**
     * Constructor for the Run Wave Page.
     * @param driver
     */
    public WavesPage(WebDriver driver) {
        this.driver = driver;
        assert (this.driver != null);
    }

    /**
     * Method to Sync for the Wave Number to be in correct status.
     * @param waveNumber
     * @param status
     */
    @Step("Sync the WaveNumber {0} to be in {1} Status")
    public void syncForWaveStatus(WebDriver driver, String waveNumber, String status) {
        TestUtils.waitForElement(driver, txtWaveNumber);
        TestUtils.waitForElement(driver, tblWaves);
        boolean isSynced = false;
        driver.findElements(tblWaves).get(0)
                .findElements(By.tagName("td")).get(1).findElement(lblWaveNumber).getText().equalsIgnoreCase(waveNumber);
        int count = 0;
        while(count < 10) {
            try {
                String sysStatus = driver.findElements(tblWaves).get(0).findElements(By.tagName("td")).get(4)
                        .findElement(lblStatus).getText();
                if (!sysStatus.equalsIgnoreCase(status)) {
                    // Sync for the Shipment status change for 3 Minutes polling every 500 Sec.
                    driver.findElement(btnRefresh).click();
                    try {
                        Thread.sleep(10000); //Sync for the Stale Element
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    WebDriverWait wait = new WebDriverWait(driver, 120, 2000);
                    wait.until(ExpectedConditions.visibilityOf(driver.findElement(tblWaves)));
                } else
                    isSynced = true;
                count +=1;
            } catch (StaleElementReferenceException e) {
                count +=1;
                e.printStackTrace();
            }
            if (isSynced) break;
        }
    }

    /**
     * Method to Select the Particular Wave Number.
     * @param waveNumber
     */
    @Step("Select the {0} Wave Number")
    public void selectWaveNumber(String waveNumber) {

        driver.findElements(tblWaves).stream().forEach(element -> {
            Boolean hasDataRow = StringUtils.containsNone(element.getAttribute("id"), "nodataRow");
            if (hasDataRow) {
                WebElement chkWaveNo = element.findElements(By.tagName("td")).get(0)
                        .findElement(chkWaveNum);
                String retWaveNumber = element.findElements(By.tagName("td")).get(1).getText();
                if (chkWaveNo.getAttribute("checked") != "true")
                    chkWaveNo.click();
                }
        });
    }

    /**
     * Method to Navigate to the Tasks Action.
     */
    @Step("Navigate to the Tasks Action")
    public void navToTasks() {
        driver.findElement(btnMore).click();
        TestUtils.waitForElement(driver, popAction);
        String popDisplay = driver.findElement(popAction).getAttribute("style");
        if (StringUtils.contains(popDisplay, "display: block;")) {
            //TODO: HardCoding the Tasks Index need to Handle it Better.
            driver.findElement(popAction).findElements(popActionList).get(15)
                    .findElement(lnkTasks).click();
        }

    }
}
