package oracle.apps.hcm.hwr.extwebclient.rest.craigslist;

import java.util.HashMap;
import java.util.Map;

import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;

public class CraigsListApiHelper {

    /** REST query engine */
    private final RestQueryEngine mQueryEngine;

    public CraigsListApiHelper(final String pUrl) {
        mQueryEngine = new RestQueryEngine(new CraigsListRestQueryProvider(pUrl));
    }

    /**
     * Executes a resume search query and returns the contents.
     * @param pQuery The search query.
     * @param pIndex The optional search index for paging.
     * @return Resume search query result.
     */
    public RestQuerySimpleResult searchResumes(String pQuery, Integer pIndex) {
        Map<String, String> myGetParams = new HashMap<String, String>();
        myGetParams.put("srchType", "A");
        myGetParams.put("query", "\"" + pQuery + "\"");
        if (pIndex != null) {
            myGetParams.put("s", pIndex.toString());
        }
        return mQueryEngine.runSimpleQuery("searchResumes", null, null, myGetParams, null);
    }

    public RestQuerySimpleResult getCustomURLContents(String pURL) {
        return mQueryEngine.runSimpleQuery(CraigsListRestQueryProvider.CUSTOM_URL_PREFIX + pURL);
    }
}
