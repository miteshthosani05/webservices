/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.linkedin;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import oracle.apps.hcm.hwr.extwebclient.rest.AccessTokenRestQueryResultValidation;
import oracle.apps.hcm.hwr.extwebclient.rest.IRestQuerySignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.OAuthException;
import oracle.apps.hcm.hwr.extwebclient.rest.OAuthSignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;
import oracle.apps.hcm.hwr.extwebclient.rest.apiuse.ConnectorApiUseage;
import oracle.apps.hcm.hwr.extwebclient.rest.oauth.OAuthAccessTokenResponse;
import oracle.apps.hcm.hwr.extwebclient.rest.oauth.OAuthRequestTokenUrlResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/**
 * @author Yenal Kal
 */
public class LinkedInAPIHelper {

    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(LinkedInAPIHelper.class);

    /** REST query engine */
    private final RestQueryEngine mQueryEngine;
    
    private ConnectorApiUseage mApiUseage;

    /** Status code when find a bad access token */
    private static final int mInvalidAccessTokenHttpStatusCode = 401;
    /** Potential messages identifying to a bad access token */
    private static final Set<String> mInvalidAccessTokenMessages = new HashSet<String>();
    static {
        mInvalidAccessTokenMessages.add("Expired access token");
        mInvalidAccessTokenMessages.add("Invalid access token");
        mInvalidAccessTokenMessages.add("This token has expired");
    }
    
    /**
     * Sets the application related data to make API calls successfully.
     * @param pApiKey API key for the application.
     * @param pApiSecret API secret for the application.
     */
    public LinkedInAPIHelper(final String pApiKey, final String pApiSecret) {
        mQueryEngine =
            new RestQueryEngine(new LinkedInQueryProvider(), new OAuthSignatureProvider(pApiKey,
                pApiSecret), new AccessTokenRestQueryResultValidation(mInvalidAccessTokenHttpStatusCode,
                    mInvalidAccessTokenMessages));      

    }
    
    public void setApiUseageClass(ConnectorApiUseage pApiUseage) {
        mApiUseage = pApiUseage;
        mQueryEngine.setConnectorApiUseage(mApiUseage);
    }
    

    /**
     * Sends a message to a linked in user.
     * @param pAccessToken The access token used to read private data.
     * @param pTokenSecret The token secret used to read private data.
     * @param pRecipientIds The list of recipient IDs.
     * @param pSubjectLine The subject.
     * @param pMessage The message.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     */
    public RestQuerySimpleResult sendMessage(String pAccessToken, String pTokenSecret,
        Collection<String> pRecipientIds, String pSubjectLine, String pMessage) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        // build the recipient values array as json array
        // will be in the form:
        // "values":[{"person":{"_path": "/people/<recipient1IdHere>"}},{"person":{"_path":
        // "/people/<recipient2IdHere>"}}]
        StringBuilder myRecipientValues = new StringBuilder("\"values\": [");
        for (String myReceipientId : pRecipientIds) {
            myRecipientValues.append("{\"person\": {\"_path\": \"/people/").append(myReceipientId)
                .append("\"}},");
        }
        // trim off trailing comma
        if (myRecipientValues.length() > 1) {
            myRecipientValues.deleteCharAt(myRecipientValues.length() - 1);
        }
        myRecipientValues.append("]");

        // build subject line in the form:
        // "subject":"<contentsOfSubjectLineHere>"
        StringBuilder mySubject = new StringBuilder("\"subject\": \"");
        mySubject.append(pSubjectLine).append("\"");

        // build body line as:
        // "body":"<contentsOfMessageHere>"
        StringBuilder myMessageBody = new StringBuilder("\"body\": \"");
        myMessageBody.append(pMessage).append("\"");

        // build the post body as json string
        String myPostBody =
            String.format("{\"recipients\": {%s},%s,%s}", myRecipientValues.toString(),
                mySubject.toString(), myMessageBody.toString());

