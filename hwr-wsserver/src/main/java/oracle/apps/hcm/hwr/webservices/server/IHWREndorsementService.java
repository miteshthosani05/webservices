package oracle.apps.hcm.hwr.webservices.server;


public interface IHWREndorsementService {

    /**
     * @param pGlobalProfileId
     * @return
     */
    String getMyEndorsements(String pGlobalProfileId);
    
    /**
     * @param pSearchCriteria
     * @return
     */
    String searchUserToEndorse(String pSearchCriteria);
    
    /**
     * @param pGlobalProfileIds
     * @return
     */
    String getUserToEndorse(String pGlobalProfileIds);
    
    /**
     * @param pEndorsement
     * @return
     */
    String endorseUser(String pEndorsement);
}
