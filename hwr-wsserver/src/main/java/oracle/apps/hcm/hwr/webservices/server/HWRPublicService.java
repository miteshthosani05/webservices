/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.webservices.server;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oracle.apps.odin.utils.base.Provider;
import oracle.apps.grc.extensibility.connector.AbstractConnectorConfig;
import oracle.apps.grc.extensibility.connector.IConnectorSpecification;
import oracle.apps.grc.extensibility.connector.IDataWriter;
import oracle.apps.grc.extensibility.connector.exception.WriteException;
import oracle.apps.grc.reasonerio.graph.blockbytype.writer.BBTDataWriter;
import oracle.apps.hcm.hwr.appservices.IReputationService;
import oracle.apps.hcm.hwr.appservices.dataaccesshelpers.SourceServiceAdapter;
import oracle.apps.hcm.hwr.appservices.dataaccesshelpers.impl.SourceServiceAdapterImpl;
import oracle.apps.hcm.hwr.appservices.jive.PersistRegisteredJiveUser;
import oracle.apps.hcm.hwr.appservices.job.remote.IRemoteJob;
import oracle.apps.hcm.hwr.connectors.jive.JiveConnectorConfig;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.Attribute;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.GraphData;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.GraphDataElement;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.JobRequest;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.JobResponse;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.MessageContainer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.MessageHeaderContainer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.MessageJobCompletionInfo;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.ProfileSyncInfoContainer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.ProgressHandlerData;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.Relation;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.RemoteProfileSyncInfoServiceDataSet;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.GraphDataSerializer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.JobRequestSerializer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.JobResponseSerializer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.ProfileSyncInfoContainerSerializer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.ProgressHandlerDataSerializer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.RemoteProfileSyncInfoServiceDataSetSerializer;
import oracle.apps.hcm.hwr.dataaccess.ConfigurationService;
import oracle.apps.hcm.hwr.dataaccess.GlobalProfileService;
import oracle.apps.hcm.hwr.dataaccess.GlobalProfileService.FilterKey;
import oracle.apps.hcm.hwr.dataaccess.MessageService;
import oracle.apps.hcm.hwr.dataaccess.ProfileControlSourceMetricService;
import oracle.apps.hcm.hwr.dataaccess.ProfileSyncInfoService;
import oracle.apps.hcm.hwr.dataaccess.SourceService;
import oracle.apps.hcm.hwr.domain.Configuration;
import oracle.apps.hcm.hwr.domain.GlobalProfile;
import oracle.apps.hcm.hwr.domain.GlobalProfile.GlobalProfileBuilder;
import oracle.apps.hcm.hwr.domain.Message;
import oracle.apps.hcm.hwr.domain.MessageHeader;
import oracle.apps.hcm.hwr.domain.MessageHeader.MessageHeaderBuilder;
import oracle.apps.hcm.hwr.domain.Profile;
import oracle.apps.hcm.hwr.domain.Source;
import oracle.apps.hcm.hwr.domain.Source.Sources;
import oracle.apps.hcm.hwr.domain.connector.ProfileSyncInfo;
import oracle.apps.hcm.hwr.domain.descendantdef.GlobalProfileDescendantDef;
import oracle.apps.hcm.hwr.domain.descendantdef.ProfileDescendantDef;
import oracle.apps.odin.domain.job.JobRun.STATUS;
import oracle.apps.hcm.hwr.domain.rewards.Player;
import oracle.apps.hcm.hwr.domain.rewards.service.PlayerService;
import oracle.apps.hcm.hwr.domain.rewards.service.UserService;
import oracle.apps.hcm.hwr.domain.rewards.service.exception.RewardObjectServiceException;
import oracle.apps.hcm.hwr.domain.webservices.RegisteredUserProfile;
import oracle.apps.hcm.hwr.domain.webservices.RegisteredUserProfile.RegisteredProfileType;
import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.WlaUserInfo;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WlaUserInfoSerializer;
import oracle.apps.hcm.hwr.extwebclient.rest.jive.JiveRestClient;
import oracle.apps.hcm.hwr.rewards.badgeville.GamificationObjectReferenceCache;
import oracle.apps.hcm.hwr.rewards.badgeville.WlaInitializationObject;
import oracle.apps.hcm.hwr.rewards.serializer.WlaInitializationObjectSerializer;
import oracle.apps.hcm.hwr.rewards.serviceprovider.GamificationCredentials;
import oracle.apps.odin.scheduler.job.executor.remote.service.RemoteJobService;

import oracle.apps.odin.datafilter.relationalquery.AbstractFilter;
import oracle.apps.odin.datafilter.relationalquery.CollectionFilter;
import oracle.apps.odin.datafilter.relationalquery.CompositeFilter;
import oracle.apps.odin.datafilter.relationalquery.SingleValueFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * The Class HWRPublicService is used to write the methods Used for Exposing Webservices.
 * @author schitull
 */
@Component
public class HWRPublicService implements IHWRPublicService {

    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(HWRPublicService.class);

    /** Poll for jobs every minute */
    private static final int POLL_JOB_INTERVAL = 60;
    
    private static final String GAMIFICATION_PLAYER_ID = "gamificationPlayerId";

    /** The profile sync info service instance */
    @Autowired
    private ProfileSyncInfoService mProfileSyncInfoService;

    /** The source service instance */
    @Autowired
    private SourceService mSourceService;

    /** The message service instance */
    @Autowired
    private MessageService mMessageService;
    
    @Autowired
    @Qualifier("GamificationCredentials")
    private Provider<GamificationCredentials> mGamificationCredentials;
    
    @Autowired
    @Qualifier("GamificationObjectReferenceCache")
    private Provider<GamificationObjectReferenceCache> mObjectReferenceCache;
    
    @Autowired
    private ConfigurationService mConfigurationService;
    
    @Autowired
    private GlobalProfileService mGlobalProfileService;
    
    @Autowired
    private PlayerService mPlayerService;
    
    @Autowired 
    private UserService mUserService;
    
    @Autowired
    private IReputationService mReputationService;
    
    @Autowired
    private ProfileControlSourceMetricService mProfileControlSourceMetricService;
    
    @Autowired
    private WebserviceUserScoreData mScoreData;
    
    /**
     * Cache the state of whether Gamification is enabled or not when WLA is initialized.
     * Update this when ever WLA is initialized again.
     */
    private boolean mIsGamificationEnabledCache = false;
    
    private static final String GAMIFICATION_ENABLED_FLAG = "GAMIFICATION_ENABLED_FLAG";

    private Source getSourceByApplicationName(String pApplicationName) {
        CompositeFilter myCompositeFilter = new CompositeFilter();

        SingleValueFilter myapplicationNameFilter = new SingleValueFilter();
        myapplicationNameFilter.setDataType(SingleValueFilter.DATA_TYPE_STRING);
        myapplicationNameFilter.setKey("applicationName");
        myapplicationNameFilter.setValue(pApplicationName);
        myCompositeFilter.add(myapplicationNameFilter);

        Collection<Source> mySources = mSourceService.loadAllSources(myCompositeFilter, null, null);

        if (mySources == null || mySources.size() == 0) {
            return null;
        }
        return mySources.iterator().next();
    }
    
