package oracle.apps.hcm.hwr.ws.client.service;

import java.util.List;

import oracle.apps.hcm.hwr.domain.Recognition;
import oracle.apps.hcm.hwr.domain.WrsaRecommender;
import oracle.apps.hcm.hwr.domain.webservices.LongSerializable;
import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.WrsaEndorsementSkill;
import oracle.apps.hcm.hwr.domain.webservices.WrsaMyEndorsements;
import oracle.apps.hcm.hwr.domain.webservices.serializer.LongSerializableSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WrsaMyEndorsementsSerializer;

import org.junit.Test;

public class GetMyEndorsementsTest extends WsClientTest {

	@Test
    public void testGetMyEndorsements()  {

        //TODO Replace with Global Profile ID of user we want to see endorsements for.
        Long myReq = 2775L;
        
        LongSerializable myVal = new LongSerializable(myReq);
        
        String mySerReq = new LongSerializableSerializer().serialize(myVal);
        
        String myResponseStr = mRequest.getMyEndorsements(mConnectionInfo, mySerReq);
        WebservicesResult myResponse = new WebservicesResultSerializer().deserialize(myResponseStr);
        
        String myResponseSer = myResponse.getResultContent();
        
        WrsaMyEndorsements myEndorsement = new WrsaMyEndorsementsSerializer().deserialize(myResponseSer);
        
        for (WrsaEndorsementSkill mySkill : myEndorsement.getEndorsementSkills()) {
            System.out.println("Skill : " + mySkill.getTopicId() + " count: " + mySkill.getEndorseCount());
        }
        
        for (Recognition myRecogniton : myEndorsement.getRecognitions()) {
            System.out.println(myRecogniton.getRecommendedBy().getName() + " " + myRecogniton.getRecognition());
        }
        
        for (WrsaEndorsementSkill mySkill : myEndorsement.getEndorsementSkills()) {
            List<WrsaRecommender> myRecommenders = myEndorsement.getRecommenderColleagues(mySkill.getTopicId());
            for (WrsaRecommender myRecommender : myRecommenders) {
                System.out.println("Skill: " + mySkill.getSkillName() + " myRecommender: " + myRecommender.getName() + " " + myRecommender.getGlobalProfileId()
                    + " " + myRecommender.getTitle() + " " + myRecommender.getEmailAddresses() + " " + myRecommender.getImageUrl());
            }
        }
        
        
        System.out.println("Done!");
        
    }
}
