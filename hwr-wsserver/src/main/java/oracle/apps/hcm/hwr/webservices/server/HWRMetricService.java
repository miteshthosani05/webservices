package oracle.apps.hcm.hwr.webservices.server;

import java.util.Date;
import java.util.Map;

import oracle.apps.hcm.hwr.dataaccess.GlobalProfileService;
import oracle.apps.hcm.hwr.dataaccess.ProfileControlSourceMetricService;
import oracle.apps.hcm.hwr.dataaccess.ProfileService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HWRMetricService implements IHWRMetricService {

	/** The logger */
	private static final Log LOGGER = LogFactory.getLog(HWRMetricService.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see oracle.apps.hcm.hwr.webservices.server.IHWRMetricService#
	 * getPostsAnalyzedCount(java.util.Date)
	 */
	@Autowired
	private ProfileControlSourceMetricService mProfileControlSourceMetricService;

	@Autowired
	private ProfileService mProfileService;

	@Autowired
	private GlobalProfileService mGlobalProfileService;

	public Map<Date,Long> getPostsAnalyzedCount(long pTimeperiod) {
		// TODO Auto-generated method stub
		return mProfileControlSourceMetricService.getPostsAnalyzedCount(pTimeperiod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * oracle.apps.hcm.hwr.webservices.server.IHWRMetricService#getIssuesCount
	 * (java.util.Date)
	 */
	@Override
	public Map<Date,Long> getIssuesCount(long pTimeperiod) {
		// TODO Auto-generated method stub
		return mProfileControlSourceMetricService.getIssuesCount(pTimeperiod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oracle.apps.hcm.hwr.webservices.server.IHWRMetricService#
	 * getProfilesAddedCount(java.util.Date)
	 */
	@Override
	public Map<Date,Long> getProfilesAddedCount(long pTimeperiod) {
		// TODO Auto-generated method stub
		return mProfileService.getProfilesAddedCount(pTimeperiod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oracle.apps.hcm.hwr.webservices.server.IHWRMetricService#
	 * getEmployeesAddedCount(java.util.Date)
	 */
	@Override
	public Map<Date,Long> getEmployeesAddedCount(long pTimeperiod) {
		// TODO Auto-generated method stub
		return mGlobalProfileService.getEmployeesAddedCount(pTimeperiod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oracle.apps.hcm.hwr.webservices.server.IHWRMetricService#
	 * getCandidatesAddedCount(java.util.Date)
	 */
	@Override
	public Map<Date,Long> getCandidatesAddedCount(long pTimeperiod) {
		// TODO Auto-generated method stub
		return mGlobalProfileService.getCandidatesAddedCount(pTimeperiod);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see oracle.apps.hcm.hwr.webservices.server.IHWRMetricService#
	 * getRegisteredEmployeeCount(java.util.Date)
	 */
	@Override
	public Map<Date,Long> getRegisteredEmployeeCount(long pTimeperiod) {
		// TODO Auto-generated method stub
		return mGlobalProfileService.getRegisteredEmployeeCount(pTimeperiod);
	}

}
