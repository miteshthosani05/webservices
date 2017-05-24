/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
 
 package oracle.apps.hcm.hwr.extwebclient.rest.facebook;

/**
 * Constants pertaining to Facebook API
 * 
 * @author Ibtisam Haque
 *
 */
 
public class FacebookConstants {

	
	public static final boolean T0_DEBUG						= false;
	
	public static final String ACCESS_TOKEN 					= "access_token=";
	public static final String ACCESS_TOKEN_GRANT_TYPE 			= "client_credentials";
	    
	public static final String ACCESS_TOKEN_ERROR 				= "error";
	public static final String ACCESS_TOKEN_ERROR_MESSAGE		= "message";
	public static final String ACCESS_TOKEN_ERROR_TYPE			= "type";
	public static final String ACCESS_TOKEN_ERROR_CODE			= "code";
	public static final String ACCESS_TOKEN_ERROR_LABEL_VALUE	= "OAuthException";
	
	public static final String ACCESS_TOKEN_SUCCESS_MSG			= "Successful";
	public static final String ACCESS_TOKEN_FAILURE_MSG			= "Failed";
	
	
	public static final String TEST_CONNECTION_API_KEY			= "ApiKey";
	public static final String TEST_CONNECTION_SECRET_KEY 		= "SecretKey";
	
    public static final int DEFAULT_PAGE_SIZE = 25;
    public static final int MAX_ITERATIONS = 50;
    
    public static final String NEXT_URL = "NEXT_URL";
    public static final String FIRST_SEARCH_USER_URL = "FIRST_SEARCH_USER_URL";

    public static final String SEARCHED_QUERY = "SEARCHED_QUERY";

    public static final String RESPONSE_FIELD_PAGING = "paging";
    public static final String RESPONSE_FIELD_PAGING_NEXT = "paging#next";
    public static final String RESPONSE_FIELD_PAGING_CURSOR_AFTER = "paging#cursors#after";
    public static final String RESPONSE_FIELD_DATA = "data";

    public static final String RESPONSE_FIELD_IDS = "ids";

    public static final String RESPONSE_VALUE_CURSOR_FINAL = "0";
    public static final String RESPONSE_VALUE_CURSOR_NEXT_AS_STRING = "next_cursor_str";
	
    public static final String ATTRIBUTE_POST_ID = "postId";
    public static final String ATTRIBUTE_USER_ID = "userId";
}
