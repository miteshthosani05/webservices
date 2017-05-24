/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

import org.apache.http.HttpRequest;

/**
 * Signs the HTTP request for authentication purposes
 * @author Yenal Kal
 */
public interface IRestQuerySignatureProvider {

    /**
     * Signs the HTTP request for authentication purposes
     * @param pRequest The HTTP request object
     * @param pParams The list of parameters used to sign the request
     * @throws RestQueryEngineException
     */
    void signRequest(HttpRequest pRequest) throws RestQueryEngineException;

    /**
     * Sets a parameter to be used by the query signature provider
     * @param pKey The unique name of the parameter
     * @param pValue The value of the parameter
     */
    void setParam(String pKey, String pValue);

    /**
     * Removes a parameter used by the query signature provider
     * @param pKey The unique name of the parameter
     */
    void removeParam(String pKey);
}