    /**
     * Converts a list of MessageContainers into a list of Messages. MessageContainers are light
     * weight version of Messages used to transfer the messages between web services.
     * @param pMessageSet The list of Message instances.
     * @return The list of MessageContainer instances.
     */
    private Set<Message> convertToMessageList(Set<MessageContainer> pMessageSet) {

        Set<Message> myNewSet = null;

        if (pMessageSet != null) {

            myNewSet = new HashSet<Message>(pMessageSet.size());

            for (MessageContainer myMessageContainer : pMessageSet) {
                Message myNewMessage = null;
                List<MessageHeader> myMessageHeaders =
                    new ArrayList<MessageHeader>(myMessageContainer.getMessageHeaders().size());

                for (MessageHeaderContainer myMessageHeaderContainer : myMessageContainer
                    .getMessageHeaders()) {

                    // Only use the processed messages
                    if (myMessageHeaderContainer.isProcessed()) {

                        // Create the message if not created
                        if (myNewMessage == null) {
                            myNewMessage =
                                Message.createMessage(myMessageContainer.getMessageID(), null,
                                    null, null, null, myMessageHeaders);
                        }

                        MessageHeaderBuilder myBuilder =
                            MessageHeader.builder(mSourceService.findById(myMessageHeaderContainer
                                .getSourceID()));
                        myBuilder.setIsProcessed(true);

                        myMessageHeaders.add(myBuilder.build());
                    }

                }

                if (myNewMessage != null) {
                    myNewSet.add(myNewMessage);
                }
            }

        }

        return myNewSet;
    }

    /*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.webservices.server.IHWRPublicService#registerUser(java.lang.String)
     */
    @Override
    public String registerUser(final String pProfile) {

        assert mProfileSyncInfoService != null;

        WebservicesResult myResult = new WebservicesResult();

        // Get the ProfileSyncInfo instance
        ProfileSyncInfoContainer myProfileSyncInfoContainer =
            new ProfileSyncInfoContainerSerializer().deserialize(pProfile);

        if (myProfileSyncInfoContainer.getProfileSyncInfo() == null) {
            myResult.setResultCode(WebservicesResult.RESULT_PROFILE_SYNC_INFO_NOT_SET);
            myResult
                .setErrorMessage("No ProfileSyncInfo instance is set in the ProfileSyncInfoContainer.");
        } else if (myProfileSyncInfoContainer.getConnectorApplicationName() == null
            || myProfileSyncInfoContainer.getConnectorApplicationName().isEmpty()) {
            myResult.setResultCode(WebservicesResult.RESULT_PROFILE_SYNC_INFO_APP_NAME_NOT_SET);
            myResult
                .setErrorMessage("No connector application name is set in the ProfileSyncInfoContainer.");
        } else {
            // Get the data source ID
            Source mySource =
                getSourceByApplicationName(myProfileSyncInfoContainer.getConnectorApplicationName());

            if (mySource == null) {
                myResult.setResultCode(WebservicesResult.RESULT_SOURCE_NOT_FOUND_FOR_APP_NAME);
                myResult.setErrorMessage("No source is found for application name '"
                    + myProfileSyncInfoContainer.getConnectorApplicationName() + "'.");
            } else {
                ProfileSyncInfo myProfileSyncInfo = myProfileSyncInfoContainer.getProfileSyncInfo();

                // Set the data source ID before saving since WLA does not know about the data
                // source ID.
                myProfileSyncInfo.setDataSourceID(mySource.getId());
                
                // Save the profile sync info instance
               try {                    
                    // See if this profile already exists
                    ProfileSyncInfo myExistingProfileSyncInfo =
                        mProfileSyncInfoService.findBy(myProfileSyncInfo.getProfileID(),
                            myProfileSyncInfo.getDataSourceID());

                    if (myExistingProfileSyncInfo != null) {                        
					
                        if (myProfileSyncInfo.getAuthToken() != null
                            && !myProfileSyncInfo.getAuthToken().isEmpty()) {
                            myExistingProfileSyncInfo
                                .setAuthToken(myProfileSyncInfo.getAuthToken());
                            myExistingProfileSyncInfo.setAuthSecret(myProfileSyncInfo
                                .getAuthSecret());
                            myExistingProfileSyncInfo.setState(myProfileSyncInfo.getState());
                            myExistingProfileSyncInfo.setAuthTokenExpirationDate(myProfileSyncInfo
                                .getAuthTokenExpirationDate());

							myExistingProfileSyncInfo.setGlobalProfileId(myProfileSyncInfo.getGlobalProfileId());
							myExistingProfileSyncInfo.setLinkedProfileData(
							    myProfileSyncInfo.getLinkedProfileId(), myProfileSyncInfo.getLinkedDataSourceId());
							
							//reset the last download fields on this profile.
							myExistingProfileSyncInfo.setLastDownloadDateProfileCore(null);
							myExistingProfileSyncInfo.setmLastDownloadDateProfileRelations(null);
							myExistingProfileSyncInfo.setmLastDownloadDateProfilePosts(null);
							myExistingProfileSyncInfo.setLinkedProfileData(myProfileSyncInfo.getLinkedProfileId(), 
							    myProfileSyncInfo.getLinkedDataSourceId());
 								
                            mProfileSyncInfoService.save(myExistingProfileSyncInfo);
                        }

                    } else {
                        
                        mProfileSyncInfoService.save(myProfileSyncInfo);
                    }
                    myResult.setResultCode(WebservicesResult.RESULT_SUCCESS);
                } catch (Throwable e) {
                    LOGGER.error(e);
                    myResult.setResultCode(WebservicesResult.RESULT_PROFILE_SYNC_INFO_SAVE_ERROR);
                    myResult.setErrorMessage(e.getMessage());
                }
            }
        }

        // Return the result
        return new WebservicesResultSerializer().serialize(myResult);
    }
   
    /*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.webservices.server.IHWRPublicService#pollForJob(java.lang.String)
     */
    @Override
    public String pollForJob(final String pJobRequest) {
    	// TODO can't unit test until singleton for RemoteJobService is removed!
        assert (mSourceService != null);

        assert (mMessageService != null);

        // Get the ProfileSyncInfo instance
        JobRequest myRequest =
            new JobRequestSerializer().deserialize(pJobRequest);

        // Create the response object
        JobResponse myResponse = new JobResponse(POLL_JOB_INTERVAL, null);

        // Try to assign a job if the request is asking for it
        if (myRequest.isAskingForJob()) {

            // Get the next job in the queue
            IRemoteJob myJob = (IRemoteJob)RemoteJobService.INSTANCE.getJob();

            if (myJob != null) {
            	myResponse.addJobInfo(myJob.getJobInfo());
            }
        }

        // See if the request contains any data source IDs to pull the connector configs for
        if (myRequest.getConnectorConfigDataSourceIDs() != null
            && myRequest.getConnectorConfigDataSourceIDs().size() > 0) {

            Map<Long, AbstractConnectorConfig> myConfigMap = null;

            for (Long myDataSourceID : myRequest.getConnectorConfigDataSourceIDs()) {
                Source mySource = mSourceService.findById(myDataSourceID);

                if (mySource != null) {
                    IConnectorSpecification mySpec = mySource.getConnectorSpecification();

                    if (mySpec != null) {
                        AbstractConnectorConfig myConfig = mySpec.getConfig();

                        if (myConfig != null) {
                            if (myConfigMap == null) {
                                myConfigMap =
                                    new HashMap<Long, AbstractConnectorConfig>(myRequest
                                        .getConnectorConfigDataSourceIDs().size());
                                myResponse.setConnectorConfigs(myConfigMap);
                            }

                            // Add the config we found to the map in the response
                            myConfigMap.put(myDataSourceID, myConfig);
                        }
                    }
                }
            }
        }

        MessageJobCompletionInfo myMessageJobCompletionInfo =
            myRequest.getMessageJobCompletionInfo();

        if (myMessageJobCompletionInfo != null) {
            // Set all the messages processed
            Set<Message> mySentMessages =
                convertToMessageList(myMessageJobCompletionInfo.getMessages());

            // Update the processed flag for all sent messages
            try {
                mMessageService.setProcessed(mySentMessages);
            } catch (Exception e) {
                LOGGER.error(e);
            }

            // When the BasicChainedProgressManager is initialized it already has
            // one item to do. This is to complete that.
            RemoteJobService.INSTANCE.incrementItemsDone(myMessageJobCompletionInfo.getJobID(), 1);

            try {
                RemoteJobService.INSTANCE.setFinished(myMessageJobCompletionInfo.getJobID(),
                    STATUS.COMPLETED);
            } catch (Exception e) {
                LOGGER.error(e);
            }
        }

        // Return the response
        return new JobResponseSerializer().serialize(myResponse);
    }

