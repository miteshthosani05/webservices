/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.webservices.server;

import static oracle.apps.hcm.hwr.connectors.remote.wsobjects.WLAUserGlobalProfile.convertToGlobalProfile;
import static org.easymock.EasyMock.anyLong;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Date;

import oracle.apps.odin.utils.collection.Lists;
import oracle.apps.odin.utils.collection.Iterables;

import oracle.apps.grc.extensibility.connector.AbstractConnectorConfig;
import oracle.apps.grc.extensibility.connector.ConnectorSyncType;
import oracle.apps.grc.extensibility.connector.IConnector;
import oracle.apps.grc.extensibility.connector.IConnectorSpecification;
import oracle.apps.grc.extensibility.connector.exception.ConnectorException;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.ProfileSyncInfoContainer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.WLAUserProfileList;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.ProfileSyncInfoContainerSerializer;
import oracle.apps.hcm.hwr.connectors.remote.wsobjects.serializer.WLAUserProfileListSerializer;
import oracle.apps.hcm.hwr.dataaccess.GlobalProfileService;
import oracle.apps.hcm.hwr.dataaccess.MessageService;
import oracle.apps.hcm.hwr.dataaccess.ProfileSyncInfoService;
import oracle.apps.hcm.hwr.dataaccess.SourceService;
import oracle.apps.hcm.hwr.domain.GlobalProfile;
import oracle.apps.hcm.hwr.domain.Source;
import oracle.apps.hcm.hwr.domain.Source.Sources;
import oracle.apps.hcm.hwr.domain.connector.ProfileSyncInfo;
import oracle.apps.hcm.hwr.domain.descendantdef.GlobalProfileDescendantDef;
import oracle.apps.hcm.hwr.domain.rewards.Player;
import oracle.apps.hcm.hwr.domain.rewards.service.PlayerService;
import oracle.apps.hcm.hwr.domain.rewards.service.UserService;
import oracle.apps.hcm.hwr.domain.rewards.service.exception.RewardObjectServiceException;
import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;
import oracle.apps.hcm.hwr.rewards.badgeville.GamificationObjectReferenceCache;
import oracle.apps.hcm.hwr.rewards.badgeville.WlaInitializationObject;
import oracle.apps.hcm.hwr.rewards.serializer.WlaInitializationObjectSerializer;
import oracle.apps.hcm.hwr.rewards.serviceprovider.GamificationCredentials;
import oracle.apps.odin.datafilter.relationalquery.CompositeFilter;
import oracle.apps.odin.datafilter.relationalquery.OrderInfo;
import oracle.apps.odin.datafilter.relationalquery.PagingInfo;
import oracle.apps.odin.datafilter.relationalquery.SingleValueFilter;
import oracle.apps.odin.utils.base.Provider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Yenal Kal
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:hwrPublicServiceTest-config.xml", })
public class HWRPublicServiceTest {

    @Autowired
    private IHWRPublicService hwrPublicService;
    
    @Autowired
    private ProfileSyncInfoService profileSyncInfoService;

    @Autowired
    private SourceService sourceService;

    @Autowired
    private MessageService messageService;
    
    @Autowired
    @Qualifier("GamificationCredentials")
    private Provider<GamificationCredentials> gamificationCredentials;
    
    @Autowired
    @Qualifier("GamificationObjectReferenceCache")
    private Provider<GamificationObjectReferenceCache> objectReferenceCache;
    
    @Autowired
    private GlobalProfileService globalProfileService;
    
    @Autowired
    private PlayerService playerService;
    
    @Autowired 
    private UserService userService;
    
    private static final String FACEBOOK_APP_NAME = Sources.FACEBOOK.getApplicationName();
    private static final Source FACEBOOK = 
        Source.createSource(1L, Source.Sources.FACEBOOK.getApplicationName(), emptySpec());
    
    @Before
    public void setUp() throws Exception {
       reset(  profileSyncInfoService, 
               sourceService, 
               messageService, 
               gamificationCredentials, 
               objectReferenceCache, 
               globalProfileService,
               userService,
               playerService);
    }

    @Test
    public void testRegisterExistingUser() {
        ProfileSyncInfoContainer profileInfoContainer = containerWithProfileSyncInfoImLookingFor();
        String serializedProfileSyncInfoContainer =
            new ProfileSyncInfoContainerSerializer()
            .serialize(profileInfoContainer);
        
        expect(profileSyncInfoService.findBy((String)anyObject(), anyLong()))
            .andReturn(existingProfileSyncInfo());
        
        expect(sourceService.loadAllSources((CompositeFilter)anyObject(), (PagingInfo)isNull() , (OrderInfo)isNull()))
            .andReturn(Lists.newArrayList(FACEBOOK));
        
        replay(profileSyncInfoService, sourceService);

        String serializedResult = hwrPublicService.registerUser(serializedProfileSyncInfoContainer);
        WebservicesResult deserializedResult = new WebservicesResultSerializer()
                .deserialize(serializedResult);
        
        assertEquals("registerUser operation failed.", 0, deserializedResult.getResultCode());
    }
    
