package functional.pages;


import functional.entities.NALCERResult;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;
import java.util.Optional;

public class TasksPage extends BasePage{
    private WebDriver driver;
    /**
     * Locators
     */
    public By txtTaskId = By.cssSelector("[type='text'][id='dataForm:lview:filterId:field1value1']");
    //Table Details.
    public By tblTasksList = By.cssSelector("table[id$='dataTable_body']");
    public By lblTaskID = By.cssSelector("span[id$='taskIdVal']");
    public By lblTaskType = By.cssSelector("span[id$='descVal1']");
    public By lblHeaderStatus = By.cssSelector("span[id$='statusVal']");
    public By lblPriority = By.cssSelector("span[id$='descVal3']");
    public By lblStartWorkGroup = By.cssSelector("span[id$='descVal10']");
    public By lblStartWorkArea = By.cssSelector("span[id$='descVal10WA']");
    /**
     * Constructor for the Page.
     * @param driver
     */
    public TasksPage(WebDriver driver) {
        this.driver = driver;
        assert (this.driver != null);
    }

    /**
     * Validate the Task Details provided.
     * @param testingResult
     * @return
     */
    @Step("Validating the Task Details")
    public boolean validateTasksDetails(NALCERResult testingResult) {
        boolean isValid = false;
        Optional<WebElement> firstRow = driver.findElements(tblTasksList).stream().findFirst();
        List<WebElement> columns = firstRow.get().findElements(By.tagName("td"));
        boolean isValidTask = columns.get(3).findElement(lblTaskType).getText().trim().equalsIgnoreCase(testingResult.getTaskType());
        boolean isValidHeaderStatus = columns.get(5).findElement(lblHeaderStatus).getText().trim().equalsIgnoreCase(testingResult.getHeaderStatus());
        boolean isValidPriority = columns.get(4).findElement(lblPriority).getText().trim().equalsIgnoreCase(testingResult.getPriority());
        if (isValidTask && isValidHeaderStatus && isValidPriority)
            isValid = true;
        return isValid;
    }
}