    /*
     * (non-Javadoc)
     * @see
     * oracle.apps.hcm.hwr.webservices.server.IHWRPublicService#mergeProfileSyncInfoData(java.lang
     * .String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public String mergeProfileSyncInfoData(final String pDataSet) {

        assert mProfileSyncInfoService != null;

        RemoteProfileSyncInfoServiceDataSet myDataSet = new RemoteProfileSyncInfoServiceDataSet();

        RemoteProfileSyncInfoServiceDataSetSerializer myDeserializer =
            new RemoteProfileSyncInfoServiceDataSetSerializer();

        myDataSet = myDeserializer.deserialize(pDataSet);

        if (myDataSet != null) {
            List<ProfileSyncInfo> myExistingRecords = myDataSet.getProfileSyncInfosUpdated();
            List<ProfileSyncInfo> myDeletedRecords = myDataSet.getProfileSyncInfosDeleted();

            if (myExistingRecords != null) {
                mProfileSyncInfoService.save(myExistingRecords
                    .toArray(new ProfileSyncInfo[myExistingRecords.size()]));
            }

            if (myDeletedRecords != null) {
                for (ProfileSyncInfo myProfileSyncInfo : myDeletedRecords) {
                    mProfileSyncInfoService.delete(myProfileSyncInfo.getProfileID(),
                        myProfileSyncInfo.getDataSourceID());
                }
            }
        }

        WebservicesResult myResult = new WebservicesResult();
        myResult.setResultCode(WebservicesResult.RESULT_SUCCESS);

        // Return the result
        return new WebservicesResultSerializer().serialize(myResult);
    }

    /**
     * Writes an Attribute instance to the graph using the specified writer.
     * @param pDataWriter The data writer.
     * @param pAttribute The Attribute instance.
     * @throws WriteException
     */
    private void writeAttribute(IDataWriter pDataWriter, Attribute pAttribute)
        throws WriteException {
        pDataWriter.addAttribute(pAttribute.getAttributeURI(), pAttribute.getNodeName(),
            pAttribute.getDataSourceId(), pAttribute.getPrimaryKeys(), pAttribute.getValue(),
            pAttribute.getLocale());

    }

    /**
     * Writes a Relation instance to the graph using the specified writer.
     * @param pDataWriter The data writer.
     * @param pRelation The Attribute instance.
     * @throws WriteException
     */
    private void writeRelation(IDataWriter pDataWriter, Relation pRelation) throws WriteException {
        pDataWriter.addRelation(pRelation.getRelationURI(), pRelation.getDataSourceId(),
            pRelation.getSubjectPrimaryKeys(), pRelation.getObjectPrimaryKeys(),
            pRelation.getLocale());
    }

    /*
     * (non-Javadoc)
     * @see
     * oracle.apps.hcm.hwr.webservices.server.IHWRPublicService#writeConnectorData(java.lang.String)
     */
    @Override
    public String writeConnectorData(String pInput) {

    	WebservicesResult myResult = new WebservicesResult();
    	myResult.setResultCode(WebservicesResult.RESULT_SUCCESS);
    	
        GraphData myData =
            new GraphDataSerializer().deserialize(pInput);

        if (myData != null) {
            // Create a data writer
            IDataWriter myWriter = new BBTDataWriter();

            // Get all the elements
            Collection<GraphDataElement> myElements = myData.getElements();

            for (GraphDataElement myElement : myElements) {
                try {
                    if (myElement instanceof Attribute) {
                        writeAttribute(myWriter, (Attribute) myElement);
                    } else if (myElement instanceof Relation) {
                        writeRelation(myWriter, (Relation) myElement);
                    } else {
                        // Unsupported type. This is seriously wrong
                        myResult.setResultCode(WebservicesResult.RESULT_UNKNOWN_GRAPH_DATA_ELEMENT_TYPE_ERROR);
                        myResult.setErrorMessage("Unknown GraphDataElement is encountered.");
                        break;
                    }
                } catch (WriteException e) {
                    myResult.setResultCode(WebservicesResult.RESULT_UNABLE_TO_WRITE_GRAPH_DATA_ELEMENT_ERROR);
                    myResult.setErrorMessage("Write exception: " + e.getMessage());
                    break;
                }
            }
        }

        // Return the result
        return new WebservicesResultSerializer().serialize(myResult);
    }

    /*
     * (non-Javadoc)
     * @see
     * oracle.apps.hcm.hwr.webservices.server.IHWRPublicService#updateJobProgress(java.lang.String)
     */
    @Override
    public String updateJobProgress(String pInput) {

    	WebservicesResult myResult = new WebservicesResult();
    	myResult.setResultCode(WebservicesResult.RESULT_SUCCESS);
    	
        ProgressHandlerData myProgressHandlerData =
            new ProgressHandlerDataSerializer().deserialize(pInput);

        //if (myProgressHandlerData.getItemsToDo() > 0) {
        //    RemoteJobService.INSTANCE.incrementItemsTodo(myProgressHandlerData.getJobID(),
        //        myProgressHandlerData.getItemsToDo());
        //}

        //if (myProgressHandlerData.getItemsDone() > 0) {
        //    RemoteJobService.INSTANCE.incrementItemsDone(myProgressHandlerData.getJobID(),
        //        myProgressHandlerData.getItemsDone());
        //}

        if (myProgressHandlerData.getException() != null) {

            //RemoteJobService.INSTANCE.setError(myProgressHandlerData.getJobID(),
            //    myProgressHandlerData.getException());
            try {
                RemoteJobService.INSTANCE.setFinished(myProgressHandlerData.getJobID(),
                    STATUS.ERRORED);
            } catch (Exception e) {
                LOGGER.error(e);
                myResult.setResultCode(WebservicesResult.RESULT_JOB_PROGRESS_HANDLER_UPDATE_ERROR);
                myResult.setErrorMessage(e.getMessage());
            }

        } else if (myProgressHandlerData.getIsFinished()) {

            // When the BasicChainedProgressManager is initialized it already has
            // one item to do. This is to complete that.
            RemoteJobService.INSTANCE.incrementItemsDone(myProgressHandlerData.getJobID(), 1);

            try {
                RemoteJobService.INSTANCE.setFinished(myProgressHandlerData.getJobID(),
                    STATUS.COMPLETED);
            } catch (Exception e) {
                LOGGER.error(e);
                myResult.setResultCode(WebservicesResult.RESULT_JOB_PROGRESS_HANDLER_UPDATE_ERROR);
                myResult.setErrorMessage(e.getMessage());
            }
        }

        // Return the result
        return new WebservicesResultSerializer().serialize(myResult);
    }

