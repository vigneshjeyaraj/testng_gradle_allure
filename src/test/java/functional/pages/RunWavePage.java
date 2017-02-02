package functional.pages;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Step;
import utils.TestUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.apache.commons.lang3.StringUtils.containsNone;

public class RunWavePage extends BasePage{
    private WebDriver driver;
    /**
     * Locators
     */
    public By tblRules = By.cssSelector("[id^='dataForm:lview:dataTable'] > tbody > tr");
    public By chkRulesCheck = By.cssSelector("[type='checkbox'][id*='ruleHdrUseStatusCheckbox']");
    public By lblRuleName = By.cssSelector("[id$='ruleNameText']");
    public By radioBtnRule = By.cssSelector("[type='radio'][id^='checkAll_c_dataForm']");
    public By btnAddRule = By.id("dataForm:lview:ruleHdrAddButton");
    public By btnDeleteRule = By.id("dataForm:lview:ruleHdrDeleteButton");
    public By divPanelTop = By.cssSelector("div.pnltopdiv");

    //All the fields in Rule Set Section.
    public By tblRulesSet = By.cssSelector("[id$='ruleSelDtlDataTable_body'] > tbody > tr");
    public By imgRuleSetRefresh = By.cssSelector("[id$='ruleSelDtlDataTable_rfsh_but']");
    public By selRuleSetColumn = By.cssSelector("[id$='ruleSelDtlColumnList']");
    public By txtRuleSetCompareValue = By.cssSelector("[id$='ruleSelDtlRuleCmparValue']");
    public By btnSubmit = By.cssSelector("[type='button'][id*='SubmitWave']");
    public By btnSaveConfiguration = By.cssSelector("[type='button'][id*='SaveConfiguration']");
    public By btnCancel = By.cssSelector("[type='button'][id*='Cancel']");

    public By lnkWaveNumber = By.id("dataForm:AwvNbrRun");

    /**
     * Constructor for the Run Wave Page.
     * @param driver
     */
    public RunWavePage(WebDriver driver) {
        this.driver = driver;
        assert (this.driver != null);
    }

    public void selRuleUpdateRuleSet(String rule, String orderNumber) {
        selectRequiredRule(rule);
        updateRuleSetTable(orderNumber);
    }
    /**
     *  Method to select the Required Rule.
     * @param ruleName
     */
    @Step("Select the {0} Rule.")
    public void selectRequiredRule(String ruleName) {
        TestUtils.waitForElement(driver, tblRules);
        clearAllSelectedRule();

        driver.findElements(tblRules).stream().forEach(element -> {
            Boolean hasDataRow = StringUtils.containsNone(element.getAttribute("id"), "nodataRow");
            if (hasDataRow) {
                String name = element.findElements(By.tagName("td")).get(1).findElement(lblRuleName).getText().trim();
                if (name.equalsIgnoreCase(ruleName)) {
                    List<WebElement> allColumns = element.findElements(By.tagName("td"));
                    try {
                        WebElement radioRule = allColumns.get(0).findElement(radioBtnRule);
                        //Normal Click is not Working Here.
                        JavascriptExecutor executor = (JavascriptExecutor)driver;
                        Thread.sleep(6000);
                        executor.executeScript("arguments[0].click();", radioRule);
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (StringUtils.contains(element.getAttribute("class"), "syncrowon")) {
                        allColumns.get(2).findElement(chkRulesCheck).click();
                    } else {
                        Assert.assertTrue(false, "Rule Sync doesn't Happen");
                    }
                }
            }
        });
    }

    /**
     * Method to clear all the existing checked Rules.
     */
    @Step("Clear all the Existing Selected Rule.")
    public void clearAllSelectedRule() {

        driver.findElements(tblRules).stream().forEach(element -> {
            Boolean hasDataRow = StringUtils.containsNone(element.getAttribute("id"), "nodataRow");
            if (hasDataRow) {
                WebElement chkBox = element.findElements(By.tagName("td")).get(2)
                        .findElement(chkRulesCheck);
                String isChecked = chkBox.getAttribute("checked");
                //Clicking on the checkbox if checked.
                if (isChecked != null && isChecked.equalsIgnoreCase("true"))
                    chkBox.click();
            }
        });
    }

    /**
     * Method to Update the Rule Set Table with the respective Distribution Order.
     * @param distributionOrderId
     */
    @Step("Update the Rule Set table with the {0} Order")
    public void updateRuleSetTable(String distributionOrderId) {
        AtomicInteger counter = new AtomicInteger(0);
        TestUtils.waitForElement(driver, tblRulesSet);
        try {
            Thread.sleep(2000); //stale reference for the table.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.findElements(tblRulesSet).stream().forEach(element -> {
            Boolean hasDataRow = StringUtils.containsNone(element.getAttribute("id"), "nodataRow");
            if (hasDataRow) {
                WebElement selColumn = element.findElements(By.tagName("td")).get(2)
                        .findElement(selRuleSetColumn);
                String columnText = new Select(selColumn).getFirstSelectedOption().getText();
                if (columnText.equalsIgnoreCase("Order number")) {
                    counter.getAndAdd(1);
                    WebElement compareValue = element.findElements(By.tagName("td")).get(4)
                            .findElement(txtRuleSetCompareValue);
                    compareValue.clear();
                    if (counter.get() == 1)
                        compareValue.sendKeys(distributionOrderId);
                    else
                        compareValue.sendKeys("xx");
                }
            }
        });
        driver.findElement(divPanelTop).click();
    }

    /**
     * Method to Submit the Run Wave and Retrieve Wave Number.
     * @return
     */
    @Step("Submit the Run Wave and Retrieve Wave Number.")
    public String submitRunWave() {
        driver.findElement(btnSubmit).click();
        TestUtils.waitForElement(driver, lnkWaveNumber);
        return driver.findElement(lnkWaveNumber).getText();
    }

    /**
     * Method to Navigate to the Wave Number.
     * @param waveNumber
     */
    @Step("Navigating to {0} wave Number")
    public void navToWaveNumber(String waveNumber) {
        TestUtils.waitForElement(driver, lnkWaveNumber);
        driver.findElement(lnkWaveNumber).click();
    }


}
