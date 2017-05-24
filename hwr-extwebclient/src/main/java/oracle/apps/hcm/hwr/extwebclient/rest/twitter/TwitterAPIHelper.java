/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.twitter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQuerySignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.OAuthException;
import oracle.apps.hcm.hwr.extwebclient.rest.OAuthSignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngineException;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryMapResult;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;
import oracle.apps.hcm.hwr.extwebclient.rest.apiuse.ConnectorApiUseage;
import oracle.apps.hcm.hwr.extwebclient.rest.oauth.OAuthAccessTokenResponse;
import oracle.apps.hcm.hwr.extwebclient.rest.oauth.OAuthRequestTokenResponse;
import oracle.apps.hcm.hwr.extwebclient.rest.query.QueryConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * @author Yenal Kal
 * @author Ibtisam Haque
 * @author pglogows
 */
public class TwitterAPIHelper {

    /** REST query engine */
    private final RestQueryEngine mQueryEngine;

    /** The logger instance */
    private static final Log LOGGER = LogFactory.getLog(TwitterAPIHelper.class);

    private ConnectorApiUseage mApiUseage;

    /**
     * Sets the application related data to make API calls successfully.
     * @param pApiKey API key for the application.
     * @param pApiSecret API secret for the application.
     */
    public TwitterAPIHelper(final String pApiKey, final String pApiSecret) {
        mQueryEngine =
            new RestQueryEngine(new TwitterRestQueryProvider(), new OAuthSignatureProvider(pApiKey, pApiSecret));
    }

    public void setApiUseageClass(final ConnectorApiUseage pApiUseage) {
        mApiUseage = pApiUseage;
        mQueryEngine.setConnectorApiUseage(mApiUseage);
    }

    /**
     * Returns an OAuthRequestTokenUrlResponse instance that contains the request token URL, auth
     * token and secret.
     * @return An OAuthRequestTokenUrlResponse instance that contains the request token URL, auth
     *         token and secret.
     * @throws OAuthException
     */
    public OAuthRequestTokenResponse getRequestToken(final String pCallbackUrl) throws OAuthException {
        setOAuthCallback(pCallbackUrl);

        final RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery("getRequestToken");

        if (myResult == null) {
            throw new OAuthException(
                "mQueryEngine.runSimpleQuery(\"getRequestToken\") returned null. This should not have happened. Please investigate.");
        }

        if (myResult.getStatusCode() != 200) {
            throw new OAuthException(
                "Unexpected status code returned from Twitter while getting the request token. Status Code: "
                    + myResult.getStatusCode());
        }

        // The list of params in the query string returned
        final List<NameValuePair> myParams = new ArrayList<NameValuePair>();

        // Parse the params
        URLEncodedUtils.parse(myParams, new Scanner(myResult.getResponse()), "UTF-8");

        // Found in param oauth_callback_confirmed
        String myCallbackConfirmed = null;

        // Found in param oauth_token
        String myAuthToken = null;

        // Found in param oauth_token_secret
        String myAuthTokenSecret = null;

        for (final NameValuePair myParam : myParams) {
            if (myParam.getName().equals("oauth_token")) {
                myAuthToken = myParam.getValue();
            } else if (myParam.getName().equals("oauth_token_secret")) {
                myAuthTokenSecret = myParam.getValue();
            } else if (myParam.getName().equals("oauth_callback_confirmed")) {
                myCallbackConfirmed = myParam.getValue();
            }
        }

        if (myCallbackConfirmed == null || myAuthToken == null || myAuthTokenSecret == null) {
            throw new OAuthException(
                "Get request token URL response from Twitter did not contain all required params: "
                    + myResult.getResponse());
        }

        return new OAuthRequestTokenResponse(myAuthToken, myAuthTokenSecret, myCallbackConfirmed);
    }

