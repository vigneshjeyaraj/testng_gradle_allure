package functional.specs;


import functional.entities.NALCERData;
import functional.entities.WMSConstants;
import functional.pages.Navigation;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import utils.TestUtils;

import java.io.IOException;

@Features("NALC ER Functional Tests")
@Stories("Cart Pick Non Pack")
public class NALCFunctionalSpec extends AutomationBaseSpec{

    @BeforeClass
    public void setUp() throws IOException {
        super.setUp();
        navigation.navigateToPage(Navigation.Menu.DISTRIBUTIONORDERS.toString());
    }

    @AfterClass
    public void cleanUp() {
       // driverUtils.getDriver().quit();
    }

    @Title("OB_1014_PW03AT_HP_12_SW_INT3_Replen_from_Cart_Pick_Non_Packs")
    @Test
    public void OB_1014_PW03AT_HP_12_SW_INT3_Replen_from_Cart_Pick_Non_Packs() throws InterruptedException {
        //Given the data & Order available in the right status.
        String testCaseName = super.getTestCaseName();
        java.util.Optional<NALCERData> data = testDataList.stream()
                .filter(list -> list.getTestCase().equalsIgnoreCase(testCaseName))
                .findFirst();
        String orderNumber = xmlUtils.readXMLNode(testCaseName, "nalc", "DistributionOrderId");
        Assert.assertNotNull(data, "Test data is not present for "+testCaseName);
        //Search & Select the order in Distribution Orders Page.
        boolean distOrderReceived = distributionOrdersPage.searchDistributionOrder(orderNumber);
        Assert.assertTrue(distOrderReceived, "Distribution Order Received");
        distributionOrdersPage.syncForFulfillmentStatus();
        distributionOrdersPage.selectDistributionOrder();

        //When all the Routing wave functionality is performed.
        navigation.navigateToPage(Navigation.Menu.RUNWAVES.toString());
        runWavesPage.filterSelectWaves(data.get().getRoutingWave());
        runWavePage.selRuleUpdateRuleSet(data.get().getRoutingRule(), orderNumber);

        //Validate the Routing Wave number created Successfully.
        String routingWaveNumber = runWavePage.submitRunWave();
        driverUtils.getDriver().findElement(runWavePage.lnkWaveNumber).click();
        wavesPage.syncForWaveStatus(driverUtils.getDriver(), routingWaveNumber, WMSConstants.Status.SHIPWAVECOMPLETED.toString());

        //Verify the Shipment ID.
        navigation.navigateToPage(Navigation.Menu.DISTRIBUTIONORDERS.toString());
        distributionOrdersPage.selectNavToDistributionOrder();
        TestUtils.waitForElement(driverUtils.getDriver(), distributionOrderPage.lnkShipmentId);
        String shipmentID = driverUtils.getDriver().findElement(distributionOrderPage.lnkShipmentId).getText();
        Assert.assertNotNull(shipmentID, "The shipmentID is {} not Null");

        //When all the Picking wave functionality is performed.
        navigation.navigateToPage(Navigation.Menu.RUNWAVES.toString());
        runWavesPage.filterSelectWaves(data.get().getPickingWave());
        runWavePage.selRuleUpdateRuleSet(data.get().getPickingRule(), orderNumber);

        //Validate the Routing Wave number created Successfully.
        String pickingWaveNumber = runWavePage.submitRunWave();
        driverUtils.getDriver().findElement(runWavePage.lnkWaveNumber).click();
        wavesPage.syncForWaveStatus(driverUtils.getDriver(), pickingWaveNumber, WMSConstants.Status.SHIPWAVECOMPLETED.toString());
        wavesPage.selectWaveNumber(pickingWaveNumber);

        //Navigate to Tasks
        wavesPage.navToTasks();

        //Validate the Testing result.
        tasksPage.validateTasksDetails(data.get().getTestingResult());
    }
}
