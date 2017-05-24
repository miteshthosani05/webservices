package oracle.apps.hcm.hwr.ws.client.service;

import oracle.apps.hcm.hwr.domain.WrsaRecommender;
import oracle.apps.hcm.hwr.domain.webservices.StringPagingRequest;
import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.WrsaRecommenderCollection;
import oracle.apps.hcm.hwr.domain.webservices.serializer.StringPagingRequestSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaRecommenderCollectionSerializer;
import oracle.apps.odin.datafilter.relationalquery.PagingInfo;

import org.junit.Test;

public class SearchUserToEndorseTest extends WsClientTest {

	@Test
    public void testSearchUserToEndorse()  {
      //@TODO - replace with name of user we want to search for.    
      searchUserToEndorse("Emory Perez");
  }
  
  private WrsaRecommenderCollection searchUserToEndorse(String pSearchString) {
      StringPagingRequest myReq = new StringPagingRequest(pSearchString, new PagingInfo());
      
      String mySerReq = new StringPagingRequestSerializer().serialize(myReq);
      
      String myResponseStr = mRequest.searchUserToEndorse(mConnectionInfo, mySerReq);
      WebservicesResult myResponse = new WebservicesResultSerializer().deserialize(myResponseStr);
      
      String myResponseSer = myResponse.getResultContent();
      
      WrsaRecommenderCollection myWrsaResp = new WrsaRecommenderCollectionSerializer().deserialize(myResponseSer);
      
      for (WrsaRecommender myRecommender : myWrsaResp.getWrsaRecommenders()) {
          System.out.println(" GlobalID: " + myRecommender.getGlobalProfileId() + " emails: " + myRecommender.getEmailAddresses() 
              + " Title: " + myRecommender.getTitle() + " Name: " + myRecommender.getName());
      }
      
      return myWrsaResp;
    }

}
