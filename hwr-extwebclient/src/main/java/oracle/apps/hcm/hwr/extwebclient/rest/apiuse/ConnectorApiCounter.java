package oracle.apps.hcm.hwr.extwebclient.rest.apiuse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


//counter class, used so we don't have to do a get & a put to update the map
public class ConnectorApiCounter {

    /** The logger instance */
    private static final Log LOGGER = LogFactory.getLog(ConnectorApiCounter.class);

    int myVal = 0;
    int mMaxVal = 0;
        
    public ConnectorApiCounter(int pMaxVal) {
        mMaxVal = pMaxVal;
    }
        
    public void increment(int pIncrementNum) throws ApiLimitException {
        myVal += pIncrementNum; 

        if(myVal > mMaxVal) {
            LOGGER.info("ConnectorRateLimit: calls exceeded.  max is: " + mMaxVal + " curr val is: " + myVal);
            throw new ApiLimitException();
        }
    }
        
    public int getCurrCount() {
        return myVal; 
    }
        
    public int getMaxCount() {
        return mMaxVal;
    }
}
