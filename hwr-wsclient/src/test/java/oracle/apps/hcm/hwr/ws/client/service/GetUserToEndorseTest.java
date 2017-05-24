package oracle.apps.hcm.hwr.ws.client.service;

import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.WrsaEndorseUser;
import oracle.apps.hcm.hwr.domain.webservices.WrsaEndorseUserRequest;
import oracle.apps.hcm.hwr.domain.webservices.WrsaUserEndorsement;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaEndorseUserRequestSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaEndorseUserSerializer;

import org.junit.Test;

public class GetUserToEndorseTest extends WsClientTest {

    //@TODO - replace with Global Profile IDs from the system.
    private static long mIdOfLoggedInUser = 2775L;
    private static long mIdOfUserToEndorse = 9796L;
    
	@Test
    public void testGetEndorseUser()  {
	    getEndorseUser(mIdOfLoggedInUser, mIdOfUserToEndorse);
	}
	
	 public WrsaEndorseUser getEndorseUser(long pIdOfUser, long pIdOfUserToEndorse) {
	        WrsaEndorseUserRequest myReq = new WrsaEndorseUserRequest(pIdOfUser, pIdOfUserToEndorse);
	        
	        String mySerReq = new WrsaEndorseUserRequestSerializer().serialize(myReq);
	        
	        String myResponseStr = mRequest.getUserToEndorse(mConnectionInfo, mySerReq);
	        WebservicesResult myResponse = new WebservicesResultSerializer().deserialize(myResponseStr);
	        
	        String myResponseSer = myResponse.getResultContent();
	        
	        WrsaEndorseUser myWrsaResp = new WrsaEndorseUserSerializer().deserialize(myResponseSer);
	        
	        for (WrsaUserEndorsement myEndorsement : myWrsaResp.getEndorsements()) {
	            System.out.println(myEndorsement.getEndorsement() + " " + myEndorsement.getIsEndorsed() + " " + myEndorsement.getId());
	        }
	        

	        return myWrsaResp;
	    }

}
