/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.osn;

import java.util.Map;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;
import oracle.apps.hcm.hwr.extwebclient.rest.osn.OsnConstants;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * Query provider for the OSN API
 * @author Yiwei Zhang
 */
public class OsnRestQueryProvider implements IRestQueryProvider {

	private final String mBaseUrl;
	private String mObjectID;
	
	/**
     * Constructor of OSN rest query provider, sets the base URL of OSN.
     * @param pBaseUrl The base URL of OSN.
     */
	public OsnRestQueryProvider(String pBaseUrl)
	{
		mBaseUrl = pBaseUrl;
	}
	
	/**
     * Sets the query object ID.
     * @param pObjectID The query object ID.
     */
	public void SetObjectID(String pObjectID)
	{
		mObjectID = pObjectID;
	}
	
	/*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.client.rest.IRestQueryProvider#findQuery(java.lang.String)
     */
    @Override
    public RestQuery findQuery(String pQueryName) {
       
        if (pQueryName.equals("getConnections")) {
        	return new RestQuery("getConnections", RestQuery.Method.POST,
                    mBaseUrl + OsnConstants.CONNECTION_STR);
        } else if (pQueryName.equals("getProfiles")) {
            return new RestQuery("getProfiles", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.PEOPLE_STR);
        }
        else if (pQueryName.equals("getProfileWithID")) {
            return new RestQuery("getProfileWithID", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.PEOPLE_STR + "/" + mObjectID);
        }
        else if (pQueryName.equals("getConversations")) {
            return new RestQuery("getConversations", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.CONVERSATION_STR);
        }
        else if (pQueryName.equals("getMessages")) {
            return new RestQuery("getMessages", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.CONVERSATION_STR + "/" + mObjectID + OsnConstants.MESSAGE_STR);
        }
        else if (pQueryName.equals("getFollowers")) {
            return new RestQuery("getFollowers", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.FOLLOWERS_STR);
        }
        else if (pQueryName.equals("getFollowersWithID")) {
            return new RestQuery("getFollowersWithID", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.PEOPLE_STR + "/" + mObjectID + OsnConstants.FOLLOWERS_STR);
        }
        else if (pQueryName.equals("getFollowing")) {
            return new RestQuery("getFollowing", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.FOLLOWING_STR);
        }
        else if (pQueryName.equals("getFollowingWithID")) {
            return new RestQuery("getFollowingWithID", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.PEOPLE_STR + "/" + mObjectID + OsnConstants.FOLLOWING_STR);
        }
        else if (pQueryName.equals("getContacts")) {
            return new RestQuery("getContacts", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.CONTACTS_STR);
        }
        else if (pQueryName.equals("getGroups")) {
            return new RestQuery("getGroups", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.GROUPS_STR);
        }
        else if (pQueryName.equals("getLikes")) {
            return new RestQuery("getLikes", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.MESSAGE_STR + "/" + mObjectID + OsnConstants.LIKES_STR);
        }
        else if (pQueryName.equals("getFollowups")) {
            return new RestQuery("getFollowups", RestQuery.Method.GET,
            		mBaseUrl + OsnConstants.MESSAGE_STR + "/" + mObjectID + OsnConstants.FOLLOWUPS_STR);
        }
        return null;
    }
    
    /*
     * (non-Javadoc)
     * @see
     * oracle.apps.hcm.hwr.client.rest.IRestQueryProvider#executeCommonQueryTasks(org.apache.http
     * .client.methods.HttpRequestBase)
     */
    @Override
    public void executeCommonQueryTasks(HttpRequestBase pRequest) {

    }

    /*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.client.rest.IRestQueryProvider#procesQueryResponse(int,
     * java.lang.String)
     */
    @Override
    public Map<String, Object> procesQueryResponse(int statusCode, String pQueryResponse) {
        return JsonHelper.jsonStringToMap(pQueryResponse);
    }
}
