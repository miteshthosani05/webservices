package oracle.apps.hcm.hwr.ws.client;

import com.oracle.xmlns.apps.hcm.hwr.coreservice.ServiceException;
import com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService;


public class InitializeWla implements ISendServerMsg {

    /**
     * Call the auto-generated client code to send an initializeWla message
     * to the server.
     */
    @Override
    public String sendServerMsg(WorkforceReputationService pWorkforceReputationPublicService,
            String pMsgToSend) throws ServiceException {
        return pWorkforceReputationPublicService.initializeWLA(pMsgToSend);
    }

	@Override
	public String getMethodName() {
		return "initializeWLA";
	}

}
