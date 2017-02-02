package functional.pages;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.annotations.Step;
import utils.TestUtils;

public class Navigation extends BasePage{
    private WebDriver driver;
    /**
     * Locators
     */
    public By navMenu = By.id("phMenu");
    public By menuSearch = By.cssSelector("input.asbasinp");
    public By menuFirstItem = By.cssSelector("div.asbasdd > ol >li");
    public By navActions = By.id("Actions");
    public By navTools = By.id("Tools");

    /**
     * Selenium Page Constructor.
     * @param driver
     */
    public Navigation(WebDriver driver) {
        this.driver = driver;
        assert (driver.findElement(navMenu).isDisplayed());
    }

    /**
     * Enum Class for the Menu Items.
     */
    public enum Menu {

        DISTRIBUTIONORDERS("Distribution Orders"),
        POSTMESSAGE("Post Message"),
        RUNWAVES("Run Waves"),
        TASKS("Tasks"),
        WAVES("Waves");

        private String menuName;

        Menu(String roleName) {
            this.menuName = roleName;
        }

        public String getMenuName() {
            return this.menuName;
        }

        @Override
        public String toString() {
            return this.getMenuName();
        }
    }

    /**
     * Navigate to a particular page based on the provided Menu name.
     * @param menuItem
     */
    @Step("Navigating to {0} MenuItem")
    public void navigateToPage(String menuItem) {
        WebDriverWait wait = new WebDriverWait(driver, 120, 500);
        driver.findElement(navMenu).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(menuSearch))));
        TestUtils.populateField(driver, menuSearch, menuItem);
        driver.findElements(menuFirstItem).get(0).click();
    };
}
