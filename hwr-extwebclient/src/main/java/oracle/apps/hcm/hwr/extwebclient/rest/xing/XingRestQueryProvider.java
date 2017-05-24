/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.extwebclient.rest.xing;

import java.util.Map;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpRequestBase;


/**
 * Rest Query Provider for Xing API
 * 
 * @author Ibtisam Haque
 * 
 */
 
public class XingRestQueryProvider implements IRestQueryProvider {

	 /** The logger instance */
    private static final Log LOGGER = LogFactory.getLog(XingRestQueryProvider.class);
	
	/**
	 * Method used to find the urls for the the Xing Query.
	 * 
	 * @param pQueryName	The query for which the url is asked.
	 * 						<br>If its null or if its doesn't match one of the constant query strings, it would return null.
	 * 
	 */
	@Override
	public RestQuery findQuery(String pQueryName) {
		LOGGER.info("XingRestQueryProvider: findQuery(): Finding query for :" + pQueryName + ".");  

		if (pQueryName == null)
		{
			LOGGER.error("XingRestQueryProvider: findQuery(): The query name provided is NULL. Exiting the method."); 
			return null;
		}
		
		pQueryName = pQueryName.trim();
		
		if (pQueryName.equals(XingConstants.ACTION_GET_MY_PROFILE)) 
		{
			RestQuery myQuery = new RestQuery(XingConstants.ACTION_GET_MY_PROFILE, 		RestQuery.Method.GET, 	"https://api.xing.com/v1/users/me");
			return myQuery;
		} 
		else if (pQueryName.equals(XingConstants.ACTION_GET_CONTACTS_IDS)) 
		{
			return new RestQuery(XingConstants.ACTION_GET_CONTACTS_IDS, 				RestQuery.Method.GET, 	"https://api.xing.com/v1/users/me/contacts");
		}
		else if (pQueryName.equals(XingConstants.ACTION_GET_CONTACTS_PROFILE)) 
		{
			RestQuery myQuery = new RestQuery(XingConstants.ACTION_GET_CONTACTS_PROFILE, RestQuery.Method.GET, 	"https://api.xing.com/v1/users/");
			return myQuery;
		} 
		else if (pQueryName.equals(XingConstants.ACTION_GET_MY_PROFILE_MESSAGE)) 
		{
			return new RestQuery(XingConstants.ACTION_GET_MY_PROFILE_MESSAGE, 			RestQuery.Method.GET, 	"https://api.xing.com/v1/users/me/profile_message");
		}
		else if (pQueryName.equals(XingConstants.ACTION_GET_MY_ACTIVITES_FEED)) 
		{
			return new RestQuery(XingConstants.ACTION_GET_MY_ACTIVITES_FEED, 			RestQuery.Method.GET, 	"https://api.xing.com/v1/users/me/feed");
		}
		else if (pQueryName.equals(XingConstants.ACTION_GET_MY_ACTIVITY_LIKES))
		{
			return new RestQuery(XingConstants.ACTION_GET_MY_ACTIVITY_LIKES, 			RestQuery.Method.GET, 	"https://api.xing.com/v1/activities/");
		}
		else if (pQueryName.equals(XingConstants.ACTION_GET_REQUEST_TOKEN)) 
		{
			return new RestQuery(XingConstants.ACTION_GET_REQUEST_TOKEN, 				RestQuery.Method.POST, 	"https://api.xing.com/v1/request_token");
		} 
		else if (pQueryName.equals(XingConstants.ACTION_GET_ACCESS_TOKEN)) 
		{
			return new RestQuery(XingConstants.ACTION_GET_ACCESS_TOKEN, 				RestQuery.Method.POST, 	"https://api.xing.com/v1/access_token");
		}
		else if (pQueryName.equals(XingConstants.ACTION_SEND_PRIVATE_MESSAGE)) 
		{
			return new RestQuery(XingConstants.ACTION_SEND_PRIVATE_MESSAGE, 			RestQuery.Method.POST, 	"https://api.xing.com/v1/users/me/conversations");
		}
		
		// else case
		LOGGER.error("XingRestQueryProvider: findQuery(): The query name '" + pQueryName + "' couldn't be resolved. Exiting the method.");  
		return null;
	}

	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider#executeCommonQueryTasks(org.apache.http.client.methods.HttpRequestBase)
	 */
	@Override
	public void executeCommonQueryTasks(HttpRequestBase pRequest) {

	}
	
	
	
	/*
	 * (non-Javadoc)
	 * @see oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider#procesQueryResponse(int, java.lang.String)
	 */
	@Override
	public Map<String, Object> procesQueryResponse(int statusCode, String pQueryResponse) {
		return JsonHelper.jsonStringToMap(pQueryResponse);
	}
	
	
	

}
