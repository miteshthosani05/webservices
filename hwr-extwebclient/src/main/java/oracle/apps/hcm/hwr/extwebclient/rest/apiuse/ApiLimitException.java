package oracle.apps.hcm.hwr.extwebclient.rest.apiuse;


public class ApiLimitException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Construct a simple ConnectorException.
     */
    public ApiLimitException() {
        super();
    }
    /**
     * Construct a simple ConnectorException with chained exception e.
     * @param e - exception to chain
     */
    public ApiLimitException(final Throwable e) {
        super(e);
    }
    /**
     * Construct a ConnectorException with the specified
     * string message.
     * @param s - the string message
     */
    public ApiLimitException(final String s) {
        super(s);
    }
    /**
     * Construct a ConnectorException with the specified
     * string message and chained exception.
     * @param s - the string message
     * @param e - the exception to chain
     */
    public ApiLimitException(final String s, final Throwable e) {
        super(s, e);
    }
}
