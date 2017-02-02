package db.specs;

import db.entities.DBConfiguration;
import db.entities.DBQuery;
import utils.DBUtils;
import utils.XMLUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


public class DBBaseSpec {
    DBUtils dbUtils = new DBUtils();
    XMLUtils xmlUtils = new XMLUtils();

    List<DBConfiguration> dbConfigurations;
    List<DBQuery> queries;

    public void initialize() throws IOException {
        queries = dbUtils.readNALCQueryInfo();
        dbConfigurations = dbUtils.readDBConfig();
    }

    public Optional<DBConfiguration> getDbConfigurations(String environment) {

        Optional<DBConfiguration> configuration = (this.dbConfigurations).stream()
                .filter(x -> x.getEnvironment().equals(environment)).findFirst();
        return configuration;
    }

    public Map<String, String> getDBQueries() {
         Map<String, String> tcQuery = this.queries.stream()
                .collect(Collectors.toConcurrentMap(item -> item.getTestCase(), item -> String.join(" ", item.getQuery())));
        return tcQuery;
    }

    public Map<String, String> getExeQueries(String singleTest)
    {
        Map<String, String> allTestQueries = new HashMap<>();
        Map<String, String> filteredTestQueries = new HashMap<>();

        allTestQueries = getDBQueries();
        //Filtering the Map to execute a Single Test
        if (singleTest != null)
            filteredTestQueries = allTestQueries.entrySet().stream()
                    .filter(map -> map.getKey().equalsIgnoreCase(singleTest))
                    .collect(Collectors.toConcurrentMap(p -> p.getKey(), p -> p.getValue()));
        else
            filteredTestQueries = allTestQueries;
        return filteredTestQueries;
    }
}
