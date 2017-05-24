/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;

/**
 * The engine used to execute RESTful service calls with shared cookie info
 * @author Yiwei Zhang
 */
public class SharedCookieRestQueryEngine extends RestQueryEngine {

	 /** The cookie store to be shared between HTTP clients */
	CookieStore mSharedCookieStore = null;
	
	/**
     * Constructs a REST query engine with the given query provider
     * @param pQueryProvider
     */
	public SharedCookieRestQueryEngine(IRestQueryProvider pQueryProvider) {
        super(pQueryProvider);
    }
	
	/*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine,
     * For some social media sites like OSN, we need to keep the cookie info within the REST query
     * So we need to save and share the cookies between HTTP clients
     */
	@Override
	public DefaultHttpClient getThreadSafeClient() {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        ClientConnectionManager myManager = httpClient.getConnectionManager();
        HttpParams myParams = httpClient.getParams();
        httpClient =
            new DefaultHttpClient(new ThreadSafeClientConnManager(myParams,
                myManager.getSchemeRegistry()), myParams);
        
        if (mSharedCookieStore == null) {
        	mSharedCookieStore = httpClient.getCookieStore();
    	}
    	else {
    		httpClient.setCookieStore(mSharedCookieStore);
    	}
        
        return httpClient;
    }
	
	/**
     * Clears all the cookies
     */
	public void clearCookies() {
		
		if (mSharedCookieStore != null) {
			mSharedCookieStore.clear();
    	}
	}
}

