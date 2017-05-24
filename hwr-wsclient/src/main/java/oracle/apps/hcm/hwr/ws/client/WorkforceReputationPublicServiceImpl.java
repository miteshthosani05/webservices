/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.ws.client;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import oracle.apps.hcm.hwr.connectors.remote.wsobjects.ProfileSyncInfoContainer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.ProfileSyncInfoContainerSerializer;
import oracle.apps.hcm.hwr.csf.WSCredentials;
import oracle.apps.hcm.hwr.domain.connector.ProfileSyncInfo;
import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.util.serialization.SerializationUtilException;
import oracle.apps.hcm.hwr.ws.client.common.ConnectionInfo;
import oracle.apps.hcm.hwr.ws.client.common.util.ResultFormatting;
import oracle.webservices.ClientConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import weblogic.wsee.jws.jaxws.owsm.SecurityPolicyFeature;

import com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService;
import com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService_Service;

/**
 * The Class WokfoceReputationPublicServiceImpl.
 */
public class WorkforceReputationPublicServiceImpl implements 
        oracle.apps.hcm.hwr.ws.client.WorkforceReputationPublicService {

    /** Logger. */
    private static Log LOGGER = LogFactory.getLog(WorkforceReputationPublicServiceImpl.class);

    /** Default protocol */
    private static final String DEFAULT_WEB_PROTOCOL = "http";

    @Autowired
    private ServletContext mServletContext;
    
    /** Used for the auto generated code */
    private static WorkforceReputationService_Service mWorkforceReputationPublicService_Service;
    private static WorkforceReputationService mWorkforceReputationPublicService;
    Map<String, Object> reqContext;
    
    protected Map<String, Object> getReqContext() {
    	return reqContext;
    }
    
    public WorkforceReputationPublicServiceImpl(ServletContext pServletContext) {
    	mServletContext = pServletContext;
    }
    
    public WorkforceReputationPublicServiceImpl() {
    	
    }
    
    /**
     * Set up the connection between the auto-generated client code and the server. 
     * Also add in code needed for the OWSM security policies.
     * 
     * @param pConnectionInfo
     */
    public void createWorkforceReputationPublicService(final ConnectionInfo pConnectionInfo) {
        mWorkforceReputationPublicService_Service = new WorkforceReputationService_Service(getUrl(pConnectionInfo));
        SecurityPolicyFeature[] securityFeatures =
                new SecurityPolicyFeature[] { new SecurityPolicyFeature("oracle/wss11_username_token_with_message_protection_client_policy") };

        mWorkforceReputationPublicService = mWorkforceReputationPublicService_Service.getWorkforceReputationServiceSoapHttpPort(securityFeatures);
        reqContext = ((BindingProvider) mWorkforceReputationPublicService).getRequestContext();
        setupRequestContext();
    }
    
    protected void setupRequestContext() {
        if(notUsingAppProperties()) {
            setCredentials(
                WSCredentials.getUsername(),
                WSCredentials.getPassword(),
                WSCredentials.getKeystoreTypeKey(),
                WSCredentials.getKeystorePassword());
        } else {
            LOGGER.warn("Using WS parameter settings from app.properties instead of CSF.");
            setCredentials(
                System.getProperty("ws.sec_username"),
                System.getProperty("ws.sec_password"),
                System.getProperty("ws.sec_keystore_type"),
                System.getProperty("ws.sec_keystore_password"));
        }
    }
    
    private boolean notUsingAppProperties() {
    	if(System.getProperty("ws.sec_credentials_defined_in_configuration") == null) {
    		return true;
    	}
    	if(!System.getProperty("ws.sec_credentials_defined_in_configuration").toLowerCase().equals("true")) {
    		return true;
    	} else {
    		return false;
    	}
    }

    private void setCredentials(
        String pUsername,
        String pPassword,
        String pKeystoreType,
        String pKeystorePassword) {
    	
    	String myKeystoreLocationRelativeToServletContext = System.getProperty("ws.keystore.location.relative.to.servlet.context");
    	
    	String myKeystoreLocation = null;
    	
    	if(myKeystoreLocationRelativeToServletContext != null && !myKeystoreLocationRelativeToServletContext.equals("")) {
    		myKeystoreLocation = mServletContext.getRealPath(myKeystoreLocationRelativeToServletContext);
    		reqContext.put(ClientConstants.WSSEC_KEYSTORE_LOCATION, myKeystoreLocation);
    		LOGGER.info("Using keystore location provided in app.properties for Fusion web services security.");
    	} else {
    		LOGGER.info("The property ws.keystore.location.relative.to.servlet.context is not present in the configuration. Using default keystore location for Fusion web services security.");
    	}
    	
        reqContext.put(BindingProvider.USERNAME_PROPERTY, pUsername);
        reqContext.put(BindingProvider.PASSWORD_PROPERTY, pPassword);
        if(pKeystoreType != null && !pKeystoreType.trim().equals("")) {
        	reqContext.put(ClientConstants.WSSEC_KEYSTORE_TYPE, pKeystoreType);
        } else {
        	reqContext.put(ClientConstants.WSSEC_KEYSTORE_TYPE, "JKS");
        }
        
        reqContext.put(ClientConstants.WSSEC_KEYSTORE_PASSWORD, pKeystorePassword);
    }
    
    // Can be used to test CSF
	@SuppressWarnings("unused")
	private void logCredentials() {
    	for(Map.Entry<String, Object> credential: reqContext.entrySet()) {
            LOGGER.info("Credential found: '" + credential.getKey() +"': '" + credential.getValue() + "'");
        }
    }

    /**
     * Poll for job.
     * @param pConnectionInfo the connection info
     * @param pRequestParams the request params
     * @return the string representing a serialized result object.
     */
    public String pollForJob(final ConnectionInfo pConnectionInfo, final String pWLA) {
    	LOGGER.info("Calling webservices method: pollForJob().");
        return prepareWebserverResultServerMsgSend(new PollForJob(), pWLA, pConnectionInfo);
    }

    /**
     * Update job progress.
     * @param pConnectionInfo the connection info
     * @param pRequestParams the request params
     * @return the string representing a serialized result object.
     */
    public String updateJobProgress(final ConnectionInfo pConnectionInfo, final String pJobId) {
    	LOGGER.info("Calling webservices method: updateJobProgress().");
        return prepareWebserverResultServerMsgSend(new UpdateJobProgress(), pJobId, pConnectionInfo);
    }

    /**
     * Write connector data.
     * @param pConnectionInfo the connection info
     * @param pRequestParams the request params
     * @return the string representing a serialized result object.
     */
    public String writeConnectorData(final ConnectionInfo pConnectionInfo, final String pDataWriter) {
    	LOGGER.info("Calling webservices method: writeConnectorData().");
        return prepareWebserverResultServerMsgSend(new WriteConnectorData(), pDataWriter, pConnectionInfo);
    }

    /**
     * Merge persona sync data.
     * @param pConnectionInfo the connection info
     * @param pRequestParams the request params
     * @return the string representing a serialized result object.
     */
    public String mergePersonaSyncData(final ConnectionInfo pConnectionInfo,
            final String pPersonaSyncdata) {
    	LOGGER.info("Calling webservices method: mergePersonaSyncData().");
        return prepareWebserverResultServerMsgSend(new MergePersonaSyncData(), pPersonaSyncdata, pConnectionInfo);
    }

    /**
     * Register user.
     * @param pConnectionInfo the connection info
     * @param pRequestParams the request params
     * @return the string representing a serialized result object.
     */
    public String registerUser(final ConnectionInfo pConnectionInfo, final String pPersonaSyncdata) {
            logRegisterUser(pPersonaSyncdata);
            LOGGER.info("Calling webservices method: registerUser().");
        return prepareWebserverResultServerMsgSend(new RegiserUser(), pPersonaSyncdata, pConnectionInfo);
    }
    
    /**
     * Initialize WLA.
     * @param pConnectionInfo the connection info
     * @param The Wla
     * @return the string representing a serialized result object.
     */
    public String initializeWla(final ConnectionInfo pConnectionInfo, final String pWla) {
    	LOGGER.info("Calling webservices method: initializeWla().");
        return prepareWebserverResultServerMsgSend(new InitializeWla(), pWla, pConnectionInfo);
    }
    
    /**
     * Get user profile.
     * @param pConnectionInfo the connection info
     * @param pUserProfile the user profile
     * @return the string representing a serialized result object.
     */
    public String getUserProfile(final ConnectionInfo pConnectionInfo, final String pUserProfile) {
    	LOGGER.info("Calling webservices method: getUserProfile().");
        return prepareWebserverResultServerMsgSend(new GetUserProfile(), pUserProfile, pConnectionInfo);
    }

    private void logRegisterUser(String pMsgToSend) {
        ProfileSyncInfoContainer myProfileSyncInfoContainer = 
                new ProfileSyncInfoContainerSerializer().deserialize(pMsgToSend);
        LOGGER.info("ProfileSyncInfo: ApplicationName: " + myProfileSyncInfoContainer.getConnectorApplicationName());

        ProfileSyncInfo myProfileSyncInfo = myProfileSyncInfoContainer.getProfileSyncInfo();
        LOGGER.info("Profile ID: " + myProfileSyncInfo.getProfileID());
    }
    
    public String endorseUser(final ConnectionInfo pConnectionInfo, final String pUserToEndorse) {
    	LOGGER.info("Calling webservices method: endorseUser().");
        return prepareWebserverResultServerMsgSend(new EndorseUser(), pUserToEndorse, pConnectionInfo);
    }
    
    public String getMyEndorsements(final ConnectionInfo pConnectionInfo, final String pGlobalProfileId) {
    	LOGGER.info("Calling webservices method: getMyEndorsements().");
        return prepareWebserverResultServerMsgSend(new GetMyEndorsements(), pGlobalProfileId, pConnectionInfo);
    }
    
    public String getUserToEndorse(final ConnectionInfo pConnectionInfo, final String pGlobalProfileIds) {
    	LOGGER.info("Calling webservices method: getUserToEndorse().");
        return prepareWebserverResultServerMsgSend(new GetUserToEndorse(), pGlobalProfileIds, pConnectionInfo);
    }
    
    public String searchUserToEndorse(final ConnectionInfo pConnectionInfo, final String pSearchCriteria) {
    	LOGGER.info("Calling webservices method: searchUserToEndorse().");
        return prepareWebserverResultServerMsgSend(new SearchUserToEndorse(), pSearchCriteria, pConnectionInfo);
    }

    /**
     * Gets the url.
     * @return the url
     */
    protected final String getUrl(final ConnectionInfo pConnectionInfo) {
        StringBuffer myUrl = new StringBuffer();

        // In old client, protocol would default, so do that here for backwards compatibility. 
        if (!pConnectionInfo.hasProtocol()) {
            pConnectionInfo.setProtocol(DEFAULT_WEB_PROTOCOL);
        }

        myUrl.append(pConnectionInfo.getProtocol() + "://").append(pConnectionInfo.getHost()).append(":")
            .append(pConnectionInfo.getPort()).append(pConnectionInfo.getServiceUrl());
        
        LOGGER.info("Trying to connect to server at address: " + myUrl.toString());

        return myUrl.toString();
    }
    
    /**
     * Calls methods to prepare and send the message to the server, as well as
     * format the results.
     * 
     * All new methods should be calling this, and all existing should be updated to it
     * for subsequent builds.
     * 
     * @param pServerTypeToSend - The type of message we are sending.
     * @param pMsgToSend - The contents to send.
     * @param pConnectionInfo - The connection info for the server.
     * @return Serialized Result object.
     */
    private String prepareWebserverResultServerMsgSend(ISendServerMsg pServerTypeToSend, String pMsgToSend,
        final ConnectionInfo pConnectionInfo) {
        String myResultSerialized = null;
        
        try {
        	createWorkforceReputationPublicService(pConnectionInfo);
            myResultSerialized = pServerTypeToSend.sendServerMsg(mWorkforceReputationPublicService, 
                pMsgToSend);
            
            if(myResultSerialized == null || myResultSerialized.trim().equals("")) {
            	LOGGER.error("Unable to get a response from the server.");
            } else {
            	try {
            		WebservicesResult myResult = ResultFormatting.deserializeWebserviceResultObject(myResultSerialized);
            		if(myResult == null) {
            			throw new SerializationUtilException("Returned object from deserialization was null.");
            		}
            		int myResultCode = myResult.getResultCode();
            		if(myResultCode != WebservicesResult.RESULT_SUCCESS && 
            				myResultCode != WebservicesResult.RESULT_SUCCESS_GAMIFICATION_DISABLED) {
            			String myErrorMessage = "Webservices error from server: ";
            			myErrorMessage += myResult.getErrorMessage();
            			myErrorMessage += " (Error Code: '" + myResultCode + "')";
            			LOGGER.error(myErrorMessage);
            			throw new WSClientException(myResultCode, myErrorMessage);
            		}
            		String myResultContent = myResult.getResultContent();
            		String myMethod = pServerTypeToSend.getMethodName();
            		if(myResultContent == null || myResultContent.trim().equals("")) {
            			LOGGER.info("The server returned void for method: " + myMethod);
            		}
            	} catch(SerializationUtilException e) {
            		String myErrorMessage = "Webservices error on client side: ";
            		myErrorMessage += "Unable to deserialize the object received from the server.";
            		LOGGER.error(myErrorMessage, e);
            		throw new WSClientException(WebservicesResult.RESULT_SERIALIZATION_ERROR, myErrorMessage, e);
            	}
            }
        } catch(WebServiceException e) {
        	String myErrorMessage = "Web services error on client side while trying to reach the server: ";
        	myErrorMessage += "\nPossible causes of the failure:\n";
        	myErrorMessage += "The Keystore containing the trust certificate for webservices certificate vaildation is not present in one of the expected locations.\n";
        	myErrorMessage += "The password of the Trust Keystore is invalid or missing in the credentials store of the server running WRSA.\n";
        	myErrorMessage += "The user name and/or password for web services authentication are invalid or missing in the credentials store of the server running WRSA.\n";
            myErrorMessage += "The URL of the server might be wrong ('" + getUrl(pConnectionInfo) + "').\n";
            myErrorMessage += "Failure message: '" + e.getMessage() + "'";
            LOGGER.error(myErrorMessage, e);
            throw new WSClientException(WebservicesResult.RESULT_INVALID_URL, myErrorMessage, e);
        } catch(WSClientException e) {
        	throw e;
        } catch (Exception e) {
            String myErrorMessage = "Webservices error on client side while trying to reach the server: ";
            myErrorMessage += e.getClass() + ": " + e.getMessage();
            LOGGER.error(myErrorMessage, e);
            throw new WSClientException(WebservicesResult.RESULT_UNKNOWN_ERROR, myErrorMessage, e);
        }
        
        return myResultSerialized;
    }

}
