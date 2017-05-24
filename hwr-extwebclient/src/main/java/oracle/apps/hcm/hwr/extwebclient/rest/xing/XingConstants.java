/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.extwebclient.rest.xing;

/**
 * Constants pertaining to Xing API
 * 
 * @author Ibtisam Haque
 *
 */
public class XingConstants {

	
	// debug constants
	public static final boolean T0_DEBUG							= false;
	public static final String DEBUG_SEPERATOR_START				= "**********************************************";
	public static final String DEBUG_SEPERATOR_MIDDLE				= "----------------------------------------------";
    public static final String DEBUG_SEPERATOR_END					= "##############################################";
	    
	
	public static final String GET_USER_ERROR_MESSAGE				= "errorMessage";
	public static final String GET_USER_ERROR_NAME					= "errorName";
	
    public static final Integer API_SUCCESS_CODE_200				= 200;
    public static final String ARRAY_OBJECT_KEYWORD_USERS			= "users";
    public static final String ARRAY_OBJECT_KEYWORD_CONTACTS		= "contacts";
    public static final String ARRAY_OBJECT_KEYWORD_IDS				= "id";
    public static final String ARRAY_OBJECT_KEYWORD_IDS_TYPE		= "type";
    public static final String ARRAY_OBJECT_KEYWORD_LIKES			= "likes";
    public static final String ARRAY_OBJECT_KEYWORD_ACTIVITY_FEED 	=  "network_activities";
    
    // making calls to api
    public static final Integer API_CONTACTS_LIMIT					= 95;	// to change to 100 however, haven't checked 100 limit
    public static final String ACTION_GET_MY_PROFILE				= "GETMYPROFILE";
    public static final String ACTION_GET_CONTACTS_IDS				= "GETUSERIDS"; 
    public static final String ACTION_GET_CONTACTS_PROFILE			= "GETCONTACTSPROFILE";
    public static final String ACTION_GET_MY_PROFILE_MESSAGE		= "GETMYPROFILEMESSAGE";
    public static final String ACTION_GET_MY_ACTIVITES_FEED			= "GETMYACTIVITIESFEED";
    public static final String ACTION_GET_MY_ACTIVITY_LIKES			= "GETMYACTIVITYLIKES";
    public static final String ACTION_GET_REQUEST_TOKEN				= "GETREQUESTTOKEN";
    public static final String ACTION_GET_ACCESS_TOKEN				= "GETACCESSTOKEN";
    public static final String ACTION_SEND_PRIVATE_MESSAGE			= "SENDPRIVATEMESSAGE";
    
    
    // OAuth
    public static final String API_OAUTH_CALLBACK					= "oauth_callback";
    public static final String API_OAUTH_VERIFIER					= "verifier";
    public static final Integer API_OAUTH_SUCCESS_CODE_201			= 201;
    
    public static final String API_OAUTH_RESPONSE_OAUTH_TOKEN		= "oauth_token";
    public static final String API_OAUTH_RESPONSE_OAUTH_SECRET		= "oauth_token_secret";
    public static final String API_OAUTH_RESPONSE_OAUTH_CALLBACK	= "oauth_callback_confirmed";
    public static final String API_OAUTH_RESPONSE_USER_ID			= "user_id";
    
    
    public static final String API_CONTACTS_TOTAL					= "TotalContacts";
    
     
    // fixup Dob
    public static final String API_PROFILE_DOB							= "Date_of_birth";
    public static final String API_PROFILE_DOB_day						= "Date_of_birth_day";
    public static final String API_PROFILE_DOB_month					= "Date_of_birth_month";
    public static final String API_PROFILE_DOB_year						= "Date_of_birth_year";

    // fixup/rename Gender
    public static final String API_PROFILE_GENDER_M_SHORT				= "m";
    public static final String API_PROFILE_GENDER_M_LONG				= "male";
    public static final String API_PROFILE_GENDER_F_SHORT				= "f";
    public static final String API_PROFILE_GENDER_F_LONG				= "female";
  
    
    // fixup Experience
    public static final String API_PROFILE_EXPERIENCE					= "professional_experience";
    public static final String API_PROFILE_EXPERIENCE_COMPANY_PRIMARY	= "primary_company";
    public static final String API_PROFILE_EXPERIENCE_COMPANY_NON_PRIMARY = "non_primary_companies";
    public static final String API_PROFILE_EXPERIENCE_NAME				= "name";
    public static final String API_PROFILE_EXPERIENCE_TITLE				= "title";
    public static final String API_PROFILE_EXPERIENCE_COMPANY_SIZE		= "company_size";
    public static final String API_PROFILE_EXPERIENCE_TAG				= "tag";
    public static final String API_PROFILE_EXPERIENCE_URL				= "url";
    public static final String API_PROFILE_EXPERIENCE_CAREER_LEVEL		= "career_level";
    public static final String API_PROFILE_EXPERIENCE_BEGIN_DATE		= "begin_date";
    public static final String API_PROFILE_EXPERIENCE_DESCRIPTION		= "description";
    public static final String API_PROFILE_EXPERIENCE_END_DATE			= "end_date";
    public static final String API_PROFILE_EXPERIENCE_INDUSTRY			= "industry";
    
