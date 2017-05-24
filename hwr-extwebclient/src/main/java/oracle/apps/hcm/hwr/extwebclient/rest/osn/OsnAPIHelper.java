/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.osn;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;
import oracle.apps.hcm.hwr.extwebclient.rest.SharedCookieRestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.osn.exceptions.LoginException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * OSN API Helper
 * @author Yiwei Zhang
 */
public class OsnAPIHelper {

	/** REST query engine */
    private final SharedCookieRestQueryEngine mQueryEngine;
    
    /** OSN REST query provider */
    private final OsnRestQueryProvider mQueryProvider;

    /** The logger instance */
    private static final Log LOGGER = LogFactory.getLog(OsnAPIHelper.class);
    
    /** The query limit when polling paged objects */
    private static final Integer mQueryLimit = 20;
    
    /** The credential used for polling OSN data */
    private String mRandomID = null;

    private final String mUsername;
    private final String mPassword;
    
    /**
     * Sets the application related data to make API calls successfully.
     * @param pBaseUrl OSN base URL.
     * @param pUsername User name to log into OSN.
     * @param pPassname Password to log into OSN
     */
    public OsnAPIHelper(String pBaseUrl, String pUsername, String pPassword) {
    	
    	mUsername = pUsername;
    	mPassword = pPassword;
    	mQueryProvider = new OsnRestQueryProvider(pBaseUrl);
    	
        mQueryEngine =
            new SharedCookieRestQueryEngine(mQueryProvider);
        mQueryEngine.setUseProxy(false);
    }
    
    /**
     * Returns the conversations within current account.
     * @param pFilterMap The map that contains filter info
     * @param pKeyReplacementMap The map that contains JSON key replacement
     * @return The list of conversations.
     */
    public List<Map<String, Object>> getConversations(Map<String, Object> pFilterMap, Map<String, String> pKeyReplacementMap) {
    	
    	return getOsnPagedObjects("getConversations", pFilterMap, pKeyReplacementMap);
    }
    
    /**
     * Returns the messages within a conversation.
     * @param pConversationID The conversation ID.
     * @param pFilterMap The map that contains filter info
     * @param pKeyReplacementMap The map that contains JSON key replacement
     * @return The list of messages.
     * @throws 
     */
    public List<Map<String, Object>> getMessages(String pConversationID, Map<String, Object> pFilterMap, Map<String, String> pKeyReplacementMap) {

    	mQueryProvider.SetObjectID(pConversationID);
    	return getOsnPagedObjects("getMessages", pFilterMap, pKeyReplacementMap);
    }
    
    /**
     * Returns the likes within a message.
     * @param pMessageID The message ID.
     * @param pFilterMap The map that contains filter info
     * @param pKeyReplacementMap The map that contains JSON key replacement
     * @return The list of likes.
     * @throws 
     */
    public List<Map<String, Object>> getLikes(String pMessageID, Map<String, Object> pFilterMap, Map<String, String> pKeyReplacementMap) {

    	mQueryProvider.SetObjectID(pMessageID);
    	return getOsnPagedObjects("getLikes", pFilterMap, pKeyReplacementMap);
    }
    
    /**
     * Returns the follow ups within a message.
     * @param pMessageID The message ID.
     * @param pFilterMap The map that contains filter info
     * @param pKeyReplacementMap The map that contains JSON key replacement
     * @return The list of follow ups.
     * @throws 
     */
    public List<Map<String, Object>> getFollowups(String pMessageID, Map<String, Object> pFilterMap, Map<String, String> pKeyReplacementMap) {

    	mQueryProvider.SetObjectID(pMessageID);
    	return getOsnPagedObjects("getFollowups", pFilterMap, pKeyReplacementMap);
    }
    
    /**
     * Returns the user profile.
     * @param pProfileID The profile ID.
     * @param pKeyReplacementMap The map that contains JSON key replacement
     * @return The profile.
     * @throws 
     */
    public Map<String, Object> getProfile(String pProfileID, Map<String, String> pKeyReplacementMap) {

    	mQueryProvider.SetObjectID(pProfileID);
    	return this.getOsnObject("getProfileWithID", null, pKeyReplacementMap);
    }
    
