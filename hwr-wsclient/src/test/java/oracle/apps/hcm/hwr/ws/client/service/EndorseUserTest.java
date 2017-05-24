package oracle.apps.hcm.hwr.ws.client.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EndorseUserTest extends WsClientTest {

	@Test
    public void testEndorseUser()  {
        String myInput = "test message from client: endorseUser";
        
        String myResponseStr = mRequest.endorseUser(mConnectionInfo, myInput);
        System.out.println("Response from server: "+myResponseStr);
    }

}
