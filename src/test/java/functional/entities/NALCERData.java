package functional.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NALCERData {

    private String testCase;
    private String routingWave;
    private String routingRule;
    private String pickingWave;
    private String pickingRule;

    @JsonProperty ("testingResult")
    private NALCERResult testingResult;

    public String getTestCase() {
        return testCase;
    }

    public void setTestCase(String testCase) {
        this.testCase = testCase;
    }

    public String getRoutingWave() {
        return routingWave;
    }

    public void setRoutingWave(String routingWave) {
        this.routingWave = routingWave;
    }

    public String getRoutingRule() {
        return routingRule;
    }

    public void setRoutingRule(String routingRule) {
        this.routingRule = routingRule;
    }

    public String getPickingWave() {
        return pickingWave;
    }

    public void setPickingWave(String pickingWave) {
        this.pickingWave = pickingWave;
    }

    public String getPickingRule() {
        return pickingRule;
    }

    public void setPickingRule(String pickingRule) {
        this.pickingRule = pickingRule;
    }

    public NALCERResult getTestingResult() {
        return testingResult;
    }

    public void setTestingResult(NALCERResult testingResult) {
        this.testingResult = testingResult;
    }

}
