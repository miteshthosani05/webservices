package oracle.apps.hcm.hwr.extwebclient.rest;


public class ExpiredAccessTokenException extends RuntimeException {

    /**
     * For serialization
     */
    private static final long serialVersionUID = 49164393451803105L;

    /**
     * Instantiates a new ExpiredAccessTokenException.
     * @param pMessage the message
     */
    public ExpiredAccessTokenException(final String pMessage) {
        super(pMessage);
    }

    /**
     * Instantiates a new ExpiredAccessTokenException.
     * @param pCause the cause
     */
    public ExpiredAccessTokenException(final Throwable pCause) {
        super(pCause);
    }

    /**
     * Instantiates a new ExpiredAccessTokenException.
     * @param pMessage the message
     * @param pCause the cause
     */
    public ExpiredAccessTokenException(final String pMessage, final Throwable pCause) {
        super(pMessage, pCause);
    }

    
}
