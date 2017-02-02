package data.specs;


import functional.pages.Navigation;
import functional.specs.AutomationBaseSpec;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Title;

import java.io.IOException;
import java.util.Map;

public class NALCPostXMLSpec extends AutomationBaseSpec{
    String executeSingleTest;

    @BeforeClass
    public void setUp() throws IOException {
        super.setUp();
        navigation.navigateToPage(Navigation.Menu.POSTMESSAGE.toString());
        executeSingleTest = System.getProperty("test.single");
    }

    @AfterClass
    public void cleanUp() {
        driverUtils.getDriver().quit();
    }

    @Title("POST NALC XML Data in WMS")
    @Test
    public void post_nalc_xml_request() throws IOException {

        Map<String, String> fileDetails = xmlUtils.filterReadTestCaseXML("nalc", executeSingleTest);

        //Posting Multiple NALC XML.
        fileDetails.forEach((key, value) -> {
            postMessagePage.postXMLData(value);
            //Need to validate the response to ensure that there is no Errors.
            postMessagePage.verifyPOSTXML();
        });
    }
}