    /**
     * Returns the access tokens.
     * @param pAuthToken The auth token used to get the access tokens.
     * @param pAuthTokenSecret The auth token secret used to get the access tokens.
     * @param pVerifier The code used to verify if we were the original requestee for the access
     *        tokens.
     * @return The access tokens.
     */
    public OAuthAccessTokenResponse getAccessTokens(final String pAuthToken, final String pAuthTokenSecret,
        final String pVerifier) {

        setAccessTokenAndSecret(pAuthToken, pAuthTokenSecret);

        // Set the verifier
        setVerifier(pVerifier);

        final RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery("getAccessToken");

        if (myResult == null) {
            throw new OAuthException(
                "mQueryEngine.runSimpleQuery(\"getAccessToken\") returned null. This should not have happened. Please investigate.");
        }

        if (myResult.getStatusCode() != 200) {
            throw new OAuthException(
                "Unexpected status code returned from Twitter while getting the access tokens. Status Code: "
                    + myResult.getStatusCode() + ". Response: " + myResult.getResponse());
        }

        // The list of params in the query string returned
        final List<NameValuePair> myParams = new ArrayList<NameValuePair>();

        // Parse the params
        URLEncodedUtils.parse(myParams, new Scanner(myResult.getResponse()), "UTF-8");

        // Found in param oauth_token
        String myAccessToken = null;

        // Found in param oauth_token_secret
        String myAccessTokenSecret = null;

        for (final NameValuePair myParam : myParams) {
            if (myParam.getName().equals("oauth_token")) {
                myAccessToken = myParam.getValue();
            } else if (myParam.getName().equals("oauth_token_secret")) {
                myAccessTokenSecret = myParam.getValue();
            }

            if (myAccessToken != null && myAccessTokenSecret != null) {
                break;
            }
        }

        if (myAccessToken == null || myAccessTokenSecret == null) {
            throw new OAuthException("Get access token response from Twitter did not contain all required params: "
                + myResult.getResponse());
        }

        return new OAuthAccessTokenResponse(myAccessToken, myAccessTokenSecret);
    }

    /**
     * Returns the bearer Token as part of Application level credentials. Assumption: This method
     * assumes that pEncodedCustomerKeyAndSecret is neither null nor empty. This validation is
     * performed by the caller of this method.
     * @param pEncodedCustomerKeyAndSecret Encoded consumer key and secret key
     * @return
     * @throws RestQueryEngineException
     */
    public RestQuerySimpleResult getOAuth2(final String pCustomerKey, final String pCustomerSecret,
        final String pEncodedCustomerKeyAndSecret) throws RestQueryEngineException {
        LOGGER.info("TwitterAPIHelper: getOAuth2(): Entering method.");

        final Map<String, String> myHeaderFields = new LinkedHashMap<String, String>();
        myHeaderFields.put("Authorization", "Basic " + pEncodedCustomerKeyAndSecret);
        myHeaderFields.put("Content-Type", TwitterConstants.OAUTH2_TOKEN_CONTENT_TYPE);

        final Map<String, String> myPostParams = new HashMap<String, String>(1);
        myPostParams.put("grant_type", TwitterConstants.OAUTH2_TOKEN_GRANT_TYPE);

        final RestQueryEngine mQueryEngineTemp =
            new RestQueryEngine(new TwitterRestQueryProvider(), new OAuthSignatureProvider(pCustomerKey,
                pCustomerSecret));
        mQueryEngineTemp.setSignatureProvider(null);

        final RestQuerySimpleResult myResult =
            mQueryEngineTemp.runSimpleQuery(TwitterConstants.QUERY_STRING_OAUTH2, null, myHeaderFields, null,
                myPostParams, null);

        if (TwitterConstants.T0_DEBUG)
            LOGGER.debug("TwitterAPIHelper: getOAuth2(): myResult: " + myResult.toString());

        return myResult;

    }

    /**
     * Returns the profile information for the owner of the access token.
     * @param pProfileID The profile ID.
     * @param pAccessToken The access token for private data access.
     * @param pAccessTokenSecret The access token secret for private data access.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     * @throws RestQueryEngineException
     */
    public RestQuerySimpleResult getProfile(final String pProfileID, final String pAccessToken,
        final String pAccessTokenSecret) throws RestQueryEngineException {

        // Set parameters for OAuth
        setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

        final Map<String, String> myGetParams = new HashMap<String, String>(2);
        myGetParams.put("user_id", pProfileID);
        myGetParams.put("include_entities", "false");

        return mQueryEngine.runSimpleQuery(TwitterRestQueryProvider.GET_PROFILE, null, null, myGetParams, null);
    }

