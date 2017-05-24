package oracle.apps.hcm.hwr.coreService.serviceProvider;

import java.util.Date;
import java.util.Map;

import oracle.apps.hcm.hwr.coreService.adapters.HWRServiceHolder;
import oracle.apps.hcm.hwr.webservices.server.IHWRMetricService;

public class MetricServiceProvider {
    public MetricServiceProvider() {
        super();
    }

    public Map<Date,Long> getPostsAnalyzedCount(long pTimePeriod) {
         IHWRMetricService myService =
            HWRServiceHolder.getInstance().getHWRMetricService();
        return myService.getPostsAnalyzedCount(pTimePeriod);
    }

    public Map<Date,Long> getIssuesCount(long pTimePeriod) {
        IHWRMetricService myService =
            HWRServiceHolder.getInstance().getHWRMetricService();
        return myService.getIssuesCount(pTimePeriod);
    }

    public Map<Date,Long> getProfilesAddedCount(long pTimePeriod) {
        IHWRMetricService myService =
            HWRServiceHolder.getInstance().getHWRMetricService();
        return myService.getProfilesAddedCount(pTimePeriod);
    }

    public Map<Date,Long> getEmployeesAddedCount(long pTimePeriod) {
        IHWRMetricService myService =
            HWRServiceHolder.getInstance().getHWRMetricService();
        return myService.getEmployeesAddedCount(pTimePeriod);
    }

    public Map<Date,Long> getCandidatesAddedCount(long pTimePeriod) {
        IHWRMetricService myService =
            HWRServiceHolder.getInstance().getHWRMetricService();
        return myService.getCandidatesAddedCount(pTimePeriod);
    }

    public Map<Date,Long> getRegisteredEmployeeCount(long pTimePeriod) {
        IHWRMetricService myService =
            HWRServiceHolder.getInstance().getHWRMetricService();
        return myService.getRegisteredEmployeeCount(pTimePeriod);
    }

}