    @Test
    public void testWLAInitialization() {
        GamificationCredentials provisionedCredentials = new GamificationCredentials("v1", "v2", "v3", "v4");
        GamificationObjectReferenceCache provisionedCache = new GamificationObjectReferenceCache();     
        
        expect(gamificationCredentials.get())
            .andReturn(provisionedCredentials);
        
        expect(objectReferenceCache.get())
            .andReturn(provisionedCache);
        
        replay(gamificationCredentials, objectReferenceCache);
        
        String serializedInitObject = hwrPublicService.initializeWLA(null);
        WlaInitializationObject initObject =
                new WlaInitializationObjectSerializer().deserialize(serializedInitObject);
        
        GamificationCredentials deserializedCredentials = initObject.getGamificationCredentials();
        GamificationObjectReferenceCache deserializedCache = initObject.getGamificationObjectReferenceCache();
        
        assertEquals(provisionedCredentials.getNetworkID(), deserializedCredentials.getNetworkID());
        assertEquals(provisionedCredentials.getPrivateApi(), deserializedCredentials.getPrivateApi());
        assertEquals(provisionedCredentials.getSiteId(), deserializedCredentials.getSiteId());
        assertEquals(provisionedCredentials.getSiteURL(), deserializedCredentials.getSiteURL());
        
        assertNotNull(deserializedCache);
    }
    
    @Test
    public void testGetUserProfileCreatesNewPlayerIfNoPlayerIdExists() throws Exception {
        GlobalProfile anaBradleysProfile = anaBradleysProfile(null, null);
        Player myPlayer = buildAnaBradleyPlayerObject(anaBradleysProfile);
        
        expect(globalProfileService.loadAllGlobalProfiles((CompositeFilter)anyObject(), (PagingInfo)isNull(), (OrderInfo)isNull(),
                (GlobalProfileDescendantDef)isNull()))
            .andReturn(Lists.newArrayList(anaBradleysProfile));
        
        expect(userService.create((Player)anyObject()))
            .andReturn(playerWithUserID(myPlayer));
        
        expect(playerService.create((Player)anyObject()))
            .andReturn(playerwithPlayerID(myPlayer));
        
        replay(globalProfileService, userService, playerService);
        
        WLAUserProfileListSerializer profilesSerializer = 
                new WLAUserProfileListSerializer();
        
        String serializedFilter = compositeFilterWithValues().toXml();
        String serializedUserProfiles = hwrPublicService.getUserProfile(serializedFilter);
        WLAUserProfileList profilesList = profilesSerializer.deserialize(serializedUserProfiles);
        
        Iterable<GlobalProfile> globalProfiles = 
                Iterables.transform(profilesList.getUserProfiles(), convertToGlobalProfile);
        GlobalProfile deserializedAnaBradley = globalProfiles.iterator().next();
        
        assertEquals(anaBradleysProfile.getID(), deserializedAnaBradley.getID());
        assertEquals(myPlayer.getID(), deserializedAnaBradley.getGamificationPlayerId());
    }
    
    @Test
    public void testGetUserProfileUsesExistingPlayerIdAndUserId() throws Exception {
        GlobalProfile anaBradleysProfile = anaBradleysProfile("1001", "202");
        
        expect(globalProfileService.loadAllGlobalProfiles((CompositeFilter)anyObject(), (PagingInfo)isNull(), (OrderInfo)isNull(),
                (GlobalProfileDescendantDef)isNull()))
            .andReturn(Lists.newArrayList(anaBradleysProfile));
        
        expect(userService.create((Player)anyObject()))
            .andThrow(new RuntimeException("Should Not Create User"));
        
        expect(playerService.create((Player)anyObject()))
            .andThrow(new RuntimeException("Should Not Create Player "));
        
        replay(globalProfileService, userService, playerService);
        
        WLAUserProfileListSerializer profilesSerializer = 
                new WLAUserProfileListSerializer();
        
        String serializedFilter = compositeFilterWithValues().toXml();
        String serializedUserProfiles = hwrPublicService.getUserProfile(serializedFilter);
        WLAUserProfileList profilesList = profilesSerializer.deserialize(serializedUserProfiles);
        
        Iterable<GlobalProfile> globalProfiles = 
                Iterables.transform(profilesList.getUserProfiles(), convertToGlobalProfile);
        GlobalProfile deserializedAnaBradley = globalProfiles.iterator().next();
        
        assertEquals(anaBradleysProfile.getID(), deserializedAnaBradley.getID());
        assertEquals(anaBradleysProfile.getGamificationPlayerId(), deserializedAnaBradley.getGamificationPlayerId());
    }
    
