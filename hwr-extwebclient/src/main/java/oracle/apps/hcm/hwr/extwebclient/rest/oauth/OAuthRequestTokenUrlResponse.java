/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.oauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Yenal Kal
 */
public class OAuthRequestTokenUrlResponse {

    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(OAuthRequestTokenUrlResponse.class);

    /** Request token base URL. This URL will not work without appending the auth token. */
    private String mRequestTokenBaseUrl;

    /**
     * The query string parameter name to append the auth token to the base URL to form the full
     * URL.
     */
    private String mQueryStringParamNameForAuthToken;

    /** The auth token. Appended to the base URL. */
    private String mAuthToken;

    /** The auth token secret. Used for getting the access token once the user authorizes the app. */
    private String mAuthTokenSecret;

    public OAuthRequestTokenUrlResponse(String pRequestTokenBaseUrl,
        String pQueryStringParamNameForAuthToken, String pAuthToken, String pAuthTokenSecret) {
        mRequestTokenBaseUrl = pRequestTokenBaseUrl;
        mQueryStringParamNameForAuthToken = pQueryStringParamNameForAuthToken;
        mAuthToken = pAuthToken;
        mAuthTokenSecret = pAuthTokenSecret;
    }

    /**
     * @return the mRequestTokenBaseUrl
     */
    public String getRequestTokenBaseUrl() {
        return mRequestTokenBaseUrl;
    }

    public String getRequestTokenUrlWithOauthToken() {
        String myAuthTokenUrlEncoded;
        try {
            myAuthTokenUrlEncoded = URLEncoder.encode(mAuthToken, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This should never happen.
            LOGGER.error("URL encoding exception occured. Please investigate.", e);
            return null;
        }
        return mRequestTokenBaseUrl + "?" + mQueryStringParamNameForAuthToken + "="
            + myAuthTokenUrlEncoded;
    }

    /**
     * @return the mAuthToken
     */
    public String getAuthToken() {
        return mAuthToken;
    }

    /**
     * @return the mAuthTokenSecret
     */
    public String getAuthTokenSecret() {
        return mAuthTokenSecret;
    }
}
