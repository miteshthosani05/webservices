/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.facebook;

import java.util.HashMap;
import java.util.Map;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery.Method;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * The IRESTQueryProvider implementation for Facebook
 * @author Yenal Kal
 * @author Ibtisam Haque
 */
public class FacebookRestQueryProvider implements IRestQueryProvider {

    public static final String GET_ACCESS_TOKEN = "getAccessToken";

    public static final String GET_PROFILE = "getProfile";

    public static final String GET_PROFILE_BY_ID = "getProfileById";

    public static final String GET_USER_POSTS = "getUserPosts";

    public static final String RUN_FQL = "runFql";

    public static final String RUN_BATCHED_QUERY = "runBatchedQuery";

    public static final String SEARCH_USERS = "searchUsers";

    public static final String GET_POSTS = "getPosts";

    public static final String GET_USER_LIKES = "getUserLikes";

    public static final String GET_USER_COMMENTS = "getUserComments";

    public static final String GET_FRIENDS = "getFriends";

    public static final String GET_GROUPS = "getGroups";

    public static final String PUBLISH_POST_BODY_FORMAT =
        "{\"method\":\"POST\",\"relative_url\":\"%s/feed\",\"body\":\""
            + "message=%s&picture=%s&link=%s&name=%s&caption=%s" + ""
                + "&description=%s&place=%s&tags=%s\"},";

    private static final String API_BASE_URL = "https://graph.facebook.com/";

    private static final String API_VERSION = "v2.3";

    private final Map<String, RestQuery> mQueriesByName = new HashMap<String, RestQuery>();

    public FacebookRestQueryProvider() {
        this(API_BASE_URL, API_VERSION);
    }

    public FacebookRestQueryProvider(final String pApiBaseUrl, final String pApiVersion) {
        initQueriesByName(pApiBaseUrl, pApiVersion);
    }

    private void initQueriesByName(final String pApiBaseUrl, final String pApiVersion) {
        final String myVersionedApiUrl = pApiBaseUrl + pApiVersion + "/";
        mQueriesByName.put(GET_ACCESS_TOKEN, new RestQuery(GET_ACCESS_TOKEN, Method.GET, pApiBaseUrl,
            "oauth/access_token"));
        mQueriesByName.put(GET_PROFILE, new RestQuery(GET_PROFILE, Method.GET, myVersionedApiUrl, "me"));
        mQueriesByName.put(GET_PROFILE_BY_ID, new RestQuery(GET_PROFILE_BY_ID, Method.GET, myVersionedApiUrl));
        mQueriesByName.put(GET_POSTS, new RestQuery(GET_POSTS, Method.GET, myVersionedApiUrl, "me/posts"));
        mQueriesByName.put(RUN_FQL, new RestQuery(RUN_FQL, Method.GET, pApiBaseUrl, "fql"));
        mQueriesByName.put(RUN_BATCHED_QUERY, new RestQuery(RUN_BATCHED_QUERY, Method.POST, myVersionedApiUrl));
        mQueriesByName.put(SEARCH_USERS, new RestQuery(SEARCH_USERS, Method.GET, myVersionedApiUrl, "search"));
        mQueriesByName.put(GET_USER_POSTS, new RestQuery(GET_USER_POSTS, Method.GET, myVersionedApiUrl));
        mQueriesByName.put(GET_USER_COMMENTS, new RestQuery(GET_USER_COMMENTS, Method.GET, myVersionedApiUrl));
        mQueriesByName.put(GET_USER_LIKES, new RestQuery(GET_USER_LIKES, Method.GET, myVersionedApiUrl));
        mQueriesByName.put(GET_FRIENDS, new RestQuery(GET_FRIENDS, Method.GET, myVersionedApiUrl));
        mQueriesByName.put(GET_GROUPS, new RestQuery(GET_GROUPS, Method.GET, myVersionedApiUrl));
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
