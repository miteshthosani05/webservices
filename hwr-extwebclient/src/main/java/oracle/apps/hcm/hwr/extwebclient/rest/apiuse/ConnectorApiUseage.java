package oracle.apps.hcm.hwr.extwebclient.rest.apiuse;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ConnectorApiUseage {

    private static final Log LOGGER = LogFactory.getLog(ConnectorApiUseage.class);
    
    private Map<String, ConnectorApiCounter> mApiParts;
    
    //convert string into something combined, so we can combine multiple api calls to same counter.
    private IRateLimitStringConverter mStringConverter;
    
    /**
     * Constructor
     * 
     * @param pApiParts - Map containing 
     *    - string value of the "Api Call" (passed into restQueryEngine)
     *    - Integer - Max API calls for this piece.
     * @param pStringConverter - Class used to convert grouped "API calls" (passed into restQueryEngine)
     *     into unique strings.
     */
    public ConnectorApiUseage(Map<String, ConnectorApiCounter> pApiParts,
        IRateLimitStringConverter pStringConverter) {
        
        mStringConverter = pStringConverter;
        mApiParts = pApiParts;
    }
    
    /**
     * Get current useage of this part.
     * 
     * @param pPart
     * @return
     */
    public int getUseage(String pPart) {
        int myRet = 0;
        
        String myConvertedPart = mStringConverter.getConvertedString(pPart);
        
        ConnectorApiCounter myCounter = mApiParts.get(myConvertedPart);
        if (myCounter != null) {
            myRet = myCounter.getCurrCount();
        }
        
        return myRet;
    }
    
    /**
     * Increment the API useage.
     * Throws exception if we exceed the API limit.
     * @param pKey
     * @param pIncrementNum
     */
    public void incrementApiUseage(String pKey, int pIncrementNum) throws ApiLimitException {
        
        String myConvertedPart = mStringConverter.getConvertedString(pKey);
        
        ConnectorApiCounter myCounter = mApiParts.get(myConvertedPart);
        if (myCounter != null) {
            myCounter.increment(pIncrementNum);
        } else {
            LOGGER.info("Did not count API call.  Not in map: " + pKey);
        }
    }
    
    /**
     * Determine if we can process the next profile.
     * @return
     */
    public boolean processNextProfile() {
        boolean myRet = true;
        
        for (ConnectorApiCounter myCounter : mApiParts.values()) {
            int myMaxVal = myCounter.getMaxCount();
            int myCurrVal = myCounter.getCurrCount();
            
            if (myCurrVal >= myMaxVal) {
                myRet = false;
                break;
            }
        }
                
        return myRet;
    }
}
