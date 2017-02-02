package utils;

import db.entities.DBConfiguration;
import db.entities.DBQuery;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;

public class DBUtils {
    /**
     * Method to read the DB COnfigurations from the json file.
     * @return
     */
    public List<DBConfiguration> readDBConfig() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        URL url = this.getClass().getResource("/db/dbConfiguration.json");
        File f = new File(url.getPath());
        List<DBConfiguration> configurations =Arrays.asList(mapper.readValue(f, DBConfiguration[].class));
        return configurations;
    }

    /**
     * Method to read the NALC Query from the json file.
     * @return
     */
    public List<DBQuery> readNALCQueryInfo() throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        URL url = this.getClass().getResource("/db/nalc-queries.json");
        File f = new File(url.getPath());
        List<DBQuery> queries = Arrays.asList(mapper.readValue(f, DBQuery[].class));
        return queries;
    }

    /**
     * Method to get the DBConnection with the Specified DB configurations.
     * @param config
     * @return
     * @throws SQLException
     */
    public Connection getDBConnection(DBConfiguration config) throws SQLException {
        Connection connection = null;

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your Oracle JDBC Driver?");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(config.getHostName(), config.getUserId(), config.getPassword());
        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();
        }
        if (connection != null) {
            return connection;
        } else {
            System.out.println("Failed to make connection!");
        }
        return connection;
    }

    /**
     * Method to execute a dbQuery by making a connection
     * @param connection
     * @param query
     * @param group
     * @param testCase
     * @return
     * @throws SQLException
     */
    public Map<String, Map<String, String>>  dbExecuteQuery(Connection connection, String query, String group, String testCase) throws SQLException {
        Map<String, Map<String, String>> dbResult = new HashMap<>();

        if (connection != null) {
            //create the statement object
            Statement stmt=connection.createStatement();
            //execute query
           ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                dbResult.put(testCase, extractResultSet(rs, group));
            }
        } else {
            System.out.println("Failed to make connection!");
        }
        return dbResult;
    }

    /**
     * Method to extract the result set and put it in a Map.
     * @param rs
     * @param group
     * @return
     * @throws SQLException
     */

    public Map<String, String> extractResultSet(ResultSet rs, String group) throws SQLException {
        Map<String, String> dbResult = new HashMap<>();
        switch (group) {
            case "nalc":
                dbResult.put("ItemName", rs.getString("ITEM_NAME"));
                dbResult.put("OrderQty", rs.getString("QTY"));
                dbResult.put("ItemAttribute1", rs.getString("ITEM_ATTR_1"));
        }
        return dbResult;
    }
}
