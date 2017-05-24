/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.jive;

import java.util.ArrayList;
import java.util.List;

import oracle.apps.hcm.hwr.extwebclient.rest.BasicSignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngineException;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;
import oracle.apps.hcm.hwr.extwebclient.rest.apiuse.ConnectorApiUseage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


/**
 * Jive API Helper class
 * 
 * @author Ibtisam Haque
 *
 */
public class JiveAPIHelper {

	/** REST query engine */
	private final RestQueryEngine mQueryEngine;

	/** The logger instance */
	private static final Log LOGGER = LogFactory.getLog(JiveAPIHelper.class);

	private ConnectorApiUseage mApiUseage;
	
	/**
	 * Constructor
	 * Sets the application related data to make API calls successfully.
	 * 
	 * @param pUsername		Jive acccount username
	 * @param pPassword		Jive acccount password
	 * @param pURL			Jive URL to use
	 *  
	 */
	public JiveAPIHelper(String pUsername, String pPassword, String pURL) {
		mQueryEngine = new RestQueryEngine(new JiveRestQueryProvider(pURL), new BasicSignatureProvider(pUsername, pPassword));

	}

	
	/**
	 * Get Rest Query Engine
	 * @return
	 */
	public RestQueryEngine getmQueryEngine() {
		return mQueryEngine;
	}

    public void setApiUseageClass(ConnectorApiUseage pApiUseage) {
        mApiUseage = pApiUseage;
        mQueryEngine.setConnectorApiUseage(mApiUseage);
    }
	
	/**
	 * Gets the profile id from the username.
	 * <br> It also validates the username provided.
	 * 
	 * @param pUsername		
	 * 				Username which needs to be validated and for which profile id needs to be determined
	 * @return		
	 * 				Profile id of the user (specified by username). Null if username provided is invalid.
	 * 
	 * @throws RestQueryEngineException
	 */
	public String getProfileIdFromUserName(String pUsername) throws RestQueryEngineException {
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: getProfileIdFromUserName(): Entering method. Params: Usermame: " + pUsername  + "."); 
		}
		
		String userId 								= null;
		RestQuerySimpleResult myUserProfileResult 	= getProfileByUserName(pUsername);
		
		if (myUserProfileResult == null)
		{
			LOGGER.error("JiveAPIHelper: getProfileIdFromUserName(): Unable to get profile for username: " + pUsername  + ". Exiting the method.");
			return null;
		}
		
