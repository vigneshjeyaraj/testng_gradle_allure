package utils;

import functional.entities.NALCERData;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class JsonUtils {
    /**
     * Method to read the NALC Query from the json file.
     * @return
     */
    public List<NALCERData> readNALCTestData() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        URL url = this.getClass().getResource("/functional/NALCtest-data.json");
        File f = new File(url.getPath());
        List<NALCERData> testData = Arrays.asList(mapper.readValue(f, NALCERData[].class));
        return testData;
    }
}
