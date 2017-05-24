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
 * @author Yenal Kal
 */
public abstract class AbstractRestQueryResult {

    /** The HTTP status code */
    private int mStatusCode;

    /** The response size in bytes */
    private Long mResponseSize;

    /**
     * Constructs an AbstractRestQueryResult
     * @param pStatusCode The HTTP status code
     */
    public AbstractRestQueryResult(final int pStatusCode, final Long pResponseSize) {
        mStatusCode = pStatusCode;
        mResponseSize = pResponseSize;
    }

    /**
     * @return the mResponseSize
     */
    public long getResponseSize() {
        return mResponseSize == null ? 0L : mResponseSize.longValue();
    }

    /**
     * @return the mStatusCode
     */
    public int getStatusCode() {
        return mStatusCode;
    }
}
