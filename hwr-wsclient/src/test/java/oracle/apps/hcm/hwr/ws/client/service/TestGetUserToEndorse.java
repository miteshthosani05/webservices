package oracle.apps.hcm.hwr.ws.client.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import oracle.apps.hcm.hwr.domain.WrsaRecommender;
import oracle.apps.hcm.hwr.domain.webservices.StringPagingRequest;
import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.WrsaEndorseUser;
import oracle.apps.hcm.hwr.domain.webservices.WrsaEndorseUserRequest;
import oracle.apps.hcm.hwr.domain.webservices.WrsaRecommenderCollection;
import oracle.apps.hcm.hwr.domain.webservices.WrsaUserEndorsement;
import oracle.apps.hcm.hwr.domain.webservices.serializer.StringPagingRequestSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaEndorseUserRequestSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaEndorseUserSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaRecommenderCollectionSerializer;
import oracle.apps.hcm.hwr.ws.client.service.WsClientTest;
import oracle.apps.odin.datafilter.relationalquery.PagingInfo;

import org.junit.Test;


public class TestGetUserToEndorse extends WsClientTest {

    //@TODO replace the two users with ones we want to use, and update # of endorsements to give.
    private static final String mLoggedInUserSearchStr = "Ana Bradley";
    private static final String mUserToEndorseSearchStr = "Emory Perez";
    private static final int mNumSkillsToEndorse = 1;
    //@TODO give endorsement text to add to user.
    private static final String mRecognition = "Recognition1";
    
    private final List<Long> mSkillsShownAsEndorsed = new ArrayList<Long>();
    
    @Test
    public void test() {
        
        //search for users
        WrsaRecommenderCollection mUserCollection = searchUserToEndorse(mLoggedInUserSearchStr);
        WrsaRecommenderCollection mUserToEndorseCollection = searchUserToEndorse(mUserToEndorseSearchStr);
        
        //get users we searched for.  Verify they exist.
        WrsaRecommender mUser = getAndValidatesearchUserToEndorse(mUserCollection, mLoggedInUserSearchStr);
        WrsaRecommender mUserToEndorse = getAndValidatesearchUserToEndorse(
            mUserToEndorseCollection, mUserToEndorseSearchStr);
        
        //get endorsements between these 2 users.
        WrsaEndorseUser myEndorseUser = getEndorseUser(new Long(mUser.getGlobalProfileId()), 
            new Long(mUserToEndorse.getGlobalProfileId()));        
        
        //endorse user for 2 skills.
        endorseForSkills(myEndorseUser, mNumSkillsToEndorse);
                
        //verify skills show as "endorsed"
        verifyEndorsedSkills(myEndorseUser);
        
        //set recognition for user
        myEndorseUser.setRecognition(mRecognition);
        
        //verify recognition
        verifyRecognition(myEndorseUser, mRecognition);
        
        //save endorsement
        saveEndorsements(myEndorseUser);        
      
       //verify data we saved on the server is correct
        verifyEndorsements(myEndorseUser, mRecognition, new Long(mUser.getGlobalProfileId()),
            new Long(mUserToEndorse.getGlobalProfileId()));
    }
    
    public WrsaEndorseUser getEndorseUser(long pIdOfUser, long pIdOfUserToEndorse) {
        WrsaEndorseUserRequest myReq = new WrsaEndorseUserRequest(pIdOfUser, pIdOfUserToEndorse);
        
        String mySerReq = new WrsaEndorseUserRequestSerializer().serialize(myReq);
        
        String myResponseStr = mRequest.getUserToEndorse(mConnectionInfo, mySerReq);
        WebservicesResult myResponse = new WebservicesResultSerializer().deserialize(myResponseStr);
        
        String myResponseSer = myResponse.getResultContent();
        
        WrsaEndorseUser myWrsaResp = new WrsaEndorseUserSerializer().deserialize(myResponseSer);
        
        for (WrsaUserEndorsement myEndorsement : myWrsaResp.getEndorsements()) {
            System.out.println(myEndorsement.getEndorsement() + " " + myEndorsement.getIsEndorsed() + " " + myEndorsement.getId());
        }
        

        return myWrsaResp;
    }
    
    public void addEndorseSkill(WrsaEndorseUser pEndorsementObj, long pSkillId) {
        pEndorsementObj.endorseSkill(pSkillId);
    }
    
    public void saveEndorsements(WrsaEndorseUser pEndorseUser) {
        String mySerReq = new WrsaEndorseUserSerializer().serialize(pEndorseUser);
        
        String myResponseStr = mRequest.endorseUser(mConnectionInfo, mySerReq);
        WebservicesResult myResponse = new WebservicesResultSerializer().deserialize(myResponseStr);
        
        System.out.println("Response for saveEndorsements: " + myResponse.getResultCode());
    }
    
