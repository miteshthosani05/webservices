/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

import java.util.Map;

/**
 * A REST query result object using a map of string and object pairs. Usually used for JSON.
 * @author Yenal Kal
 */
public class RestQueryMapResult extends AbstractRestQueryResult {

    /** The HTTP response is stuffed in this map */
    private Map<String, Object> mResultMap;

    /**
     * @param pStatusCode The HTTP status code
     * @param pResultMap The map that contains the String, Object pairs
     */
    public RestQueryMapResult(int pStatusCode, Map<String, Object> pResultMap) {
        this(pStatusCode, pResultMap, null);
    }

    public RestQueryMapResult(final int pStatusCode, final Map<String, Object> pResultMap, final Long pResponseSize) {
        super(pStatusCode, pResponseSize);
        
        mResultMap = pResultMap;
    }

    /**
     * @return the mResultMap
     */
    public Map<String, Object> getResultMap() {
        return mResultMap;
    }

    @Override
    public String toString() {
        return "Status code: " + getStatusCode() + "\nResponse size: " + getResponseSize() + "\n" + mResultMap;
    }

}
