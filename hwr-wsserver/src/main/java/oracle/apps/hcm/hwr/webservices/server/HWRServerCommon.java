package oracle.apps.hcm.hwr.webservices.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;

public class HWRServerCommon {

    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(HWRServerCommon.class);
    
    public static String handleException(int pErrorCode, String pErrorMessage) {
        String errorMessage = "Server error: ";
        LOGGER.error(pErrorMessage);
        return generateErrorWebservicesResult(pErrorCode, errorMessage + pErrorMessage);
    }
    
    public static String generateErrorWebservicesResult(int pErrorCode, String pErrorMessage) {
        WebservicesResult myResult = new WebservicesResult();
        myResult.setResultCode(pErrorCode);
        myResult.setErrorMessage(pErrorMessage);
        return new WebservicesResultSerializer().serialize(myResult);
    }
    
    public static String generateResult(String pInputObject) {
        
        WebservicesResult myResult = new WebservicesResult();
        myResult.setResultContent(pInputObject);
        myResult.setResultCode(WebservicesResult.RESULT_SUCCESS);
        
        return new WebservicesResultSerializer().serialize(myResult);
    }
}
