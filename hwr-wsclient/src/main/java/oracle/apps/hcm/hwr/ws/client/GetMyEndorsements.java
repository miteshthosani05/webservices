package oracle.apps.hcm.hwr.ws.client;

import com.oracle.xmlns.apps.hcm.hwr.coreservice.ServiceException;
import com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService;


public class GetMyEndorsements implements ISendServerMsg {

	 @Override
	    public String sendServerMsg(WorkforceReputationService pWorkforceReputationPublicService,
	            String pMsgToSend) throws ServiceException {
	        return pWorkforceReputationPublicService.getMyEndorsements(pMsgToSend);
	    }

		@Override
		public String getMethodName() {
			return "getMyEndorsements";
		}

}