		if (myUserProfileResult != null && myUserProfileResult.getResponse() != null && myUserProfileResult.getResponse().trim().length() != 0 && myUserProfileResult.getStatusCode() == JiveConstants.API_SUCCESS_CODE_200) 
		{
			myUserProfileResult.trimMResponseFirstOccuranceOnly(JiveConstants.RESPONSE_STRING_TO_TRIM, JiveConstants.RESPONSE_STRING_TO_TRIM_WITH);
	 		
			Object myParsedObject 	= JsonHelper.parse(myUserProfileResult.getResponse());

			if (myParsedObject == null) {
				LOGGER.error("JiveAPIHelper: getProfileIdFromUserName(): myParsedObject is null. This occurred for pUsername:" + pUsername + ". Exiting the method.");
				return null;
			}

			if (!(myParsedObject instanceof JSONObject)) {
				LOGGER.error("JiveAPIHelper: getProfileIdFromUserName(): Error parsing Json object. This occurred for pUsername:" + pUsername + ". Exiting the method.");
				return null;
			}

			JSONObject myParsedJsonObject 	= (JSONObject) myParsedObject;		
			if (myParsedJsonObject != null)
			{
				userId 						= (String) myParsedJsonObject.get("id");
				if (userId != null){
					userId = userId.trim();
				}
			}
		
			
			LOGGER.info("JiveAPIHelper: getProfileIdFromUserName(): UserId for username (" + pUsername + ") is:" + userId + ".");
			
		}
			
		
		return userId;
	}

	
	
	
	
	/**
	 * Get the profile of a particular user specified by Username
	 * 
	 * @param pUser		Username of the account whose profile is to be fetched
	 * @return			The RestQuerySimpleResult instance containing the response and the HTTP status code.
	 * 					Return NULL, if the username is null or empty. 
	 * @throws RestQueryEngineException
	 */
	public RestQuerySimpleResult getProfileByUserName(String pUser) throws RestQueryEngineException {
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: getProfileByUserName(): Entering method. Params: Usermame: " + pUser  + "."); 
		}
		
		if (pUser == null || pUser.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: getProfileByUserName(): User name provided is empty or null - " + pUser + ".");
			return null;
		}
		pUser = pUser.trim();
		RestQuerySimpleResult myResult 	=  mQueryEngine.runSimpleQuery(JiveConstants.GET_USER_BY_USERNAME, pUser, null, null, null);
		
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: getProfileByUserName(): myResult: " + (myResult == null?"null": ("Satus Code: " +myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize() )) + ".");
		}
		
		return myResult;
	}

	
	/**
	 *  Returns the profile of user as part of application's credential verification
	 *  
	 * @param pUserName				User Name to verify
	 * @param pUserPassword			Corresponding user password to use
	 * @param pJiveUrl				Jive url to use
	 * @return
	 * @throws RestQueryEngineException
	 */
	public RestQuerySimpleResult getUserPublicProfileForConnectionVerification(String pUserName, String pUserPassword, String pJiveUrl) throws RestQueryEngineException {
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: getUserPublicProfileForConnectionVerification(): Entering method. Params: Usermame: " + pUserName  + "." ); 
		}
		
		if (pUserName == null || pUserName.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: getUserPublicProfileForConnectionVerification(): User name provided is empty or null - " + pUserName + ".");
			return null;
		}
		if (pUserPassword == null || pUserPassword.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: getUserPublicProfileForConnectionVerification(): User password provided is empty or null - " + pUserPassword + ".");
			return null;
		}
		if (pJiveUrl == null || pJiveUrl.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: getUserPublicProfileForConnectionVerification(): User Jive Url provided is empty or null - " + pJiveUrl + ".");
			return null;
		}
		
		pUserName 		= pUserName.trim();
		pUserPassword 	= pUserPassword.trim();
		pJiveUrl		= pJiveUrl.trim();
		
		RestQueryEngine mQueryEngineTemp = new RestQueryEngine(new JiveRestQueryProvider(pJiveUrl), new BasicSignatureProvider(pUserName, pUserPassword));    	
		
		RestQuerySimpleResult myResult 	=  mQueryEngineTemp.runSimpleQuery(JiveConstants.GET_USER_BY_USERNAME, pUserName, null, null, null);
		
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: getUserPublicProfileForConnectionVerification(): myResult: " + (myResult == null?"null": ("Satus Code: " +myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize() )) + ".");
		}
		
		return myResult;
	}
	


	/**
	 * Get the profile of a particular user specified by User Id
	 * 
	 * @param pUserID	User ID of the account whose profile is to be fetched
	 * @return			The RestQuerySimpleResult instance containing the response and the HTTP status code.
	 * 					Return NULL, if the userID is null or empty. 
	 * @throws RestQueryEngineException
	 */
	public RestQuerySimpleResult getProfileByUserId(String pUserId) throws RestQueryEngineException {
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: getProfileByUserId(): Entering method. Params: User ID: " + pUserId  + "."); 
		}
		
		if (pUserId == null || pUserId.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: getProfileByUserId(): User ID provided is empty or null - " + pUserId + ".");
			return null;
		}
		
		pUserId 	= pUserId.trim();
		if (!pUserId.matches("[0-9]+")) {
			LOGGER.error("JiveAPIHelper: getProfileByUserId(): User ID provided should only be Numeric. User ID provided is: " + pUserId + ".");
			return null;
		}
		
		RestQuery queryToRun	= mQueryEngine.findQuery(JiveConstants.GET_USER_BY_ID);
		queryToRun.setURL(queryToRun.getURL() + pUserId);
		RestQuerySimpleResult myResult 	= mQueryEngine.runSimpleQueryAsSuch(queryToRun);
		
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: getProfileByUserId(): Running the query: " + queryToRun.getURL() );
			LOGGER.info("JiveAPIHelper: getProfileByUserId(): myResult: " + (myResult == null?"null": ("Satus Code: " +myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize() )) + ".");
		}
		return myResult;

	}
	
	public RestQuerySimpleResult getLastProfileUpdatedDateByUsername(String pUsername) throws RestQueryEngineException {
		RestQuery getPersonByUsername = mQueryEngine.findQuery(JiveConstants.GET_USER_BY_USERNAME);
		String url = getPersonByUsername.getURL();
		url += pUsername + "?fields=jive.lastProfileUpdate";
		getPersonByUsername.setURL(url);
		
		return mQueryEngine.runSimpleQueryAsSuch(getPersonByUsername);
	}
	
	public RestQuerySimpleResult getLastProfileUpdatedDateById(String pUserId) throws RestQueryEngineException {
		RestQuery getPersonByUsername = mQueryEngine.findQuery(JiveConstants.GET_USER_BY_ID);
		String url = getPersonByUsername.getURL();
		url += pUserId + "?fields=jive.lastProfileUpdate";
		getPersonByUsername.setURL(url);
		
		return mQueryEngine.runSimpleQueryAsSuch(getPersonByUsername);
	}
	 
	 /**
	  * Gets contents of a particular activity specified by the param
	  * 
	  * @param pContentIdUrlToRun		String url for the activity
	  * @return							RestQuerySimpleResult response from the Jive API
	  * @throws RestQueryEngineException
	  */
	 public RestQuerySimpleResult getActivityContent(String pContentIdUrlToRun) throws RestQueryEngineException {
			 RestQuery queryToRun 				= mQueryEngine.findQuery(JiveConstants.GET_USER_ACTIVITIES_CONTENT_Key);
			 queryToRun.setURL(pContentIdUrlToRun+ "?brevity=TextOnly");	// overridding
			 RestQuerySimpleResult myResult 	= mQueryEngine.runSimpleQueryAsSuch(queryToRun);		
			 
			 return myResult;
		 }
	
	 
	 /**
	  * Gets List of users who liked a particular activity specified by the param
	  * 
	  * @param pContentIdUrlToRun		String url for the activity
	  * @return							RestQuerySimpleResult response from the Jive API
	  * @throws RestQueryEngineException
	  */
	 public RestQuerySimpleResult getActivityLikesUsers(String pContentIdUrlToRun) throws RestQueryEngineException {
		 RestQuery queryToRun 				= mQueryEngine.findQuery(JiveConstants.GET_USER_ACTIVITIES_CONTENT_Key);
		 queryToRun.setURL(pContentIdUrlToRun+ "/likes?count="+JiveConstants.PAGINATION_PAGE_SIZE);	// overridding
		 RestQuerySimpleResult myResult 	= mQueryEngine.runSimpleQueryAsSuch(queryToRun);		 
		 
		 return myResult;
	 }
	 
	 
	
	/**
	 * Gets activities associated with a particular user id (and not user name).
	 * 
	 * @param pUserId			User Id for which corresponding information needs to be fetched
	 * @param pAfterDate		String date in the ISO-8601 format. If specified, will get all user activities since this date.		
	 * @param pBeforeDate		String date in the ISO-8601 format. If specified, will get all user activities prior to this date.	
	 * 				
	 * 							NOTE: ONLY ONE 	pAfterDate or pBeforeDate can be specified, and NOT BOTH. However, both can be NULL.
	 * @return					ArrayList of the RestQuerySimpleResult containing the paginated response from the Jive API
	 * @throws RestQueryEngineException
	 */
	public ArrayList<RestQuerySimpleResult> getUserActivities(String pUserId, String pAfterDate, String pBeforeDate) throws RestQueryEngineException {
		if (JiveConstants.T0_DEBUG){
			LOGGER.info("JiveAPIHelper: getUserActivities(): Entering method. Params are: " + "pUserId: " + pUserId + ".");
		}

		if (pUserId == null || pUserId.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: getUserActivities(): User ID provided is empty or Null - " + pUserId + ".");
			return null;
		}

		if (!pUserId.matches("[0-9]+")) {
			LOGGER.error("JiveAPIHelper: getUserActivities(): User ID provided should only be Numeric. User ID provided is: " + pUserId + ".");
			return null;
		}
		
		if ( pAfterDate != null && pBeforeDate != null  )
		{
			LOGGER.error("JiveAPIHelper: getUserActivities(): You cannot specify both the 'After Date' and 'Before Date' to search for activities. One of these MUST be null. (Both could also be NULL)");
			return null;
		}
		
		ArrayList<RestQuerySimpleResult> myResultArrayList 	= new ArrayList<RestQuerySimpleResult>();
		pUserId	 						= pUserId.trim();
		RestQuery queryToRun 			= null;
		String strToAppend 				= null;
		RestQuery queryToRunInsideLoop	= null;
		
		strToAppend 					= JiveConstants.GET_USER_ACTIVITIES_Value;
		
		queryToRun 						= mQueryEngine.findQuery(JiveConstants.GET_USER_MISC_ATTRIBUTES);
		
		queryToRunInsideLoop 			= (RestQuery) queryToRun.clone();

		queryToRunInsideLoop.setURL(queryToRunInsideLoop.getURL() + pUserId + "/" + strToAppend + "?count=" + JiveConstants.PAGINATION_PAGE_SIZE);
		
		if (pAfterDate != null && !pAfterDate.isEmpty())
		{
			queryToRunInsideLoop.setURL(queryToRunInsideLoop.getURL() + "&after=" + pAfterDate);
		}
		else if (pBeforeDate != null && !pBeforeDate.isEmpty())
		{
			queryToRunInsideLoop.setURL(queryToRunInsideLoop.getURL() + "&before=" + pBeforeDate);
		}
			
		
		RestQuerySimpleResult myResult 	= mQueryEngine.runSimpleQueryAsSuch(queryToRunInsideLoop);
		
		if (!validateMyLinkResponse(myResult))
		{
			LOGGER.error("JiveAPIHelper: getUserActivities(): Unable to get activities for the user: " + pUserId + ". Respose received from the Jive API is not valid.");
			myResultArrayList.add(myResult);
			return myResultArrayList;
		}
		
		myResultArrayList.add(myResult);
		
		if (JiveConstants.T0_DEBUG){
			LOGGER.info("JiveAPIHelper: getUserActivities(): Activity: " + JiveConstants.GET_USER_ACTIVITIES_Key + ", Query to run 'as such' : " + queryToRunInsideLoop.getURL() + ".");
			LOGGER.info("JiveAPIHelper: getUserActivities(): Response status code: " + myResult.getStatusCode() + ", size of response:" + myResult.getResponseSize() + ", myResultArrayList.size: " + myResultArrayList.size());
		}
		
		String[] prevAndNextLinksForMyResult 	= getPreviousAndNextLinks(myResult, pUserId, JiveConstants.GET_USER_ACTIVITIES_Key);		
		if (prevAndNextLinksForMyResult != null)
		{
			RestQuerySimpleResult myResultInsideLoop 		= null;
			String[] prevAndNextLinksForMyResultNextLoop 	= new String[]{null,null}; 
			do{
				// processing previous link recursively
				if (prevAndNextLinksForMyResult[0] != null)
				{
					queryToRunInsideLoop 	= (RestQuery) queryToRun.clone();
					queryToRunInsideLoop.setURL(prevAndNextLinksForMyResult[0]);
					myResultInsideLoop 		= mQueryEngine.runSimpleQueryAsSuch(queryToRunInsideLoop);
					
					if (JiveConstants.T0_DEBUG){
						LOGGER.info("JiveAPIHelper: getUserActivities(): Response status code: " + myResultInsideLoop.getStatusCode() + ", size of response:" + myResultInsideLoop.getResponseSize());
						LOGGER.info("JiveAPIHelper: getUserActivities(): Getting Previous link. Activity: " + JiveConstants.GET_USER_ACTIVITIES_Key + ", Query to run 'as such' : " + queryToRunInsideLoop.getURL() + ".");
					}
					
					if (validateMyLinkResponse(myResultInsideLoop))
					{
						myResultArrayList.add(myResultInsideLoop);
						String[] prevAndNextLinksForMyResultPrev 	= getPreviousAndNextLinks(myResultInsideLoop, pUserId, JiveConstants.GET_USER_ACTIVITIES_Key);
						if (prevAndNextLinksForMyResultPrev != null && prevAndNextLinksForMyResultPrev[0] != null)
						{
							prevAndNextLinksForMyResultNextLoop[0] = prevAndNextLinksForMyResultPrev[0];
						}
						else
						{
							prevAndNextLinksForMyResultNextLoop[0] = null;
						}
					}
					else
					{
						prevAndNextLinksForMyResultNextLoop[0] = null;
					}
						
				}
				
				myResultInsideLoop = null;
				// processing next link 
				if (prevAndNextLinksForMyResult[1] != null)
				{
					queryToRunInsideLoop 	= (RestQuery) queryToRun.clone();
					queryToRunInsideLoop.setURL(prevAndNextLinksForMyResult[1]);
					myResultInsideLoop 		= mQueryEngine.runSimpleQueryAsSuch(queryToRunInsideLoop);
					
					if (JiveConstants.T0_DEBUG){
						LOGGER.info("JiveAPIHelper: getUserActivities(): Getting Next link. Activity: " + JiveConstants.GET_USER_ACTIVITIES_Key + ", Query to run 'as such' : " + queryToRunInsideLoop.getURL() + ".");
						LOGGER.info("JiveAPIHelper: getUserActivities(): Response status code: " + myResultInsideLoop.getStatusCode() + ", size of response:" + myResultInsideLoop.getResponseSize());
					}
					if (validateMyLinkResponse(myResultInsideLoop))
					{
						myResultArrayList.add(myResultInsideLoop);
						String[] prevAndNextLinksForMyResultNexy 	= getPreviousAndNextLinks(myResultInsideLoop, pUserId, JiveConstants.GET_USER_ACTIVITIES_Key);
						if (prevAndNextLinksForMyResultNexy != null && prevAndNextLinksForMyResultNexy[1] != null)
						{
							prevAndNextLinksForMyResultNextLoop[1] = prevAndNextLinksForMyResultNexy[1];
						}
						else
						{
							prevAndNextLinksForMyResultNextLoop[1] = null;
						}
					}
					else
					{
						prevAndNextLinksForMyResultNextLoop[1] = null;
					}
					
				}
			
				prevAndNextLinksForMyResult = prevAndNextLinksForMyResultNextLoop;
			
			} while (	prevAndNextLinksForMyResultNextLoop != null 
						&& 	(prevAndNextLinksForMyResultNextLoop[0] !=null || prevAndNextLinksForMyResultNextLoop[1] !=null )
					);
		}
		
		if (JiveConstants.T0_DEBUG){
			LOGGER.info("JiveAPIHelper: getUserActivities(): Exiting method. Size of ResultArrayList: " + myResultArrayList.size());
		}
			
		return myResultArrayList;	
					
		
		
	}

	
	
	public ArrayList<RestQuerySimpleResult> getUserFollowersAndFollowings(String pUserId, String pQueryName, Long pCount) throws RestQueryEngineException {

		if (JiveConstants.T0_DEBUG){
			LOGGER.info("JiveAPIHelper: getUserFollowersAndFollowings(): Entering method. Params are: " + "pUserId: " + pUserId + ", pQueryName: " + pQueryName + ", pCount: " + pCount + ".");
		}

		if (pUserId == null || pUserId.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: getUserFollowersAndFollowings(): User ID provided is empty or Null - " + pUserId + ".");
			return null;
		}

		if (!pUserId.matches("[0-9]+")) {
			LOGGER.error("JiveAPIHelper: getUserFollowersAndFollowings(): User ID provided should only be Numeric. User ID provided is: " + pUserId + ".");
			return null;
		}
		
		ArrayList<RestQuerySimpleResult> myResultArrayList 	= new ArrayList<RestQuerySimpleResult>();
		ArrayList<Integer[]> paramToAppendArrayList 		= new ArrayList<Integer[]>(); 	// [start index (zero based), number of records to retrieve]	
		pUserId	 					= pUserId.trim();
		RestQuery queryToRun 		= null;
		String strToAppend 			= null;
		
		
		// getting user's followers
		 if (pQueryName.equals(JiveConstants.GET_USER_FOLLOWERS_ACTIVITY_Key)) {

			if (pCount == null || pCount == 0) {
				if (pCount == null) {
					LOGGER.error("JiveAPIHelper: getUserFollowersAndFollowings(): Follower count provided to this method is NULL, hence skipping the call to jive api for user id:" + pUserId + ".");
				} 
				else {
					if (JiveConstants.T0_DEBUG){
						LOGGER.info("JiveAPIHelper: getUserFollowersAndFollowings(): Follower count provided to this method is Zero, hence skipping the call to jive api for user id:" + pUserId + ".");
					}
				}
				return null;
			}

			strToAppend 		= JiveConstants.GET_USER_FOLLOWERS_ACTIVITY_Value;
			queryToRun 			= mQueryEngine.findQuery(JiveConstants.GET_USER_MISC_ATTRIBUTES);
			// determining the count and the startindex
			int tempStartIndex 	= 0;
			int startIndexJump 	= JiveConstants.PAGINATION_PAGE_SIZE;

			while (tempStartIndex < pCount) // less than since startIndex is zero-based index ;; assumption int would be sufficient even though pCount value is Long
			{
				paramToAppendArrayList.add(new Integer[] { tempStartIndex, startIndexJump });
				tempStartIndex	+= startIndexJump;
			}

			/*
			 * count ===> Number of followers to be returned (default is 25)
			 * 				Max count we can set up is 1000. After increasing up to 1001 or above, it throws error - 400 error.
			 * startIndex ===> Zero-relative index of the first follower to be returned
			 * 
			 * The response also returns the pre and post sequence calls links
			 */

		} 	// GET_USER_FOLLOWERS_ACTIVITY_Key

		
		// getting user's friends(following)
		else if (pQueryName.equals(JiveConstants.GET_USER_FOLLOWING_ACTIVITY_Key)) {

			if (pCount == null || pCount == 0) {
				if (pCount == null) {
					LOGGER.error("JiveAPIHelper: getUserFollowersAndFollowings(): Following count provided to this method is NULL, hence skipping the call to jive api for user id:"	+ pUserId + ".");
				} 
				else {
					if (JiveConstants.T0_DEBUG){
						LOGGER.info("JiveAPIHelper: getUserFollowersAndFollowings(): Following count provided to this method is Zero, hence skipping the call to jive api for user id:"	+ pUserId + ".");
					}
				}
				return null;
			}

			strToAppend 		= JiveConstants.GET_USER_FOLLOWING_ACTIVITY_Value;
			queryToRun 			= mQueryEngine.findQuery(JiveConstants.GET_USER_MISC_ATTRIBUTES);
			// determining the count and the startindex
			int tempStartIndex 	= 0;
			int startIndexJump 	= JiveConstants.PAGINATION_PAGE_SIZE;

			while (tempStartIndex < pCount) // less than since startIndex is zero-based index ;; assumption int would be sufficient even though pCount value is Long
			{
				paramToAppendArrayList.add(new Integer[] { tempStartIndex, startIndexJump });
				tempStartIndex 	+= startIndexJump;
			}

			/*
			 * count ===> Number of followers to be returned (default is 25)
			 * 				Max count we can set up is 1000. After increasing up to 1001 or above, it throws error - 400 error.
			 * startIndex ===> Zero-relative index of the first following to be returned
			 * 
			 * The response also returns the pre and post sequence calls links
			 * 
			 * "links" : { "previous" :
			 * "https://sandbox.jiveon.com/api/core/v3/people/2003/@following?fields=%40summary&count=5&startIndex=5"
			 * , "next" :
			 * "https://sandbox.jiveon.com/api/core/v3/people/2003/@following?fields=%40summary&count=5&startIndex=15"
			 * },
			 * 
			 */

		}	// GET_USER_FOLLOWING_ACTIVITY_Key

		
		if (queryToRun == null || queryToRun.getURL() == null || queryToRun.getURL().trim().length() == 0 ) {
			LOGGER.error("JiveAPIHelper: getUserFollowersAndFollowings(): queryToRun is empty or Null - Userid: " + pUserId + ", pQueryName: " + pQueryName + ".");
			return null;
		}

		
		for (Integer[] paramsToAppendForPagination : paramToAppendArrayList) {
			if ((paramsToAppendForPagination != null) && (paramsToAppendForPagination.length == 2)) {
				String paramsToAppendForPaginationStr = "?startIndex=" + paramsToAppendForPagination[0] + "&count=" + paramsToAppendForPagination[1];

				RestQuery queryToRunInsideLoop 			= (RestQuery) queryToRun.clone();
				String queryToRunInsideLoopStr			= queryToRun.getURL() + pUserId + "/" + strToAppend + (paramsToAppendForPaginationStr == null ? "" : paramsToAppendForPaginationStr); 
				queryToRunInsideLoop.setURL(queryToRunInsideLoopStr);

				RestQuerySimpleResult myResult 			= mQueryEngine.runSimpleQueryAsSuch(queryToRunInsideLoop);

				if (JiveConstants.T0_DEBUG){
					LOGGER.info("JiveAPIHelper: getUserFollowersAndFollowings(): Running Query for user id: " + pUserId + ", pQueryName: " + pQueryName + ", query used: " + queryToRunInsideLoop.getURL() + ".");
					LOGGER.info("JiveAPIHelper: getUserFollowersAndFollowings(): Result of query is: " + "Status code: " + myResult.getStatusCode() + ", Response size: "+ myResult.getResponseSize() + ", Response: " + myResult.getResponse() + ".");
				}

				myResultArrayList.add(myResult);

			}
		}
		
		if (JiveConstants.T0_DEBUG){
			LOGGER.info("JiveAPIHelper: getUserFollowersAndFollowings(): Exiting method. Size of the resultArraylist: " + ( myResultArrayList==null?0:myResultArrayList.size()) + ".");
		}

		return myResultArrayList;
				
	}
	
	
	
	
	
	/**
	 * A helper method used to validate the response from the Jive API, for pagination.
	 * @param pResult
	 * @return
	 */
	private boolean validateMyLinkResponse(RestQuerySimpleResult pResult)
	{
		if (pResult == null || pResult.getStatusCode() != 200 || pResult.getResponse() == null || pResult.getResponse().trim().length() == 0  )
		{
			return false;
		}
			
		pResult.trimMResponseFirstOccuranceOnly(JiveConstants.RESPONSE_STRING_TO_TRIM, JiveConstants.RESPONSE_STRING_TO_TRIM_WITH);
		
		Object myParsedObject = JsonHelper.parse(pResult.getResponse());
		if (!(myParsedObject instanceof JSONObject)) {
			return false;
		}

		JSONObject myParsedJsonObject = (JSONObject) myParsedObject;
		Object myParsedJsonObjectArray = myParsedJsonObject.get("list");

		if (!(myParsedJsonObjectArray != null && myParsedJsonObjectArray instanceof JSONArray && myParsedJsonObjectArray instanceof List<?>)) {
			return false;
		}
		
		return true;
		
	}
	
	
	/**
	 * A helper method used for obtaining paginated response from JIVE. It extracts the next and previous links if any from the Jive response so that subsequent
	 * calls can be made using next or previous links.
	 * @param pResult
	 * @return
	 */
	private String[] getPreviousAndNextLinks(RestQuerySimpleResult pResult, String pUserId, String pActionPerformed) {
		
		if (JiveConstants.T0_DEBUG){
			if (pResult == null) {
				LOGGER.info("JiveAPIHelper: getPreviousAndNextLinks(): pResult is null. Exiting the method.");
				return null;
			}
			LOGGER.info("JiveAPIHelper: getPreviousAndNextLinks(): Entering method. Params: RestQueryResult status: " + pResult.getStatusCode() + ", RestQueryResult size: " + pResult.getResponseSize() + ",  pUserId: " + pUserId + ", pActionPerformed: " + pActionPerformed + "." );
		}
		
		if (pResult.getResponse() != null && pResult.getResponse().length() != 0 && pResult.getStatusCode() == 200) {
			
			pResult.trimMResponseFirstOccuranceOnly(JiveConstants.RESPONSE_STRING_TO_TRIM, JiveConstants.RESPONSE_STRING_TO_TRIM_WITH);

			Object myParsedObject = JsonHelper.parse(pResult.getResponse());

			if (!(myParsedObject instanceof JSONObject)) {
				LOGGER.error("JiveAPIHelper: getPreviousAndNextLinks(): Error parsing Json object. This occured for userID: " + pUserId + ", Action Perfomed: " + pActionPerformed + ".");
				return null;
			}

			JSONObject myParsedJsonObject 	= (JSONObject) myParsedObject;
			Object myParsedLinksObject 		= myParsedJsonObject.get(JiveConstants.PAGINATION_NEXT_AND_PREVIOUS_LINKS);

			if (myParsedLinksObject != null && myParsedLinksObject instanceof JSONObject)
			{
				JSONObject myParsedLinksJasonObject = (JSONObject)myParsedLinksObject;
				
				String nextLink = (String) myParsedLinksJasonObject.get(JiveConstants.PAGINATION_NEXT_LINK);
				String prevLink = (String) myParsedLinksJasonObject.get(JiveConstants.PAGINATION_PREVIOUS_LINK);
				// no need for decoding
				if (JiveConstants.T0_DEBUG){
					LOGGER.info("JiveAPIHelper: getPreviousAndNextLinks(): Previous Link: " + (prevLink==null?null:prevLink) + ", Next Link: " + (nextLink==null?null:nextLink) + ".");
				}
				return new String[]{prevLink, nextLink};
				
			}
			else
			{
				if (JiveConstants.T0_DEBUG){
					if (myParsedLinksObject == null)
					{
						LOGGER.info("JiveAPIHelper: getPreviousAndNextLinks(): No links found. ");
						return null;
					}
					else
					{
						LOGGER.error("JiveAPIHelper: getPreviousAndNextLinks(): Error getting myParsedLinksObject. ");
						return null;
					}
				}
				
			}
			
		}
		
		else
		{
			if (JiveConstants.T0_DEBUG){
				if (pResult.getStatusCode() != 200)  {   	
					LOGGER.info("JiveAPIHelper: getPreviousAndNextLinks(): Error: pResult.getStatusCode() != 200. " );
				}
				else
				{
					LOGGER.error("JiveAPIHelper: getPreviousAndNextLinks(): Error: pResult is null. " );
				}
			}
		}
		
		return null;

	}	


	
	
	 /**
     * 
     *	Sends a message to a Jive user or Posts on user's activity board.
     * 	
     * This is not being used right now.
     * 
     */
	
    public RestQuerySimpleResult sendPrivateMessage(String pUsername, String pMessageSubject, String pMessageContent, String pToUserList) {
    	/*
  	
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: sendPrivateMessage(): Entering method.");
		}

		if (pUsername == null || pUsername.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: sendPrivateMessage(): User name provided is empty or null - " + pUsername + ".");
			return null;
		}

		if (pMessageSubject == null || pMessageSubject.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: sendPrivateMessage(): Message Subject is Null or Empty. We cannot send a message without a subject. Exiting the method.");
			return null;
		}
		if (pMessageContent == null || pMessageContent.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: sendPrivateMessage(): Message Content is Null or Empty. We cannot send a message without any content/body. Exiting the method.");
			return null;
		}
		if (pToUserList == null || pToUserList.trim().length() == 0) {
			LOGGER.error("JiveAPIHelper: sendPrivateMessage(): Message Recipient(s) is Null or Empty. We cannot send a message without any recipient(s) info. Exiting the method.");
			return null;
		}
		
		pUsername			= pUsername.trim();
		pMessageSubject 	= pMessageSubject.trim();
		pMessageContent 	= pMessageContent.trim();
		pToUserList 		= pToUserList.trim();
		
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: sendPrivateMessage(): Calling Xing API...");
		}

		Map<String, String> myPostParams	= new HashMap<String, String>(2);
		
		StringBuilder mySubject 			= new StringBuilder("\"subject\": \"");
        mySubject.append(pMessageSubject).append("\"");
        
        StringBuilder myType 				= new StringBuilder("\"type\": \"");
        myType.append("post").append("\"");
        
        
        StringBuilder myMessageBody 		= new StringBuilder("\"content\": {\"type\" : \"text/html\", \"text\" :  \"<body><p>");
        myMessageBody.append(pMessageContent).append("</p></body>\" }");
        
		//myPostParams.put("subject",		pMessageSubject	);
		//myPostParams.put("type",			"post"	);
		
		myPostParams.put("author",			pUsername	);
		//myPostParams.put("content",		pMessageContent	);
		
		myPostParams.put("recipient_ids",	pToUserList	);
		//myPostParams.put("user_id",			pFromUser	);
		
		 // build the post body as json string
        String myPostBody 					=  String.format("{%s,%s,%s}", myMessageBody.toString(), mySubject.toString(), myType.toString());
		
		
		
		 String myPostBody2 = "{"+
							  "\"content\":"+
							     " {"+
							    "  \"type\": \"text/html\","+
							   "   \"text\": \"<body><p>Some interesting text</p></body>\" "+
							  "    },"+
							  "\"subject\": \"New Post in Personal Blog32423423423432423432432\","+
							  "\"type\": \"post"+
							  "}";
		
		LOGGER.info("JiveAPIHelper: sendPrivateMessage(): myPostBody2 >>> : " + myPostBody2);
		 
		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(JiveConstants.ACTION_SEND_PRIVATE_MESSAGE,null, null, null, null, myPostBody2);

		LOGGER.info(myResult);
		
		if (JiveConstants.T0_DEBUG) {
			LOGGER.info("JiveAPIHelper: sendPrivateMessage(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}

		return myResult;
		
		*/
    	
    	return null;

	}
   


}


