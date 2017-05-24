/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

 package oracle.apps.hcm.hwr.extwebclient.rest.twitter;

/**
 * Constants pertaining to Twitter API
 *
 * @author Ibtisam Haque
 *
 */
public class TwitterConstants {


	public static final boolean T0_DEBUG				= false;

	public static final String QUERY_STRING_OAUTH2		= "getOAuth2Token";
	public static final String QUERY_STRING_OAUTH2_URL	= "https://api.twitter.com/oauth2/token";

	public static final String OAUTH2_TOKEN_GRANT_TYPE 	= "client_credentials";
	public static final String OAUTH2_TOKEN_CONTENT_TYPE= "application/x-www-form-urlencoded;charset=UTF-8";
	public static final String OAUTH2_ACCESS_TOKEN 		= "access_token";
	public static final String OAUTH2_TOKEN_TYPE 		= "token_type";
	public static final String OAUTH2_TOKEN_TYPE_VALUE	= "bearer";


	public static final String OAUTH2_ERROR 			= "errors";
	public static final String OAUTH2_ERROR_MESSAGE		= "message";
	public static final String OAUTH2_ERROR_LABEL		= "label";
	public static final String OAUTH2_ERROR_CODE		= "code";

	public static final String OAUTH2_SUCCESS_MSG		= "Successful";
	public static final String OAUTH2_FAILURE_MSG		= "Failed";

	public static final String TEST_CONNECTION_API_KEY	= "ApiKey";
	public static final String TEST_CONNECTION_SECRET_KEY = "SecretKey";

    public static final String RESPONSE_FIELD_IDS = "ids";

    public static final String RESPONSE_FIELD_CURSOR_NEXT_AS_STRING = "next_cursor_str";
    public static final String RESPONSE_VALUE_CURSOR_FINAL = "0";
    
    public static final Long INVALID_MAX_ID = -1L;
    public static final Long INVALID_SINCE_ID = -2L;
}