    /**
     * Returns the profile information for the screen name passed in.
     * @param pScreenName The screen name.
     * @param pAccessToken The access token for private data access.
     * @param pAccessTokenSecret The access token secret for private data access.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     * @throws RestQueryEngineException
     */
    public RestQuerySimpleResult getProfileByScreenName(final String pScreenName, final String pAccessToken,
        final String pAccessTokenSecret) throws RestQueryEngineException {

        // Set parameters for OAuth
        setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

        final Map<String, String> myGetParams = new HashMap<String, String>(2);
        myGetParams.put("screen_name", pScreenName);
        myGetParams.put("include_entities", "false");

        return mQueryEngine.runSimpleQuery(TwitterRestQueryProvider.GET_PROFILE, null, null, myGetParams, null);
    }

    /**
     * Returns the profile information for the owner of the access token.
     * @param pAccessToken
     * @param pAccessTokenSecret
     * @return The id of the user currently logged
     * @throws OAuthException
     */
    public String getCurrentProfileId(final String pAccessToken, final String pAccessTokenSecret)
        throws RestQueryEngineException {

        // Set parameters for OAuth
        setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

        final RestQuerySimpleResult result = mQueryEngine.runSimpleQuery(TwitterRestQueryProvider.GET_CURRENT_PROFILE);

        final Map<String, Object> resultMap = JsonHelper.jsonStringToMap(result.getResponse());

        if (!resultMap.containsKey("id")) {
            final String exceptionMessage = "Failed to get profile ID. Http status code: " + result.getResponse();
            throw new OAuthException(exceptionMessage);
        }

        return resultMap.get("id").toString();
    }

    /**
     * Returns tweets for particular user. Map in {@link RestQueryMapResult} has only one mapping:
     * items --> List<Map<String, Object>> that contains tweets
     * @param pProfileID user to retrieve tweets.
     * @param pAccessToken  access token.
     * @param pAccessTokenSecret access token secret.
     * @param pMaxID max tweet id ( inclusive ). It is used to paging result
     * @param pSinceID min tweet id ( include this one ). Used for paging result.
     * @return {@link RestQueryMapResult} contains tweet ( items --> Tweets )
     * @throws RestQueryEngineException
     */
    public RestQueryMapResult getTweets(final String pProfileID, final String pAccessToken,
        final String pAccessTokenSecret, final Long pMaxID, final Long pSinceID) throws RestQueryEngineException {
        // Set parameters for OAuth
        setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);
        QueryConfig queryConfig = new QueryConfig();
        queryConfig.addGetParam("user_id", pProfileID);
        
        if (pMaxID != null && pMaxID != Long.MAX_VALUE) {
            queryConfig.addGetParam("max_id", pMaxID.toString());
        }

