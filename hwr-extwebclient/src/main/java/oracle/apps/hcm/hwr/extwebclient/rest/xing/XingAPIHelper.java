/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.extwebclient.rest.xing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQuerySignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.OAuthException;
import oracle.apps.hcm.hwr.extwebclient.rest.OAuthSignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;
import oracle.apps.hcm.hwr.extwebclient.rest.apiuse.ConnectorApiUseage;
import oracle.apps.hcm.hwr.extwebclient.rest.oauth.OAuthAccessTokenResponse;
import oracle.apps.hcm.hwr.extwebclient.rest.oauth.OAuthRequestTokenResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * Xing API Helper class
 * 
 * @author Ibtisam Haque
 *
 */
 
public class XingAPIHelper {

	/** REST query engine */
	private final RestQueryEngine mQueryEngine;

	/** The logger instance */
	private static final Log LOGGER = LogFactory.getLog(XingAPIHelper.class);

	private ConnectorApiUseage mApiUseage;
	
	/**
	 * Sets the application related data to make API calls successfully.
	 * 
	 * @param pApiKey
	 * 				(Customer Key) API key for the application.
	 * @param pApiSecret
	 * 				(Customer Secret) API secret for the application.
	 */
	public XingAPIHelper(String pApiKey, String pApiSecret) {
		mQueryEngine = new RestQueryEngine(new XingRestQueryProvider(), new OAuthSignatureProvider(pApiKey, pApiSecret));
		LOGGER.info("XingAPIHelper: XingAPIHelper(): Created an instance of Xing APIHelper.");
	}

    public void setApiUseageClass(ConnectorApiUseage pApiUseage) {
        mApiUseage = pApiUseage;
        mQueryEngine.setConnectorApiUseage(mApiUseage);
    }
	
	/**
	 * Returns the profile information for the owner of the access token.
	 * 
	 * @param pAccessToken
	 *            	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *            	The access secret used to read private data.
	 *            
	 * @return 		
	 * 				The RestQuerySimpleResult instance containing the response and the HTTP status code.
	 */
	public RestQuerySimpleResult getMyProfile(String pAccessToken, String pAccessTokenSecret) {

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyProfile(): Entering method.");
		}