    private WrsaRecommenderCollection searchUserToEndorse(String pSearchString) {
        StringPagingRequest myReq = new StringPagingRequest(pSearchString, new PagingInfo());
        
        String mySerReq = new StringPagingRequestSerializer().serialize(myReq);
        
        String myResponseStr = mRequest.searchUserToEndorse(mConnectionInfo, mySerReq);
        WebservicesResult myResponse = new WebservicesResultSerializer().deserialize(myResponseStr);
        
        String myResponseSer = myResponse.getResultContent();
        
        WrsaRecommenderCollection myWrsaResp = new WrsaRecommenderCollectionSerializer().deserialize(myResponseSer);
        
        for (WrsaRecommender myRecommender : myWrsaResp.getWrsaRecommenders()) {
            System.out.println(" GlobalID: " + myRecommender.getGlobalProfileId() + " emails: " + myRecommender.getEmailAddresses() 
                + " Title: " + myRecommender.getTitle() + " Name: " + myRecommender.getName());
        }
        
        return myWrsaResp;
    }
    
    //validate that the user we input exists in the output, and then return that user.
    private WrsaRecommender getAndValidatesearchUserToEndorse(WrsaRecommenderCollection pCollection, String pSearchString) {
        WrsaRecommender myRet = null;
        
        for (WrsaRecommender myRecommender : pCollection.getWrsaRecommenders()) {
            if (myRecommender.getName().equals(pSearchString)) {
                myRet = myRecommender;
                break;
            }
        }
        
        assertNotNull(myRet);
        
        return myRet;
    }
    
    //randomly endorse pNumSkillsToEndorseFor skills
    private void endorseForSkills(WrsaEndorseUser pEndorseUser, int pNumSkillsToEndorseFor) {
        
        List<Long> pIds = new ArrayList<Long>();
        for (WrsaUserEndorsement myEndorsement : pEndorseUser.getEndorsements()) {
            if (myEndorsement.mIsEndorsed == false) {
                pIds.add(myEndorsement.getId());
            }
        }
        
        for (int i = 0; i < pNumSkillsToEndorseFor && pIds.size() > 0; i++) {
            int randomIdx = new Random().nextInt(pIds.size());
            System.out.println("Random NUmber: " + randomIdx);
            long myId = pIds.remove(randomIdx);
            addEndorseSkill(pEndorseUser, myId);
            mSkillsShownAsEndorsed.add(myId);
        }
    }
    
    /** Not efficient, but small number and test so not a big deal */
    private void verifyEndorsedSkills(WrsaEndorseUser pEndorseUser) {
        
        for (Long mySkillId : mSkillsShownAsEndorsed) {
            for (WrsaUserEndorsement myEndorsement : pEndorseUser.getEndorsements()) {
                if (myEndorsement.getId() == mySkillId) {
                    assertTrue(myEndorsement.mIsEndorsed);
                }
            }
        }
    }
    
    private void verifyRecognition(WrsaEndorseUser pEndorseUser, String pRecognitionStr) {
        String myStr = pEndorseUser.getRecognition();
        assertTrue(myStr.equals(pRecognitionStr));
    }
    
    private void verifyEndorsements(WrsaEndorseUser pEndorseUserOrig, String pRecognitionOrig,
            long pIdOfUser, long pIdOfUserToEndorse) {
        
        WrsaEndorseUser myEndorseUser2 = getEndorseUser(pIdOfUser, pIdOfUserToEndorse);
        
        System.out.println("Printing original endorsements: ");
        for (WrsaUserEndorsement myEndorsement : pEndorseUserOrig.getEndorsements()) {
            System.out.println(myEndorsement.getEndorsement() + " " + myEndorsement.getIsEndorsed() + " " + myEndorsement.getId());
        }
        
        System.out.println("Original recognition we set: " + pRecognitionOrig);
        System.out.println("Recognition from server: " + myEndorseUser2.getRecognition());
        
        //verify the fields we care about are identical
        assertTrue(pEndorseUserOrig.getRecognition().equals(myEndorseUser2.getRecognition()));
        assertTrue(pRecognitionOrig.equals(myEndorseUser2.getRecognition()));
        assertTrue(pEndorseUserOrig.getEndorsements().containsAll(myEndorseUser2.getEndorsements()));
        assertTrue(pEndorseUserOrig.getIdOfUser() == myEndorseUser2.getIdOfUser());
        assertTrue(pEndorseUserOrig.getIdOfUserToEndorse() == myEndorseUser2.getIdOfUserToEndorse());
        assertTrue(pEndorseUserOrig.getIdOfUserToEndorse() != myEndorseUser2.getIdOfUser());
        
    }

}