    /*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.webservices.server.IHWRPublicService#initializeWLA(java.lang.String)
     */
	@Override
	public String initializeWLA(String pInput) {
	    
	    LOGGER.info("initializeWLA start");
	    
	    WebservicesResult myResult = new WebservicesResult();
	    
	    setGamificationEnabledCache();
	    
	    if (mIsGamificationEnabledCache) {
	        myResult.setResultCode(WebservicesResult.RESULT_SUCCESS);
	        myResult.setResultContent(getWlaInitialization());
	    } else {
	        myResult.setResultCode(WebservicesResult.RESULT_SUCCESS_GAMIFICATION_DISABLED);
	        myResult.setErrorMessage("Gamification not enabled");
	    }
	    
	    LOGGER.info("initializeWLA end");
	    
	    // Return the result
        return new WebservicesResultSerializer().serialize(myResult);
	}
	
	String getWlaInitialization() {
        WlaInitializationObject initializationObject = new WlaInitializationObject();
        initializationObject.setGamificationCredentials(mGamificationCredentials.get());
        initializationObject.setGamificationObjectReferenceCache(mObjectReferenceCache.get());

        return new WlaInitializationObjectSerializer().serialize(initializationObject);
	}
	
	private void setGamificationEnabledCache() {
	    
	    boolean myResultVal = false;
	    
        Configuration isGamificationEnabledConfig =
            mConfigurationService.findBy(GAMIFICATION_ENABLED_FLAG);
        
        if (isGamificationEnabledConfig != null) {
            LOGGER.info("setGamificationEnabledCache isGamificationEnabledConfig != null");
            myResultVal = Boolean.valueOf(isGamificationEnabledConfig.value());
        }

        mIsGamificationEnabledCache = myResultVal;
        
        LOGGER.info("setGamificationEnabledCache Gamification is enabled is " + mIsGamificationEnabledCache);
	}

	/*
	 * (non-Javadoc)
	 * @see oracle.apps.hcm.hwr.webservices.server.IHWRPublicService#getUserProfile(java.lang.String)
	 */

	private String generateErrorWebservicesResult(int pErrorCode, String pErrorMessage) {
		WebservicesResult myResult = new WebservicesResult();
		myResult.setResultCode(pErrorCode);
        return new WebservicesResultSerializer().serialize(myResult);
	}
	
	/**
	 * Gets information for the user.
	 * 
	 * The profile can either be for an employee (search by UserName, or Email), or a non-employee
	 * (search by Profile ID & Source ID).
	 * 
	 * We can also get data for gamification.  Valid lookup for gamification data is Collection<String>
     * containing the global profile IDs.  Will return the global profiles that match those Ids.
	 * 
	 * We can also get score data for the user.  If we want score data, then can specify this 
	 * in the request.
	 * 
	 * If we want just score data and not profile data, then we need to specify the 
	 * GlobalProfileID of the user to this call, since a user must have a GlobalProfile to 
	 * have score data.
	 * 
	 * Use class WlaUserInfoRequest to build the appropriate filters with this information.
	 */	
    @Override
	public String getUserProfile(String profileInformation) {
    
	    try {
            LOGGER.info("getUserProfile Start server :" + profileInformation);
            //System.out.println("getUserProfile Start server :" + profileInformation);

            String myRet;

            CompositeFilter myUserInfoFilter = null;

            // extract the filter
            try {
                if (profileInformation != null) {
                    myUserInfoFilter = (new CompositeFilter()).fromXml(profileInformation);
                    LOGGER.info("getUserProfile: myGlobalProfileFilter: " + myUserInfoFilter);
                    //System.out
                    //    .println("getUserProfile: myGlobalProfileFilter: " + myUserInfoFilter);
                } else {
                    String myErrorMessage = "Global Profile filter is null.";
                    return handleException(
                        WebservicesResult.RESULT_GLOBAL_PROFILE_FILTER_IS_NULL_ERROR,
                        myErrorMessage);
                }

            } catch (Exception fEx) {
                String myErrorMessage =
                    "Error deserilizing Global Profile Filter :" + profileInformation;
                return handleException(
                    WebservicesResult.RESULT_GLOBAL_PROFILE_FILTER_DESERIALIZATION_ERROR,
                    myErrorMessage);
            }

            WlaUserInfo myUserInfo = null;

            GlobalProfile myGP = null;

            // handle getting profile/global profile information
            if (myUserInfoFilter.getKey().equals(WlaUserInfo.FilterKey.NON_EMPLOYEE.toString())) {
                LOGGER.info("getUserProfile handle non-employee");
                //System.out.println("getUserProfile handle non-employee");
                myUserInfo = handleNonEmployee(myUserInfoFilter);
            } else if (myUserInfoFilter.getKey().equals(WlaUserInfo.FilterKey.EMPLOYEE.toString()) ||
                    myUserInfoFilter.getKey().equals(WlaUserInfo.FilterKey.GAMIFIACTION_PLAYER_ID.toString())) {
                LOGGER.info("getUserProfile handle employee");
                //System.out.println("getUserProfile handle employee");
                myUserInfo = handleEmployee(myUserInfoFilter);
            } else if (!myUserInfoFilter.getKey().equals(
                WlaUserInfo.FilterKey.SCORES_ONLY.toString())) {
                String myErrorMessage = "Error invalid key set in CompositeFilter";
                return handleException(WebservicesResult.RESULT_GLOBAL_PROFILE_FILTER_KEY_INVALID,
                    myErrorMessage);
            }

            // extract GP for getting scores
            if (myUserInfoFilter.getKey().equals(WlaUserInfo.FilterKey.SCORES_ONLY.toString())) {
                LOGGER.info("getUserProfile handle score only");
                myGP = extractGlobalProfile(myUserInfoFilter);
                myUserInfo = new WlaUserInfo();
            } else if (myUserInfo != null && myUserInfo.getGlobalProfile() != null) {
                LOGGER.info("getUserProfile handle non-score only get GP");
                Iterator<GlobalProfile> myGPIt = myUserInfo.getGlobalProfile().iterator();
                if (myGPIt.hasNext()) {
                    myGP = myGPIt.next();
                }
            }

            // handle getting scores if GP is not null. If null then can't get a score based on it
            if (myGP != null) {
                LOGGER.info("getUserProfile handleScoreData");
                mScoreData.handleScoreData(myUserInfoFilter, myGP, myUserInfo);
            } else {
                LOGGER.info("getUserProfile myGP null don't handleScoreData");
            }

            LOGGER.info("myUserInfo == null ? " + (myUserInfo == null ? " true" : " false"));
            
            return serializeWlaUserInfo(myUserInfo);
        
	    } catch (HWRServerInternalException e) {
	        return handleException(e.getErrorCode(), e.getMessage());
	    } catch (Exception e) {
	        LOGGER.info(e);
	        return handleException(WebservicesResult.RESULT_UNKNOWN_ERROR, e.getMessage());
	    }
	}
	
