/*******************************************************************************
* Copyright 2011, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
* rights, and intellectual property rights in and to this software remain with Oracle Corporation.
* Oracle Corporation hereby reserves all rights in and to this software. This software may not be
* copied, modified, or used without a license from Oracle Corporation. This software is
* by international copyright laws and treaties, and may be protected by other laws. Violation of
* copyright laws may result in civil liability and criminal penalties.
******************************************************************************/
package oracle.apps.hcm.hwr.ws.client.service;


import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

/**
 * The Class MergePersonaSyncDataIntegrationTest.
 */

public class MergePersonaSyncDataIntegrationTest extends WsClientTest {

    /** Logger. */
    private static Log LOGGER = LogFactory.getLog(MergePersonaSyncDataIntegrationTest.class);
    
    /**
     * Test egrcm import.
     * @throws ServiceException 
     */
    @Test
    public void testMergePersonaSyncDataRequest() throws ServiceException {
        String myResponseStr = mRequest.mergePersonaSyncData(mConnectionInfo, "testMergePersonaSyncDataRequest");
        WebservicesResult myResponse = new WebservicesResultSerializer().deserialize(myResponseStr);
        LOGGER.info("Response from server: '" + myResponse.getResultContent() + "'");
        //assertNotNull(myResponse.getResultContent());
    }
}
