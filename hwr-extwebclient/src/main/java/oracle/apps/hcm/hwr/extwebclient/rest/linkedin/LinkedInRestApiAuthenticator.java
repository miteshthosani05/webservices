/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.linkedin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestApiAuthenticator;
import oracle.apps.hcm.hwr.extwebclient.rest.OAuthSignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.RestExchangeTokensResponse;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngineException;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * @author Yenal Kal
 */
public class LinkedInRestApiAuthenticator implements IRestApiAuthenticator {

    /** The REST query engine */
    private final RestQueryEngine mEngine;

    /**
     * Instantiates a LinkedInRestApiAuthenticator instance.
     * @param pApiKey LinkedIn API key.
     * @param pSecretKey LinkedIn API secret key.
     */
    public LinkedInRestApiAuthenticator(String pApiKey, String pSecretKey) {

        if (pApiKey == null || pApiKey.isEmpty()) {
            throw new IllegalArgumentException("pApiKey cannot be empty or null.");
        }

        // Create the engine to execute the exchange token query
        mEngine =
            new RestQueryEngine(new LinkedInQueryProvider(), new OAuthSignatureProvider(pApiKey,
                pSecretKey));
    }

    /*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.client.rest.IRestApiAuthenticator#exchangeTokens(java.lang.String,
     * java.lang.String)
     */
    @Override
    public RestExchangeTokensResponse exchangeTokens(String pUserId, String pShortLivedAccessToken)
        throws RestQueryEngineException {

        Map<String, String> myPostParams = new HashMap<String, String>();

        // Set the short lived access token
        myPostParams.put("xoauth_oauth2_access_token", pShortLivedAccessToken);

        RestQuerySimpleResult myResult =
            mEngine.runSimpleQuery("getAccessToken", null, null, null, myPostParams);

        List<NameValuePair> myParams = new ArrayList<NameValuePair>();
        URLEncodedUtils.parse(myParams, new Scanner(myResult.getResponse()), "UTF-8");

        String myOAuthToken = null;
        String myOAuthTokenSecret = null;
        String myOAuthProblem = null;

        for (NameValuePair myParam : myParams) {
            if (myParam.getName().equals("oauth_token")) {
                myOAuthToken = myParam.getValue();
            } else if (myParam.getName().equals("oauth_token_secret")) {
                myOAuthTokenSecret = myParam.getValue();
            } else if (myParam.getName().equals("oauth_problem")) {
                myOAuthProblem = myParam.getValue();
                break;
            }
        }

        if (myOAuthToken != null && myOAuthTokenSecret != null) {
            return new RestExchangeTokensResponse(myOAuthToken, myOAuthTokenSecret);
        }

        if (myOAuthProblem == null || myOAuthProblem.isEmpty()) {
            myOAuthProblem = "Unknown exception occured";
        }

        return new RestExchangeTokensResponse(myOAuthProblem);
    }

}
