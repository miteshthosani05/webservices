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
 * The information required to access restricted data
 * @author Yenal Kal
 */
public class ResourceAccessInfo {

    /** The unique ID */
    private String mId;

    /** The OAuth access token */
    private String mAccessToken;

    /** The OAuth access token secret */
    private String mAccessTokenSecret;

    /**
     * @return the mId
     */
    public String getId() {
        return mId;
    }

    /**
     * @param mId the mId to set
     */
    public void setId(String mId) {
        this.mId = mId;
    }

    /**
     * @return the mAccessToken
     */
    public String getAccessToken() {
        return mAccessToken;
    }

    /**
     * @param mAccessToken the mAccessToken to set
     */
    public void setAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    /**
     * @return the mAccessTokenSecrey
     */
    public String getAccessTokenSecret() {
        return mAccessTokenSecret;
    }

    /**
     * @param mAccessTokenSecrey the mAccessTokenSecrey to set
     */
    public void setAccessTokenSecret(String mAccessTokenSecrey) {
        this.mAccessTokenSecret = mAccessTokenSecrey;
    }
}
