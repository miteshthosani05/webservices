package oracle.apps.hcm.hwr.extwebclient.rest.craigslist;

import java.util.Map;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;

import org.apache.http.client.methods.HttpRequestBase;

public class CraigsListRestQueryProvider implements IRestQueryProvider {

    private String mUrl = "";

    public static final String CUSTOM_URL_PREFIX = "[CustomURL]=";

    public CraigsListRestQueryProvider(final String pUrl) {
        mUrl = pUrl;
    }

    @Override
    public RestQuery findQuery(String pQueryName) {

        if (pQueryName.equalsIgnoreCase("searchResumes")) {
            return new RestQuery(pQueryName, RestQuery.Method.GET, mUrl + "search/res");
        } else if (pQueryName.startsWith(CUSTOM_URL_PREFIX)) {
            String myCustomURL = pQueryName.substring(CUSTOM_URL_PREFIX.length());
            return new RestQuery(pQueryName, RestQuery.Method.GET, myCustomURL);
        }

        return null;
    }

    @Override
    public void executeCommonQueryTasks(HttpRequestBase pRequest) {

    }

    @Override
    public Map<String, Object> procesQueryResponse(int statusCode, String pQueryResponse) {
        return null;
    }
}
