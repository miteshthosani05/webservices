package oracle.apps.hcm.hwr.webservices.server;

import java.util.Date;
import java.util.Map;

public interface IHWRMetricService {

	/**
	 * @param pTimePeriod
	 * @return
	 */
	Map<Date,Long> getPostsAnalyzedCount(final long pTimePeriod);

	/**
	 * @param pTimePeriod
	 * @return
	 */
	Map<Date,Long> getIssuesCount(final long pTimePeriod);

	/**
	 * @param pTimePeriod
	 * @return
	 */
	Map<Date,Long> getProfilesAddedCount(final long pTimePeriod);

	/**
	 * @param pTimePeriod
	 * @return
	 */
	Map<Date,Long> getEmployeesAddedCount(final long pTimePeriod);

	/**
	 * @param pTimePeriod
	 * @return
	 */
	Map<Date,Long> getCandidatesAddedCount(final long pTimePeriod);

	/**
	 * @param pTimePeriod
	 * @return
	 */
	Map<Date,Long> getRegisteredEmployeeCount(final long pTimePeriod);

}
