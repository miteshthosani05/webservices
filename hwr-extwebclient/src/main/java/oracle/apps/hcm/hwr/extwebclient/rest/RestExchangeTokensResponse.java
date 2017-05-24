/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

/**
 * The result object for the {@link IRestApiAuthenticator#exchangeTokens(String, String)} method.
 * The operation is successful if the mAccessToken string is not null. If it's null the consumer
 * should check the error message.
 * @author Yenal Kal
 */
public class RestExchangeTokensResponse {

    /** The access token */
    private String mAccessToken;

    /** The access token secret (optional) */
    private String mAccessTokenSecret;

    /** The error message if applicable */
    private String mErrorMessage;

    /**
     * Constructs a RestExchangeTokensResponse instance by setting the error message.
     * @param pErrorMessage
     */
    public RestExchangeTokensResponse(String pErrorMessage) {
        mErrorMessage = pErrorMessage;
    }

    /**
     * Constructs a RestExchangeTokensResponse instance by setting the access token and the secret.
     * @param pAccessToken
     * @param pAccessTokenSecret
     */
    public RestExchangeTokensResponse(String pAccessToken, String pAccessTokenSecret) {
        mAccessToken = pAccessToken;
        mAccessTokenSecret = pAccessTokenSecret;
    }

    /**
     * @return the mAccessToken
     */
    public String getAccessToken() {
        return mAccessToken;
    }

    /**
     * @return the mAccessTokenSecret
     */
    public String getAccessTokenSecret() {
        return mAccessTokenSecret;
    }

    /**
     * @return the mErrorMessage
     */
    public String getErrorMessage() {
        return mErrorMessage;
    }
}
