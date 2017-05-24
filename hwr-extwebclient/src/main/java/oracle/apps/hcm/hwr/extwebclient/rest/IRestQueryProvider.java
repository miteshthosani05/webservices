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

import org.apache.http.client.methods.HttpRequestBase;

/**
 * The interface for the REST query providers
 * @author Yenal Kal
 */
public interface IRestQueryProvider {

    /**
     * Finds the query object with the given name
     * @param pQueryName The unique name of the query
     * @return The query object matching the given name
     */
    RestQuery findQuery(String pQueryName);

    /**
     * Executes common tasks with the HTTPUrlConnection such as adding specific HTTP headers.
     * @param pRequest The HTTP request object
     */
    void executeCommonQueryTasks(HttpRequestBase pRequest);

    /**
     * Processes the response returned from the REST service and returns the result as a map
     * @param statusCode HTTP status code
     * @param pQueryResponse The response from the REST service
     * @return The map containing String, Object pairs
     */
    Map<String, Object> procesQueryResponse(int statusCode, String pQueryResponse);
}
