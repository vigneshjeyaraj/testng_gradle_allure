package functional.pages;

import functional.entities.WMSConstants;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ThreadGuard;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Step;
import utils.TestUtils;

import java.util.List;

public class DistributionOrdersPage extends BasePage {
    private WebDriver driver;
    /**
     * Locators
     */
    public By txtDistributionOrderId = By.cssSelector("[id*='DistributionOrderlist'][alt='Find Distribution Order']");
    public By txtContainingItem = By.cssSelector("[id*='DistributionOrderlist1:itemLookUpId']");
    public By txtShipment = By.cssSelector("[type='text'][alt='Find Shipment']");
    public By btnApply = By.cssSelector("[id$='DistributionOrderlist1apply'][type='button']");
    public By btnRefresh = By.cssSelector("[id$='MainListTable_rfsh_but'][alt='Refresh']");
    public By tblDistributionOrder = By.cssSelector("[id$='_MainListTable_body'] > tbody > tr");
    public By chkDistOrderFirstRow = By.cssSelector(".tbl_checkBox > input");

    public By tblNoDataFound = By.cssSelector("[id$='MainListTable:nodataRow']");

    public By btnView = By.cssSelector("[id$='View_button'][type='submit']");
    public By btnMore = By.cssSelector("span.moreButtonSpan > input");

    /**
     * Constructor for the Page.
     * @param driver
     */
    public DistributionOrdersPage(WebDriver driver) {
        this.driver = driver;
        assert (this.driver != null);
    }

    /**
     * Method to Search for the Distribution Order.
     * @param orderNumber
     * @return
     */
    @Step("Search for the {0} Distribution Order")
    public boolean searchDistributionOrder(String orderNumber) throws InterruptedException {
        boolean dataRetrieved = false;
        TestUtils.waitForElement(driver, txtDistributionOrderId);
        TestUtils.populateField(driver, txtDistributionOrderId, orderNumber);
        driver.findElement(btnApply).click();
        TestUtils.waitForElement(driver, tblDistributionOrder);
        Thread.sleep(3000); // Sleep for the search to return data otherwise Stale Element Reference exception.
        WebElement firstRow = driver.findElements(tblDistributionOrder).get(0);
        WebDriverWait wait = new WebDriverWait(driver, 120, 500);
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(firstRow)));
        wait.until(ExpectedConditions.visibilityOf(firstRow));
        try {
            if (firstRow.findElement(chkDistOrderFirstRow).isDisplayed()) {
                dataRetrieved = true;
            }
        } catch (NoSuchElementException e) {
            e.printStackTrace();
        }

        return dataRetrieved;
    }

    /**
     * Method to select the Distribution Order.
     */
    @Step("Select the Distribution Order")
    public void selectDistributionOrder() {
        List<WebElement> rowlist = driver.findElements(tblDistributionOrder);
        WebElement firstRow = rowlist.get(0);
        String fulfillmentStatus = firstRow.findElements(By.tagName("td")).get(6).getText();
        if (fulfillmentStatus.trim().equalsIgnoreCase(WMSConstants.Status.RELEASED.toString().trim())) {
            if (firstRow.findElement(chkDistOrderFirstRow).getAttribute("checked") == null)
                firstRow.findElement(chkDistOrderFirstRow).click();
        } else {
            syncForFulfillmentStatus();
            if (firstRow.findElement(chkDistOrderFirstRow).getAttribute("checked") == null)
                firstRow.findElement(chkDistOrderFirstRow).click();
        }
    }

    /**
     * Method to Select and Navigate to Distribution Order.
     */
    @Step("Select and Navigate to Distribution Order")
    public void selectNavToDistributionOrder() {
        selectDistributionOrder();
        driver.findElement(btnView).click();
    }

    /**
     * Sync for the Distribution Order in Released Status
     * @return
     */
    @Step("Sync for the Distribution Order in Released Status")
    public boolean syncForFulfillmentStatus() {
        int i = 0;
        boolean isSynch = false;
        TestUtils.waitForElement(driver, tblDistributionOrder);

        while (i < 10) {
            WebElement fulfillmentStatus = driver.findElements(tblDistributionOrder).get(0).findElements(By.tagName("td")).get(6);
            if (!(fulfillmentStatus.getText().trim().equalsIgnoreCase(WMSConstants.Status.RELEASED.toString()))) {
                driver.findElement(btnRefresh).click();
                try {
                    Thread.sleep(5000); // Sleep for 2 Secs
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            } else
                isSynch = true;
            if (isSynch) break;
        }
        return isSynch;
    }
}