		boolean areAllInputsValid = performSanityCheckOnVariables(pAccessToken, pAccessTokenSecret);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getMyProfile():  We cannot make a private API call using empty/null Access Token or Access Secret. Exiting the method.");
			return null;
		}

		pAccessToken 		= pAccessToken.trim();
		pAccessTokenSecret 	= pAccessTokenSecret.trim();

		// Set parameters for OAuth
		setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyProfile(): Calling Xing API...");
		}

		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_GET_MY_PROFILE, null, null, null, null);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyProfile(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}

		return myResult;

	}
	
	

	
	/**
	 * Gets the count of the number of contacts for the current user (owner of the access token).
	 * <br>This method by default uses limit 0, to merely get the count without any user ids. 
	 * <p>
	 * Note: If you want to use limit or get contacts' ids, use the other method - getMyContactsIds()
	 * 
	 * @param pAccessToken
	 *            	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *       		The access secret used to read private data.
	 *            
	 * @return		
	 * 				The RestQuerySimpleResult instance containing the response and the HTTP status code.
	 * 
	 */
	public RestQuerySimpleResult getMyContactsCount(String pAccessToken, String pAccessTokenSecret) {

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyContactsCount(): Entering method.");
		}

		boolean areAllInputsValid = performSanityCheckOnVariables(pAccessToken, pAccessTokenSecret);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getMyContactsCount():  We cannot make a private API call using empty/null Access Token or Access Secret. Exiting the method.");
			return null;
		}

		pAccessToken 		= pAccessToken.trim();
		pAccessTokenSecret 	= pAccessTokenSecret.trim();

		// Set parameters for OAuth
		setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

		Map<String, String> myGetParams = new HashMap<String, String>(1);
		myGetParams.put("limit", "0");

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyContactsCount(): Calling Xing API...");
		}

		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_GET_CONTACTS_IDS, null, null, myGetParams, null);
		
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyContactsCount(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}

		return myResult;

	}

	
	
	
	/**
	 * Performs sanity check on the input parameters (user key + user secret) to XingAPIHelper class methods before making private API calls
	 * 
	 * @param pAccessToken
	 *            	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *            	The access secret used to read private data.
	 * @return
	 * 				<br>True	--- If both the access token AND access secret are:
	 * 									<br>Not null
	 * 									<br>Non-empty
	 * 									<br>Alphanumeric - Characters allowed: [a-zA-Z0-9]
	 * 				<p>False	--- Otherwise
	 * 
	 */
	private boolean performSanityCheckOnVariables(String pAccessToken, String pAccessTokenSecret) {
		if (pAccessToken == null || pAccessToken.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariables(): Access Token is Null or Empty. We cannot make a private API call using empty/null Access Token.");
			return false;
		}

		if (pAccessTokenSecret == null || pAccessTokenSecret.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariables(): Access Secret is Null or Empty. We cannot make a private API call using empty/null Access Secret.");
			return false;
		}

		if (!pAccessToken.matches("[a-zA-Z0-9]+")) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariables(): Access Token provided should only be Alphanumeric. Characters allowed: [a-zA-Z0-9].");
			return false;
		}

		if (!pAccessTokenSecret.matches("[a-zA-Z0-9]+")) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariables(): Access Secret provided should only be Alphanumeric. Characters allowed: [a-zA-Z0-9].");
			return false;
		}

		return true;

	}

	
	
	
	/**
	 * Gets the ids of the contacts of the current user
	 * 
	 * @param pProfileID
	 * 				Profile id of the current user. (this is needed just for logging). Must be a non-empty/non-null string.
	 * @param pAccessToken
	 *           	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *            	The access secret used to read private data.
	 * @param pCount
	 *            	Total number of contacts ids to fetch. This count is NOT zero based.
	 *            	<p>If pCount == null, 		it returns null 
	 *            	<br>If pCount < -1, 		it returns null 
	 *            	<br>If pCount == 0, 		it returns empty arraylist (arraylist of size 0)
	 *            	<br>If 1<=pCount>=100, 		it return list of ids 
	 *            	<br>If pCount > 100, 		it returns paginated list of ids back with each paginated list (except the last one) of size 100 
	 *            	<br>(By default, pCount value is 10, if it is not specified)
	 * 
	 * @return		
	 * 				ArrayList of the RestQuerySimpleResult containing the paginated response from the Xing API
	 * 
	 */
	public ArrayList<RestQuerySimpleResult> getMyContactsIds(String pProfileID, String pAccessToken, String pAccessTokenSecret, Long pCount) {
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyContactsIds(): Entering method. ProfileId:" + pProfileID + ", Count:" + pCount + ".");
		}

		if (pProfileID == null || pProfileID.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: getMyContactsIds(): Profile Id is Null or Empty. We cannot make a private API call when the profile id is empty/null."
					+ "\n Note: This has been done as part of sanity check. ");
			return null;
		}

		boolean areAllInputsValid = performSanityCheckOnVariables(pAccessToken, pAccessTokenSecret);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getMyContactsIds():  We cannot make a private API call using empty/null Access Token or Access Secret. ProfileId:"	+ pProfileID + ". Exiting the method.");
			return null;
		}
		
		pProfileID 				= pProfileID.trim();
		pAccessToken 			= pAccessToken.trim();
		pAccessTokenSecret 		= pAccessTokenSecret.trim();

		
		if (pCount == null) {
			LOGGER.error("XingAPIHelper: getMyContactsIds(): Contacts count provided to this method is NULL, hence skipping the call to xing api for contacts' id. ProfileId:" + pProfileID + ". Exiting the method.");
			return null;
		} 
		else if (pCount < 0L) {
			LOGGER.error("XingAPIHelper: getMyContactsIds(): Contacts count provided is less than MIN limit of 0. Skipping the call to xing api for contacts' id. Count:" + pCount + ", ProfileId:" + pProfileID
					+ ". Exiting the method.");
			return null;
		} 
		else if (pCount == 0L) {
			LOGGER.info("XingAPIHelper: getMyContactsIds(): Contacts count provided to this method is Zero, hence skipping the call to xing api for contacts id. ProfileId:" + pProfileID + ". Exiting the method.");
			// this means that this user doesn't have any contacts
			return new ArrayList<RestQuerySimpleResult>(0);
		}


		ArrayList<RestQuerySimpleResult> myResultArrayList 	= new ArrayList<RestQuerySimpleResult>();
		ArrayList<Integer[]> paramToAppendArrayList 		= new ArrayList<Integer[]>(); // [start index (zero based), number of records to retrieve]
		
		
		// Set parameters for OAuth
		setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

		// determining the count and the startindex
		int tempStartIndex 	= 0;
		int startIndexJump 	= XingConstants.API_CONTACTS_LIMIT;

		
		/*
		 * less than since startIndex is zero-based index;
		 * assumption int would be sufficient even though pCount value is Long;
		 * i.e max value currently this method would support would be 2,147,483,647 (inclusive)
		 * 
		 */
		while (tempStartIndex < pCount) 
		{
			paramToAppendArrayList.add(new Integer[] { tempStartIndex, startIndexJump });
			tempStartIndex += startIndexJump;
		}

		for (Integer[] paramsToAppendForPagination : paramToAppendArrayList) {
			if ((paramsToAppendForPagination != null) && (paramsToAppendForPagination.length == 2)) 
			{

				Map<String, String> myGetParams = new HashMap<String, String>(2);
				
				myGetParams.put("offset", 	("" + paramsToAppendForPagination[0]));
				myGetParams.put("limit", 	("" + paramsToAppendForPagination[1]));

				if (XingConstants.T0_DEBUG) 
				{
					LOGGER.info("XingAPIHelper: getMyContactsIds(): Calling Xing API...");
				}

				RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_GET_CONTACTS_IDS, null, null, myGetParams, null);
				
				if (XingConstants.T0_DEBUG) {
					LOGGER.info("XingAPIHelper: getMyContactsIds(): INSIDE LOOP only. myResult: "
							+ (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
				}
				
				if (myResult == null)
				{
					LOGGER.error("XingAPIHelper: getMyContactsIds(): INSIDE LOOP only. myResult is NULL for "+ myGetParams.toString() + ". Profile id: " + pProfileID + ".");
				}
				if (myResult.getStatusCode() != XingConstants.API_SUCCESS_CODE_200)
				{
					LOGGER.error("XingAPIHelper: getMyContactsIds(): INSIDE LOOP only. myResult is was NOT successful for "+ myGetParams.toString() + ". Profile id: " + pProfileID + ".");
				}
				
				// in either of the above case, i will still add result to myResultArrayList
				
				myResultArrayList.add(myResult);

			}
		}

		
		
		if (XingConstants.T0_DEBUG) 
		{
			LOGGER.info("XingAPIHelper: getMyContactsIds(): Exiting method. Size of the resultArraylist: " + (myResultArrayList == null ? 0 : myResultArrayList.size()) + ".");
		}

		return myResultArrayList;

	}
	
	
	

	/**
	 * Gets the profiles of contacts in relation to the current user.
	 * This in relation is important. Because, a user X who is a connection of both A and B, could POTENTIALLY have different profiles when fetched via A than via B, 
	 * based upon X's privacy settings in relation to A and B respectively.
	 * 
	 * @param pProfileID
	 * 				Profile id of the current user (this is needed just for logging). Must be a non-empty/non-null string.
	 * @param pAccessToken
	 *           	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *            	The access secret used to read private data.
	 * @param pContactsIdList
	 *            	String List specifying contacts' ids. 
	 *            	<br>If list is null, 		it returns null 
	 *            	<br>If list is empty, 		it returns empty arraylist 
	 *            	<br>else,					it returns paginated response of profiles 
	 *            	
	 * @return		
	 * 				ArrayList of the RestQuerySimpleResult containing the paginated response from the Xing API
	 * 
	 */
	public ArrayList<RestQuerySimpleResult> getMyContactsProfiles(String pProfileID, String pAccessToken, String pAccessTokenSecret, List<String> pContactsIdList) {
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyContactsProfiles(): Entering method. ProfileId:" + pProfileID + ", pContactsIdList:" + pContactsIdList + ".");
		}

		if (pProfileID == null || pProfileID.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: getMyContactsProfiles(): Profile Id is Null or Empty. We cannot make a private API call when the profile id is empty/null."
					+ "\n Note: This has been done as part of sanity check. ");
			return null;
		}

		boolean areAllInputsValid = performSanityCheckOnVariables(pAccessToken, pAccessTokenSecret);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getMyContactsProfiles():  We cannot make a private API call using empty/null Access Token or Access Secret. ProfileId:"
					+ pProfileID + ". Exiting the method.");
			return null;
		}

		pProfileID 			= pProfileID.trim();
		pAccessToken 		= pAccessToken.trim();
		pAccessTokenSecret 	= pAccessTokenSecret.trim();

		if (pContactsIdList == null) 
		{
			LOGGER.error("XingAPIHelper: getMyContactsProfiles(): Contacts id list provided to this method is NULL, hence skipping the call to xing api for contacts' profile. ProfileId:"
					+ pProfileID + ". Exiting the method.");
			return null;
		} 
		else if (pContactsIdList.size() == 0) 
		{
			LOGGER.info("XingAPIHelper: getMyContactsProfiles(): Contacts id list provided to this method is Zero, hence skipping the call to xing api for contacts' profile. ProfileId:"
					+ pProfileID + ". Exiting the method.");;
			return new ArrayList<RestQuerySimpleResult>(0);
		}

		ArrayList<RestQuerySimpleResult> myResultArrayList	= new ArrayList<RestQuerySimpleResult>();
		ArrayList<Integer[]> paramToAppendArrayList 		= new ArrayList<Integer[]>(); // [start index (zero based), number of records to retrieve]

		// Set parameters for OAuth
		setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

		// determining the substring points of the contact id lists
		int startIndex = 0;
		
		/*
		 * less than since startIndex is zero-based index;
		 * assumption int would be sufficient even though pCount value is Long;
		 * i.e max value currently this method would support would be 2,147,483,647 (inclusive)
		 * 
		 */
		while (startIndex < pContactsIdList.size()) 
		{
			int endIndex = startIndex + XingConstants.API_CONTACTS_LIMIT;

			if (endIndex > pContactsIdList.size()) {
				endIndex = pContactsIdList.size();
			}
			paramToAppendArrayList.add(new Integer[] { startIndex, endIndex });
			startIndex = endIndex;
		}

		
		for (Integer[] paramsToAppendForPagination : paramToAppendArrayList) {
			if ((paramsToAppendForPagination != null) && (paramsToAppendForPagination.length == 2)) 
			{

				RestQuery queryToRun 		= mQueryEngine.findQuery(XingConstants.ACTION_GET_CONTACTS_PROFILE);
				StringBuilder strToAppend 	= new StringBuilder("");
				for (int i = paramsToAppendForPagination[0]; i < paramsToAppendForPagination[1]; i++)
				{
					strToAppend.append(pContactsIdList.get(i));
					if (i != (paramsToAppendForPagination[1] - 1) )
					{
						strToAppend.append(",");
					}
				}
				
				queryToRun.setURL(queryToRun.getURL() + strToAppend);

				if (XingConstants.T0_DEBUG) {
					LOGGER.info("XingAPIHelper: getMyContactsProfiles(): Calling Xing API...");
					LOGGER.info("XingAPIHelper: getMyContactsProfiles(): Calling Xing API. queryToRun:" + queryToRun.getURL());
				}

				RestQuerySimpleResult myResult = mQueryEngine.runSimpleQueryAsSuch(queryToRun);

				if (XingConstants.T0_DEBUG) {
					LOGGER.info("XingAPIHelper: getMyContactsProfiles(): INSIDE LOOP only. myResult: "
							+ (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
				}
				
				if (myResult == null)
				{
					LOGGER.error("XingAPIHelper: getMyContactsProfiles(): INSIDE LOOP only. myResult is NULL for "+ strToAppend.toString() + ". Profile id: " + pProfileID + ".");
				}
				if (myResult.getStatusCode() != XingConstants.API_SUCCESS_CODE_200)
				{
					LOGGER.error("XingAPIHelper: getMyContactsProfiles(): INSIDE LOOP only. myResult is was NOT successful for "+ strToAppend.toString() + ". Profile id: " + pProfileID + ".");
				}
				
				// in either of the above case, i will still add result to myResultArrayList
				
				myResultArrayList.add(myResult);

			}
		}

		
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyContactsProfiles(): Exiting method. Size of the resultArraylist: "
					+ (myResultArrayList == null ? 0 : myResultArrayList.size()) + ".");
		}

		return myResultArrayList;

	}

	
	
	
	/**
	 * Returns the profile message for the owner of the access token.
	 * 
	 * @param pAccessToken
	 *            	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *            	The access secret used to read private data.
	 *            
	 * @return 		
	 * 				The RestQuerySimpleResult instance containing the response and the HTTP status code.
	 * 
	 */
	public RestQuerySimpleResult getMyProfileMessage(String pAccessToken, String pAccessTokenSecret) {

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyProfileMessage(): Entering method.");
		}

		boolean areAllInputsValid = performSanityCheckOnVariables(pAccessToken, pAccessTokenSecret);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getMyProfileMessage():  We cannot make a private API call using empty/null Access Token or Access Secret. Exiting the method.");
			return null;
		}

		pAccessToken 		= pAccessToken.trim();
		pAccessTokenSecret 	= pAccessTokenSecret.trim();

		// Set parameters for OAuth
		setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyProfileMessage(): Calling Xing API...");
		}

		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_GET_MY_PROFILE_MESSAGE, null, null, null, null);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyProfileMessage(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}

		return myResult;

	}
	
	
	
	
	/**
	 * Returns the activities feed for the owner of the access token.
	 * 
	 * @param pAccessToken
	 *            	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *            	The access secret used to read private data.
	 * @param pLastRunDateStr
	 *            	Last Run date. 
	 *            	<br>If its null, then it will return everything.
	 *            
	 * @return 		
	 * 				The RestQuerySimpleResult instance containing the response and the HTTP status code.
	 * 
	 */
	public RestQuerySimpleResult getMyActivitiesFeed(String pAccessToken, String pAccessTokenSecret, String pLastRunDateStr) {

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyActivitiesFeed(): Entering method. pLastRunDateStr:" + pLastRunDateStr);
		}

		boolean areAllInputsValid = performSanityCheckOnVariables(pAccessToken, pAccessTokenSecret);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getMyActivitiesFeed():  We cannot make a private API call using empty/null Access Token or Access Secret. Exiting the method.");
			return null;
		}

		pAccessToken 					= pAccessToken.trim();
		pAccessTokenSecret 				= pAccessTokenSecret.trim();
		RestQuerySimpleResult myResult 	= null;
		
		// Set parameters for OAuth
		setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyActivitiesFeed(): Calling Xing API...");
		}
		
		if (pLastRunDateStr != null && pLastRunDateStr.trim().length() != 0)
		{
			Map<String, String> myGetParams = new HashMap<String, String>(1);
			myGetParams.put("since", pLastRunDateStr);
			myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_GET_MY_ACTIVITES_FEED, null, null, myGetParams, null);
		}
		else
		{
			myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_GET_MY_ACTIVITES_FEED, null, null, null, null);

		}
		
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getMyActivitiesFeed(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}

		return myResult;

	}
	
	
	
	
	/**
	 * 
	 * Returns a list of users who liked a particular activity.
	 * 
	 * @param pAccessToken
	 *            	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *            	The access secret used to read private data.
	 * @param pPostMajorId
	 * 				Major Id of an activity
	 * 
	 * @return
	 * 				The RestQuerySimpleResult instance containing the response and the HTTP status code.
	 * 
	 */
	public RestQuerySimpleResult getPostLikesUsers(String pAccessToken, String pAccessTokenSecret,  String pPostMajorId) {

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getPostLikesUsers(): Entering method.");
		}

		boolean areAllInputsValid = performSanityCheckOnVariables(pAccessToken, pAccessTokenSecret);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getPostLikesUsers():  We cannot make a private API call using empty/null Access Token or Access Secret. Exiting the method.");
			return null;
		}

		pAccessToken 		= pAccessToken.trim();
		pAccessTokenSecret 	= pAccessTokenSecret.trim();

		if (pPostMajorId == null || pPostMajorId.trim().length() == 0) 
		{
			LOGGER.error("XingAPIHelper: getPostLikesUsers(): Major Post ID provided to this method is NULL or empty string. hence skipping the call to xing api. PostMajorId:"
					+ pPostMajorId + ". Exiting the method.");
			return null;
		} 		
		
		// Set parameters for OAuth
		setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getPostLikesUsers(): Calling Xing API...");
		}

		RestQuery queryToRun 	= mQueryEngine.findQuery(XingConstants.ACTION_GET_MY_ACTIVITY_LIKES);
		String strToAppend 		= pPostMajorId + "/likes";
		queryToRun.setURL(queryToRun.getURL() + strToAppend);
		
		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQueryAsSuch(queryToRun);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getPostLikesUsers(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}

		return myResult;

	}
	
	
	
	
	/**
	 * Sets the access token and token secret required for accessing private data.
	 * 
	 * @param pAccessToken
	 *            User access token.
	 * @param pTokenSecret
	 *            User access secret.
	 *            
	 */
	private void setAccessTokenAndSecret(String pAccessToken, String pTokenSecret) {
		IRestQuerySignatureProvider myProvider = mQueryEngine.getSignatureProvider();

		if (myProvider != null) 
		{
			myProvider.setParam("accessToken", pAccessToken);
			myProvider.setParam("tokenSecret", pTokenSecret);
		}
		else
		{
			LOGGER.error("XingAPIHelper: setAccessTokenAndSecret(): myProvider is NULL. So unable to set up Access Token and Access Secret prior to making private call.");
		}
		
	}
	
	
	
	
	
	
	
	/**
	 * Returns an OAuthRequestTokenResponse instance that contains the request token URL, auth token and secret.
	 * 
	 * @param pAuthToken
	 * 				App access token.
	 * @param pAuthTokenSecret
	 * 				App access secret.
	 * @param pCallbackUrl
	 * 				App callback url.
	 * 
	 * @return
	 * 				An OAuthRequestTokenResponse instance that contains the request token URL, auth token and secret.
	 * 
	 * @throws OAuthException
	 */
	public OAuthRequestTokenResponse getRequestToken(String pAuthToken, String pAuthTokenSecret, String pCallbackUrl) throws OAuthException {
		
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getRequestToken(): Entering method.");
		}

		boolean areAllInputsValid = performSanityCheckOnVariablesForOAuth(pAuthToken, pAuthTokenSecret, pCallbackUrl);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getRequestToken():  We cannot make a API call of OAuth using empty/null App Token/App Secret/Callback url. Exiting the method.");
			return null;
		}
		
		pAuthToken 			= pAuthToken.trim();
		pAuthTokenSecret 	= pAuthTokenSecret.trim();
		pCallbackUrl 		= pCallbackUrl.trim();
		
		// Set parameters for OAuth Request Token
		setOAuthCallback(pCallbackUrl);
		
		//setParamsForRequestToken(pAuthToken, pAuthTokenSecret, pCallbackUrl);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getRequestToken(): Calling Xing API...");
		}
		
		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_GET_REQUEST_TOKEN);

		if (myResult == null || myResult.getResponseSize() == 0) {
			LOGGER.error("XingAPIHelper: getRequestToken(): Unable to get the API response for OAuth Request token. Response is " + (myResult == null? "NULL. ": "EMPTY. ")
					+ 	"This should not have happened. Please investigate. Exiting the method.");
			throw new OAuthException("XingAPIHelper: getRequestToken(): mQueryEngine.runSimpleQuery(\"getRequestToken\") returned null. This should not have happened. Please investigate.");
		}
		
		if (XingConstants.T0_DEBUG)
		{
			LOGGER.info("XingAPIHelper: getRequestToken(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}


		if (myResult.getStatusCode() != XingConstants.API_OAUTH_SUCCESS_CODE_201) {
			throw new OAuthException("XingAPIHelper: getRequestToken(): Unexpected status code returned from Xing while getting the request token. Status Code: " + myResult.getStatusCode() + ".");
		}

		// The list of params in the query string returned
		List<NameValuePair> myParams 	= new ArrayList<NameValuePair>();

		// Parse the params
		URLEncodedUtils.parse(myParams, new Scanner(myResult.getResponse()), "UTF-8");

		// Found in param oauth_callback_confirmed
		String myCallbackConfirmed 		= null;

		// Found in param oauth_token
		String myAuthToken 				= null;

		// Found in param oauth_token_secret
		String myAuthTokenSecret 		= null;

//		if (XingConstants.T0_DEBUG)
//		{
//			LOGGER.debug("XingAPIHelper: getRequestToken(): myParams: " + (myParams == null ? "null" : myParams.toString() ) + ".");
//		}
	    
		
		for (NameValuePair myParam : myParams) 
		{
			if (myParam.getName().equals(XingConstants.API_OAUTH_RESPONSE_OAUTH_TOKEN)) 
			{
				myAuthToken 		= myParam.getValue();
			} 
			else if (myParam.getName().equals(XingConstants.API_OAUTH_RESPONSE_OAUTH_SECRET)) 
			{
				myAuthTokenSecret 	= myParam.getValue();
			} 
			else if (myParam.getName().equals(XingConstants.API_OAUTH_RESPONSE_OAUTH_CALLBACK)) 
			{
				myCallbackConfirmed = myParam.getValue();
			}
			
		}

		if (myCallbackConfirmed == null || myAuthToken == null || myAuthTokenSecret == null
				|| myCallbackConfirmed.trim().length() == 0 || myAuthToken.trim().length() == 0 || myAuthTokenSecret.trim().length() == 0	) 
		{
			LOGGER.error("XingAPIHelper: getRequestToken(): Get request token response from Xing did not contain all required params.");
			throw new OAuthException("XingAPIHelper: getRequestToken(): Get request token response from Xing did not contain all required params." + myResult.getResponse() + ".");
		}

		myAuthToken 		= myAuthToken.trim();
		myAuthTokenSecret 	= myAuthTokenSecret.trim();
		myCallbackConfirmed	= myCallbackConfirmed.trim();
		
		
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getRequestToken(): Exiting the method.");
		}
		
		return new OAuthRequestTokenResponse(myAuthToken, myAuthTokenSecret, myCallbackConfirmed);
		
		
	}
	
	
	
	
	
	/**
	 * Returns the access tokens containing User Id, Access Token and Access Token Secret.
	 * 
	 * @param pAuthToken
	 * 			The auth token used to get the access tokens.
	 * @param pAuthTokenSecret
	 * 			The auth token secret used to get the access tokens.
	 * @param pVerifier
	 * 			The code used to verify if we were the original requestee for the access tokens.
	 * 
	 * @return
	 * 			The access tokens containing User Id, Access Token and Access Token Secret.
	 * 
	 * @throws OAuthException
	 */
	public OAuthAccessTokenResponse getAccessTokens(String pAuthToken, String pAuthTokenSecret, String pVerifier) throws OAuthException {
		
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getAccessTokens(): Entering method.");
		}
		
		boolean areAllInputsValid = performSanityCheckOnVariablesForOAuth(pAuthToken, pAuthTokenSecret, pVerifier);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: getAccessTokens():  We cannot make a API call of OAuth using empty/null App Token/App Secret/Callback url. Exiting the method.");
			return null;
		}
		
		pAuthToken	 		= pAuthToken.trim();
		pAuthTokenSecret	= pAuthTokenSecret.trim();
		pVerifier 			= pVerifier.trim();
		
		// Set parameters for OAuth Access Token
        setAccessTokenAndSecret(pAuthToken, pAuthTokenSecret);

        // Set the verifier
        setVerifier(pVerifier);
        
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getAccessTokens(): Calling Xing API...");
		}
		
		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_GET_ACCESS_TOKEN);

		if (myResult == null || myResult.getResponseSize() == 0) {
			LOGGER.error("XingAPIHelper: getAccessTokens(): Unable to get the API response for OAuth Access token. Response is " + (myResult == null? "NULL. ": "EMPTY. ")
					+ 	"This should not have happened. Please investigate. Exiting the method.");
			throw new OAuthException("XingAPIHelper: getAccessTokens(): mQueryEngine.runSimpleQuery(\"getAccessTokens\") returned null. This should not have happened. Please investigate.");
		}
		
		if (XingConstants.T0_DEBUG)
		{
			LOGGER.info("XingAPIHelper: getAccessTokens(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}
		
		if (myResult.getStatusCode() != XingConstants.API_OAUTH_SUCCESS_CODE_201) {
			throw new OAuthException("XingAPIHelper: getAccessTokens(): Unexpected status code returned from Xing while getting the request token. Status Code: " + myResult.getStatusCode() + ".");
		}

		// The list of params in the query string returned
		List<NameValuePair> myParams 	= new ArrayList<NameValuePair>();

		// Parse the params
		URLEncodedUtils.parse(myParams, new Scanner(myResult.getResponse()), "UTF-8");

        // Found in param oauth_token
        String myAccessToken 			= null;

        // Found in param oauth_token_secret
        String myAccessTokenSecret 		= null;
        
        // Found in param user_id
        String myUserId 				= null;

//		if (XingConstants.T0_DEBUG)
//		{
//			LOGGER.debug("XingAPIHelper: getAccessTokens(): myParamsResult: " + (myParams == null ? "null" : myParams.toString() ) + ".");
//		}
	    
		
		for (NameValuePair myParam : myParams) 
		{
			if (myParam.getName().equals(XingConstants.API_OAUTH_RESPONSE_OAUTH_TOKEN)) 
			{
				myAccessToken 			= myParam.getValue();
			} 
			else if (myParam.getName().equals(XingConstants.API_OAUTH_RESPONSE_OAUTH_SECRET)) {
				myAccessTokenSecret		= myParam.getValue();
			} 
			else if (myParam.getName().equals(XingConstants.API_OAUTH_RESPONSE_USER_ID)) {
				myUserId 				= myParam.getValue();
			}
		}

		if (myAccessToken == null || myAccessTokenSecret == null || myUserId == null
				|| myAccessToken.trim().length() == 0 || myAccessTokenSecret.trim().length() == 0 || myUserId.trim().length() == 0	) 
		{
			LOGGER.error("XingAPIHelper: getAccessTokens(): Get access token response from Xing did not contain all required params.");
			throw new OAuthException("XingAPIHelper: getAccessTokens(): Get access token response from Xing did not contain all required params." + myResult.getResponse() + ".");
		}
		
		myAccessToken 		= myAccessToken.trim();
		myAccessTokenSecret = myAccessTokenSecret.trim();
		myUserId			= myUserId.trim();

		
		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: getAccessTokens(): Exiting the method.");
		}
		
		return new OAuthAccessTokenResponse(myAccessToken, myAccessTokenSecret, myUserId);
		
		
	}
	
	


	
	/**
	 * Performs sanity check on the input parameters to XingAPIHelper class methods in relation to OAuth
	 * 
	 * @param pAccessToken
	 * 				App / Request token.
	 * @param pTokenSecret
	 * 				App access secret.
	 * @param pCallback
	 * 				App callback url.
	 * 
	 * @return
	 * 				<br>True	--- If both the app token AND app secret are:
	 * 									<br>Not null
	 * 									<br>Non-empty
	 * 									<br>Alphanumeric - Characters allowed: [a-zA-Z0-9]
	 * 								<br>AND
	 * 							<br>callback url is:
	 * 									<br>Not null
	 * 									<br>Non-empty
	 * 				<p>False	--- Otherwise
	 * 
	 */
	private boolean performSanityCheckOnVariablesForOAuth(String pAccessToken, String pTokenSecret, String pCallback) {
		if (pAccessToken == null || pAccessToken.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariablesForOAuth(): App/Request Token is Null or Empty. We cannot make API call for OAuth using empty/null App/Request Token.");
			return false;
		}

		if (pTokenSecret == null || pTokenSecret.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariablesForOAuth(): App Secret is Null or Empty. We cannot make API call for OAuth using empty/null App Secret.");
			return false;
		}

		if (pCallback == null || pCallback.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariablesForOAuth(): Callback url is Null or Empty. We cannot make API call for OAuth using empty/null Callback URL.");
			return false;
		}
		
		if (!pAccessToken.matches("[a-zA-Z0-9]+")) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariablesForOAuth(): App/Request Token provided should only be Alphanumeric. Characters allowed: [a-zA-Z0-9].");
			return false;
		}

		if (!pTokenSecret.matches("[a-zA-Z0-9]+")) {
			LOGGER.error("XingAPIHelper: performSanityCheckOnVariablesForOAuth(): App Secret provided should only be Alphanumeric. Characters allowed: [a-zA-Z0-9].");
			return false;
		}

		return true;

	}
	
	
	
	
	 /**
     * Sets the verifier required for authentication.
     * 
     * @param pVerifier 
     * 				The verifier.
     */
    private void setVerifier(String pVerifier) {
        IRestQuerySignatureProvider myProvider = mQueryEngine.getSignatureProvider();

        if (myProvider != null) 
        {
            myProvider.setParam(XingConstants.API_OAUTH_VERIFIER, pVerifier);
        }
        else
        {
    		LOGGER.error("XingAPIHelper: setVerifier(): myProvider is NULL. Unable to set up the verifier parameter.");        		
        }
        
    }
	
    
    
    /**
     * Sets the callback url required for authentication.
     * 
     * @param pCallback 
     * 			App callback url.
     */
    private void setOAuthCallback(String pCallback) {
        IRestQuerySignatureProvider myProvider = mQueryEngine.getSignatureProvider();

        if (myProvider != null) 
        {
            myProvider.setParam(XingConstants.API_OAUTH_CALLBACK, pCallback);
        }
        else
        {
    		LOGGER.error("XingAPIHelper: setOAuthCallback(): myProvider is NULL. Unable to set up the callback parameter.");        		
        }
        	
        	
    }   
	
    
    
    
    /**
     * 
     *	Sends a private message to a Xing user.
     * 	
     * 	Note: 
     * 	Due to Xing policies, we cannot sender (pFromUser) cannot be included as one of the recipient ids (pToUserList). 
     * 	If it does, it will throw a 403 error - "error_name":"NOT_ALLOWED","message":"Not allowed to send message to recipient." 
     * 
	 * @param pAccessToken
	 *           	The access token used to read private data.
	 * @param pAccessTokenSecret
	 *            	The access secret used to read private data.
     * @param pMessageSubject
     * 				Subject of the message. Must be a non-empty/non-null string.
     * @param pMessageContent
     * 				Message content or body. Must be a non-empty/non-null string.
     * @param pFromUser
     * 				Profile id of the current user.	Must be a non-empty/non-null string.
     * @param pToUserList
     * 				Comma separated list of recipient profile ids.
     * 				<p>This assumes that this toList has been validated by the calling method - esp. that its just a comma separated list of empty string (,,,, or , , , , ).
     * 				<br>Also, MAX size of this list is 10 profile ids. If you need to send private message to more users, recursively make call to this method with subsequent comma-separated list of profile ids.
     * 				<br>Also, there should NOT be a space between the profile ids and the comma.
     * 
     * @return
     * 				The RestQuerySimpleResult instance containing the response and the HTTP status code.
     * 
     */
    public RestQuerySimpleResult sendPrivateMessage(String pAccessToken, String pAccessTokenSecret, String pMessageSubject, String pMessageContent, String pFromUser, String pToUserList) {

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: sendPrivateMessage(): Entering method.");
		}

		boolean areAllInputsValid = performSanityCheckOnVariables(pAccessToken, pAccessTokenSecret);

		if (!areAllInputsValid) {
			LOGGER.error("XingAPIHelper: sendPrivateMessage():  We cannot make a private API call using empty/null Access Token or Access Secret. Exiting the method.");
			return null;
		}

		if (pMessageSubject == null || pMessageSubject.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: sendPrivateMessage(): Message Subject is Null or Empty. We cannot send a message without a subject. Exiting the method.");
			return null;
		}
		if (pMessageContent == null || pMessageContent.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: sendPrivateMessage(): Message Content is Null or Empty. We cannot send a message without any content/body. Exiting the method.");
			return null;
		}
		if (pFromUser == null || pFromUser.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: sendPrivateMessage(): Message Sender is Null or Empty. We cannot send a message without sender's id. Exiting the method.");
			return null;
		}
		if (pToUserList == null || pToUserList.trim().length() == 0) {
			LOGGER.error("XingAPIHelper: sendPrivateMessage(): Message Recipient(s) is Null or Empty. We cannot send a message without any recipient(s) info. Exiting the method.");
			return null;
		}
		
		
		pAccessToken 		= pAccessToken.trim();
		pAccessTokenSecret 	= pAccessTokenSecret.trim();
		pMessageSubject 	= pMessageSubject.trim();
		pMessageContent 	= pMessageContent.trim();
		pFromUser 			= pFromUser.trim();
		pToUserList 		= pToUserList.trim();
		
		
		// Set parameters for OAuth
		setAccessTokenAndSecret(pAccessToken, pAccessTokenSecret);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: sendPrivateMessage(): Calling Xing API...");
		}

		
		Map<String, String> myPostParams = new HashMap<String, String>(2);
		
		myPostParams.put("content",			pMessageContent	);
		myPostParams.put("subject",			pMessageSubject	);
		myPostParams.put("recipient_ids",	pToUserList	);
		myPostParams.put("user_id",			pFromUser	);
		
		
		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(XingConstants.ACTION_SEND_PRIVATE_MESSAGE, null, null, null, myPostParams);

		if (XingConstants.T0_DEBUG) {
			LOGGER.info("XingAPIHelper: sendPrivateMessage(): myResult: " + (myResult == null ? "null" : ("Satus Code: " + myResult.getStatusCode() + ", Response Size:" + myResult.getResponseSize())) + ".");
		}

		return myResult;

	}
}
