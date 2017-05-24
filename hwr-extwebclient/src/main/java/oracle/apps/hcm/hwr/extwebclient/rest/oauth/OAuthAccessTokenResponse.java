/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.oauth;

/**
 * @author Yenal Kal
 * @author Ibtisam Haque
 */
public class OAuthAccessTokenResponse {

    /** The access token */
    private String mAccessToken;

    /** The access token secret */
    private String mAccessTokenSecret;
    
    /** The user id or profile id */
    private String mUserId;
    
    /**
     * Creates an OAuthAccessTokenResponse instance.
     * @param pAccessToken The access token.
     * @param pAccessTokenSecret The access token secret.
     */
    public OAuthAccessTokenResponse(String pAccessToken, String pAccessTokenSecret) {
        mAccessToken 		= pAccessToken;
        mAccessTokenSecret 	= pAccessTokenSecret;
        mUserId 			= null;
    }

    
    /**
     * Creates an OAuthAccessTokenResponse instance.
     * @param pAccessToken 			
     * 				The access token.
     * @param pAccessTokenSecret 
     * 				The access token secret.
     * @param pUserId	
     * 				The user/profile id.
     */
    public OAuthAccessTokenResponse(String pAccessToken, String pAccessTokenSecret, String pUserId) {
        mAccessToken 		= pAccessToken;
        mAccessTokenSecret 	= pAccessTokenSecret;
        mUserId 			= pUserId;
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
     * @return User Id
     */
	public String getUserId() {
		return mUserId;
	}
    
    
}