	/**
	 * Get the global profile by extracting the ID out of the filter.
	 * 
	 * @param myGlobalProfileFilter
	 * @return
	 */
	private GlobalProfile extractGlobalProfile(CompositeFilter myGlobalProfileFilter) {
	    
	    GlobalProfile myGP = null;

	    for(AbstractFilter myAbstractFilter : myGlobalProfileFilter.getFilters()) {
	        
	        if (myAbstractFilter.getKey().equals(WlaUserInfo.FilterKey.GBL_PRFL_ID.toString())) {
	            String myGPStringID = (String) myAbstractFilter.getValueObject();
	            
	            Long myId = new Long(myGPStringID);
	            
	            myGP = mGlobalProfileService.findById(myId, createGlobalProfileDescendantDef());
	        }
	    }
	    
	    return myGP;
	}
	
	/**
	 * Extract the profileID and SourceID from the filter and build WlaUserInfo 
	 * 
	 * @param myGlobalProfileFilter
	 * @return
	 * @throws HWRServerInternalException
	 */
	private WlaUserInfo handleNonEmployee (CompositeFilter myGlobalProfileFilter) 
	        throws HWRServerInternalException {
	    
        String myLoginProfileId = null;
        Source myLoginSrc = null;
        
        LOGGER.info("myGlobalProfileFilter: " + myGlobalProfileFilter + " Filtersize: " + myGlobalProfileFilter.getFilters().size());

        for(AbstractFilter myAbstractFilter : myGlobalProfileFilter.getFilters()) {
            LOGGER.info("getUserProfle: filter: " + myAbstractFilter.getKey());
            if(isNonEmployeeLoginProfileId(myAbstractFilter)) {
                LOGGER.info("getUserProfle: filter: isNonEmployeeLoginProfileId true");
                myLoginProfileId = getLoginProfileId(myAbstractFilter);
            } else if(isNonEmployeeLoginSrcId(myAbstractFilter)) {
                LOGGER.info("getUserProfle: filter: isNonEmployeeLoginSrcId is true");
                myLoginSrc = getLoginSrc(myAbstractFilter);
            }
        }
        
        if (myLoginProfileId == null || myLoginSrc == null) {
            throw new HWRServerInternalException(
                WebservicesResult.RESULT_INVALID_ARG_ERROR, 
                "Necessary arguments not found.  ProfileID: " + myLoginProfileId + " source: " + myLoginSrc.getId());
        }
        
        LOGGER.info("getUserProfile: Passed in values for myLoginProfileId: "  + myLoginProfileId + 
            " myLoginSrcId: " + myLoginSrc.getId());
        
        return getNonEmployeeUserInfo(myLoginProfileId, myLoginSrc);
	}
	
	/**
	 * Build the WlaUserInfo for a non-employee
	 * 
	 * @param myLoginProfileId
	 * @param myLoginSrc
	 * @return
	 * @throws HWRServerInternalException
	 */
	private WlaUserInfo getNonEmployeeUserInfo(String myLoginProfileId, Source myLoginSrc) 
	        throws HWRServerInternalException {
	    
        //get the root profileSync Info if it exists.  This is what the user originally logged in with.
        ProfileSyncInfo myExistingProfileSyncInfo =
                mProfileSyncInfoService.loadNonRegisteredRootProfile(myLoginProfileId, myLoginSrc.getId());
                
        //if root profile sync info exists, returning user.  Use the IDs of root instead of the ones passed into the method
        if (myExistingProfileSyncInfo != null && myExistingProfileSyncInfo.getLinkedProfileId() != null &&
                !myExistingProfileSyncInfo.getLinkedProfileId().isEmpty() &&
                myExistingProfileSyncInfo.getLinkedDataSourceId() != 0) {
            LOGGER.info("getUserProfile: Got the root profile.  Swapping values");
     
            myLoginProfileId = myExistingProfileSyncInfo.getLinkedProfileId();
            Source mySource = mSourceService.findById(myExistingProfileSyncInfo.getLinkedDataSourceId());
            
            if (mySource == null) {
                throw new HWRServerInternalException(
                    WebservicesResult.RESULT_INVALID_ARG_ERROR, 
                    "Failed to find Source for ID " + myExistingProfileSyncInfo.getLinkedDataSourceId());
            }
            
            myLoginSrc = mySource;
        }

        LOGGER.info("Swapped values for getUserProfile: myLoginProfileId: "  + myLoginProfileId + 
            " myLoginSrcId: " + myLoginSrc.getId());

        List<String> myProfileList = new ArrayList<String>();
        myProfileList.add(myLoginProfileId);
        
        //no descendant def only a partial profile
        Map<String, GlobalProfile> myGPMap = mGlobalProfileService.findGlobalProfileForProfile(
            myProfileList, myLoginSrc.getId());
        
        LOGGER.info("getUserProfile: myGPMap null?" + (myGPMap == null ? "true" : "false"));
        
        WlaUserInfo myUserInfo;
        
        //GP exists for this user
        if (myGPMap != null && myGPMap.size() > 0) {
            
            //get full profile
            Collection<GlobalProfile> myGps = getFullGlobalProfiles(myGPMap.values());
            
            LOGGER.info("getUserProfile: myGPMap GP exists");
            //Turn the global profiles we retrieved earlier into full profiles.
            myUserInfo = buildNonRegisteredWlaUserInfo(myGps, myLoginProfileId,
                myLoginSrc);
        } else {
            LOGGER.info("getUserProfile: myGPMap GP does not exist");
            myUserInfo = buildNonRegisteredWlaUserInfo(myLoginProfileId, myLoginSrc);
        }
        
        return myUserInfo;
	}
	
	/**
	 * Builds the non-registered WlaUserInfo object for a user who has a global profile already
	 * 
	 * @param pGps
	 * @param pProfileId
	 * @param pSource
	 * @return
	 */
	private WlaUserInfo buildNonRegisteredWlaUserInfo(Collection<GlobalProfile> pGps,
	        String pProfileId, Source pSource) {
        
        RegisteredUserProfile myRegisteredProfiles = setRegisteredProfilesForNonEmployee(
            pProfileId, pSource);
	    
        return new WlaUserInfo(pGps, myRegisteredProfiles, pProfileId, pSource.getId());
	}
	
	/**
	 * Builds the non-registered WlaUserInfo object for a user who has no global profile 
	 * in the system.
	 * 
	 * @param pProfileId
	 * @param pSource
	 * @return
	 */
	private WlaUserInfo buildNonRegisteredWlaUserInfo(String pProfileId, Source pSource) {
	
	    RegisteredUserProfile myRegisteredProfiles = setRegisteredProfilesForNonEmployee(
	        pProfileId, pSource);
	    
	    return new WlaUserInfo(myRegisteredProfiles, pProfileId, pSource.getId());
	}
    
	/**
	 * Takes in partial global profiles and returns the full global profiles we need.
	 *  
	 * @param pGPsPartial
	 * @return
	 */
    private Collection<GlobalProfile> getFullGlobalProfiles(Collection<GlobalProfile> pGPsPartial) {
        Collection<GlobalProfile> myGlobalProfilesList = new ArrayList<GlobalProfile>();
        
        GlobalProfileDescendantDef myDef = createGlobalProfileDescendantDef();
        
        for (GlobalProfile myGp : pGPsPartial) {
            GlobalProfile myFullGp = mGlobalProfileService.findById(myGp.getID(), myDef);
            myGlobalProfilesList.add(myFullGp);
        }
        
        return myGlobalProfilesList;
    }
	
