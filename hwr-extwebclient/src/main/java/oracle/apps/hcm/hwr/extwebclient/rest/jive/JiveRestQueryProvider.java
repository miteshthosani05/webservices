/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.extwebclient.rest.jive;

import java.util.Map;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.methods.HttpRequestBase;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;

/**
 * Rest Query Provider for Jive API
 * 
 * @author Ibtisam Haque
 * 
 */
public class JiveRestQueryProvider implements IRestQueryProvider {

	 /** The logger instance */
    private static final Log LOGGER = LogFactory.getLog(JiveRestQueryProvider.class);

    private String mURL				= null;
    
	public JiveRestQueryProvider(String pURL) {
		super();
		mURL = pURL;
	}
	

	/**
	 * Method used to find the urls for the the Jive Query.
	 * 
	 * @param pQueryName	The query for which the url is asked. \n 
	 * 						Currently, following urls are being supported: \n 
	 * 						i) 	JiveConstants.GET_USER_BY_ID 			-- >	For getting user's profile by user id
	 * 						ii)	JiveConstants.GET_USER_MISC_ATTRIBUTES 	-- > 	For getting user's follower's profiles, following(friend)'s profiles, activities
	 * 																			(Some post processing is needed for this at the caller's end)
	 * 						iii)JiveConstants.GET_USER_BY_USERNAME 		-- >	For getting user's profile by username				
	 * 						iv)	JiveConstants.GET_USER_ACTIVITIES_CONTENT_Key 			
	 * 						v)  JiveConstants.GET_USER_ACTIVITIES_CONTENT_Key_ALL 
	 * 						vi) JiveConstants.GET_USER_BY_EMAIL 
	 * 
	 */
	@Override
	public RestQuery findQuery(String pQueryName) {
		LOGGER.info("JiveRestQueryProvider: findQuery(): Finding query for :" + pQueryName + ".");  
		if (pQueryName.equals(JiveConstants.GET_USER_BY_USERNAME)) {
			RestQuery myQuery = new RestQuery(JiveConstants.GET_USER_BY_USERNAME, RestQuery.Method.GET, this.mURL + JiveConstants.GET_USER_VALUE);
			return myQuery;
		} 
		else if (pQueryName.equals(JiveConstants.GET_USER_BY_ID)) {
			RestQuery myQuery = new RestQuery(JiveConstants.GET_USER_BY_ID, RestQuery.Method.GET, this.mURL + JiveConstants.GET_USER_BY_ID_VALUE);
			return myQuery;
		} 
		else if (pQueryName.equals(JiveConstants.GET_USER_MISC_ATTRIBUTES)) {
			RestQuery myQuery = new RestQuery(JiveConstants.GET_USER_MISC_ATTRIBUTES, RestQuery.Method.GET, this.mURL + JiveConstants.GET_USER_MISC_ATTRIBUTES_VALUE);
			return myQuery;
		}
		else if (pQueryName.equals(JiveConstants.GET_USER_ACTIVITIES_CONTENT_Key)) {
			RestQuery myQuery = new RestQuery(JiveConstants.GET_USER_ACTIVITIES_CONTENT_Key, RestQuery.Method.GET, 
					this.mURL + JiveConstants.GET_USER_ACTIVITIES_CONTENT_Value);
			return myQuery;
		}
		else if (pQueryName.equals(JiveConstants.GET_USER_ACTIVITIES_CONTENT_Key_ALL)) {
			RestQuery myQuery = new RestQuery(JiveConstants.GET_USER_ACTIVITIES_CONTENT_Key_ALL, RestQuery.Method.GET, 
					this.mURL + JiveConstants.GET_USER_ACTIVITIES_CONTENT_Value_ALL + this.mURL + JiveConstants.GET_USER_BY_ID_VALUE);
			return myQuery;
		}
		else if(pQueryName.equals(JiveConstants.GET_USER_BY_EMAIL)) {
			RestQuery myQuery = new RestQuery(JiveConstants.GET_USER_BY_EMAIL, RestQuery.Method.GET,this.mURL + "/people/email/");
            return myQuery;
        }
		else if(pQueryName.equals(JiveConstants.ACTION_SEND_PRIVATE_MESSAGE)) {
			// this is currently not being used
			RestQuery myQuery = new RestQuery(JiveConstants.ACTION_SEND_PRIVATE_MESSAGE, RestQuery.Method.POST,this.mURL + "/contents");
            return myQuery;
        }
		// else case
		LOGGER.error("JiveRestQueryProvider: findQuery(): The query name '" + pQueryName + "' couuldn't be resolved.");  
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider#
	 * executeCommonQueryTasks(org.apache.http.client.methods.HttpRequestBase)
	 */
	@Override
	public void executeCommonQueryTasks(HttpRequestBase pRequest) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider#procesQueryResponse
	 * (int, java.lang.String)
	 */
	@Override
	public Map<String, Object> procesQueryResponse(int statusCode, String pQueryResponse) {
		return JsonHelper.jsonStringToMap(pQueryResponse);
	}

}
