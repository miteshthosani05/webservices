/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.extwebclient.rest.jive;

import java.net.URL;

import java.util.regex.Pattern;

import oracle.apps.hcm.hwr.extwebclient.rest.BasicSignatureProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQueryEngine;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuerySimpleResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JiveRestClient {

	private static final Log LOGGER = LogFactory.getLog(JiveRestClient.class);
	private final String classErrorMessageIdentifier = "Jive REST API: ";
	
	private RestQueryEngine mQueryEngine;
	
	public JiveRestClient(URL pJiveServer, String pUserName, String pPassword) {		
		mQueryEngine = new RestQueryEngine(
		        new JiveRestQueryProvider(pJiveServer.toString()), new BasicSignatureProvider(pUserName, pPassword));
	}
	
	public String getUserIdByUserName(String pUserName) {
		String methodErrorMessageIdentifier = "getUserIdByUserName(): ";
		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(JiveConstants.GET_USER_BY_USERNAME, 
	            pUserName, null, null, null);

		int myStatusCode = myResult.getStatusCode();
        if(myStatusCode != 200) {
        	LOGGER.error("While trying to access the Jive REST API for retieving the user id given the user name, instead of getting 200 got the following status code: " + myStatusCode);
        }
        
        String userId = extractUserIdFromResponse(pUserName,
				methodErrorMessageIdentifier, myResult);
		
		return userId;
	}
	
	public String getUserIdByEmail(String pUserEmail) {
		String methodErrorMessageIdentifier = "getUserIdByEmail(): ";
		RestQuerySimpleResult myResult = mQueryEngine.runSimpleQuery(JiveConstants.GET_USER_BY_EMAIL, 
	            pUserEmail, null, null, null);

		int myStatusCode = myResult.getStatusCode();
        if(myStatusCode != 200) {
        	LOGGER.error("While trying to access the Jive REST API for retieving the user id given the email, instead of getting 200 got the following status code: " + myStatusCode);
        }
        
        String userId = extractUserIdFromResponse(pUserEmail,
				methodErrorMessageIdentifier, myResult);
		
		return userId;
	}

	private String extractUserIdFromResponse(String pUser,
			String methodErrorMessageIdentifier, RestQuerySimpleResult myResult) {
		String response = myResult.getResponse();
        response = fixResponse(response);
        
        LOGGER.info(classErrorMessageIdentifier + methodErrorMessageIdentifier + "Response: " + response);
        
        String userId = null;
        JSONParser parser = new JSONParser();
		try {
			JSONObject parsedJSONObject = (JSONObject) parser.parse(response);
			userId = (String) parsedJSONObject.get("id");
			LOGGER.info(classErrorMessageIdentifier + methodErrorMessageIdentifier + "User id: " + userId);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		if(userId == null || userId.equals("")) {
			throw new NoSuchUserException(pUser);
		}
		return userId;
	}
	
	public static String fixResponse(String response) {
    	String illegalStartString = "";
    	for(Character illegalChar: response.toCharArray()) {
    		if(illegalChar.equals('{') || illegalChar.equals('[')) {
    			break;
    		}
    		
    		illegalStartString += illegalChar;
    	}
    	
    	if(illegalStartString.length() > 0) {
    		response = response.replaceFirst(Pattern.quote(illegalStartString), "");
    	}
    	
    	return response;
    }
}