        if (pSinceID != null && pSinceID != Long.MAX_VALUE) {
            queryConfig.addGetParam("since_id", pSinceID.toString());
        }
        return mQueryEngine.runListedMapQuery(TwitterRestQueryProvider.GET_TWEETS, queryConfig);
    }

    /**
     * Returns the follower IDs for the owner of the access token.
     * @param pProfileID The profile ID.
     * @param pCursorValue The cursor for the next portion of collection.
     * @param pAccessToken The access token for private data access.
     * @param pAccessTokenSecret The access token secret for private data access.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     * @throws RestQueryEngineException
     */
    public RestQueryMapResult getFollowerIDs(final String pProfileID, final String pCursor, final String pAccessToken,
        final String pAccessTokenSecret) throws RestQueryEngineException {
        // Set parameters for OAuth
        setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

        final QueryConfig pQueryConfig = getProfileIdConfig(pProfileID, pCursor);

        return mQueryEngine.runMapQuery(TwitterRestQueryProvider.GET_FOLLOWER_IDS, pQueryConfig);
    }

    /**
     * Returns the friend IDs for the owner of the access token.
     * @param pProfileID The profile ID.
     * @param pCursorValue cursor for next collection
     * @param pAccessToken The access token for private data access.
     * @param pAccessTokenSecret The access token secret for private data access.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     * @throws RestQueryEngineException
     */
    public RestQueryMapResult getFriendIDs(final String pProfileID, final String pCursor, final String pAccessToken,
        final String pAccessTokenSecret) throws RestQueryEngineException {
        // Set parameters for OAuth
        setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

        final QueryConfig pQueryConfig = getProfileIdConfig(pProfileID, pCursor);

        return mQueryEngine.runMapQuery(TwitterRestQueryProvider.GET_FRIEND_IDS, pQueryConfig);
    }

    private QueryConfig getProfileIdConfig(final String pProfileID, final String pCursor) {
        final QueryConfig pQueryConfig = new QueryConfig();
        pQueryConfig.addGetParam(TwitterParams.USER_ID, pProfileID);
        if (pCursor != null) {
            pQueryConfig.addGetParam(TwitterParams.CURSOR, pCursor);
        }
        return pQueryConfig;
    }

    /**
     * Returns the friend IDs for the owner of the access token. You can at most supply 100 profile
     * IDs.
     * @param pAccessToken The access token for private data access.
     * @param pAccessTokenSecret The access token secret for private data access.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     * @throws RestQueryEngineException
     */
    public RestQuerySimpleResult getProfiles(final String pAccessToken, final String pAccessTokenSecret,
        final Collection<Number> pProfileIDs) throws RestQueryEngineException {
        // Set parameters for OAuth
        setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

        if (pProfileIDs == null || pProfileIDs.size() == 0) {
            throw new IllegalArgumentException("pProfileIDs must contain at least one profile ID.");
        }

        if (pProfileIDs.size() > 100) {
            throw new IllegalArgumentException("You cannot supply more than 100 profile IDs.");
        }

        final StringBuilder mySB = new StringBuilder();
        for (final Number myProfileID : pProfileIDs) {
            if (mySB.length() > 0) {
                mySB.append(",");
            }
            mySB.append(myProfileID);
        }

        final Map<String, String> myPostParams = new HashMap<String, String>(1);
        myPostParams.put("user_id", mySB.toString());
        myPostParams.put("include_entities", "0");

        return mQueryEngine.runSimpleQuery(TwitterRestQueryProvider.GET_PROFILES, null, null, null, myPostParams);
    }

    /**
     * Searches and return Twitter users matching the query supplied.
     * @param pQuery The query used for the search.
     * @param pPageIndex 1 based paged index.
     * @param pCountPerPage The number of users per page.
     * @param pAccessToken The access token used to do the search.
     * @param pAccessTokenSecret The access token secret used to do the search.
     * @return The Twitter users matching the query supplied.
     * @throws RestQueryEngineException
     */
    public RestQuerySimpleResult searchUsers(final String pQuery, final int pPageIndex, final int pCountPerPage,
        final String pAccessToken, final String pAccessTokenSecret) throws RestQueryEngineException {
        // Set parameters for OAuth
        setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

        if (pPageIndex < 1) {
            throw new IllegalArgumentException("pPageIndex can not be less than 1.");
        }

        if (pCountPerPage > 20) {
            throw new IllegalArgumentException("pCountPerPage can not be greater than 20.");
        }

        if (pCountPerPage < 1) {
            throw new IllegalArgumentException("pCountPerPage can not be less than 1.");
        }

        if (pCountPerPage > 20) {
            throw new IllegalArgumentException("pCountPerPage can not be greater than 20.");
        }

        final Map<String, String> myGetParams = new HashMap<String, String>(2);
        myGetParams.put("q", pQuery);
        myGetParams.put("page", Integer.toString(pPageIndex));
        myGetParams.put("count", Integer.toString(pCountPerPage));
        myGetParams.put("include_entities", "false");

        return mQueryEngine.runSimpleQuery(TwitterRestQueryProvider.SEARCH_USERS, null, null, myGetParams, null);
    }

    /**
     * Sets the access token and token secret required for accessing private data.
     * @param pAccessToken The access token.
     * @param pTokenSecret The token secret.
     */
    private void setAccessTokenAndSecret(final String pAccessToken, final String pTokenSecret) {
        final IRestQuerySignatureProvider myProvider = mQueryEngine.getSignatureProvider();

        if (myProvider != null) {
            myProvider.setParam("accessToken", pAccessToken);
            myProvider.setParam("tokenSecret", pTokenSecret);
        }
    }

    /**
     * Sets the verifier required for authentication.
     * @param pVerifier The verifier.
     */
    private void setVerifier(final String pVerifier) {
        final IRestQuerySignatureProvider myProvider = mQueryEngine.getSignatureProvider();

        if (myProvider != null) {
            myProvider.setParam("verifier", pVerifier);
        }
    }

    /**
     * Sets the verifier required for authentication.
     * @param pVerifier The verifier.
     */
    private void setOAuthCallback(final String pCallback) {
        final IRestQuerySignatureProvider myProvider = mQueryEngine.getSignatureProvider();

        if (myProvider != null) {
            myProvider.setParam("oauth_callback", pCallback);
        }
    }
}
