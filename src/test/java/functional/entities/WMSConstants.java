package functional.entities;

public class WMSConstants {
    /**
     * Enum Class for all the different Status.
     */
    public enum Status {

        RELEASED("Released"),
        PSEUDOCUBINGCOMPLETE("Pseudo Cubing Complete"),
        SHIPWAVESTARTED("Ship wave started"),
        SHIPWAVEQUEUED("Ship wave queued"),
        SHIPWAVECOMPLETED("Ship wave completed");

        private String status;

        Status(String status) {
            this.status = status;
        }

        public String getStatus() {
            return this.status;
        }

        @Override
        public String toString() {
            return this.getStatus();
        }
    }
}
