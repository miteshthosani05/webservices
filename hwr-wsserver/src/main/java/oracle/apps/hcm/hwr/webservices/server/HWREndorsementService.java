package oracle.apps.hcm.hwr.webservices.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.apps.grc.domain.wrm.Topic;
import oracle.apps.hcm.hwr.dataaccess.EndorsementRepository;
import oracle.apps.hcm.hwr.dataaccess.GlobalProfileService;
import oracle.apps.hcm.hwr.dataaccess.RecognitionRepository;
import oracle.apps.hcm.hwr.dataaccess.TopicService;
import oracle.apps.hcm.hwr.dataaccess.impl.HWRSearchCriteriaService;
import oracle.apps.hcm.hwr.dataaccess.result.GblProfConverter;
import oracle.apps.hcm.hwr.dataaccess.result.WRMSearchResult;
import oracle.apps.hcm.hwr.domain.Endorsement;
import oracle.apps.hcm.hwr.domain.GlobalProfile;
import oracle.apps.hcm.hwr.domain.Recognition;
import oracle.apps.hcm.hwr.domain.WrsaRecommender;
import oracle.apps.hcm.hwr.domain.WrsaRecommender.RecommendedByRelationType;
import oracle.apps.hcm.hwr.domain.descendantdef.GlobalProfileDescendantDef;
import oracle.apps.hcm.hwr.domain.descendantdef.ProfileDescendantDef;
import oracle.apps.hcm.hwr.domain.result.SearchResultProfile;
import oracle.apps.hcm.hwr.domain.search.ResourceSearchCriteria;
import oracle.apps.hcm.hwr.domain.webservices.LongSerializable;
import oracle.apps.hcm.hwr.domain.webservices.StringPagingRequest;
import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.WrsaEndorseUser;
import oracle.apps.hcm.hwr.domain.webservices.WrsaEndorseUserRequest;
import oracle.apps.hcm.hwr.domain.webservices.WrsaMyEndorsements;
import oracle.apps.hcm.hwr.domain.webservices.WrsaRecommenderCollection;
import oracle.apps.hcm.hwr.domain.webservices.serializer.LongSerializableSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.StringPagingRequestSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaEndorseUserRequestSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaEndorseUserSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaMyEndorsementsSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaRecommenderCollectionSerializer;
import oracle.apps.odin.datafilter.relationalquery.PagingInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HWREndorsementService implements IHWREndorsementService {

    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(HWREndorsementService.class);
    
    @Autowired
    private EndorsementRepository mEndorsementRepository;
    
    @Autowired 
    private RecognitionRepository mRecognitionRepository;
    
    @Autowired
    private HWRSearchCriteriaService mSearchService;
    
    @Autowired
    private GlobalProfileService mGlobalProfileService;
    
    @Autowired
    private TopicService mTopicService;
    
    @Override
    public String getMyEndorsements(String pGlobalProfileId) {
        String myResult;
        
        try {
            LongSerializable myDeserializedData = new LongSerializableSerializer()
                .deserialize(pGlobalProfileId);
            long myGlobalprofileId = myDeserializedData.getLongVal();
            
            WrsaMyEndorsements myEndorsement = new WrsaMyEndorsements(
                getTopicsForUser(myGlobalprofileId),
                getEndorsements(myGlobalprofileId),
                getRecognitions(myGlobalprofileId));
            
            myResult = HWRServerCommon.generateResult(
                new WrsaMyEndorsementsSerializer().serialize(myEndorsement));
            
        } catch (Exception e) {
            String myErrorMessage = "Error while processing getMyEndorsements.  Exception message: " + e.getMessage();
            
            LOGGER.error(myErrorMessage);
            
            myResult = HWRServerCommon.generateErrorWebservicesResult(WebservicesResult.RESULT_UNKNOWN_ERROR,
                myErrorMessage);
        }
        
        return myResult;
    }

    /**
     * Main method call to do the searching.
     * @param pSearchCriteria
     * @return
     */
    @Override
    public String searchUserToEndorse(String pSearchCriteria) {

        LOGGER.info("Web service searchUserToEndorse has been called");
        
        PagingInfo myPagingInfo = null;
        
        Collection<GlobalProfile> myGlobalProfilesList = new ArrayList<GlobalProfile>();
        try {
            StringPagingRequest myRequest = parseSearchUserProfileParameters(pSearchCriteria);
            
             myPagingInfo = myRequest.getPagingInfo();
            
            LOGGER.info("searchUserToEndorse: Searching for global profile matching: " + myRequest.getStringCriteria());
            WRMSearchResult mySearchResult = getSearchResult(myRequest.getStringCriteria(),
                myPagingInfo);
            
            LOGGER.info("searchUserToEndorse: Found " + mySearchResult.getGlobalProfiles().size() + 
                " matching global profiles");
            
            for (SearchResultProfile myProfile : mySearchResult.getGlobalProfiles()) {
                myGlobalProfilesList.add(myProfile.getGlobalProfile());
            }        
                        
        } catch (Exception e) {
            String myErrorMessage = "Error while processing searchUserToEndorse.  Exception message: " + e.getMessage();
            
            LOGGER.error(myErrorMessage);
            
            return HWRServerCommon.handleException(WebservicesResult.RESULT_GLOBAL_PROFILE_NOT_AVAILABLE_ERROR, myErrorMessage);
        }

        //prepare the resulting profiles for return.  Don't need to populate RegisteredUserProfile.
        WrsaRecommenderCollection myResults = new WrsaRecommenderCollection(myGlobalProfilesList, myPagingInfo);

        return serializeWrsaRecommenderCollection(myResults);
    }
    
    
    @Override
    public String getUserToEndorse(String pGlobalProfileIds) {
        
        String myResult;
        
        try {
            WrsaEndorseUserRequest myDeserializedData = new WrsaEndorseUserRequestSerializer().deserialize(pGlobalProfileIds);
            long myGlobalProfileId = myDeserializedData.getIdOfUserToEndorse();
            
            WrsaEndorseUser myEndorseUser = new WrsaEndorseUser(
                myDeserializedData.getIdOfUserEndorsing(),
                myGlobalProfileId,
                getTopicsForUser(myGlobalProfileId),
                getEndorsements(myGlobalProfileId),
                getRecognitions(myGlobalProfileId));
            
            myResult = HWRServerCommon.generateResult(
                new WrsaEndorseUserSerializer().serialize(myEndorseUser));
            
        } catch (Exception e) {
            String myErrorMessage = "Error while processing getUserToEndorse.  Exception message: " + e.getMessage();
            
            LOGGER.error(myErrorMessage);
            
            myResult = HWRServerCommon.generateErrorWebservicesResult(WebservicesResult.RESULT_UNKNOWN_ERROR,
                myErrorMessage);
        }
        
        return myResult;
    }
    

    /**
     * Endorse user.
     * 
     * Input - serialzied string containing WrsaEndorseUser
     * Return - Serialized String 
     */
    @Override
    public String endorseUser(String pEndorsement) {
        
        String myResult = "";
        
        try {
            WrsaEndorseUser myDeserializedData = new WrsaEndorseUserSerializer().deserialize(pEndorsement);
             
            if (myDeserializedData.getEndorsements() != null && myDeserializedData.getEndorsements().size() > 0 
                    || myDeserializedData.isRecognitionUpdated()) {
                saveEndorsementRecognition(myDeserializedData);
            }
 
            WebservicesResult myResultObj = new WebservicesResult();
            myResultObj.setResultCode(WebservicesResult.RESULT_SUCCESS);

            myResult = HWRServerCommon.generateResult(
                    new WebservicesResultSerializer().serialize(myResultObj));
            
        } catch (Exception e) {
            String myErrorMessage = "Error while processing endorseUser.  Exception message: " + e.getMessage();
            
            LOGGER.error(myErrorMessage);
            
            myResult = HWRServerCommon.generateErrorWebservicesResult(WebservicesResult.RESULT_UNKNOWN_ERROR,
                myErrorMessage);
        }
        
        return myResult;
    }
    
    /******************* TODO - PUT IN LATER - FILTER DOES NOT SEEM TO WORK RIGHT NOW!!!  */    
    
//    private Collection<Recognition> getRecognitions(long pGlobalProfileId) {
//        CompositeFilter myCompositeFilter = getFilter(RecognitionRepository.FilterKey.RECOGNITION_ID.toString(),
//            mRecognitionRepository.findAssociationByGlobalProfileId(pGlobalProfileId));
//        
//        return mRecognitionRepository.loadAll(myCompositeFilter, new PagingInfo(), new OrderInfo());
//    }
//
//    private Collection<Endorsement> getEndorsements(long pGlobalProfileId) {
//
//        CompositeFilter myCompositeFilter = getFilter(EndorsementRepository.FilterKey.ENDORSEMENT_ID.toString(),
//            mEndorsementRepository.findAssociationByGlobalProfileId(pGlobalProfileId));
//        
//        return mEndorsementRepository.loadAll(myCompositeFilter, new PagingInfo(), new OrderInfo());
//    }
    
//    private CompositeFilter getFilter(String pKey, List<String> pListVals) {
//        CompositeFilter myFilter = new CompositeFilter();
//        CollectionFilter myFilterObjs = new CollectionFilter();
//        myFilterObjs.setKey(pKey);
//        myFilterObjs.setValues(pListVals);
//        myFilterObjs.setDataType(CollectionFilter.DATA_TYPE_STRING);
//        myFilterObjs.setOperation(CollectionFilter.CRIT_TYPE_IN);
//        myFilter.add(myFilterObjs);
//
//        return myFilter;
//    }

    
    /** Get recognitions for a profile */
    private Collection<Recognition> getRecognitions(long pGlobalProfileId) {
        List<String> myIDs =
            mRecognitionRepository.findAssociationByGlobalProfileId(pGlobalProfileId);

        Collection<Recognition> myRecognitions = new ArrayList<Recognition>();

        if (myIDs != null) {

            for (String myId : myIDs) {
                myRecognitions.add(mRecognitionRepository.findById(myId));
            }
        }

        return myRecognitions;
    }

    /** Get endorsements for a profile */
    private Collection<Endorsement> getEndorsements(long pGlobalProfileId) {
        List<String> myIDs =
            mEndorsementRepository.findAssociationByGlobalProfileId(pGlobalProfileId);

        Collection<Endorsement> myEndorsements = new ArrayList<Endorsement>();

        if (myIDs != null) {
            for (String myId : myIDs) {
                myEndorsements.add(mEndorsementRepository.findById(myId));
            }
        }

        return myEndorsements;
    }


    /** Save endorsement and recognition data */
    private void saveEndorsementRecognition(WrsaEndorseUser myDeserializedData) {
        WrsaRecommender myRecommender = null;
        
        List<Long> myNewEndorsements = myDeserializedData.getNewEndorsements();

        myRecommender = buildRecommender(Long.toString(myDeserializedData.getIdOfUser()));
        
        if (myDeserializedData.isRecognitionUpdated()) {
            saveRecognition(myRecommender, myDeserializedData.getRecognition(), myDeserializedData.getIdOfUserToEndorse());
        }
        
        if (myNewEndorsements != null && myNewEndorsements.size() > 0) {
            saveEndorsements(myRecommender, myNewEndorsements, myDeserializedData.getIdOfUserToEndorse());
        }
    }
    
    /** Save an endorsement */
    private void saveEndorsements(WrsaRecommender pRecommender, List<Long> pEndorsements, long pPersonToEndorse) {       
        Collection<Endorsement> myEndorsements = new ArrayList<Endorsement>();
        Collection<String> myEndorsementIds = new ArrayList<String>();
        
        for (Long myEndorsement : pEndorsements) {
            String myUniqueId = pPersonToEndorse + "_" + pRecommender.getGlobalProfileId() + "_" + myEndorsement;
            myEndorsementIds.add(myUniqueId);
            myEndorsements.add(buildEndorsement(myUniqueId, "", new Date(), myEndorsement, pRecommender));
        }
        
        mEndorsementRepository.save(myEndorsements);
        //now add relation
        for (String myID : myEndorsementIds) {
            mEndorsementRepository.saveAssociation(myID, pPersonToEndorse);
        }
    }
    
    /** Save a recognition */
    private void saveRecognition(WrsaRecommender pRecommender, String pRecognitionStr, long pPersonToRecommend) {
        String myUniqueId = pPersonToRecommend + "_" + pRecommender.getGlobalProfileId();
        
        mRecognitionRepository.save(buildRecognition(myUniqueId, pRecognitionStr, new Date(), pRecommender));
        mRecognitionRepository.saveAssociation(myUniqueId, pPersonToRecommend);
    }
    
    /**
     * Only way to get topic is through getSearchResult at this time.  Not really efficient probably
     * since we have to search through everything... but it's what we have...
     * 
     * @param pGlobalProfileId
     * @return
     */
    private List<Topic> getTopicsForUser(long pGlobalProfileId) throws Exception {
        List<Topic> myRet = new ArrayList<Topic>();
        
        LOGGER.info("TOMDEBUG: Searching for Profile: " + pGlobalProfileId);
        
        WRMSearchResult mySearchResult = getSearchResult(Long.toString(pGlobalProfileId).trim(), new PagingInfo());
       
        LOGGER.info("TOMDEBUG: Found result " );
        
        for (SearchResultProfile myProfile : mySearchResult.getGlobalProfiles()) {
            
            LOGGER.info("TOMDEBUG: Comparing result:  " + myProfile.getGlobalProfile().getID() + " with: " +  pGlobalProfileId);
            
            if(myProfile.getGlobalProfile().getID() == pGlobalProfileId) {
                
                LOGGER.info("TOMDEBUG: Found RESULT!!");
                //TODO swap out with real topics when we get a fix from engine team.
                myRet = swapToRealTopics(myProfile.getTopics());
                
                if (myRet != null) {
                    LOGGER.info("TOMDEBUG: Found # of topics: " + myRet.size());
                } else {
                    LOGGER.info("TOMDEBUG: Topics is null :( ");
                }
            }
        }        
        
        return myRet;
    }
    
    /** 
     *  Temp hack.  Not getting topics correctly from engine team, so do this
     *  until we figure out what a real solution is.
     *  
     *  Also hard code 2nd arg to "1" since that isn't coming over correctly either.
     *  Always showing 0, when the first upload shows "1" in the DB.
     */
    private List<Topic> swapToRealTopics(List<Topic> pBadTopics) {
        List<Topic> myTopics = new ArrayList<Topic>();
        
        for (Topic myTopic : pBadTopics) {
            LOGGER.info("TOMDEBUG: swapping Topic: " + myTopic.getDisplayText() + " " + myTopic.getTopicKey());

            Collection<Topic> myTempTopics = mTopicService.loadAllRevisions(myTopic.getTopicKey(), 1);
            if (myTempTopics != null && myTempTopics.size() > 0) {
                
                //debug only
                int i = 0;
                for (Topic myTempResult : myTempTopics) {
                    if (myTempResult.getId() != null && myTempResult.getDisplayText() != null) {
                    
                        LOGGER.info("TOMDEBUG: Returned from topic API Topic# " + i++ + " " + myTempResult.getId() + " "+ myTempResult.getDisplayText());
                    } else {
                        LOGGER.info("TOMDEBUG: Returned from topic API>  Topic# " + i++ +" has null values. ");
                    }
                }
                //end debug only
                
                //grab first one returned... temp hack.
                myTopics.add(myTempTopics.iterator().next());
            } else {
                LOGGER.warn("Swapped Topic API is null or size 0!");
            }
        }
        return myTopics;
    }
    
    /**
     * For the searchUserToEndorse webservices call.
     * Verify the passed in parameters, and return the deserialized object.
     * @param pSerializedParameters - Passed in serialized webservices string.
     * @return - The deserialized object on success.
     * @throws Exception - Containing the reason for the parsing error on failure.
     */
    private StringPagingRequest parseSearchUserProfileParameters(String pSerializedParameters)
            throws Exception {
        
        if(pSerializedParameters == null) {
             throw new Exception("Paremeter passed to webservice is null.");            
        } 
        
        StringPagingRequest myStringPagingRequest = null;
        
        try {
            myStringPagingRequest =
                new StringPagingRequestSerializer().deserialize(pSerializedParameters);
        } catch (Exception e) {
            throw new Exception("Error deserializing StringPagingRequest from passed in string: " 
                + pSerializedParameters);  
        }
        
        if (myStringPagingRequest == null) {
            throw new Exception("Passed in paremeter is null after deserialization.");
        }
        
        String myCritString = myStringPagingRequest.getStringCriteria();
        
        if (myCritString == null || myCritString.length() == 0) {
            throw new Exception("Passed in criteria to search is null.");
        }
        
        //allow for null pagingInfo, just set to default to avoid backend errors.
        if (myStringPagingRequest.getPagingInfo() == null) {
            LOGGER.debug("searchUserToEndorse PagingInfo is null, setting to default.");
            myStringPagingRequest.setPagingInfo(new PagingInfo());
        }
        
        return myStringPagingRequest;
    }
    
    /**
     * Method that does the searching.
     * @param pSearchString
     * @param pPagingInfo
     * @return
     * @throws Exception
     */
    private WRMSearchResult getSearchResult(String pSearchString, PagingInfo pPagingInfo)
            throws Exception {
        
        //set up org data.  Use default to search through all users.
        Map<String, List<String>> myFacetMap1 = new HashMap<String, List<String>>();
        myFacetMap1.put(GblProfConverter.ORG_LEVEL, new ArrayList<String>());       
        
        //build search
        ResourceSearchCriteria.Builder myCriteriaBuilder = new ResourceSearchCriteria.Builder();
        myCriteriaBuilder.setSearchString(pSearchString);
        myCriteriaBuilder.setPagingInfo(pPagingInfo);
        myCriteriaBuilder.setFaceSearchMap(myFacetMap1);
        ResourceSearchCriteria mySearchCriteria = myCriteriaBuilder.build();

        WRMSearchResult mySearchResult = null;
        
        try {
            mySearchResult = mSearchService.search(mySearchCriteria);
        } catch (Exception e) {
            LOGGER.error("getSearchResult: Error occured during search for String: " + e.getMessage());
            throw new Exception("getSearchResult: Error occured during search for String: " + pSearchString);
        }

        return mySearchResult;
    }
    
    
    private String serializeWrsaRecommenderCollection(WrsaRecommenderCollection pWrsaRecommenderCollection) {
        // serialize the global profile object
        String mySerializedData =
                new WrsaRecommenderCollectionSerializer().serialize(pWrsaRecommenderCollection);

        WebservicesResult myResult = new WebservicesResult();
        myResult.setResultContent(mySerializedData);
        myResult.setResultCode(WebservicesResult.RESULT_SUCCESS);

        return new WebservicesResultSerializer().serialize(myResult);
    }
    
    /** Build an endorsement */
    private Endorsement buildEndorsement(String pID, String pStr, Date pDate, long pTopicId,
            WrsaRecommender pWrsaRecommender) {
        return Endorsement.builder(pID).setEndorsement(pStr).setDate(pDate).setTopicId(pTopicId)
            .setRecommender(pWrsaRecommender).build();
    }
    
    /** Build a recognition */
    private Recognition buildRecognition(String pID, String pStr, Date pDate, WrsaRecommender pWrsaRecommender) {
        return Recognition.builder(pID).setRecognition(pStr).setDate(pDate).setRecommendedBy(pWrsaRecommender).build();
    }
    
    /** Build recommender without the relation */
    private WrsaRecommender buildRecommender(String pGlobalProfileId) {
        GlobalProfile myProfile = mGlobalProfileService.findById(new Long(pGlobalProfileId), createGlobalProfileDescendantDef());
        
        return WrsaRecommender.builder(pGlobalProfileId).setGlobalProfileId(pGlobalProfileId)
                .setName(myProfile.getFullName()).setTitle(myProfile.getTitle())
                .setEmailAddresses(myProfile.getEmailAddress()).setImageUrl(myProfile.getPicture()).build();
    }
    
    /** Build recommender with a relation */
    private WrsaRecommender buildRecommender(String pGlobalProfileId, RecommendedByRelationType pRelationType) {
        GlobalProfile myProfile = mGlobalProfileService.findById(new Long(pGlobalProfileId), createGlobalProfileDescendantDef());
        
        return WrsaRecommender.builder(pGlobalProfileId).setGlobalProfileId(pGlobalProfileId)
                .setName(myProfile.getFullName()).setTitle(myProfile.getTitle())
                .setEmailAddresses(myProfile.getEmailAddress()).setImageUrl(myProfile.getPicture())
                .setRecommenderRelation(pRelationType).build();
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
}
