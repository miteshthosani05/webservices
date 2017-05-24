/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.extwebclient.rest.jive;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Constants pertaining to Jive API
 * 
 * @author Ibtisam Haque
 *
 */
public class JiveConstants {

	
	// debug constants
	public static final boolean T0_DEBUG							= false;
	public static final String DEBUG_SEPERATOR_START				= "**********************************************";
	public static final String DEBUG_SEPERATOR_MIDDLE				= "----------------------------------------------";
    public static final String DEBUG_SEPERATOR_END					= "##############################################";
	
	
    // response
    public static final String RESPONSE_STRING_TO_TRIM 				= "throw 'allowIllegalResourceCall is false.';";
    public static final String RESPONSE_STRING_TO_TRIM_WITH 		= "";
    public static final Integer API_SUCCESS_CODE_200				= 200;
    public static final String GET_USER_ERROR_MESSAGE				= "errorMessage";
    public static final String JIVE_API_URL_POSTFIX					= "/api/core/v3";
	
	
	public static final String GET_USER_BY_USERNAME 				= "getUserByUsername";
    public static final String GET_USER_VALUE						= "/people/username/";
    public static final String GET_USER_BY_ID 						= "getUserById";
    public static final String GET_USER_BY_ID_VALUE					= "/people/";
    public static final String GET_USER_BY_EMAIL					= "/people/email/";
    public static final String GET_USER_MISC_ATTRIBUTES				= "getUserMiscAttributes";
    public static final String GET_USER_MISC_ATTRIBUTES_VALUE		= "/people/";
    public static final String GET_USER_ACTIVITIES_Key 				= "getUserActivity";
    public static final String GET_USER_ACTIVITIES_Value 			= "activities";
    public static final String GET_USER_BLOG_Key 					= "getUserBlog";
    public static final String GET_USER_BLOG_Value 					= "blog";
    public static final String GET_USER_FOLLOWERS_ACTIVITY_Key 		= "getProfileFollowers";
    public static final String GET_USER_FOLLOWERS_ACTIVITY_Value 	= "@followers";
    public static final String GET_USER_FOLLOWERS_MAP_KEY			= "profileFollowers";
    public static final String GET_USER_FOLLOWING_ACTIVITY_Key 		= "getProfileFollowing";
    public static final String GET_USER_FOLLOWING_ACTIVITY_Value 	= "@following";
    public static final String GET_USER_FOLLOWING_MAP_KEY			= "profileFollowing";
    public static final String PAGINATION_NEXT_AND_PREVIOUS_LINKS	= "links";
    public static final String PAGINATION_NEXT_LINK					= "next";
    public static final String PAGINATION_PREVIOUS_LINK				= "previous";
    public static final Integer PAGINATION_PAGE_SIZE 				= 450;	// don't go above 500  
    //public static final String BASE_URL 							= "https://sandbox.jiveon.com/api/core/v3";
    public static final String GET_USER_ACTIVITIES_CONTENT_Key_ALL	= "getUserActivityLikesCountALL";
    public static final String GET_USER_ACTIVITIES_CONTENT_Value_ALL= "/contents?sort=latestActivityAsc&filter=author(";
    public static final String GET_USER_ACTIVITIES_CONTENT_Key		= "getUserActivityContent";
    public static final String GET_USER_ACTIVITIES_CONTENT_Value	= "/contents/";
    