    // fixup Address
    public static final String API_PROFILE_ADDRESS_PRIVATE				= "private_address";
    public static final String API_PROFILE_ADDRESS_BUSINESS				= "business_address";
    public static final String API_PROFILE_CONTACT_EMAIL				= "email_address";
    public static final String API_PROFILE_CONTACT_CITY					= "city";
    public static final String API_PROFILE_CONTACT_COUNTRY				= "country";
    public static final String API_PROFILE_CONTACT_ZIPCODE				= "zip_code";
    public static final String API_PROFILE_CONTACT_STREET				= "street";
    public static final String API_PROFILE_CONTACT_PHONE				= "phone";
    public static final String API_PROFILE_CONTACT_FAX					= "fax";
    public static final String API_PROFILE_CONTACT_PROVINCE				= "province";
    public static final String API_PROFILE_CONTACT_PHONE_MOBILE			= "mobile_phone";
    
    // fixup Education
    public static final String API_PROFILE_EDUCATION					= "educational_background";
    public static final String API_PROFILE_EDUCATION_SCHOOLS			= "educational_background#schools";
    public static final String API_PROFILE_EDUCATION_SCHOOLS_NAME		= "schools";
    public static final String API_PROFILE_EDUCATION_SCHOOLS_DEGREE		= "degree";
    public static final String API_PROFILE_EDUCATION_SCHOOLS_NOTES		= "notes";
    public static final String API_PROFILE_EDUCATION_SCHOOLS_SUBJECT	= "subject";
    public static final String API_PROFILE_EDUCATION_SCHOOLS_BEGIN_DATE	= "begin_date";
    public static final String API_PROFILE_EDUCATION_SCHOOLS_END_DATE	= "end_date";
    
    
    // fixup Language
    public static final String API_PROFILE_LANGUAGES					= "languages";
    
    // fixup IM Accounts
    public static final String API_PROFILE_IM_ACCOUNTS					= "instant_messaging_accounts";
    
    // fixup web profiles
    public static final String API_PROFILE_WEB_PROFILES					= "web_profiles";
    
    public static final String API_PROFILE_HAVES						= "haves";
    public static final String API_PROFILE_QUALIFICATIONS				= "qualifications";
    
    // fixup Awards
    public static final String API_PROFILE_AWARDS						= "professional_experience#awards";
    public static final String API_PROFILE_AWARDS_NAME					= "name";
    public static final String API_PROFILE_AWARDS_DATE_AWARDED			= "date_awarded";
    public static final String API_PROFILE_AWARDS_URL					= "url";
    
    
    // mapping keys for activities feed
    public static final String API_ACTIVITY_ID_MAJOR					= "activity_major_id";
    public static final String API_ACTIVITY_VERB_ACTIVITY_TYPE			= "verb";
    public static final String API_ACTIVITY_VERB_ACTIVITY_TYPE_POST		= "post";
    public static final String API_ACTIVITY_VERB_ACTIVITY_TYPE_JOIN		= "join";
    public static final String API_ACTIVITY_VERB_ACTIVITY_TYPE_OBJECTS	= "objects";
    public static final String API_ACTIVITY_VERB_ACTIVITY_TYPE_SHARE	= "share";
    
    // fixup ActivityObject --> join-group
    public static final String API_ACTIVITY_OBJECT_TYPE_GROUP			= "group";
    // fixup ActivityObject --> post-bookmarks
    public static final String API_ACTIVITY_OBJECT_TYPE_BOOKMARK		= "bookmark";
	// fixup ActivityObject --> post-status
    public static final String API_ACTIVITY_OBJECT_TYPE_STATUS			= "status";
    
    // fixup ActivityObject --> esp. for group, bookmark,
    public static final String API_ACTIVITY_OBJECT_TYPE					= "type";
    public static final String API_ACTIVITY_OBJECT_ID					= "id";
    public static final String API_ACTIVITY_OBJECT_CREATED_AT			= "created_at";
    public static final String API_ACTIVITY_OBJECT_DISPLAY_NAME			= "display_name";
    public static final String API_ACTIVITY_OBJECT_URL					= "permalink";
    public static final String API_ACTIVITY_OBJECT_TITLE				= "title";
    public static final String API_ACTIVITY_OBJECT_CREATEDBY			= "creator";
    public static final String API_ACTIVITY_OBJECT_CREATEDBY_TYPE		= "creator#type";
    public static final String API_ACTIVITY_OBJECT_CREATEDBY_ID			= "creator#id";
    public static final String API_ACTIVITY_OBJECT_URL2					= "url";
    public static final String API_ACTIVITY_OBJECT_DESCRIPTION			= "description";
    public static final String API_ACTIVITY_OBJECT_IMAGE				= "image";
    public static final String API_ACTIVITY_OBJECT_BOOKMARKTYPE			= "bookmark_type";
    public static final String API_ACTIVITY_OBJECT_CONTENT				= "content";
    
    
    // mapping keys used for writeProfileContainerToStore() & writeProfileConnectionContainerToStore() methods
    public static final String XING_CONTAINER_PROFILE 					= "profile";
    public static final String XING_CONTAINER_CONTACTS 					= "connections";
    public static final String XING_CONTAINER_ConnectionIDPrefix_CONTACTS= "__XG_CN_";
    public static final String XING_CONTAINER_GROUPS 					= "groups";
    
    // fixUpMetadata
    public static final String API_ACTIVITY_LIKED_COUNT					= "activity_liked_count";
    public static final String API_ACTIVITY_LIKED_USERS					= "activity_liked_users";
    
    
    

    
    
}
