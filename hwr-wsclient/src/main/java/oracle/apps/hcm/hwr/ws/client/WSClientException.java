package oracle.apps.hcm.hwr.ws.client;

public class WSClientException extends RuntimeException {

	private static final long serialVersionUID = 4334423113631468177L;
	
	private int mErrorCode;
	
	public WSClientException(int pErrorCode) {
		mErrorCode = pErrorCode;
	}

	public WSClientException(int pErrorCode, String message) {
		super(message);
		mErrorCode = pErrorCode;
	}

	public WSClientException(int pErrorCode, Throwable cause) {
		super(cause);
		mErrorCode = pErrorCode;
	}

	public WSClientException(int pErrorCode, String message, Throwable cause) {
		super(message, cause);
		mErrorCode = pErrorCode;
	}

	public int getErrorCode() {
		return mErrorCode;
	}
}
