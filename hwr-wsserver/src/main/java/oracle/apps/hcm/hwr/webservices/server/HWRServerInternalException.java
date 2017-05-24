package oracle.apps.hcm.hwr.webservices.server;


public class HWRServerInternalException extends Exception {

    private String mMessage;
    
    private int mErrorCode;
    
    /**
     * 
     */
    private static final long serialVersionUID = -6988461950789155319L;

    public HWRServerInternalException(int pErrorCode, String pMessage) {
        mMessage = pMessage;
        mErrorCode = pErrorCode;
    }
    
    public String getMessage() {
        return mMessage;
    }
    
    public int getErrorCode() {
        return mErrorCode;
    }
}