    // mapping keys used for writeProfileContainerToStore() method
    public static final String JIVE_CONTAINER_PROFILE 						= "profile";
    public static final String JIVE_CONTAINER_FOLLOWERS 					= "followers";
    public static final String JIVE_CONTAINER_FOLLOWING 					= "friends";	// friends, following are synonym !!
    public static final String JIVE_CONTAINER_ConnectionIDPrefix_FOLLOWERS	= "__JV_FO_";
    public static final String JIVE_CONTAINER_ConnectionIDPrefix_FOLLOWING	= "__JV_FR_";
    
    
    public static final String GET_ACTIVITY_OBJECT_OBJECTTYPE 				= "ObjectObjectType";
    public static final Set<String> GET_ACTIVITY_OBJECT_OBJECTTYPE_VALID	= new HashSet<String>(
								    		Arrays.asList("jive:post",
								    				"jive:comment", 
								    				"jive:poll", 
								    				"jive:blog",
								    				"jive:message",
								    				"jive:discussion",
								    				"jive:document",
								    				"jive:update",
												    "jive:file"												    
								    				));
												    //"jive:person" & "jive:idea" -- there is an issue with it in API !!
    

    public static final Boolean TO_USE_HTML_AS_DESCRIPTION_INSTEAD_OF_TEXT	= true;
    
    public static final String POST_ORIGINAL_LIKE_COUNT 			= "PostOriginalLikeCount";
    public static final String POST_ORIGINAL_AUTHOR					= "PostOriginalAuthor";
    public static final String POST_PARENT_ID						= "PostParentId";
    public static final String POST_PARENT_ACTOR_ID					= "PostParentActorId";
    public static final String POST_OBJECTTYPE_COMMENT				= "jive:comment";	//to status, file posts, 
    public static final String POST_OBJECTTYPE_REPLIES				= "jive:message";	// to discussions
    public static final String POST_CONTENT_TYPE					= "type";	// for jive:file, for jive:update
    public static final String POST_CONTENT_TYPE_FILE				= "file";	// for jive:file
    public static final String POST_CONTENT_MIMETYPE				= "contentType";
    public static final String POST_CONTENT_BINARY_URL				= "binaryURL";
    public static final String POST_CONTENT_TYPE_UPDATE				= "update";	// for jive:update
    public static final String POST_VERB							= "verb";	// for POST_TAG_WITHIN_THE_COMMENT_OF_POST_INSTEAD_OF_SEPERATE_TAG
    public static final String POST_VERB_MENTIONED					= "jive:mentioned";	// for POST_TAG_WITHIN_THE_COMMENT_OF_POST_INSTEAD_OF_SEPERATE_TAG
    public static final String POST_TAG_WITHIN_THE_COMMENT_OF_POST_INSTEAD_OF_SEPERATE_TAG	= "TaggedOrMentionedUserId";	// for POST_TAG_WITHIN_THE_COMMENT_OF_POST_INSTEAD_OF_SEPERATE_TAG

    
    //"repost#resources#self#ref";
    public static final String POST_CONTENT_REPOST					= "repost";
    public static final String POST_CONTENT_REPOST_RESOURCE			="resources";
    public static final String POST_CONTENT_REPOST_RESOURCE_SELF	="self";
    public static final String POST_CONTENT_REPOST_RESOURCE_SELF_REF="ref";
    public static final String POST_CONTENT_REPOST_PARENT_POST_ID	="RepostParentPostId";

    
    public static final String POST_CONTENT_TAGS_ASSOCIATED			="TagsAssociatedWithPost";
    public static final String POST_CONTENT_TAGS_ASSOCIATED_MAPPED_KEY	="tags";

    
    public static final String PUBLIC_PROFILE_ERROR_CODE			= "code";
    public static final String PUBLIC_PROFILE_ERROR_MESSAGE			= "message";
    public static final String PUBLIC_PROFILE_FAILURE_MSG			= "Failed";
    public static final String PUBLIC_PROFILE_SUCCESS_MSG			= "Successful";
	public static final String TEST_CONNECTION_USERNAME				= "Username";
	public static final String TEST_CONNECTION_USER_PASSWORD		= "UserPassword";
	public static final String TEST_CONNECTION_JIVE_URL				= "JiveUrl";
	 
    public static final String ACTION_SEND_PRIVATE_MESSAGE			= "SENDPRIVATEMESSAGE";
    
    
}