        // make the ws call to linked in and actually send the message
        return mQueryEngine.runSimpleQuery("sendMessage", null, null, null, null, myPostBody);
    }

    /**
     * Returns a string that will be used in Profile API calls for the given profile ID.
     * @param pProfileID The profile.
     * @return Properly formatted string for the Profile API calls.
     */
    public String getProfileIDAPIString(String pProfileID) {

        final String myCharset = "UTF-8";

        try {
            if (pProfileID == null || pProfileID.isEmpty()) {
                return "~";
            } else if (isPublicProfileID(pProfileID)) {
                return "url=" + URLEncoder.encode(pProfileID, myCharset);
            } else {
                return "id=" + URLEncoder.encode(pProfileID, myCharset);
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("URL encoding failed.", e);
        }
        return null;
    }

    /**
     * Indicates whether the profile ID given is a public profile URL.
     * @param pProfileID The profile ID.
     * @return Whether the profile ID given is a public profile URL.
     */
    public Boolean isPublicProfileID(String pProfileID) {
        return pProfileID != null && pProfileID.startsWith("http://www.linkedin.com/");
    }

    /**
     * Returns the map containing the profile information. If an error occurs, the map contains the
     * error information.
     * @param pProfileID The profile ID we are interested in.
     * @param pAccessToken The access token used to read private data.
     * @param pTokenSecret The token secret used to read private data.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     */
    public RestQuerySimpleResult getProfile(String pProfileID, String pAccessToken,
        String pTokenSecret) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        String myURLPostFix = getProfileIDAPIString(pProfileID);

        if (myURLPostFix == null) {
            return null;
        }

        return mQueryEngine.runSimpleQuery("getProfile", myURLPostFix, null, null, null);
    }
    
    /**
     * Returns the map containing the profile information. If an error occurs, the map contains the
     * error information.
     * @param pProfileID The profile ID we are interested in.
     * @param pAccessToken The access token used to read private data.
     * @param pTokenSecret The token secret used to read private data.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     */
    public RestQuerySimpleResult getProfileLight(String pProfileID, String pAccessToken,
        String pTokenSecret) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        String myURLPostFix = getProfileIDAPIString(pProfileID);

        if (myURLPostFix == null) {
            return null;
        }

        return mQueryEngine.runSimpleQuery("getProfileLight", myURLPostFix, null, null, null);
    }
	
	/**
     * For getting the limited profiles available with the new API changes that went live in May 2015.
     * 
     * Temporary fix since this is being back ported and we are real late in the release cycle.
     * 
     * Returns the map containing the profile information. If an error occurs, the map contains the
     * error information.
     * 
     * @param pProfileID The profile ID we are interested in.
     * @param pAccessToken The access token used to read private data.
     * @param pTokenSecret The token secret used to read private data.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     */
    public RestQuerySimpleResult getProfileNewApi(String pProfileID, String pAccessToken,
        String pTokenSecret) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        String myURLPostFix = getProfileIDAPIString(pProfileID);

        if (myURLPostFix == null) {
            return null;
        }

        return mQueryEngine.runSimpleQuery("getProfileNewApi", myURLPostFix, null, null, null);
    }
    
    public String getProfileId(String pAccessToken, String pTokenSecret) throws OAuthException {
        return getProfileId(null, pAccessToken, pTokenSecret);
    }
    
    public String getProfileId(String pProfileID, String pAccessToken, String pTokenSecret) throws OAuthException {
        RestQuerySimpleResult result = getProfileLight(pProfileID, pAccessToken, pTokenSecret);
        
        Map<String, Object> resultMap = JsonHelper.jsonStringToMap(result.getResponse());
        if (!resultMap.containsKey("id")) {
            String exceptionMessage =
                "Failed to get profile ID. Http status code: " + result.getStatusCode() + ".";

            throw new OAuthException(exceptionMessage);
        }

        return (String) resultMap.get("id");
    }
    
    public RestQuerySimpleResult getLastModifiedDate(String pProfileID, String pAccessToken,
        String pTokenSecret) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        String myURLPostFix = getProfileIDAPIString(pProfileID);

        if (myURLPostFix == null) {
            return null;
        }

        return mQueryEngine.runSimpleQuery("getLastModifiedDate", myURLPostFix, null, null, null);
    }


    
    /**
     * Returns the shares of the user who the access token belongs to.
     * 
     * @param pProfileID 
     * 				The profile ID.
     * @param pAccessToken 
     * 				The access token used to read private data.
     * @param pTokenSecret 
     * 				The token secret used to read private data.
     * @param pFilterText 
     * 				The filter text containing the extra querystring parameters to filter the data.
     * @param pBeforeDate
     * 				Timestamp before which to retrieve updates for (Ex: 1243834824000) note: precision is milliseconds since the epoch
     * @param pAfterDate
     * 				Timestamp after which to retrieve updates for (Ex: 1243834824000) note: precision is milliseconds since the epoch 
     * @param pIsGettingForSelf
     * 				True if you want to use the registered user's info. False otherwise
     * 
     * @return 
     * 				The RestQuerySimpleResult instance containing the response and the HTTP status code.
     *
     */
    public RestQuerySimpleResult getPosts(String pProfileID, String pAccessToken,
        String pTokenSecret, String pFilterText, String pBeforeDate, String pAfterDate, boolean pIsGettingForSelf
        ) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        //String myURLPostFix = getProfileIDAPIString(pProfileID);
        String myURLPostFix = getProfileIDAPIString(pIsGettingForSelf?null:pProfileID);
        
        if (myURLPostFix == null) {
            return null;
        }


        /**
         * scope=self 
         * doesn't give you status update (your own even)
         * doesn't give you comments by others on your status 
         * 
         */
        myURLPostFix +=
                "/network/updates"
        			+ "?type=SHAR" 
        			+ "&count=250";
        		
        if (pFilterText != null) {
        	// ?? <Todo Later> - along with other filters fix 
            myURLPostFix += "&" + pFilterText;
        }
        
        if (pBeforeDate != null && pBeforeDate.trim().length() > 0)
        {
        	pBeforeDate	= pBeforeDate.trim();
    		if (!pBeforeDate.matches("[0-9]+")) {
                LOGGER.error("LinkedInAPIHelper: getPosts(): Before date provided is non-numeric. pBeforeDate:" + pBeforeDate);
    		}
    		else
    		{
            	myURLPostFix += "&before=" + pBeforeDate;
    		}
        }
        
        if (pAfterDate != null && pAfterDate.trim().length() > 0)
        {
        	pAfterDate	= pAfterDate.trim();
    		if (!pAfterDate.matches("[0-9]+")) {
                LOGGER.error("LinkedInAPIHelper: getPosts(): After date provided is non-numeric. pAfterDate:" + pAfterDate);
    		}
    		else
    		{
            	myURLPostFix += "&after=" + pAfterDate;
    		}
        }

        LOGGER.debug("LinkedInAPIHelper: getPosts(): myURLPostFix:" + myURLPostFix);
        
        return mQueryEngine.runSimpleQuery("getShares", myURLPostFix, null, null, null);

    
    }
    
    
    

    /**
     * Returns the connections of the user who the access token belongs to.
     * @param pProfileID The profile ID we are interested in.
     * @param pAccessToken The access token used to read private data.
     * @param pTokenSecret The token secret used to read private data.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     */
    public RestQuerySimpleResult getConnections(String pProfileID, String pAccessToken,
        String pTokenSecret) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        String myURLPostFix = getProfileIDAPIString(pProfileID);

        if (myURLPostFix == null) {
            return null;
        }

        myURLPostFix += "/connections";

        return mQueryEngine.runSimpleQuery("getConnections", myURLPostFix, null, null, null);
    }

    /**
     * Returns the groups of the user who the access token belongs to.
     * 
     * @param pProfileID 		The profile ID we are interested in.
     * @param pAccessToken 		The access token used to read private data.
     * @param pTokenSecret 		The token secret used to read private data.
     * @param pIsGettingForSelf	True if you want to use the registered user's info. False otherwise
     * @return 					The RestQuerySimpleResult instance containing the response and the HTTP status code.
     * 
     * 
     * NOTE:
     * Not getting group posts !
     * 
     */
    public RestQuerySimpleResult getGroups(String pProfileID, String pAccessToken,
        String pTokenSecret, Boolean pIsGettingForSelf) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        String myURLPostFix = getProfileIDAPIString(pIsGettingForSelf?null:pProfileID);

        if (myURLPostFix == null) {
            return null;
        }

        myURLPostFix +=
            "/group-memberships:(group:(id,name,description,short-description,category,website-url,small-logo-url,large-logo-url,locale,num-members,site-group-url,location:(country,postal-code)))" +
            "?membership-state=member&membership-state=moderator&membership-state=manager&membership-state=owner";

        return mQueryEngine.runSimpleQuery("getGroups", myURLPostFix, null, null, null);
    }

    /**
     * Returns an OAuthRequestTokenUrlResponse instance that contains the request token URL, auth
     * token and secret.
     * @return An OAuthRequestTokenUrlResponse instance that contains the request token URL, auth
     *         token and secret.
     * @throws OAuthException
     */
    public OAuthRequestTokenUrlResponse getRequestTokenURL() throws OAuthException {
        LOGGER.info("Called updated version of Linkedin API for getRequestTokenURL.");       
        
        //most of these permissions are not available with the new API - so change to use ones that are available.
        //String permissions = "r_basicprofile,r_fullprofile,r_emailaddress,r_network,r_contactinfo,rw_nus,rw_groups,w_messages";
        
        String permissions = "r_basicprofile,r_emailaddress";
        Map<String, String> myPostParams = new HashMap<String, String>(1);
        myPostParams.put("scope", permissions);
        
        RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery("getRequestTokenURL", null, null, null, myPostParams);
        
        if (myResult == null) {
            throw new OAuthException(
                "mQueryEngine.runSimpleQuery(\"getRequestTokenURL\") returned null. This should not have happened. Please investigate.");
        }

        if (myResult.getStatusCode() != 200) {
            throw new OAuthException(
                "Unexpected status code returned from LinkedIn while getting the request token URL. Status Code: "
                    + myResult.getStatusCode());
        }
        
        // The list of params in the query string returned
        List<NameValuePair> myParams = new ArrayList<NameValuePair>();

        // Parse the params
        URLEncodedUtils.parse(myParams, new Scanner(myResult.getResponse()), "UTF-8");

        // Found in param xoauth_request_auth_url
        String myRequestURL = null;

        // Found in param oauth_token
        String myAuthToken = null;

        // Found in param oauth_token_secret
        String myAuthTokenSecret = null;

        for (NameValuePair myParam : myParams) {
            if (myParam.getName().equals("oauth_token")) {
                myAuthToken = myParam.getValue();
            } else if (myParam.getName().equals("oauth_token_secret")) {
                myAuthTokenSecret = myParam.getValue();
            } else if (myParam.getName().equals("xoauth_request_auth_url")) {
                myRequestURL = myParam.getValue();
            }
        }

        if (myRequestURL == null || myAuthToken == null || myAuthTokenSecret == null) {
            throw new OAuthException(
                "Get request token URL response from LinkedIn did not contain all required params: "
                    + myResult.getResponse());
        }

        return new OAuthRequestTokenUrlResponse(myRequestURL, "oauth_token", myAuthToken,
            myAuthTokenSecret);
    }
    
    

    /**
     * Returns the access tokens.
     * @param pAuthToken The auth token used to get the access tokens.
     * @param pAuthTokenSecret The auth token secret used to get the access tokens.
     * @param pVerifier The code used to verify if we were the original requestee for the access
     *        tokens.
     * @return The access tokens.
     */
    public OAuthAccessTokenResponse getAccessTokens(String pAuthToken, String pAuthTokenSecret,
        String pVerifier) {

        setAccessTokenAndSecret(pAuthToken, pAuthTokenSecret);

        // Set the verifier
        setVerifier(pVerifier);

        RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery("getAccessToken");

        if (myResult == null) {
            throw new OAuthException(
                "mQueryEngine.runSimpleQuery(\"getAccessToken\") returned null. This should not have happened. Please investigate.");
        }

        if (myResult.getStatusCode() != 200) {
            throw new OAuthException(
                "Unexpected status code returned from LinkedIn while getting the access tokens. Status Code: "
                    + myResult.getStatusCode() + ". Response: " + myResult.getResponse());
        }

        // The list of params in the query string returned
        List<NameValuePair> myParams = new ArrayList<NameValuePair>();

        // Parse the params
        URLEncodedUtils.parse(myParams, new Scanner(myResult.getResponse()), "UTF-8");

        // Found in param oauth_token
        String myAccessToken = null;

        // Found in param oauth_token_secret
        String myAccessTokenSecret = null;

        for (NameValuePair myParam : myParams) {
            if (myParam.getName().equals("oauth_token")) {
                myAccessToken = myParam.getValue();
            } else if (myParam.getName().equals("oauth_token_secret")) {
                myAccessTokenSecret = myParam.getValue();
            }

            if (myAccessToken != null && myAccessTokenSecret != null) {
                break;
            }
        }

        if (myAccessToken == null || myAccessTokenSecret == null) {
            throw new OAuthException(
                "Get access token response from LinkedIn did not contain all required params: "
                    + myResult.getResponse());
        }

        return new OAuthAccessTokenResponse(myAccessToken, myAccessTokenSecret);
    }

    /**
     * Returns the people matching the given search params.
     * @param pAccessToken The access token.
     * @param pTokenSecret The token secret.
     * @param pSearchParams The object that contains the search parameters.
     * @return The people matching the given search params.
     */
    public RestQuerySimpleResult peopleSearch(String pAccessToken, String pTokenSecret,
        LinkedInPeopleSearchParams pSearchParams) {

        if (pSearchParams == null) {
            throw new IllegalArgumentException("pSearchParams can not be null.");
        }

        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        Map<String, String> myGetParams = new HashMap<String, String>();

        if (pSearchParams.getKeywords() != null && !pSearchParams.getKeywords().isEmpty()) {
            myGetParams.put("keywords", pSearchParams.getKeywords());
        }

        if (pSearchParams.getFirstName() != null && !pSearchParams.getFirstName().isEmpty()) {
            myGetParams.put("first-name", pSearchParams.getFirstName());
        }

        if (pSearchParams.getLastName() != null && !pSearchParams.getLastName().isEmpty()) {
            myGetParams.put("last-name", pSearchParams.getLastName());
        }

        if (pSearchParams.getCompanyName() != null && !pSearchParams.getCompanyName().isEmpty()) {
            myGetParams.put("company-name", pSearchParams.getCompanyName());
        }

        if (pSearchParams.getCurrentCompany() != null) {
            myGetParams.put("current-company", pSearchParams.getCurrentCompany().toString());
        }

        if (pSearchParams.getTitle() != null && !pSearchParams.getTitle().isEmpty()) {
            myGetParams.put("title", pSearchParams.getTitle());
        }

        if (pSearchParams.getCurrentTitle() != null) {
            myGetParams.put("current-title", pSearchParams.getCurrentTitle().toString());
        }

        if (pSearchParams.getSchool() != null && !pSearchParams.getSchool().isEmpty()) {
            myGetParams.put("school-name", pSearchParams.getSchool());
        }

        if (pSearchParams.getCurrentSchool() != null) {
            myGetParams.put("current-school", pSearchParams.getCurrentSchool().toString());
        }

        if (pSearchParams.getCountryCode() != null && !pSearchParams.getCountryCode().isEmpty()) {
            myGetParams.put("country-code", pSearchParams.getCountryCode());
        }

        if (pSearchParams.getPostalCode() != null && !pSearchParams.getPostalCode().isEmpty()) {
            myGetParams.put("postal-code", pSearchParams.getPostalCode());
        }

        if (pSearchParams.getDistance() != null) {
            myGetParams.put("distance", pSearchParams.getDistance().toString());
        }

        myGetParams.put("start", Integer.toString(pSearchParams.getStartIndex()));
        myGetParams.put("count", Integer.toString(pSearchParams.getCountPerPage()));
        myGetParams.put("sort", pSearchParams.getSortMethod().getName());

        return mQueryEngine.runSimpleQuery("peopleSearch", null, null, myGetParams, null);
    }

    /**
     * Returns the map containing the profile information. If an error occurs, the map contains the
     * error information.
     * @param pProfileID The profile ID we are interested in.
     * @param pAccessToken The access token used to read private data.
     * @param pTokenSecret The token secret used to read private data.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     */
    public RestQuerySimpleResult getUrlOfProfile(String pProfileID, String pAccessToken,
        String pTokenSecret) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        String myURLPostFix = getProfileIDAPIString(pProfileID);

        if (myURLPostFix == null) {
            return null;
        }

        return mQueryEngine.runSimpleQuery("getOneProfileUrl", myURLPostFix, null, null, null);
    }

    /**
     * Returns the map containing the profile information. If an error occurs, the map contains the
     * error information.
     * @param pProfileID The profile ID we are interested in.
     * @param pAccessToken The access token used to read private data.
     * @param pTokenSecret The token secret used to read private data.
     * @return The RestQuerySimpleResult instance containing the response and the HTTP status code.
     */
    public RestQuerySimpleResult getUrlOfConnections(String pProfileID, String pAccessToken,
        String pTokenSecret) {
        // Required for accessing private data
        setAccessTokenAndSecret(pAccessToken, pTokenSecret);

        String myURLPostFix = getProfileIDAPIString(pProfileID);

        if (myURLPostFix == null) {
            return null;
        }

        myURLPostFix += "/connections";

        return mQueryEngine.runSimpleQuery("getConnectionProfileUrl", myURLPostFix, null, null, null);
    }
    
    /**
     * Sets the access token and token secret required for accessing private data.
     * @param pAccessToken The access token.
     * @param pTokenSecret The token secret.
     */
    private void setAccessTokenAndSecret(String pAccessToken, String pTokenSecret) {
        IRestQuerySignatureProvider myProvider = mQueryEngine.getSignatureProvider();

        if (myProvider != null) {
            myProvider.setParam("accessToken", pAccessToken);
            myProvider.setParam("tokenSecret", pTokenSecret);
        }
    }

    /**
     * Sets the verifier required for authentication.
     * @param pVerifier The verifier.
     */
    private void setVerifier(String pVerifier) {
        IRestQuerySignatureProvider myProvider = mQueryEngine.getSignatureProvider();

        if (myProvider != null) {
            myProvider.setParam("verifier", pVerifier);
        }
    }
}
