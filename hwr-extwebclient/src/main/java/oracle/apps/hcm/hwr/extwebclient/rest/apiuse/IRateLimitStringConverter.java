package oracle.apps.hcm.hwr.extwebclient.rest.apiuse;


public interface IRateLimitStringConverter {

    /** Used to combine multiple API calls to count against the same rate */
    public String getConvertedString(String pInputString);
}