	/**
	 * Handle getting the WlaUserInfo for an employee.
	 * 
	 * Valid employee based lookups are username and email.
	 * 
	 * Also handles getting profiles for gamification.  Valid lookup for gamification data is Collection<String>
	 * containing the global profile IDs.  Will return the global profiles that match those Ids.
	 * 
	 * @param myGlobalProfileFilter
	 * @return
	 * @throws HWRServerInternalException
	 */
	public WlaUserInfo handleEmployee (CompositeFilter myGlobalProfileFilter) throws HWRServerInternalException {
        
        boolean myIsUsernameId = false;
        Collection<GlobalProfile> myGlobalProfilesList = new ArrayList<GlobalProfile>();
        
	    for(AbstractFilter myAbstractFilter : myGlobalProfileFilter.getFilters()) {

	        // first check if the query is for login or rewards stream
	        // Email - login
	        // PlayerId - rewards stream
	        if(myAbstractFilter.getKey().equals(FilterKey.EMAIL.toString())) {
	            String myUser = (String) myAbstractFilter.getValueObject();

	            GlobalProfile myGlobalProfile = null;
	            // load global profile by email
	            try {
	            	myGlobalProfile = mGlobalProfileService.findGlobalProfileByEmail(myUser);
	            	LOGGER.info("getUserProfile findGlobalProfileByEmail success: " + myGlobalProfile);
	            } catch(Exception e) {
	            	LOGGER.warn("getUserProfile findGlobalProfileByEmail not retrieved. Will try by UserName.");
	            }
	            
	            // if it didn't work by email, try by user name
	            try {
	            	if(myGlobalProfile == null) {
	            		myGlobalProfile = mGlobalProfileService.findGlobalProfileByUserName(myUser);
	            		LOGGER.info("getUserProfile findGlobalProfileByUserName success: " + myGlobalProfile);
	            		myIsUsernameId = true;
	            	}
	            } catch(Exception e) {
	            	LOGGER.warn("getUserProfile findGlobalProfileByUserName not retrieved.");
	            }
				
				// Hack - our test DBs no longer have unique email addresses for users, so try lookup by Fusion Profile ID
                try {
                    if(myGlobalProfile == null) {
                        myGlobalProfile = getGlobalProfileByFusionId(myUser);
                        
                        //can use the same logic to create an email address with Profile ID that we do with username
                        if (myGlobalProfile != null) {
                            myIsUsernameId = true;
                        }
                    }
                } catch(Exception e) {
                    LOGGER.warn("getUserProfile findGlobalProfileByFusionId not retrieved - exception case.");
                }
	            
	            if(myGlobalProfile == null) {
	            	String specificErrorMessage = "getUserProfile: No Such HWR Global Profile available for user :'" + myUser + "'";
	    	        throw new HWRServerInternalException(WebservicesResult.RESULT_GLOBAL_PROFILE_NOT_AVAILABLE_ERROR, specificErrorMessage);
	            }
	            
	            LOGGER.info("HWR Global Profile available for user :" + myUser);
	            
	            // check if gamificatioId and playerId created - if not create
	            if(mIsGamificationEnabledCache &&
	                    (myGlobalProfile.getGamificationUserId() == null || myGlobalProfile.getGamificationUserId().equals(""))){
	                Player myPlayer = new Player();
	                String myEmail = getEmailFromId(myIsUsernameId, myUser);
	                myPlayer.setEmail(myEmail);
	                //check to see if createdDate has value then set it to player
	                if(myGlobalProfile.getCreationDate() != null) {
	                	myPlayer.setGlobalCreatedDate(myGlobalProfile.getCreationDate());
	                } else {
	                	LOGGER.error("HWRPublicService: Global Profile has Null create date!!!");
	                	myPlayer.setGlobalCreatedDate(new Date(System.currentTimeMillis()));
	                }
	                try {
	                    myPlayer = mUserService.create(myPlayer);
	                    if (myPlayer.getID() == null || myPlayer.getID().equals("")) {
	                        myPlayer = mPlayerService.create(myPlayer);
	                    } 

	                    // update global profile
	                    GlobalProfile myUpdatedGlobalProfile = addUserIdAndPlayerIdToGlobalProfile(myPlayer, myGlobalProfile);
	                    myGlobalProfilesList.add(myUpdatedGlobalProfile);
	                    mGlobalProfileService.updateGamification(myGlobalProfilesList);

	                    LOGGER.info("HWR Global Profile updated with gamification user and player Ids.");

	                } 
	                catch (RewardObjectServiceException ex) {
	                    LOGGER.info("getUserProfile myGlobalProfilesList.size() " + myGlobalProfilesList.size());	         
	                    LOGGER.warn("getUserProfile: Error creating gamification User and Player: " + ex.getMessage());
	                    //TODO Determine if we should really return profile or send exception to WRSA.
	                    //I think returning profile is safer action
	                    myGlobalProfilesList.add(myGlobalProfile);
	                    //return handleException(WebservicesResult.RESULT_GAMIFICATION_USER_AND_PLAYER_CREATE_ERROR, myErrorMessage);
	                }      
	            }
	            else {
	                LOGGER.info("getUserProfile no setGamification needed");
	                myGlobalProfilesList.add(myGlobalProfile);
	            }

	        } else if(mIsGamificationEnabledCache && 
	                myAbstractFilter.getKey().equals(GAMIFICATION_PLAYER_ID)) {
	            LOGGER.info("getUserProfile FilterKey is GAMIFICATION_PLAYER_ID");

	            try {
	                // load from DB
	                Collection<GlobalProfile> myGlobalProfiles = 
	                        mGlobalProfileService.loadAllGlobalProfiles(myGlobalProfileFilter, null, null, createGlobalProfileDescendantDef());

	                if (myGlobalProfiles == null || myGlobalProfiles.isEmpty()) {
	                    LOGGER.info("getUserProfile myGlobalProfiles == null");
	                    String myErrorMessage = "Unable to retrieve Global Profiles from the DB.";
	                    throw new HWRServerInternalException(WebservicesResult.RESULT_GLOBAL_PROFILE_NOT_AVAILABLE_ERROR, myErrorMessage);
	                } 

	                LOGGER.info("getUserProfile myGlobalProfiles number of elements: " + myGlobalProfiles.size());

	                for(GlobalProfile myGlobalProfile : myGlobalProfiles) {
	                    if(myGlobalProfile != null) {
	                        String myPlayerId = myGlobalProfile.getGamificationPlayerId();
	                        if(myPlayerId != null && !myPlayerId.equals("")) {
	                            myGlobalProfilesList.add(myGlobalProfile);
	                            LOGGER.info("getUserProfile Found globalProfile " + " UserID: " 
	                                    + myGlobalProfile.getGamificationUserId() +
	                                    " playerId: " + myPlayerId);
	                        }
	                    }
	                }     

	            } catch(Exception ex) {
	                String myErrorMessage = "getUserProfile  Error loading all Global Profiles: " + ex;
	                throw new HWRServerInternalException(WebservicesResult.RESULT_GLOBAL_PROFILE_NOT_AVAILABLE_ERROR, myErrorMessage);
	            }

	        } 
	    }	    
	    
	    WlaUserInfo myWlaUserInfo =
	            new WlaUserInfo(myGlobalProfilesList, setRegisteredProfiles(myGlobalProfilesList));

        return myWlaUserInfo;
    }
	
	/**
	 * Serialize the WlaUserInfo object for return.
	 * @param pWlaUserInfo
	 * @return
	 */
	private String serializeWlaUserInfo(WlaUserInfo pWlaUserInfo) {
	    
        String mySerializedGP =
            new WlaUserInfoSerializer().serialize(pWlaUserInfo);
        
        WebservicesResult myResult = new WebservicesResult();
        myResult.setResultContent(mySerializedGP);
        myResult.setResultCode(WebservicesResult.RESULT_SUCCESS);

        return new WebservicesResultSerializer().serialize(myResult);
	}
	