    /**
     * Returns the OSN objects within a page of current query.
     * @param pQueryName The REST query name
     * @param pFilterMap The map that contains filter info
     * @param pKeyReplacementMap The map that contains JSON key replacement
     * @return The list of OSN objects within a page.
     * @throws 
     */
    private List<Map<String, Object>> getOsnPagedObjects(String pQueryName, Map<String, Object> pFilterMap, Map<String, String> pKeyReplacementMap){
    	
    	List<Map<String, Object>> myPagedItemList = new ArrayList<Map<String, Object>>();
    	
    	Integer offset = 0;
    	Map<String, String> myGetParams = new HashMap<String, String>();
    	
    	if (pFilterMap != null) {
    		String filterString = "{";
    		
    		Iterator<Map.Entry<String, Object>> it = (Iterator<Map.Entry<String, Object>>)pFilterMap.entrySet().iterator();
    	    while (it.hasNext()) {
    	    	Map.Entry<String, Object> pairs = (Map.Entry<String, Object>)it.next();
    	        filterString = filterString + pairs.getKey() + ":"+ pairs.getValue().toString();
    	        
    	        if (it.hasNext()) { 
    	        	filterString = filterString + ",";
    	        }
    	    }
    	    
    	    filterString = filterString + "}";
    	    myGetParams.put("filter", filterString);
    	}
    	
    	myGetParams.put("limit", mQueryLimit.toString());
    	myGetParams.put("offset", offset.toString());

    	Boolean bHasMoreItems = true;

        while (bHasMoreItems) {
    		Map<String, Object> jsonResultMap = getOsnObject(pQueryName, myGetParams, pKeyReplacementMap);
    		bHasMoreItems = false;
    		
    		if (jsonResultMap != null) {

    			if (jsonResultMap.containsKey("items")) {
    				List<Map<String, Object>> itemList =
    	                (List<Map<String, Object>>) jsonResultMap.get("items");
    				
    				if (itemList.size() == 0) {
                        break;
                    }

                    for (Map<String, Object> item : itemList) {
                    	myPagedItemList.add(item);
                    }
    			}
    			
    			if (jsonResultMap.containsKey("hasMore")) {
    				bHasMoreItems = (Boolean)jsonResultMap.get("hasMore");
    			}
    		}
    		
    		offset += mQueryLimit;
    		myGetParams.put("offset", offset.toString());
    	}
        
    	return myPagedItemList;
    }
    
    /**
     * Returns the OSN object of current query.
     * @param pQueryName The REST query name
     * @param pGetName The REST GET parameters
     * @param pKeyReplacementMap The map that contains JSON key replacement
     * @return The OSN object.
     * @throws 
     */
    private Map<String, Object> getOsnObject(String pQueryName, Map<String, String> pGetParams, Map<String, String> pKeyReplacementMap){

    	if (mRandomID == null) {
    		mRandomID = getRandomId();
        }
    	
    	Map<String, String> myHeaderFields = new HashMap<String, String>(2);
    	myHeaderFields.put("Content-type", "application/json");
    	myHeaderFields.put("X-Waggle-RandomID", mRandomID);
    	
    	RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(pQueryName, null, myHeaderFields, pGetParams, null, null, null);
        	
    	if (myResult != null) {
    	    if (myResult.getStatusCode() == 200) {
    		    return JsonHelper.jsonStringToMap(myResult.getResponse(), pKeyReplacementMap);
    	    } else  {
    	    	// Update random ID
    	    	mRandomID = getRandomId();
        		myHeaderFields.put("X-Waggle-RandomID", mRandomID);
        		
        		// Run the query again
        		myResult = mQueryEngine.runSimpleQuery(pQueryName, null, myHeaderFields, pGetParams, null, null, null);
        		if (myResult != null && myResult.getStatusCode() == 200) {
        	    	
            		return JsonHelper.jsonStringToMap(myResult.getResponse(), pKeyReplacementMap);
            	}
    	    }
    	}
            
    	return null;
    }
    
    /**
     * Get the OSN API random ID.
     * @return The random ID.
     * @throws LoginException
     */
    private String getRandomId() {
    	String myRandomId = null;
    	
    	Map<String, String> myHeaderFields = new HashMap<String, String>(1);
    	myHeaderFields.put("Content-type", "application/json");
    	
    	String myPostBody =  "{\"name\":\"" + mUsername + "\",\"password\":\"" + mPassword + "\"}";

    	RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery("getConnections", null, myHeaderFields, null, null, null, myPostBody);
    	
    	if (myResult == null) {	
            String failureMessage = "mQueryEngine.runSimpleQuery(\"getRadomID\") returned null.";
			LOGGER.error(failureMessage);
			throw new LoginException(failureMessage);
        }

        if (myResult.getStatusCode() != 200) {
        	String failureMessage = "Unexpected status code returned from OSN while trying to get the random ID. Status Code: "
                    + myResult.getStatusCode() + ". Response: " + myResult.getResponse();
			LOGGER.error(failureMessage);
			throw new LoginException(failureMessage);
        }
        
    	Map<String, Object> osnConnectionsMap =
                JsonHelper.jsonStringToMap(myResult.getResponse(), null);
    	    
    	if (osnConnectionsMap != null && osnConnectionsMap.containsKey("apiRandomID")) {  
    		myRandomId = (String)osnConnectionsMap.get("apiRandomID");
    	}
    	else {
    		String failureMessage = "OSN Response does not contain random ID. Response: " + myResult.getResponse();
			LOGGER.error(failureMessage);
			throw new LoginException(failureMessage);
    	}
    	
    	return myRandomId;
    }
}
