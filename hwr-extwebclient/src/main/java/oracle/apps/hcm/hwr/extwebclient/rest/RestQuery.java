/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Contains all the data to build a REST query
 * @author Yenal Kal
 * @author Ibtisam Haque
 */
public final class RestQuery implements Cloneable {

    /**
     * All HTTP methods available for the REST query engine
     */
    public enum Method {
        GET, POST, PUT, DELETE
    }

    /** The logger instance */
    private static final Log LOGGER = LogFactory.getLog(RestQuery.class);

    /** The list of GET parameters */
    private Map<String, String> mGetParams;

    /** The list of HTTP header fields */
    private Map<String, String> mHeaderFields;

    /** The HTTP method used for the query */
    private Method mMethod;

    /** The unique name of the query **/
    private String mName;

    /** The list of POST parameters */
    private Map<String, String> mPostParams;

    /** The list of PUT parameters */
    private Map<String, String> mPutParams;

    /** The base URL for the service endpoint **/
    private String mURL;

    /**
     * Sometimes a specific string needs to be added to the URL after the query string. This is used
     * for that purpose.
     */
    private String mUrlPostQueryStringText;

    /**
     * This is used if there are parametric values in the URL. e.g.
     * http://www.someapiprovider.com/[someuserid]/friends?limit=50 Here "/friends" would be the
     * UrlPreQueryStringText
     */
    private String mUrlPreQueryStringText;

    /**
     * The default constructor. Used by the Jackson library to instantiate this object.
     */
    public RestQuery() {

    }

    /**
     * The constructor with the minimal information needed to build a RESTQuery object
     * @param pName The unique name of the query
     * @param pMethod The HTTP method used for the query
     * @param pURL The URL for the query
     */
    public RestQuery(final String pName, final Method pMethod, final String pURL) {
        mName = pName;
        mMethod = pMethod;
        mURL = pURL;
    }

    public RestQuery(final String pName, final Method pMethod, final String pCommonUrl, final String pUrlSuffix) {
        this(pName, pMethod, StringUtils.isEmpty(pUrlSuffix) ? pCommonUrl : pCommonUrl + pUrlSuffix);
    }

    @Override
    public Object clone() {
        RestQuery cloneObj = null;
        try {
            cloneObj = (RestQuery) super.clone();
            /*
             * Sufficient for following attributes: mURL; mUrlPostQueryStringText;
             * mUrlPreQueryStringText; mMethod; mName; s
             */

            // taking care of mGetParams
            cloneObj.mGetParams = null;
            if (this.mGetParams != null) {
                cloneObj.getGetParams();
                for (final Entry<String, String> mapEntry : this.mGetParams.entrySet()) {
                    cloneObj.getGetParams().put(mapEntry.getKey(), mapEntry.getValue());
                }
            }

            // taking care of mHeaderFields
            cloneObj.mHeaderFields = null;
            if (this.mHeaderFields != null) {
                cloneObj.getHeaderFields();
                for (final Entry<String, String> mapEntry : this.mHeaderFields.entrySet()) {
                    cloneObj.getHeaderFields().put(mapEntry.getKey(), mapEntry.getValue());
                }
            }

            // taking care of mPostParams
            cloneObj.mPostParams = null;
            if (this.mPostParams != null) {
                cloneObj.getPostParams();
                for (final Entry<String, String> mapEntry : this.mPostParams.entrySet()) {
                    cloneObj.getPostParams().put(mapEntry.getKey(), mapEntry.getValue());
                }
            }

            // taking care of mPostParams
            cloneObj.mPutParams = null;
            if (this.mPutParams != null) {
                cloneObj.getPutParams();
                for (final Entry<String, String> mapEntry : this.mPutParams.entrySet()) {
                    cloneObj.getPutParams().put(mapEntry.getKey(), mapEntry.getValue());
                }
            }

        } catch (final CloneNotSupportedException e) {
            LOGGER.warn("Unable to clone the object. Error Message: " + e.getMessage() + ".");

        }

        return cloneObj;
    }

    /**
     * @return the mQueryParams
     */
    public Map<String, String> getGetParams() {
        if (mGetParams == null) {
            mGetParams = new LinkedHashMap<String, String>();
        }
        return mGetParams;
    }

    /**
     * @return the mHeaderFields
     */
    public Map<String, String> getHeaderFields() {
        if (mHeaderFields == null) {
            mHeaderFields = new LinkedHashMap<String, String>();
        }
        return mHeaderFields;
    }

    /**
     * @return the mMethod
     */
    public Method getMethod() {
        return mMethod;
    }

    /**
     * @return the mName
     */
    public String getName() {
        return mName;
    }

    /**
     * @return the mPostParams
     */
    public Map<String, String> getPostParams() {
        if (mPostParams == null) {
            mPostParams = new LinkedHashMap<String, String>();
        }
        return mPostParams;
    }

    /**
     * @return the mPutParams
     */
    public Map<String, String> getPutParams() {
        if (mPutParams == null) {
            mPutParams = new LinkedHashMap<String, String>();
        }
        return mPutParams;
    }

    /**
     * @return the mURL
     */
    public String getURL() {
        return mURL;
    }

    public void setURL(final String pURL) {
        this.mURL = pURL;
    }

    /**
     * @return the mUrlPostQueryStringText
     */
    public String getUrlPostQueryStringText() {
        return mUrlPostQueryStringText;
    }

    /**
     * @return the mUrlPreQueryStringText
     */
    public String getUrlPreQueryStringText() {
        return mUrlPreQueryStringText;
    }

    /**
     * @param mUrlPostQueryStringText the mUrlPostQueryStringText to set
     */
    public void setUrlPostQueryStringText(final String mUrlPostQueryStringText) {
        this.mUrlPostQueryStringText = mUrlPostQueryStringText;
    }

    /**
     * @param mUrlPreQueryStringText the mUrlPreQueryStringText to set
     */
    public void setUrlPreQueryStringText(final String mUrlPreQueryStringText) {
        this.mUrlPreQueryStringText = mUrlPreQueryStringText;
    }

    @Override
    public String toString() {
        return "RestQuery [" + "mGetParams=" + mGetParams + ", mHeaderFields=" + mHeaderFields + ", mMethod=" + mMethod
            + ", mName=" + mName + ", mPostParams=" + mPostParams + ", mPutParams=" + mPutParams + ", mURL=" + mURL
            + ", mUrlPostQueryStringText=" + mUrlPostQueryStringText + ", mUrlPreQueryStringText="
            + mUrlPreQueryStringText + "]";
    }

}
