/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.facebook;

import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Modifier.limit;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Modifier.query;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Modifier.type;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Modifiers.forComments;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Modifiers.forLikes;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Modifiers.forPosts;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import oracle.apps.hcm.hwr.extwebclient.rest.AccessTokenRestQueryResultValidation;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngineException;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryMapResult;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;
import oracle.apps.hcm.hwr.extwebclient.rest.apiuse.ConnectorApiUseage;
import oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.FacebookConnections;
import oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.FacebookQueryConfigBuilder;
import oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Fields;
import oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Modifier;
import oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Post;
import oracle.apps.hcm.hwr.extwebclient.rest.query.QueryConfig;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;

/**
 * @author Yenal Kal
 * @author Ibtisam Haque
 */
public class GraphAPIHelper {

    /** The logger instance */
    private static final Log LOGGER = LogFactory.getLog(GraphAPIHelper.class);

    private ConnectorApiUseage mApiUseage;

    /** Status code when find a bad access token */
    private static final int mInvalidAccessTokenHttpStatusCode = HttpStatus.SC_BAD_REQUEST;

    /** Potential messages identifying to a bad access token */
    private static final Set<String> mInvalidAccessTokenMessages = new HashSet<String>();
    static {
        mInvalidAccessTokenMessages.add("not authorized application");
    }

    /** REST query engine */
    private final RestQueryEngine mQueryEngine = new RestQueryEngine(new FacebookRestQueryProvider(), null,
        new AccessTokenRestQueryResultValidation(mInvalidAccessTokenHttpStatusCode, mInvalidAccessTokenMessages));

    public void setApiUseageClass(final ConnectorApiUseage pApiUseage) {
        mApiUseage = pApiUseage;
        mQueryEngine.setConnectorApiUseage(mApiUseage);
    }

    /**
     * Posts a feed with the given parameters to all the profiles' walls.
     * @param pAuthToken The authentication token for the query.
     * @param pProfileIDs The list of profile IDs.
     * @param pMessage The message.
     * @param pPicture The picture.
     * @param pLink The link.
     * @param pName The name.
     * @param pCaption The caption.
     * @param pDescription The description.
     * @param pSource The source.
     * @param pPlace The place.
     * @param pTags The tags.
     * @return The responses for all the post feed actions in the same order of the profile list.
     */
    private RestQuerySimpleResult postToFeedInternal(final String pAuthToken, final Set<String> pProfileIDs,
        final Post pPost) {

        if (pProfileIDs == null || pProfileIDs.size() == 0) {
            throw new IllegalArgumentException("pProfileIDs cannot be null or empty.");
        }

        final Map<String, String> myPostParams = getParamsMapWithAccessToken(pAuthToken);

        final StringBuilder mySB = new StringBuilder();

        mySB.append("[");
        for (final String myProfileID : pProfileIDs) {
            // @todo: Perhaps create PostUrlEncodingDecorator here, so that there is no need to call
            // urlEncode every time.
            mySB.append(String.format(FacebookRestQueryProvider.PUBLISH_POST_BODY_FORMAT, myProfileID,
                urlEncode(pPost.getMessage()), urlEncode(pPost.getLinkPicture()), urlEncode(pPost.getLink()),
                urlEncode(pPost.getLinkName()), urlEncode(pPost.getLinkCaption()),
                urlEncode(pPost.getLinkDescription()), urlEncode(pPost.getPlace()), urlEncode(pPost.getTags())));
        }
        mySB.append("]");

        myPostParams.put("batch", mySB.toString());

        // Run the batched queries
        return mQueryEngine.runSimpleQuery(FacebookRestQueryProvider.RUN_BATCHED_QUERY, null, null, null, myPostParams);
    }

