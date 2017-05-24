/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.ws.client.factory;

import oracle.apps.hcm.hwr.ws.client.WorkforceReputationPublicService;

/**
 * A factory for creating IHWRWebserviceClient objects.
 */
public interface IHWRWebserviceClientFactory {

    /**
     * Gets the workforce reputation public service.
     *
     * @param pWSDL the wSDL
     * @return the workforce reputation public service
     */
    WorkforceReputationPublicService getWorkforceReputationPublicService();
    
    @Deprecated
    WorkforceReputationPublicService getWorkforceReputationPublicService(String pURL);
        
}