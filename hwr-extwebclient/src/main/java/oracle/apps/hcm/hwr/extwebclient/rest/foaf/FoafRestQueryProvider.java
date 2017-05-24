package oracle.apps.hcm.hwr.extwebclient.rest.foaf;

import java.util.Map;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;

import org.apache.http.client.methods.HttpRequestBase;

public class FoafRestQueryProvider implements IRestQueryProvider {

	@Override 
	/*
	 * Special case - we do not have a "query" string to build, we are gathering
	 * RDF files from around the web.  Therefore, pass in the location of the RDF
	 * document, and use that for the request. 
	 * (non-Javadoc)
	 * @see oracle.apps.hcm.hwr.client.rest.IRestQueryProvider#findQuery(java.lang.String)
	 */
	public RestQuery findQuery(String pQueryName) {
	   // @todo don't use twitter... replace with foaf api calls.  just a stub for now to help compile.
            RestQuery myQuery =
                new RestQuery("", RestQuery.Method.GET, pQueryName);
            
	        return myQuery;
	}

	@Override
	public void executeCommonQueryTasks(HttpRequestBase pRequest) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<String, Object> procesQueryResponse(int statusCode,
			String pQueryResponse) {
	    return JsonHelper.jsonStringToMap(pQueryResponse);
	}

}
