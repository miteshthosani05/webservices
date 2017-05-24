package oracle.apps.hcm.hwr.ws.client;

import com.oracle.xmlns.apps.hcm.hwr.coreservice.ServiceException;
import com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService;


public class MergePersonaSyncData implements ISendServerMsg {

    /**
     * Call the auto-generated client code to send a MergePersonaSyncData message
     * to the server.
     */
    @Override
    public String sendServerMsg(WorkforceReputationService pWorkforceReputationPublicService,
        String pMsgToSend) throws ServiceException {
        return pWorkforceReputationPublicService.mergePersonaSyncData(pMsgToSend);
    }

	@Override
	public String getMethodName() {
		return "mergePersonaSyncData";
	}

}
