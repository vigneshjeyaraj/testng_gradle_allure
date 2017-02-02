package functional.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import ru.yandex.qatools.allure.annotations.Step;
import utils.TestUtils;

import java.util.Map;
/**
 * Page Objects for WMS LoginPage
 *
 */
public class LoginPage extends BasePage {
    private WebDriver driver;
    /**
     * Locators
     */
    public By txtWMSLoginUserName = By.id("j_username");
    public By txtWMSLoginPassword = By.id("j_password");
    public By btnWMSSignIn = By.name("btnEnter");
    public By lnkSignOut = By.cssSelector("a.phlink.-hft_pht");

    /**
     * Constructor and Page level assertions to ensure the page loaded successfully.
     * @param driver
     */
    public LoginPage(WebDriver driver)
    {
        this.driver = driver;
        assert(this.driver != null);
        TestUtils.waitForElement(this.driver, txtWMSLoginUserName);
    }

    /**
     * Method to set the Login credentials and SignIn.
     * @param username
     * @param password
     */
    @Step("User Logged in with {0}")
    public boolean setLoginDetails(String username, String password) {
        TestUtils.populateField(driver, txtWMSLoginUserName, username);
        TestUtils.populateField(driver, txtWMSLoginPassword, password);
        //Click on the SignIN button.
        driver.findElement(btnWMSSignIn).click();
        return validateLogin();
    }

    /**
     * Method to validate is the Login Successful.
     * @return
     */
    @Step("Validate the Login was successful")
    public boolean validateLogin() {
        boolean isLoggedIn = false;
        try {
            TestUtils.waitForElement(driver, lnkSignOut);
            isLoggedIn = driver.findElement(lnkSignOut).isDisplayed();
        } catch(NoSuchElementException e) {
            e.printStackTrace();
        }
        return isLoggedIn;
    }
}
