/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.oauth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Chris Hluchan
 */
public class OAuthRequestTokenResponse {
    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(OAuthRequestTokenUrlResponse.class);

    /** The auth token. Appended to the base URL. */
    private String mAuthToken;

    /** The auth token secret. Used for getting the access token once the user authorizes the app. */
    private String mAuthTokenSecret;
    
    private boolean mCallbackConfirmed;

    public OAuthRequestTokenResponse(String pAuthToken, 
            String pAuthTokenSecret, String pCallbackConfirmed) {
        mAuthToken = pAuthToken;
        mAuthTokenSecret = pAuthTokenSecret;
        mCallbackConfirmed = "true".equals(pCallbackConfirmed);
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
