/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.ws.client;

import com.oracle.xmlns.apps.hcm.hwr.coreservice.ServiceException;
import com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author tgsnyder
 *
 */
public class RegiserUser implements ISendServerMsg {
	
	/** The logger */
    private static final Log LOGGER = LogFactory.getLog(RegiserUser.class);
	
    /**
     * Call the auto-generated client code to send a RegiserUser message
     * to the server.
     */
    @Override
    public String sendServerMsg(WorkforceReputationService pWorkforceReputationPublicService,
        String pMsgToSend) throws ServiceException {
    	
        String myResponse = pWorkforceReputationPublicService.registerUser(pMsgToSend);
        
        LOGGER.info("my server response: " + myResponse);
        
        return myResponse;
    }

	@Override
	public String getMethodName() {
		return "registerUser";
	}

}