    /**
     * Posts a feed with the given parameters to all the profiles' walls.
     * @param pAuthToken The authentication token for the query.
     * @param pProfileIDs The list of profile IDs.
     * @param pMessage The message.
     * @param pPicture The picture.
     * @param pLink The link.
     * @param pName The name.
     * @param pCaption The caption.
     * @param pDescription The description.
     * @param pSource The source.
     * @param pPlace The place.
     * @param pTags The tags.
     * @return The set of profile IDs for which the operation succeeded for.
     */
    @SuppressWarnings("unchecked")
    public Set<String> postToFeed(final String pAuthToken, final Set<String> pProfileIDs, final Post pPost) {

        // Execute the query
        final RestQuerySimpleResult myBatchedResult = postToFeedInternal(pAuthToken, pProfileIDs, pPost);

        // Get the combined results into a map
        final Map<String, Object> myBatchedResultMap = JsonHelper.jsonStringToMap(myBatchedResult.getResponse(), null);

        if (myBatchedResultMap.get("items") == null || !(myBatchedResultMap.get("items") instanceof List<?>)) {
            return Collections.<String> emptySet();
        } else {
            final List<Map<String, Object>> myResponses = (List<Map<String, Object>>) myBatchedResultMap.get("items");

            // Make sure we got responses for all the requests
            if (myResponses.size() != pProfileIDs.size()) {
                return Collections.<String> emptySet();
            }

            final Set<String> myResult = new HashSet<String>();

            int i = 0;
            for (final String myProfileID : pProfileIDs) {
                final Map<String, Object> myResponse = myResponses.get(i++);
                if (HttpStatus.SC_OK == ((Number) myResponse.get("items#code")).intValue()) {
                    myResult.add(myProfileID);
                }
            }
            return myResult;
        }
    }

    /**
     * Returns the list of members who belong to the list of member groups.
     * @param pAuthToken The authentication token for the query.
     * @param pMemberGroupIDs The IDs of groups, friend lists etc.
     * @return The list of members who belong to the list of member groups.
     * @throws RestQueryEngineException
     */
    public RestQuerySimpleResult getMembers(final String pAuthToken, final Collection<String> pMemberGroupIDs)
        throws RestQueryEngineException {

        final Map<String, String> myPostParams = getParamsMapWithAccessToken(pAuthToken);

        // Second query depends on first.
        final StringBuilder mySB = new StringBuilder("[");
        for (final String myMemberGroupID : pMemberGroupIDs) {
            mySB.append("{ \"method\": \"GET\", \"name\": \"get-members-");
            mySB.append(myMemberGroupID);
            mySB.append("\", \"relative_url\": \"");
            mySB.append(myMemberGroupID);
            mySB.append("/members?limit=5000&fields=id\"}," + "  { \"method\": \"GET\", "
                + "\"relative_url\": \"?ids={result=get-members-");
            mySB.append(myMemberGroupID);
            mySB.append(":$.data.*.id}\" },");
        }
        mySB.append("]");

        myPostParams.put("batch", mySB.toString());

        // Run the batched queries
        return mQueryEngine.runSimpleQuery("runBatchedQuery", null, null, null, myPostParams);
    }

    /**
     * Retrieves posts from users dashboard ( main page ) from the newest to oldest order. For now
     * paging is not supported so it gets only first chunk of data.
     * @param pProfileID - profile from where we download posts
     * @param pAuthToken - access token
     * @return
     */
    public RestQueryMapResult getAllPostsFromFeedChronologically(final String pProfileID, final String pAuthToken) {
        return getPostsFromFeedChronologically(pProfileID, pAuthToken, Modifier.LIMIT_NOT_SPECIFIED,
            TimeFrame.notSpecified());
    }

    /**
     * Behave like {@link GraphAPIHelper#getAllPostsFromFeedChronologically(String, String)} but
     * allows to specify page size and also time frame to retrieve posts.
     * @param pProfileID - profile from where we download posts
     * @param pAuthToken - access token
     * @param pSize - the size of page.
     * @param pTimePeriod - time frame for posts.
     * @return
     */
    public RestQueryMapResult getPostsFromFeedChronologically(final String pProfileID, final String pAuthToken,
        final int pSize, final TimeFrame pTimePeriod) {
        final String myUrlPostfix = pProfileID + "/feed";

        final QueryConfig pQueryConfig =
            FacebookQueryConfigBuilder.get().withAuthToken(pAuthToken).withFields(Fields.forPosts())
                .withModifiers(forPosts(pSize, pTimePeriod)).withConnections(FacebookConnections.forPosts())
                .withUrlPostfix(myUrlPostfix).buildQueryConfig();

        return mQueryEngine.runMapQueryWithoutFlattening(FacebookRestQueryProvider.GET_USER_POSTS, pQueryConfig);
    }

    /**
     * Get comments for particular post. They are ordered from newest to oldest. It gets all posts
     * because pagination is supported.
     * @param pPostID - Post for which we want to get comments.
     * @param pAuthToken - access token.
     * @return mapped and not flatten JSON map.
     */
    public RestQueryMapResult getCommentsForPostChronologically(final String pPostID, final String pAuthToken) {
        final String myUrlPostfix = pPostID + "/comments";

        final QueryConfig myQueryConfig =
            FacebookQueryConfigBuilder.get().withAuthToken(pAuthToken).withFields(Fields.forComments())
                .withModifiers(forComments()).withUrlPostfix(myUrlPostfix).buildQueryConfig();

        return mQueryEngine.runMapQueryWithoutFlattening(FacebookRestQueryProvider.GET_USER_COMMENTS, myQueryConfig);
    }

