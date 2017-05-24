/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.webservices.server;

/**
 * @author Yenal Kal
 */
public interface IHWRPublicService {

    /**
     * Register user.
     * @param pProfileSyncInfo The serialized ProfileSyncInfo instance
     * @return null string
     */
    String registerUser(final String pProfile);

    /**
     * Returns a job matching the request if one is available.
     * @param pJobRequest The serialized JobRequest instance.
     * @return
     */
    String pollForJob(final String pJobRequest);

    /**
     * Merges the profile sync info data sent using the service.
     * @param pDataSet the persona sync info
     */
    String mergeProfileSyncInfoData(final String pDataSet);

    /**
     * Writes the data passed in to the graph.
     * @param pInput The input that contains the data.
     * @return The result of the operation.
     */
    public String writeConnectorData(String pInput);

    /**
     * Updates the job progress.
     * @param pInput The input that contains the job related data such as job ID and progress.
     * @return The result of the operation.
     */
    public String updateJobProgress(String pInput);

    /**
     * Provide the WLA with any data required for initialization (e.g., static Rewards data)
     * @param pInput
     * @return The result of the operation.
     */
    public String initializeWLA(String pInput);
    
    /** 
     * Get the user's HWR profile data
     * @param pInput Information identifying the user
     * @return The user profile data
     */
    public String getUserProfile(String pInput);
}