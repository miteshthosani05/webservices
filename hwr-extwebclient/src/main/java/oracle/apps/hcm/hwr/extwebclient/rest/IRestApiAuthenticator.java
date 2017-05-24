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
 * Authenticator interface for all REST APIs
 * @author Yenal Kal
 */
public interface IRestApiAuthenticator {

    /**
     * Exchanges a short lived access token with a long lived access token.
     * @param pUserId The user ID.
     * @param pShortLivedAccessToken The short lived access token.
     * @return RestExchangeTokensResponse instance that contains either the error message or the
     *         access token and optionally the secret.
     * @throws RestQueryEngineException
     */
    RestExchangeTokensResponse exchangeTokens(String pUserId, String pShortLivedAccessToken)
        throws RestQueryEngineException;
}
