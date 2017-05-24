/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.twitter;

import java.util.HashMap;
import java.util.Map;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery.Method;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Query provider for the Twitter API
 * @author Yenal Kal
 * @author Ibtisam Haque
 */
public class TwitterRestQueryProvider implements IRestQueryProvider {

    public static final String GET_PROFILE = "getProfile";

    public static final String GET_CURRENT_PROFILE = "getCurrentProfile";

    public static final String GET_TWEETS = "getTweets";

    public static final String GET_FOLLOWER_IDS = "getFollowerIDs";

    public static final String GET_FRIEND_IDS = "getFriendIDs";

    public static final String GET_PROFILES = "getProfiles";

    public static final String SEARCH_USERS = "searchUsers";

    public static final String GET_ACCESS_TOKEN = "getAccessToken";

    public static final String GET_REQUEST_TOKEN = "getRequestToken";

    public static final String GET_OAUTH2_TOKEN = "getOAuth2Token";

    private static final String BASE_URL = "https://api.twitter.com/";

    private static final String API_VERSION = "1.1";

    private final Map<String, RestQuery> mQueriesByName = new HashMap<String, RestQuery>();

    public TwitterRestQueryProvider() {
        this(BASE_URL, API_VERSION);
    }

    public TwitterRestQueryProvider(final String pApiBaseUrl, final String pApiVersion) {
        initQueriesByNameMap(pApiBaseUrl, pApiVersion);
    }

    private void initQueriesByNameMap(final String pApiBaseUrl, final String pApiVersion) {
        final String myVersionedApiUrl = pApiBaseUrl + pApiVersion + "/";
        mQueriesByName.put(GET_PROFILE, new RestQuery(GET_PROFILE, Method.GET, myVersionedApiUrl, "users/show.json"));
        mQueriesByName.put(GET_CURRENT_PROFILE, new RestQuery(GET_CURRENT_PROFILE, Method.GET, myVersionedApiUrl,
            "account/verify_credentials.json"));
        mQueriesByName.put(GET_TWEETS, new RestQuery(GET_TWEETS, Method.GET, myVersionedApiUrl,
            "statuses/user_timeline.json"));
        // This is rather ugly, RestQuery should be refactored so that it only contains one params
        // map.
        mQueriesByName.get(GET_TWEETS).getGetParams().put("include_rts", "1");
        mQueriesByName.get(GET_TWEETS).getGetParams().put("trim_user", "1");
        mQueriesByName.get(GET_TWEETS).getGetParams().put("count", "200");
        mQueriesByName.put(GET_FOLLOWER_IDS, new RestQuery(GET_FOLLOWER_IDS, Method.GET, myVersionedApiUrl,
            "followers/ids.json"));
        mQueriesByName.put(GET_FRIEND_IDS, new RestQuery(GET_FRIEND_IDS, RestQuery.Method.GET, myVersionedApiUrl,
            "friends/ids.json"));
        mQueriesByName.put(GET_PROFILES, new RestQuery(GET_PROFILES, RestQuery.Method.POST, myVersionedApiUrl,
            "users/lookup.json"));
        mQueriesByName.put(SEARCH_USERS, new RestQuery(SEARCH_USERS, RestQuery.Method.GET, myVersionedApiUrl,
            "users/search.json"));
        mQueriesByName.put(GET_REQUEST_TOKEN, new RestQuery(GET_REQUEST_TOKEN, RestQuery.Method.POST, pApiBaseUrl,
            "oauth/request_token"));
        mQueriesByName.put(GET_ACCESS_TOKEN, new RestQuery(GET_ACCESS_TOKEN, RestQuery.Method.POST, pApiBaseUrl,
            "oauth/access_token"));
        mQueriesByName.put(GET_OAUTH2_TOKEN, new RestQuery(GET_OAUTH2_TOKEN, RestQuery.Method.POST, pApiBaseUrl,
            "oauth2/token"));
    }

    @Override
    public RestQuery findQuery(final String pQueryName) {
        return mQueriesByName.get(pQueryName);
    }

    @Override
    public void executeCommonQueryTasks(final HttpRequestBase pRequest) {}

    @Override
    public Map<String, Object> procesQueryResponse(final int statusCode, final String pQueryResponse) {
        return JsonHelper.jsonStringToMap(pQueryResponse);
    }

}
