package uk.gov.nbn.data.gis.processor.atlas;

/**
 * The following enumeration states the different atlas grade operations which
 * can be performed on an atlas grade capable map service
 * @author Christopher Johnson
 */
public enum Type {
    LEGEND("legend"), MAP("map"), ACKNOWLEDGMENT("acknowledgement");
    private String request;

    private Type(String request) {
        this.request = request;
    }

    public String getRequest() {
        return request;
    }
}
