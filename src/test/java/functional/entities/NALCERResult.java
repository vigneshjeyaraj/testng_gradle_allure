package functional.entities;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NALCERResult {

    private String taskType;
    private String headerStatus;
    private String priority;

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getHeaderStatus() {
        return headerStatus;
    }

    public void setHeaderStatus(String headerStatus) {
        this.headerStatus = headerStatus;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