    /**
     * Get comments for particular post. They are ordered from newest to oldest. Pagination is
     * supported.
     * @param pPostID - post for with we will get likes.
     * @param pAuthToken - access token.
     * @return
     */
    public RestQueryMapResult getLikesForPostChronologically(final String pPostID, final String pAuthToken) {
        final String myUrlPostfix = pPostID + "/likes";

        final QueryConfig myQueryConfig =
            FacebookQueryConfigBuilder.get().withAuthToken(pAuthToken).withUrlPostfix(myUrlPostfix)
                .withFields(Fields.forLikes()).withModifiers(forLikes()).buildQueryConfig();

        return mQueryEngine.runMapQueryWithoutFlattening(FacebookRestQueryProvider.GET_USER_LIKES, myQueryConfig);
    }

    /**
     * Returns the profile data including friends, friend lists and groups
     * @param pAuthToken The authentication token for the query.
     * @param pProfileId The profile ID. Use "me" to refer to the owner of the authentication token.
     * @param pLight True for just ID and name, false for full-fledged profile.
     * @return The query result
     */
    public RestQueryMapResult getProfile(final String pAuthToken, final String pProfileId, final boolean pLight) {
        final String myUrlPostfix = pProfileId;
        final QueryConfig myQueryConfig =
            FacebookQueryConfigBuilder.get().withAuthToken(pAuthToken).withUrlPostfix(myUrlPostfix)
                .withFields(pLight ? Fields.forProfileLight() : Fields.forProfile()).buildQueryConfig();
        return mQueryEngine.runMapQueryWithoutFlattening(FacebookRestQueryProvider.GET_PROFILE_BY_ID, myQueryConfig);
    }

    public RestQueryMapResult getFriends(final String pAuthToken, final String pProfileId) {
        final String myUrlPostfix = pProfileId + "/friends";
        final QueryConfig myQueryConfig =
            FacebookQueryConfigBuilder.get().withAuthToken(pAuthToken).withUrlPostfix(myUrlPostfix)
                .withFields(Fields.forProfile()).buildQueryConfig();
        return mQueryEngine.runMapQueryWithoutFlattening(FacebookRestQueryProvider.GET_FRIENDS, myQueryConfig);
    }

    public RestQueryMapResult getGroups(final String pAuthToken, final String pProfileId) {
        final String myUrlPostfix = pProfileId + "/groups";
        final QueryConfig myQueryConfig =
            FacebookQueryConfigBuilder.get().withAuthToken(pAuthToken).withUrlPostfix(myUrlPostfix)
                .withFields(Fields.forGroup()).buildQueryConfig();
        return mQueryEngine.runMapQueryWithoutFlattening(FacebookRestQueryProvider.GET_GROUPS, myQueryConfig);
    }

    /**
     * Returns the users matching the supplied query.
     * @param pQuery The search query.
     * @param pAccessToken The access token.
     * @return The users matching the supplied query.
     */
    public RestQueryMapResult searchUsers(final String pQuery, final String pAccessToken) {
        return searchUsers(pQuery, pAccessToken, FacebookConstants.DEFAULT_PAGE_SIZE);
    }

    public RestQueryMapResult searchUsers(final String pQuery, final String pAccessToken, final int pPageSize) {
        if (pQuery == null || pQuery.isEmpty()) {
            throw new IllegalArgumentException("pQuery can not be null or empty string.");
        }

        if (pAccessToken == null || pAccessToken.isEmpty()) {
            throw new IllegalArgumentException("pAuthToken can not be null or empty string.");
        }

        final QueryConfig pQueryConfig =
            FacebookQueryConfigBuilder.get().withAuthToken(pAccessToken).withFields(Fields.forProfile())
                .withModifiers(query(pQuery), limit(pPageSize), type("user")).buildQueryConfig();

        return mQueryEngine.runMapQuery(FacebookRestQueryProvider.SEARCH_USERS, pQueryConfig);
    }

