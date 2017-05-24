package oracle.apps.hcm.hwr.extwebclient.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Used to verify access tokens.  Throws an exception if they are not valid.
 * 
 * @author tgsnyder
 *
 */
public class AccessTokenRestQueryResultValidation implements IRestQueryResultValidation {

    /** Field to pull out of the JSON response. */
    private static final String JSON_STR = "message";
    
    /** Status code when find a bad access token */
    private final int mHttpStatusCode;
    /** Potential messages identifying to a bad access token */
    private final Set<String> mMessages;
    
    /**
     * 
     * @param mHttpStatusCode - status codes to check for invalid
     * @param mTokenStrs - The set of strings to check the message for.
     */
    public AccessTokenRestQueryResultValidation(int pHttpStatusCode, Set<String> pMessages) {
        mHttpStatusCode = pHttpStatusCode;
        mMessages = pMessages;
    }
    
    
    @Override
    public void validateResponse(RestQuerySimpleResult pResult) throws ExpiredAccessTokenException {
        //unauthorized
        if (pResult.getStatusCode() == mHttpStatusCode) {
            
            //build map to flatten expired keys JSON
            Map<String, String> myExpiredTokenMap = new HashMap<String, String>(1);
            myExpiredTokenMap.put(JSON_STR, JSON_STR);

            //flatten JSON
            Map<String, Object> myErrorMap =
                JsonHelper.jsonStringToMap(pResult.getResponse(), myExpiredTokenMap);
            
            if (mMessages != null && myErrorMap != null) {
                if (mMessages.contains((String)myErrorMap.get(JSON_STR))) {
                    throw new ExpiredAccessTokenException((String)myErrorMap.get("message"));
                }
            }
        }
    }

}
