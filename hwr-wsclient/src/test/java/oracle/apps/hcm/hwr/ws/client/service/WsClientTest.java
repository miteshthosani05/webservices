package oracle.apps.hcm.hwr.ws.client.service;

import oracle.apps.hcm.hwr.ws.client.WorkforceReputationPublicServiceImpl;
import oracle.apps.hcm.hwr.ws.client.common.ConnectionInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;

public class WsClientTest {

	protected String HWR_HOST = "slc05ggn.us.oracle.com";
	protected int HWR_PORT = 7021; 
	protected String KEYSTORE_PASSWORD = "Welcome1";
	
	protected String SERVICE_PATH = "/workforceReputationServiceCore/WorkforceReputationService?wsdl";
	protected String WS_USER_NAME = "APPLICATION_IMPLEMENTATION_CONSULTANT";
	protected String WS_USER_PASSWORD = "Welcome1";
	
    /** Logger. */
	protected static Log LOGGER = LogFactory.getLog(WsClientTest.class);
    
	protected ConnectionInfo mConnectionInfo;
	
	protected WorkforceReputationPublicServiceImpl mRequest;
	
	@Before
	public void setUp() {
		mConnectionInfo = new ConnectionInfo(HWR_HOST,
	            HWR_PORT, SERVICE_PATH, 
	                WS_USER_NAME, WS_USER_PASSWORD);
		
		mRequest = new WorkforceReputationPublicServiceMock.Builder("/scratch/jks/default-keystore.jks")
		.setUserName(WS_USER_NAME)
		.setPassword(WS_USER_PASSWORD)
		.setKeystorePassword(KEYSTORE_PASSWORD)
		.build();
	}
}
