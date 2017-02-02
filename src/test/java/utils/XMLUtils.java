package utils;

import org.apache.commons.io.FileUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLUtils {

    /**
     * Method to find the XML in the resource and update it.
     * @param testCase
     * @param group
     * @param result
     */
    public void updateTestCaseXml(String testCase, String group, Map<String, Map<String, String>> result) {
        try {

            SAXBuilder builder = new SAXBuilder();
            URL url = this.getClass().getResource("/data/" + group + "/" + testCase+".xml");

            File xmlFile = new File(url.getPath());

            Document doc = (Document) builder.build(xmlFile);
            Element rootNode = doc.getRootElement();

            // update staff id attribute
            Element baseElement = rootNode.getChild("Message");
            updateXMLNode(baseElement, group, result.get(testCase));
            XMLOutputter xmlOutput = new XMLOutputter();

            // display nice nice
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, new FileWriter(new File("src/test/resources/data/" + group + "/" + testCase+".xml")));
            System.out.println("File updated!");
        } catch (IOException io) {
            io.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method to update the nodes on the XML based on the test group.
     * @param baseElement
     * @param group
     * @param result
     */
    private void updateXMLNode(Element baseElement, String group, Map<String, String> result) {
        String randomID = "AT" + Calendar.getInstance().getTimeInMillis();
        switch(group) {
            case "nalc":
                baseElement.getChild("DistributionOrder").getChild("DistributionOrderId").setText(randomID);
                Element lineItemNode = baseElement.getChild("DistributionOrder").getChild("LineItem");
                //Updating the XML file.
                lineItemNode.getChild("ItemName").setText(result.get("ItemName"));
                lineItemNode.getChild("Quantity").getChild("OrderQty").setText(result.get("OrderQty"));
                lineItemNode.getChild("InventoryAttributes").getChild("ItemAttribute1").setText(result.get("ItemAttribute1"));
        }

    }

    /**
     * Method to read the DIstributionOrderId from the XML.
     * @param testCase
     * @param group
     * @param distributionOrderId
     * @return
     */
    public String readXMLNode(String testCase, String group, String distributionOrderId) {
        SAXBuilder builder = new SAXBuilder();
        URL url = this.getClass().getResource("/data/"+ group +"/"+ testCase+".xml");
        File xmlFile = new File(url.getPath());

        try {

            Document document = (Document) builder.build(xmlFile);
            Element rootNode = document.getRootElement();
            List list = rootNode.getChild("Message").getChildren("DistributionOrder");

            for (int i = 0; i < list.size(); i++) {

                Element node = (Element) list.get(i);
                distributionOrderId = node.getChildText("DistributionOrderId");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return distributionOrderId;
    }
    /**
     * Method to read the complete XML content.
     * @param group
     * @param testCase
     */
    public Map<String, String> filterReadTestCaseXML(String group, String testCase) throws IOException{
        File xmlFile;
        String folderPath;
        String fileName, fileContent;
        Map<String, String> fileDetails = new HashMap<>();
        // If running on CI it will take the Resource DIR otherwise it will get your local Info.
        String resourceDir = System.getProperty("RESOURCES_DIR");
        if (resourceDir == null)
            folderPath = "src/test/resources/data/" + group + "/";
         else
            folderPath = resourceDir + "/data/" + group + "/";

        if (testCase != null) {
            String testXMLPath = folderPath + testCase + ".xml";
            xmlFile = new File(testXMLPath);
            fileName = xmlFile.getName();
            fileContent = xmlFile.getCanonicalPath();
            fileDetails.put(fileName, fileContent);
        } else {
            xmlFile = new File(folderPath);
            xmlFile.listFiles();
            for(File fileEntry: xmlFile.listFiles()) {
                if (fileEntry.isFile()) {
                    fileName = fileEntry.getName();
                    fileContent = fileEntry.getCanonicalPath();
                    fileDetails.put(fileName, fileContent);
                }
            }
        }

        return fileDetails;
    }

}
