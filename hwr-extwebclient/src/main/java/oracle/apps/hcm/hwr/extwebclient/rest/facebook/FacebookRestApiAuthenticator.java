/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.facebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestApiAuthenticator;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.OAuthException;
import oracle.apps.hcm.hwr.extwebclient.rest.RestExchangeTokensResponse;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngineException;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryMapResult;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * @author Yenal Kal
 */
public class FacebookRestApiAuthenticator implements IRestApiAuthenticator {

    /** The REST query engine */
    private final RestQueryEngine mEngine;

    /** The API key */
    private final String mApiKey;

    /** The API secret */
    private final String mSecretKey;

    /**
     * Instantiates a FacebookRestApiAuthenticator instance.
     * @param pApiKey Facebook API key.
     * @param pSecretKey Facebook API secret key.
     * @param pProxyUrl The proxy URL.
     * @param pProxyPort The proxy port.
     */
    public FacebookRestApiAuthenticator(final String pApiKey, final String pSecretKey) {

        // Create the engine to execute the exchange token query
        mEngine = new RestQueryEngine(new FacebookRestQueryProvider(), null);

        if (pApiKey == null || pApiKey.isEmpty()) {
            throw new IllegalArgumentException("pApiKey cannot be empty or null.");
        }

        mApiKey = pApiKey;
        mSecretKey = pSecretKey;
    }

    /**
     * exchanges a facebook generated code for the short lived access token.
     * @param pRedirectUri
     * @param pFBGeneratedCode
     * @return
     */
    public RestExchangeTokensResponse getShortLivedAccessToken(final String pRedirectUri, final String pFBGeneratedCode) {

        final Map<String, String> myGetParams = new HashMap<String, String>(4);
        myGetParams.put("client_id", mApiKey);
        myGetParams.put("redirect_uri", pRedirectUri);
        myGetParams.put("client_secret", mSecretKey);
        myGetParams.put("code", pFBGeneratedCode);

        // Execute the query
        final RestQuerySimpleResult myResult = mEngine.runSimpleQuery("getAccessToken", null, null, myGetParams, null);

        return parseAccessTokenResponse(myResult);
    }

    /*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.client.rest.IRestApiAuthenticator#exchangeTokens(java.lang.String,
     * java.lang.String)
     */
    @Override
    public RestExchangeTokensResponse exchangeTokens(final String pUserId, final String pShortLivedAccessToken)
        throws RestQueryEngineException {

        // Build the query string
        final Map<String, String> myGetParams = new HashMap<String, String>(4);

        myGetParams.put("client_id", mApiKey);
        myGetParams.put("client_secret", mSecretKey);

        myGetParams.put("grant_type", "fb_exchange_token");
        myGetParams.put("fb_exchange_token", pShortLivedAccessToken);

        // Execute the query
        final RestQuerySimpleResult myResult = mEngine.runSimpleQuery("getAccessToken", null, null, myGetParams, null);

        return parseAccessTokenResponse(myResult);
    }

    private RestExchangeTokensResponse parseAccessTokenResponse(final RestQuerySimpleResult myResult) {
        String myErrorMessage = null;
        if (myResult.getStatusCode() == 200) {

            final List<NameValuePair> myParams = new ArrayList<NameValuePair>();
            URLEncodedUtils.parse(myParams, new Scanner(myResult.getResponse()), "UTF-8");

            String myAccessToken = null;

            for (final NameValuePair myParam : myParams) {
                if (myParam.getName().equals("access_token")) {
                    myAccessToken = myParam.getValue();
                    break;
                }
            }

            if (myAccessToken != null) {
                return new RestExchangeTokensResponse(myAccessToken, null);
            }
        } else {
            Map<String, Object> myResultMap;
            try {
                myResultMap = JsonHelper.jsonStringToMap(myResult.getResponse());
            } catch (final Throwable e) {
                throw new RestQueryEngineException(e.getMessage(), e);
            }
            myErrorMessage = (myResultMap.get("error") != null ? myResultMap.get("error").toString() : null);
        }

        if (myErrorMessage == null || myErrorMessage.isEmpty()) {
            myErrorMessage = "Unknown exception occurred.";
        }

        return new RestExchangeTokensResponse(myErrorMessage);
    }

    /**
     * Returns the profile ID for the access token given.
     * @param pAccessToken The access token.
     * @return The profile ID for the access token given.
     */
    public String getProfileID(final String pAccessToken) throws OAuthException {
        final GraphAPIHelper myAPI = new GraphAPIHelper();

        if (pAccessToken == null || pAccessToken.isEmpty()) {
            throw new IllegalArgumentException("pAccessToken can not be null or empty string.");
        }

        final RestQueryMapResult myProfileResult = myAPI.getProfile(null, pAccessToken, true);

        if (myProfileResult == null || myProfileResult.getResultMap() == null) {
            throw new OAuthException("myAPI.getProfileLight returned a null response. This is not expected.");
        }

        final Map<String, Object> myResultMap = JsonHelper.flattenJsonObject(myProfileResult.getResultMap(), null);

        if (!myResultMap.containsKey("id")) {
            final String pGraphAPIErrorMessage = (String) myResultMap.get("error#message");
            String pExceptionMessage =
                "Failed to get profile ID. Http status code: " + myProfileResult.getStatusCode() + ".";

            if (pGraphAPIErrorMessage != null) {
                pExceptionMessage += " " + pGraphAPIErrorMessage;
            }

            throw new OAuthException(pExceptionMessage);
        }

        return (String) myResultMap.get("id");
    }
}
