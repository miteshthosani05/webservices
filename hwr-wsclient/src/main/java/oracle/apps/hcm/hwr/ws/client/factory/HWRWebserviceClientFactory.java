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
import oracle.apps.hcm.hwr.ws.client.WorkforceReputationPublicServiceImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * A factory for creating HWRWebserviceClient objects.
 */
public class HWRWebserviceClientFactory implements IHWRWebserviceClientFactory {
    
    /** The logger. */
    private static final Log logger = LogFactory.getLog(HWRWebserviceClientFactory.class);
    
    /** The Constant INSTANCE. */
    private static final IHWRWebserviceClientFactory INSTANCE = new HWRWebserviceClientFactory();
    
    /**
     * Instantiates a new hWR webservice client factory.
     */
    private HWRWebserviceClientFactory() {
    }
    
    /**
     * Gets the single instance of HWRWebserviceClientFactory.
     *
     * @return single instance of HWRWebserviceClientFactory
     */
    public static IHWRWebserviceClientFactory getInstance() {
        return INSTANCE;
    }
    
    @Override
    public WorkforceReputationPublicService getWorkforceReputationPublicService() {
        WorkforceReputationPublicServiceImpl myService = new WorkforceReputationPublicServiceImpl();
        return myService;
    }

    @Override
    public WorkforceReputationPublicService getWorkforceReputationPublicService(String pURL) {
        return null;
    }

}
