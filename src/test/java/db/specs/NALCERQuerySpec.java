package db.specs;


import db.entities.DBConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Title;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class NALCERQuerySpec extends DBBaseSpec {
    String env = "NALCER";
    String executeSingleTest = System.getProperty("test.single");
    Connection connection;
    java.util.Optional<DBConfiguration> config;
    Map<String, String> allTestQueries = new HashMap<>();

    @BeforeClass
    public void setUpSpec() throws IOException, SQLException {
        super.initialize();
        config = super.getDbConfigurations(env);
        connection = dbUtils.getDBConnection(config.get());
        allTestQueries = super.getExeQueries(executeSingleTest);
    }

    @AfterClass
    public void cleanUpSpec() throws SQLException{
        connection.close();
    }

    @Title("Execute and Update NALC XML files")
    @Test
    public void execute_query_and_update_NALCXml() {

        String testGroup = "nalc";
        allTestQueries.forEach((testCase, query)-> {
            System.out.println("TestCase : " + testCase + " Query : " + query);
            try {
                Map<String, Map<String, String>> result = new HashMap<>();
                result = dbUtils.dbExecuteQuery(connection, query, testGroup, testCase);
                xmlUtils.updateTestCaseXml(testCase, testGroup, result);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
