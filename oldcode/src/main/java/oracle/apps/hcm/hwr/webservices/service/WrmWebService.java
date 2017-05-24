package oracle.apps.hcm.hwr.webservices.service;

import java.util.HashMap;
import java.util.Map;

import oracle.apps.hcm.hwr.webservices.server.AbstractWebServiceRequestFactory;
import oracle.apps.hcm.hwr.webservices.server.IWebServiceFactory;

import org.apache.abdera.model.Feed;
/**
 * The WrmWsClient sends a request to the server, to the appropriate webservice sending
 * in the appropriate parameters. The WsClient first calls apache axis which then delegates to 
 * one of these methods. Note the parameters are case sensitive. 
 * 
 * @author Mr. Nima Falaki - nima.falaki@oracle.com
 *
 */
public class WrmWebService {
	
	/**
	 * Need to keep this constructor public
	 * so axis can call it
	 */
	public WrmWebService() {
		
	}
	
	/**
	 * Authorization webservice
	 * @return
	 */
	public Feed Authorize()  {  
		//add the parameters that you need in this map
		Map<String, String> myParamValueMap = new HashMap<String, String>();
		//Be sure to always follow this pattern. Do not use anything else.
		IWebServiceFactory myFactory = AbstractWebServiceRequestFactory.getAuthorizeRequest("1.0.0");
		return myFactory.handleWebServiceRequest(myParamValueMap);
	}
}