    @Test
    public void testGetUserProfileWithPlayerIDAndNoUserID() throws RewardObjectServiceException {
        GlobalProfile anaBradleysProfile = anaBradleysProfile("1001", null);
 
        expect(globalProfileService.loadAllGlobalProfiles((CompositeFilter)anyObject(), (PagingInfo)isNull(), (OrderInfo)isNull(),
                (GlobalProfileDescendantDef)isNull()))
            .andReturn(Lists.newArrayList(anaBradleysProfile));
        
        expect(userService.create((Player)anyObject()))
        .andThrow(new RewardObjectServiceException("Should Not Create User"));
        
        replay(globalProfileService, userService);
        
    }
    
    private ProfileSyncInfoContainer containerWithProfileSyncInfoImLookingFor() {
        ProfileSyncInfo myTestUser = new ProfileSyncInfo("person ref", 0L);
        myTestUser.setAuthToken("access token");
        
        ProfileSyncInfoContainer myTestUserContainer = new ProfileSyncInfoContainer();
        myTestUserContainer.setConnectorApplicationName(FACEBOOK_APP_NAME);
        myTestUserContainer.setProfileSyncInfo(myTestUser);
        
        return myTestUserContainer;
    }
    
    private CompositeFilter compositeFilterWithValues() {
        SingleValueFilter filterOnSomeKey = new SingleValueFilter();
        filterOnSomeKey.setKey("KEY");
        filterOnSomeKey.setValue("1001");
        filterOnSomeKey.setOperation(SingleValueFilter.CRIT_TYPE_EQUAL);
        filterOnSomeKey.setDataType(SingleValueFilter.DATA_TYPE_LONG);
        
        CompositeFilter compositeFilterWithValues = new CompositeFilter();
        compositeFilterWithValues.add(filterOnSomeKey);
        return compositeFilterWithValues;
    }
    
    private GlobalProfile anaBradleysProfile(String playerId, String userId) {
        return GlobalProfile.builder(100L)
                .addGamificationPlayerId(playerId)
                .addGamificationUserId(userId)
                .setFirstName("Ana")
                .setLastName("bradley")
                .build();
    }
    
    private Player buildAnaBradleyPlayerObject(GlobalProfile pGlobalProfile){
        Player myPlayer = new Player();
        myPlayer.setFirstName(pGlobalProfile.getFirstName());
        myPlayer.setLastName(pGlobalProfile.getLastName());
        myPlayer.setEmail("Ana.Bradleys@abc.com");
        return myPlayer;
    }
    
    private Player playerWithUserID(Player pPlayer){
        pPlayer.setUserId("1234");
        return pPlayer;
    }
    
    private Player playerwithPlayerID(Player pPlayer){
        pPlayer.setUserId("1234");
        pPlayer.setID("4567");
        return pPlayer;
    }
    
    private ProfileSyncInfo existingProfileSyncInfo() {
        ProfileSyncInfo profileSyncInfo = new ProfileSyncInfo();
        profileSyncInfo.setAuthSecret("authsecret");
        profileSyncInfo.setAuthToken("authToken");
        profileSyncInfo.setDataSourceID(100L);
        profileSyncInfo.setMetaData("metadata");
        profileSyncInfo.setNodeSaveState(100L, "nodeName", 101L, "key", new Date());
        return profileSyncInfo;
    }
    
    private static final IConnectorSpecification emptySpec() {
        return new IConnectorSpecification() {
            public Object createConfigEditor(Object arg0) {return null;};
            public AbstractConnectorConfig createDefaultConfig() {return null;};
            public IConnector createIConnector() throws ConnectorException {return null;}
            public AbstractConnectorConfig getConfig() {return null;}
            public String getConnectorTypeName() {return "";}
            public long getId() {return 0l;}
            public String getSupportedAppTypeName() {return "";}
            public String getSupportedAppTypeVersion() {return "";}
            public ConnectorSyncType getSupportedSyncType() {return null;}
            public void setConfig(AbstractConnectorConfig arg0)
                    throws ConnectorException {}
            public void setId(long arg0) { }
        };
    }
}
