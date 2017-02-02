package functional.specs;

import functional.entities.NALCERData;
import functional.pages.*;
import org.openqa.selenium.support.PageFactory;
import utils.DriverUtils;
import utils.JsonUtils;
import utils.TestUtils;
import utils.XMLUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public class AutomationBaseSpec {
    public XMLUtils xmlUtils = new XMLUtils();
    public DriverUtils driverUtils = new DriverUtils();
    public JsonUtils jsonUtils = new JsonUtils();
    public LoginPage loginPage;
    public Navigation navigation;
    public PostMessagePage postMessagePage;
    public DistributionOrdersPage distributionOrdersPage;
    public DistributionOrderPage distributionOrderPage;
    public RunWavesPage runWavesPage;
    public RunWavePage runWavePage;
    public WavesPage wavesPage;
    public TasksPage tasksPage;
    public List<NALCERData> testDataList;

    public void setUp() throws IOException {

        // Driver and PageFactory Initialization.
        driverUtils.initializeDriver();
        testDataList = jsonUtils.readNALCTestData();
        loginPage = PageFactory.initElements(driverUtils.getDriver(), LoginPage.class);;
        //LOgin to WMS
        loginPage.setLoginDetails(driverUtils.getPropertyValue("NALC_ER_Username"), driverUtils.getPropertyValue("NALC_ER_Password"));

        navigation = PageFactory.initElements(driverUtils.getDriver(), Navigation.class);
        postMessagePage = PageFactory.initElements(driverUtils.getDriver(), PostMessagePage.class);
        distributionOrdersPage = PageFactory.initElements(driverUtils.getDriver(), DistributionOrdersPage.class);
        runWavesPage = PageFactory.initElements(driverUtils.getDriver(), RunWavesPage.class);
        runWavePage = PageFactory.initElements(driverUtils.getDriver(), RunWavePage.class);
        wavesPage = PageFactory.initElements(driverUtils.getDriver(), WavesPage.class);
        distributionOrderPage = PageFactory.initElements(driverUtils.getDriver(), DistributionOrderPage.class);
        tasksPage = PageFactory.initElements(driverUtils.getDriver(), TasksPage.class);

    }

    public String getTestCaseName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }
}
