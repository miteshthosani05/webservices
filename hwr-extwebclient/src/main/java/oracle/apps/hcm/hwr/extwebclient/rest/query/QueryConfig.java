/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class encapsulates all http params that are needed to execute HTTP Query using Twitter API.
 * It contains GET, POST, PUT params, Header Fields, UrlPostfix that can be used in some social
 * media API to specify call ( e.g "/connections" suffix in LinkedIn ) and Post Body, that are used
 * in some API to provide additional criteria, for HTTP Requests
 * @author pglogows
 */
public class QueryConfig {

    private final String mUrlPostfix;

    private final String mPostBody;

    private final Map<String, String> mGetParams = new HashMap<String, String>();

    private final Map<String, String> mPostParams = new HashMap<String, String>();

    private final Map<String, String> mPutParams = new HashMap<String, String>();

    private final Map<String, String> mHeaderFields = new HashMap<String, String>();

    public QueryConfig(String pUrlPostfix, String pPostBody) {
        this.mUrlPostfix = pUrlPostfix;
        this.mPostBody = pPostBody;
    }

    public QueryConfig() {
        this.mUrlPostfix = null;
        this.mPostBody = null;
    }

    /**
     * Add header field to config. Override if header name already exists
     * @param pName param key. Not null
     * @param pValue param value. Not null
     */
    public void addHeaderField(String pName, String pValue) {
        checkForParams(pName, pValue);
        mHeaderFields.put(pName, pValue);
    }

    /**
     * @param pName get param to remove. Cannot be null;
     */
    public void removeHeaderField(String pName) {
        checkForParam(pName);
        mGetParams.remove(pName);
    }

    /**
     * Add get param to config. Override if param already exists
     * @param pName param key. Not null
     * @param pValue param value. Not null
     */
    public void addGetParam(String pName, String pValue) {
        checkForParams(pName, pValue);
        mGetParams.put(pName, pValue);
    }

    /**
     * @param pName get param to remove. Cannot be null;
     */
    public void removeGetParam(String pName) {
        checkForParam(pName);
        mGetParams.remove(pName);
    }

    /**
     * Add post param to config. Override if param already exists
     * @param pName param key. Not null
     * @param pValue param value. Not null
     */
    public void addPostParam(String pName, String pValue) {
        checkForParams(pName, pValue);
        mPostParams.put(pName, pValue);
    }

    /**
     * @param pName post param to remove. Cannot be null;
     */
    public void removePostParam(String pName) {
        checkForParam(pName);
        mPostParams.remove(pName);
    }

    /**
     * Add put param to config. Override if param already exists
     * @param pName param key. Not null
     * @param pValue param value. Not null
     */
    public void addPutParam(String pName, String pValue) {
        checkForParams(pName, pValue);
        mPutParams.put(pName, pValue);
    }

    /**
     * @param pName put param to remove. Cannot be null;
     */
    public void removePutParam(String pName) {
        checkForParam(pName);
        mPutParams.remove(pName);
    }

    public String getUrlPostfix() {
        return mUrlPostfix;
    }

    public String getPostBody() {
        return mPostBody;
    }

    /**
     * @return View of Get Params map. You cannot modify it. If You need to add or delete params use
     *         {@link QueryConfig} api to do it
     */
    public Map<String, String> getGetParams() {
        return Collections.unmodifiableMap(mGetParams);
    }

    public String getGetParam(String pName) {
        return mGetParams.get(pName);
    }

    /**
     * @return View of Post Params map. You cannot modify it. If You need to add or delete params
     *         use {@link QueryConfig} api to do it
     */
    public Map<String, String> getPostParams() {
        return Collections.unmodifiableMap(mPostParams);
    }

    public String getPostParam(String pName) {
        return mPostParams.get(pName);
    }

    /**
     * @return View of Put Params map. You cannot modify it. If You need to add or delete params use
     *         {@link QueryConfig} api to do it
     */
    public Map<String, String> getPutParams() {
        return Collections.unmodifiableMap(mPutParams);
    }

    public String getPutParam(String pName) {
        return mPutParams.get(pName);
    }

    /**
     * @return View of Header Fields map. You cannot modify it. If You need to add or delete params
     *         use {@link QueryConfig} api to do it
     */
    public Map<String, String> getHeaderFields() {
        return Collections.unmodifiableMap(mHeaderFields);
    }

    private void checkForParams(String pName, String pValue) {
        if (pName == null || pValue == null) {
            throw new IllegalArgumentException("Name or value is null !");
        }
    }

    private void checkForParam(String pName) {
        if (pName == null) {
            throw new IllegalArgumentException("Name is null !");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mGetParams == null) ? 0 : mGetParams.hashCode());
        result = prime * result + ((mHeaderFields == null) ? 0 : mHeaderFields.hashCode());
        result = prime * result + ((mPostBody == null) ? 0 : mPostBody.hashCode());
        result = prime * result + ((mPostParams == null) ? 0 : mPostParams.hashCode());
        result = prime * result + ((mPutParams == null) ? 0 : mPutParams.hashCode());
        result = prime * result + ((mUrlPostfix == null) ? 0 : mUrlPostfix.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        QueryConfig other = (QueryConfig) obj;
        if (mGetParams == null) {
            if (other.mGetParams != null)
                return false;
        } else if (!mGetParams.equals(other.mGetParams))
            return false;
        if (mHeaderFields == null) {
            if (other.mHeaderFields != null)
                return false;
        } else if (!mHeaderFields.equals(other.mHeaderFields))
            return false;
        if (mPostBody == null) {
            if (other.mPostBody != null)
                return false;
        } else if (!mPostBody.equals(other.mPostBody))
            return false;
        if (mPostParams == null) {
            if (other.mPostParams != null)
                return false;
        } else if (!mPostParams.equals(other.mPostParams))
            return false;
        if (mPutParams == null) {
            if (other.mPutParams != null)
                return false;
        } else if (!mPutParams.equals(other.mPutParams))
            return false;
        if (mUrlPostfix == null) {
            if (other.mUrlPostfix != null)
                return false;
        } else if (!mUrlPostfix.equals(other.mUrlPostfix))
            return false;
        return true;
    }
    
    
}