    /**
     * Returns Application Token Assumption: This method assumes that pCustomerKey and
     * pCustomerSecret are neither null nor empty. This validation is performed by the caller of
     * this method.
     * @param pCustomerKey Customer Key (Api Key)
     * @param pCustomerSecret Customer Secret (Secret Key)
     * @return
     * @throws RestQueryEngineException
     */
    public RestQuerySimpleResult getAppAccessToken(final String pCustomerKey, final String pCustomerSecret)
        throws RestQueryEngineException {
        LOGGER.info("GraphAPIHelper: getAppAccessToken(): Entering method.");

        final Map<String, String> myGetParams = new HashMap<String, String>();
        myGetParams.put("client_id", pCustomerKey);
        myGetParams.put("client_secret", pCustomerSecret);
        myGetParams.put("grant_type", FacebookConstants.ACCESS_TOKEN_GRANT_TYPE);

        final RestQueryEngine mQueryEngineTemp =
            new RestQueryEngine(new FacebookRestQueryProvider(), null, new AccessTokenRestQueryResultValidation(
                mInvalidAccessTokenHttpStatusCode, mInvalidAccessTokenMessages));
        mQueryEngineTemp.setSignatureProvider(null);

        final RestQuerySimpleResult myResult =
            mQueryEngineTemp.runSimpleQuery(FacebookRestQueryProvider.GET_ACCESS_TOKEN, null, null, myGetParams, null,
                null);

        if (FacebookConstants.T0_DEBUG)
            LOGGER.debug("GraphAPIHelper: getAppAccessToken(): myResult: " + myResult.toString());

        return myResult;

    }

	public String getAccessTokenPermissionList() {
        return "user_location,user_website,user_about_me,email,user_birthday," +
        		"user_education_history,user_likes,user_work_history,user_groups," +
        		"publish_stream,read_stream,user_posts,read_custom_friendlists";
    }
	
    /**
     * Creates a test user for the application specified with the given name.
     * @param pAppID The app ID.
     * @param pAppAccessToken The access token for the app.
     * @param pFullName The full name of the user.
     * @param pPermissions The permissions for the access token.
     * @return The response from Facebook.
     */
    public RestQuerySimpleResult createTestUser(final String pAppID, final String pAppAccessToken,
        final String pFullName, final String pPermissions) {

        if (pAppID == null || pAppID.isEmpty()) {
            throw new IllegalArgumentException("pAppID can not be null or empty string.");
        }

        if (pAppAccessToken == null || pAppAccessToken.isEmpty()) {
            throw new IllegalArgumentException("pAppAccessToken can not be null or empty string.");
        }

        if (pFullName == null || pFullName.isEmpty()) {
            throw new IllegalArgumentException("pFullName can not be null or empty string.");
        }

        String myInstalled = "false";
        if (pPermissions != null && !pPermissions.isEmpty()) {
            myInstalled = "true";
        }

        final StringBuilder myURLBuilder = new StringBuilder("https://graph.facebook.com/");
        myURLBuilder.append(urlEncode(pAppID));
        myURLBuilder.append("/accounts/test-users?installed=");
        myURLBuilder.append(myInstalled);
        myURLBuilder.append("&locale=en_US&method=post&name=");
        myURLBuilder.append(urlEncode(pFullName));
        myURLBuilder.append("&access_token=");
        myURLBuilder.append(urlEncode(pAppAccessToken));
        if (pPermissions != null && !pPermissions.isEmpty()) {
            myURLBuilder.append("&permissions=");
            myURLBuilder.append(urlEncode(pPermissions));
        }

        return mQueryEngine.getURL(myURLBuilder.toString());

    }

    public RestQuerySimpleResult getURL(final String pURL) {
        return mQueryEngine.getURL(pURL);
    }

    public RestQueryMapResult getUrlMapResult(final String pURL) {
        return mQueryEngine.getURLMapResult(pURL);
    }

    private static Map<String, String> getParamsMapWithAccessToken(final String pAccessToken) {
        final Map<String, String> myGetParams = new HashMap<String, String>(1);
        if (pAccessToken != null && !pAccessToken.isEmpty()) {
            myGetParams.put("access_token", pAccessToken);
        }
        return myGetParams;
    }

    private static String urlEncode(final String pString) {
        final String myCharset = "UTF-8";

        if (pString == null || pString.isEmpty()) {
            return "";
        }

        try {
            return URLEncoder.encode(pString, myCharset);
        } catch (final UnsupportedEncodingException e) {
            // This should never happen
            LOGGER.error(e);
            return pString == null ? "" : pString;
        }
    }

    private static String urlEncode(final String[] pStringArray) {
        return urlEncode(StringUtils.join(pStringArray, ","));
    }

}
