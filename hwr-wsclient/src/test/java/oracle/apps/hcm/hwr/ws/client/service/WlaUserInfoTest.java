package oracle.apps.hcm.hwr.ws.client.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import oracle.apps.hcm.hwr.domain.webservices.LongSerializable;
import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.WlaUserInfo;
import oracle.apps.hcm.hwr.domain.webservices.WlaUserInfoRequest;
import oracle.apps.hcm.hwr.domain.webservices.WrsaMyEndorsements;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WlaUserInfoSerializer;
import oracle.apps.odin.datafilter.relationalquery.CompositeFilter;

import org.junit.Test;


public class WlaUserInfoTest extends WsClientTest {

    @Test
    public void test() {
        //TODO Replace with Global Profile ID of user we want to see endorsements for.
        Long myReq = 2775L;
        
        WlaUserInfoRequest myRequest = new WlaUserInfoRequest("ABRADLEY", false, true, false);
        
    //    WlaUserInfoRequest myRequest = new WlaUserInfoRequest("132910", true, true, true);
        
   //     WlaUserInfoRequest myRequest = new WlaUserInfoRequest("2910", true, true, false);
        
    //    WlaUserInfoRequest myRequest = new WlaUserInfoRequest("100004993512520", "FB", false, false);
        
   //     WlaUserInfoRequest myRequest = new WlaUserInfoRequest("k-J83f3bwp", "LI", false, false);
        
  //      WlaUserInfoRequest myRequest = new WlaUserInfoRequest("ABC123", "LI", true, true);
    
 //       runTest(myRequest);
        
    }
    
    @Test
    public void testGamification() {
   
        List<String> myGlobalIds = new ArrayList<String>();
        
        //get global profile Ids from server and add them here
        myGlobalIds.add("2910");
        myGlobalIds.add("3221");
        myGlobalIds.add("3222");
        
        WlaUserInfoRequest myRequest = new WlaUserInfoRequest(myGlobalIds);
        
        runTest(myRequest);
    }
    
   
        
    private void runTest(WlaUserInfoRequest myRequest) {
        
        CompositeFilter myFilter = myRequest.buildFilter();
        
        String myXmlStr = null;

        try {
            myXmlStr = myFilter.toXml();
        } catch (Exception e) {
            // @todo Auto-generated catch block
            e.printStackTrace();
        }

        String myResponseStr = mRequest.getUserProfile(mConnectionInfo, myXmlStr);
        
        WebservicesResult myResponse = new WebservicesResultSerializer().deserialize(myResponseStr);
        
        String myResponseSer = myResponse.getResultContent();
        
        WlaUserInfo myInfo = new WlaUserInfoSerializer().deserialize(myResponseSer);
        
        System.out.println("Done");
        
        System.out.println(myInfo.getGlobalProfileId());
        
    }

}
