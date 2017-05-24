/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.ws.client;

import oracle.apps.hcm.hwr.ws.client.common.ConnectionInfo;

/**
 * The Interface WorkforceReputationPublicService.
 */
public interface WorkforceReputationPublicService {

    /**
     * Register user.
     *
     * @param pConnectionInfo the connection info
     * @param pPersonaSyncdata the persona syncdata
     * @return the string representing a serialized result object.
     */
    String registerUser(final ConnectionInfo pConnectionInfo, String pPersonaSyncdata);
    
    /**
     * Merge persona sync data.
     *
     * @param pConnectionInfo the connection info
     * @param pPersonaSyncdata the persona syncdata
     * @return the string representing a serialized result object.
     */
    String mergePersonaSyncData(final ConnectionInfo pConnectionInfo, String pPersonaSyncdata);
    
    /**
     * Write connector data.
     *
     * @param pConnectionInfo the connection info
     * @param pDataWriter the data writer
     * @return the string representing a serialized result object.
     */
    String writeConnectorData(final ConnectionInfo pConnectionInfo, String pDataWriter);
    
    /**
     * Poll for job.
     *
     * @param pConnectionInfo the connection info
     * @param pWLA the wla
     * @return the string representing a serialized result object.
     */
    String pollForJob(final ConnectionInfo pConnectionInfo, final String pWLA);
    
    /**
     * Update job progress.
     * @param pConnectionInfo the connection info
     * @param pRequestParams the request params
     * @return the string representing a serialized result object.
     */
    String updateJobProgress(final ConnectionInfo pConnectionInfo, String pJobId);
    
    /**
     * Initialize WLA.
     * @param pConnectionInfo the connection info
     * @param The Wla
     * @return the string representing a serialized result object.
     */
    String initializeWla(final ConnectionInfo pConnectionInfo, String pWla);
    
    /**
     * Get user profile.
     * @param pConnectionInfo the connection info
     * @param pUserProfile the user profile
     * @return the string representing a serialized result object.
     */
    String getUserProfile(final ConnectionInfo pConnectionInfo, String pUserProfile);
    
    String endorseUser(final ConnectionInfo pConnectionInfo, final String pUserToEndorse);
    
    String getMyEndorsements(final ConnectionInfo pConnectionInfo, final String pGlobalProfileId);
    
    String getUserToEndorse(final ConnectionInfo pConnectionInfo, final String pGlobalProfileIds);
    
    String searchUserToEndorse(final ConnectionInfo pConnectionInfo, final String pSearchCriteria);
    
    
}