	private boolean isNonEmployeeLoginProfileId(AbstractFilter filter) {
		if(filter.getKey().equals(WlaUserInfo.FilterKey.PRFL_ID.toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	
	private String getLoginProfileId(AbstractFilter filter) {
		return (String) filter.getValueObject();
	}
	
	private boolean isNonEmployeeLoginSrcId(AbstractFilter filter) {
		if(filter.getKey().equals(WlaUserInfo.FilterKey.CONNECTOR_APP_NAME.toString())) {
			return true;
		} else {
			return false;
		}
	}
	
	private Source getLoginSrc(AbstractFilter filter) throws HWRServerInternalException {
	    String myAppName = (String) filter.getValueObject();
	    
	    Source mySource = getSourceByApplicationName(myAppName);

	    //handle error
	    if (mySource == null) {
	        throw new HWRServerInternalException(
	            WebservicesResult.RESULT_SOURCE_NOT_FOUND_FOR_APP_NAME, 
	            "App name value: " + myAppName + " is not valid");
	    }
	    
        return mySource;
	}
	
	/**
	 * Sets the registered user profile object for a non-employee.
	 * Will build this based on which profile sync info's exist for this user, indicating
	 * that the user has already registered those sources.
	 * 
	 * Will also set the source the user is logging in with as registered.
	 *  
	 * @param pLoginProfileId
	 * @param pSource
	 * @return
	 */
	private RegisteredUserProfile setRegisteredProfilesForNonEmployee(String pLoginProfileId, 
	        Source pSource) {
	    
		RegisteredUserProfile myRegisteredUserProfile = new RegisteredUserProfile();
		
		//query profile sync infos that are "linked" together, indicating they were registered by this user.
		Collection<ProfileSyncInfo> myLinkedProfileSyncs = 
                mProfileSyncInfoService.findNonRegisteredProfileSyncInfos(pLoginProfileId, pSource.getId());
        
        if (myLinkedProfileSyncs != null  && myLinkedProfileSyncs.size() > 0) {
            for (ProfileSyncInfo myProfileSync : myLinkedProfileSyncs) {
                Source mySource = mSourceService.findById(myProfileSync.getDataSourceID());
                myRegisteredUserProfile.setValue(mySource.getConnectorAppName(), 
                    RegisteredProfileType.REGISTERED);
            }
        }
        
        //make sure this source shows as registered.
        myRegisteredUserProfile.setValue(pSource.getConnectorAppName(), 
            RegisteredProfileType.REGISTERED);
        
        return myRegisteredUserProfile;
	}
	
	private String handleException(int pErrorCode, String pErrorMessage) {
		String errorMessage = "Server error: ";
		LOGGER.error(pErrorMessage);
	    return generateErrorWebservicesResult(pErrorCode, errorMessage + pErrorMessage);
	}
	
	private GlobalProfile addUserIdAndPlayerIdToGlobalProfile(Player pPlayer, GlobalProfile pGlobalProfile){
		GlobalProfileBuilder myProfileBuilder = GlobalProfile.builder(pGlobalProfile.getID());
		myProfileBuilder
			.setFirstName(pGlobalProfile.getFirstName())
			.setGender(pGlobalProfile.getGender())
			.setLastName(pGlobalProfile.getLastName())
			.setLocation(pGlobalProfile.getLocation())
			.setMiddleName(pGlobalProfile.getMiddleName())
			.setPictureURL(pGlobalProfile.getPicture())
			.setCreationDate(pPlayer.getGlobalCreatedDate())
			.addGamificationUserId(pPlayer.getUserId())
			.addGamificationPlayerId(pPlayer.getID());
		
		for(Profile pProfile : pGlobalProfile.getProfiles()){
			myProfileBuilder.addProfile(pProfile);
		}
		
		return myProfileBuilder.build();
	}
	
	private RegisteredUserProfile setRegisteredProfiles(Collection<GlobalProfile> pGlobalProfiles) {

	    RegisteredUserProfile myRegisteredUserProfile = new RegisteredUserProfile();

	    for(GlobalProfile myGlobalProfile : pGlobalProfiles) {
	        if(myGlobalProfile != null) {
	            for (Profile myProfile : myGlobalProfile.getProfiles()) {
	                RegisteredProfileType myState = setState(myProfile);
	                if (myState != RegisteredProfileType.NOT_REGISTERED) {
	                    myRegisteredUserProfile.setValue(myProfile.getSource().getConnectorAppName(),
	                        myState);
	                } 
	            }
	        }
	    }

	    return myRegisteredUserProfile;
	}

	/**
	 * Get the state of the users registration.
	 * 
	 * @param pProfile
	 * @return
	 */
	private RegisteredProfileType setState(Profile pProfile) {
	    RegisteredProfileType myState = RegisteredProfileType.NOT_REGISTERED;

	    Source mySource = pProfile.getSource();

	    ProfileSyncInfo myProfileSyncInfo =
	            mProfileSyncInfoService.findBy(pProfile.getID(), mySource.getId());

	    if (myProfileSyncInfo != null) {
	        myState = convertStateToRegisteredProfileType(myProfileSyncInfo);
	    }

	    return myState;
	}

	/**
	 * Need to convert from the enum in the ProfileSyncInfo to the RegisteredProfileType.
	 * @param pProfileSyncInfo
	 * @return
	 */
	private RegisteredProfileType convertStateToRegisteredProfileType(ProfileSyncInfo pProfileSyncInfo) {
	    RegisteredProfileType myType = RegisteredProfileType.NOT_REGISTERED;

	    switch (pProfileSyncInfo.getState()) {

	    case REGISTERED:
	        myType = RegisteredProfileType.REGISTERED;
	        break;
	    case UNREGISTERED:           
	        if (pProfileSyncInfo != null && pProfileSyncInfo.getAuthToken() != null
	            && !pProfileSyncInfo.getAuthToken().isEmpty()) {
	            myType = RegisteredProfileType.EXPIRED;
	        }
	        break;
	        //don't care about these cases, so just break    
	    case REMOTE_CONNECTION:   
	    case INVALID:
	    default:
	        break;
	    }

	    return myType;
	}
	
    /**
     * @return GlobalProfileDescendantDef to load all global profiles full
     */
    private GlobalProfileDescendantDef createGlobalProfileDescendantDef() {
        ProfileDescendantDef myProfileDef = new ProfileDescendantDef();
        myProfileDef.setLoadContactInfo(true);
        myProfileDef.setLoadEducation(true);
        myProfileDef.setLoadExperience(true);
        myProfileDef.setLoadPublication(true);
        GlobalProfileDescendantDef myGlobalDef = new GlobalProfileDescendantDef();
        myGlobalDef.setLoadProfiles(true);
        myGlobalDef.setProfileDescendantDef(myProfileDef);
        return myGlobalDef;
    }
    
    /**
     * Gets all e-mail addresses from a list of global profiles
     * @param pGlobalProfiles
     * @return
     */
    private List<String> getEmailAddressesFromGP (Collection<GlobalProfile> pGlobalProfiles) {
        List<String> myEmailAddresses = new ArrayList<String>();
        if (pGlobalProfiles != null) {
            for (GlobalProfile myOneGlobalProfile : pGlobalProfiles) {
                myEmailAddresses.addAll(myOneGlobalProfile.getEmailAddress());
            }
        }
        return myEmailAddresses;
    }
    
    /**
     * Call to try to add the users to the profileSyncInfoService for Jive
     * 
     * @param pEmailAddresses - The list of e-mail addresses for the user
     *      we want to use to check to see if the user exists in Jive.
     */
    private void setJiveUserIds(List<String> pEmailAddresses) {
    	JiveConnectorConfig myJiveConnectorConfig = getJiveConnectorConfig();
    	
    	if(myJiveConnectorConfig == null) {
    		LOGGER.error("Unable to retrieve the JiveConnectorConfig.");
    		return;
    	}
    	
    	URL myJiveBaseUrl = null;
		try {
			myJiveBaseUrl = new URL(myJiveConnectorConfig.getUrl());
		} catch (MalformedURLException e) {
			LOGGER.error("Invalid URL for the Jive server: '" + myJiveBaseUrl + "'");
		}
    	String myJiveUserName = myJiveConnectorConfig.getName();
    	if(myJiveUserName == null || myJiveUserName.equals("")) {
    		LOGGER.error("Invalid user name to access the Jive API: '" + myJiveUserName + "'");
    	}
    	String myJivePassword = myJiveConnectorConfig.getPassword();
    	if(myJivePassword == null || myJivePassword.equals("")) {
    		LOGGER.error("Invalid password to access the Jive API: '" + myJivePassword + "'");
    	}
    	
    	persistUsers(pEmailAddresses, myJiveBaseUrl, myJiveUserName,
				myJivePassword);
    }

	private JiveConnectorConfig getJiveConnectorConfig() {
		SourceServiceAdapter mySourceServiceAdapter = new SourceServiceAdapterImpl(mSourceService);
    	Collection<Source> mySources = mySourceServiceAdapter.loadAllSourcesByApplicationName("JIVE");
    	JiveConnectorConfig myJiveConnectorConfig = null;
    	for(Source aSource: mySources) {
    		if(aSource.getName().toLowerCase().equals("jive")) {
    			myJiveConnectorConfig = aSource.getConnectorConfig();
    		}
    	}
		return myJiveConnectorConfig;
	}

	private void persistUsers(List<String> pEmailAddresses, URL myJiveBaseUrl,
			String myJiveUserName, String myJivePassword) {
		JiveRestClient mJiveRestClient = new JiveRestClient(myJiveBaseUrl, myJiveUserName, myJivePassword);
    	ExecutorService mPersistRegisteredJiveUserThreadsPool = Executors.newCachedThreadPool();
    	PersistRegisteredJiveUser myPersistRegisteredJiveUser = new PersistRegisteredJiveUser(mJiveRestClient, mProfileSyncInfoService, mPersistRegisteredJiveUserThreadsPool);
        for(String anEmail: pEmailAddresses) {
        	myPersistRegisteredJiveUser.persist(anEmail);
        }
        /* TODO: if the users don't get persisted, it might be because the threads are killed
         * because the spawning thread terminates. If that's the case, it will be necessary 
         * to check here if the Futures returned by persist are done, and if not, sleep for
         * a while. While sleeping other threads can make progress.
         * 
         * If this works without sleeping, please remove this comment.
         */
	}
	
	/**
	 * Temporary fix to create an e-mail address for badgeville to use when a user logs in using
	 * an e-mail address.  Refactor this later to use a real e-mail address.  
	 * 
	 * Note - during refactor must handle
	 * case where the user doesn't have an e-mail address attached to the global profile.
	 * 
	 * @param pIsUsername - false if user used an e-mail to log in... true if username.
	 * @param pUsername - The username provided by the user.
	 * @return - An e-mail address to use for badgeville.
	 */
	private String getEmailFromId(boolean pIsUserName, String pUsername) {
	    String myEmailStr;
	    String myEmailConcat = "@badgeville.com";
	    
	    if (!pIsUserName) {
	        myEmailStr = pUsername;
	    } else {
	        myEmailStr = pUsername.replaceAll(" ", "");
	        myEmailStr += myEmailConcat;
	    }
	    
	    LOGGER.info("getEmailFromId: Passed in username: " + pUsername + " Returning e-mail: " + myEmailStr);
	    return myEmailStr;
	}
	
	/**
	 * Get the global profile based on the Fusion profile ID.
	 * @param pGlobalProfileId
	 * @return
	 */
	private GlobalProfile getGlobalProfileByFusionId(String pGlobalProfileId) {
	    GlobalProfile myRetGP = null;
	    
        Source myFusionSource = getSourceByApplicationName(Sources.FUSION.getApplicationName());
        
        List<String> myProfileIds = new ArrayList<String>();
        myProfileIds.add(pGlobalProfileId);

        Map<String, GlobalProfile> myGlobalProfiles = mGlobalProfileService.
            findGlobalProfileForProfile(myProfileIds, myFusionSource.getId());

        //profile can only be attached to 1 GP so if not there is a problem
        if (myGlobalProfiles != null && myGlobalProfiles.size() == 1) {
            
            //GP does not have profiles attached... so swap this for the full GP with descendantdef
            // This is a hack since we don't have an API to get full GP from profile.
            myRetGP = getFullGlobalProfile(myGlobalProfiles.values().iterator().next());
            
            if (myRetGP == null) {
                LOGGER.info("getUserProfile getFullGlobalProfile failed to get full GP: " + myRetGP);
            }
           
            LOGGER.info("getGlobalProfileByFusionId success: " + myRetGP);
            
        } else {
            LOGGER.warn("getUserProfile findGlobalProfileForProfile not retrieved.");      
        }    
	    
	    return myRetGP;
	}
	
    private GlobalProfile getFullGlobalProfile(GlobalProfile pGlobalProfile) {
        
        GlobalProfile myRet = null;
        
        String myProfileId = Long.toString(pGlobalProfile.getID());
        
        CompositeFilter myFilter = getGlobalProfileFilter(myProfileId);       
        
        ProfileDescendantDef myProfileDef = new ProfileDescendantDef();
        myProfileDef.setLoadContactInfo(true);
        GlobalProfileDescendantDef myDescendantDef = new GlobalProfileDescendantDef();
        myDescendantDef.setLoadProfiles(true);
        myDescendantDef.setProfileDescendantDef(myProfileDef);
          
        Collection<GlobalProfile> myGlobalProfiles = 
                mGlobalProfileService.loadAllGlobalProfiles(myFilter, null, null, myDescendantDef);
          
        if (myGlobalProfiles != null && myGlobalProfiles.size() == 1) {
            myRet = myGlobalProfiles.iterator().next();            
        }
        
        return myRet;
    }
	
    /**
     * Create the composite search filter with global profile IDs.
     * @param pGlobalProfileIds - Collection of global profile IDs.
     * @return - The filter with global profile IDs.
     */
    private CompositeFilter getGlobalProfileFilter(String pGlobalProfileId) {
        List<String> myGlobalProfileIds = new ArrayList<String>();
        myGlobalProfileIds.add(pGlobalProfileId);
        
        CompositeFilter myFilter = new CompositeFilter();
        
            CollectionFilter myFilterObjs = new CollectionFilter();
            myFilterObjs.setKey(GlobalProfileService.FilterKey.GBL_PRFL_ID.toString());

            myFilterObjs.setValues(myGlobalProfileIds);
            myFilterObjs.setDataType(CollectionFilter.DATA_TYPE_LONG);
            myFilterObjs.setOperation(CollectionFilter.CRIT_TYPE_IN);
            myFilter.add(myFilterObjs);
        
        return myFilter;
    }
}
