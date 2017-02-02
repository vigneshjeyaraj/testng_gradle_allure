package functional.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import ru.yandex.qatools.allure.annotations.Step;
import utils.TestUtils;

public class RunWavesPage extends BasePage{
    private WebDriver driver;
    /**
     * Locators
     */
    public By btnApply = By.id("dataForm:listView:filterId:filterIdapply");
    public By btnRunWave = By.cssSelector("[type='button'][id^='rmButton_1RunWave1']");
    public By btnPreviewWave = By.cssSelector("[type='button'][value='Preview Wave']");
    public By btnRefresh = By.id("dataForm:listView:dataTable:dataTable_rfsh_but");
    public By tblRunWavesList = By.id("dataForm:listView:dataTable_body");
    public By txtDescription = By.cssSelector("[type='text'][alt='Find Description']");
    public By txtPickingWaveParameters = By.cssSelector("[type='text'][alt='Find Template']");
    public By chkRunWavesFirstRow = By.cssSelector(".tbl_checkBox > input");


    /**
     * Constructor for the Page.
     */
    public RunWavesPage(WebDriver driver) {
        this.driver = driver;
        assert (this.driver != null);
    }
    public void filterSelectWaves(String wave) throws InterruptedException{
        filterRunWaves(wave);
        selectRunWave(wave);
    }
    /**
     * Method to Search and filter the Run Waves Section.
     * @param waveDescription
     * @return
     * @throws InterruptedException
     */
    @Step("Filtering the Run Waves table for {0} wave description")
    public boolean filterRunWaves(String waveDescription) throws InterruptedException {
        boolean isFiltered = false;
        int i = 0;
        WebDriverWait wait = new WebDriverWait(driver, 60, 500);
        TestUtils.waitForElement(driver, txtDescription);
        TestUtils.populateField(driver, txtDescription, waveDescription);
        driver.findElement(btnApply).click();
        TestUtils.waitForElement(driver, tblRunWavesList);
        Thread.sleep(3000); // Sleep for the search to return data otherwise Stale Element Reference exception.
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(tblRunWavesList))));

        while (i < 2) {
            WebElement runWavesFirst = driver.findElements(tblRunWavesList).get(0);
            wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(runWavesFirst)));
            wait.until(ExpectedConditions.visibilityOf(runWavesFirst));
            try {
                if (runWavesFirst.findElement(chkRunWavesFirstRow).isDisplayed() && driver.findElements(tblRunWavesList).size() < 3) {
                    isFiltered = true;
                } else {
                    Thread.sleep(3000); // Sleep for the search to return data otherwise Stale Element Reference exception.
                    wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(runWavesFirst)));
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
            i++;
            if (isFiltered) break;
        }
        return isFiltered;
    }

    /**
     * Method to select a Run Wave.
     * @param runwave
     */
    @Step("Selecting the Filtered {0} Run Wave")
    public void selectRunWave(String runwave) {
        WebElement runWavesFirst = driver.findElements(tblRunWavesList).get(0);
        String filteredWave = runWavesFirst.findElements(By.tagName("td")).get(1).getText();
        Assert.assertTrue(filteredWave.equalsIgnoreCase(runwave), "Verifying the right Run Waves was filtered.");
        runWavesFirst.findElement(chkRunWavesFirstRow).click();
        driver.findElement(btnRunWave).click();
    }
}
