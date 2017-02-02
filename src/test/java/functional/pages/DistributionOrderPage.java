package functional.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class DistributionOrderPage extends BasePage{
    private WebDriver driver;
    /**
     * Locators
     */
    public By lnkShipmentId = By.cssSelector("a[id$='DODtlHdr_OpLnk_Shp__']");
    /**
     * Constructor for the Page.
     * @param driver
     */
    public DistributionOrderPage(WebDriver driver) {
        this.driver = driver;
        assert (this.driver != null);
    }


}
