package oracle.apps.hcm.hwr.coreService.applicationModule.server;

import java.lang.reflect.Method;

import java.sql.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import javax.interceptor.Interceptors;



import oracle.apps.hcm.hwr.coreService.MetricResponseServiceData;
import oracle.apps.hcm.hwr.coreService.WorkforceReputationMetricService;
import oracle.apps.hcm.hwr.coreService.applicationModule.WorkforceReputationMetricServiceAMImpl;

import oracle.jbo.common.Diagnostic;
import oracle.jbo.common.sdo.SDOHelper;
import oracle.jbo.server.svc.ServiceContextInterceptor;
import oracle.jbo.server.svc.ServiceImpl;
import oracle.jbo.server.svc.ServicePermissionCheckInterceptor;
import oracle.jbo.service.errors.ServiceException;

import oracle.webservices.annotations.PortableWebService;
import oracle.webservices.annotations.SecurityPolicy;
import oracle.webservices.annotations.async.AsyncWebService;
import oracle.webservices.annotations.async.AsyncWebServiceQueue;
import oracle.webservices.annotations.async.AsyncWebServiceResponseQueue;
import oracle.webservices.annotations.async.CallbackSecurityPolicy;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sun Jun 02 22:11:17 PDT 2013
// ---------------------------------------------------------------------
@Interceptors({ServiceContextInterceptor.class,ServicePermissionCheckInterceptor.class})
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@AsyncWebService(systemUser="FUSION_APPS_HCM_ASYNC_WS_APPID")
@AsyncWebServiceQueue(connectionFactory="aqjms/AsyncWSAQConnectionFactory", queue="oracle.j2ee.ws.server.async.AQRequestQueue", messageProcessorInitialPoolSize=1, messageProcessorMaxPoolSize=2)
@AsyncWebServiceResponseQueue(connectionFactory="aqjms/AsyncWSAQConnectionFactory", queue="oracle.j2ee.ws.server.async.AQResponseQueue",
messageProcessorInitialPoolSize=1, messageProcessorMaxPoolSize=2)
@Stateless(name="oracle.apps.hcm.hwr.coreService.WorkforceReputationMetricServiceBean",
    mappedName="WorkforceReputationMetricServiceBean")
@Remote(WorkforceReputationMetricService.class)
@PortableWebService(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/",
    serviceName="WorkforceReputationMetricService", portName="WorkforceReputationMetricServiceSoapHttpPort",
    endpointInterface="oracle.apps.hcm.hwr.coreService.WorkforceReputationMetricService")
public class WorkforceReputationMetricServiceImpl extends ServiceImpl implements WorkforceReputationMetricService {
    private static boolean _isInited = false;


    private static final Map _map = new HashMap();

    /**
     * This is the default constructor (do not remove).
     */
    public WorkforceReputationMetricServiceImpl() {
        init();
        setApplicationModuleDefName("oracle.apps.hcm.hwr.coreService.applicationModule.WorkforceReputationMetricServiceAM");
        setConfigurationName("WorkforceReputationMetricService");
    }


    /**
     * Generated method. Do not modify. Do initialization in the constructor
     */
    protected void init() {
        if (_isInited) {
            return;
        }
        synchronized (WorkforceReputationMetricServiceImpl.class) {
            if (_isInited) {
                return;
            }
            try {
                SDOHelper.INSTANCE.defineSchema("oracle/apps/hcm/hwr/coreService/", "WorkforceReputationMetricService.xsd");
                _map.put("getRegisteredEmployeeCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getRegisteredEmployeeCount",
                                                                                new Class[] { long.class }));
                _map.put("getProfilesAddedCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getProfilesAddedCount",
                                                                                new Class[] { long.class }));
                _map.put("getPostsAnalyzedCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getPostsAnalyzedCount",
                                                                                new Class[] { long.class }));
                _map.put("getIssuesCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getIssuesCount",
                                                                                new Class[] { long.class }));
                _map.put("getEmployeesAddedCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getEmployeesAddedCount",
                                                                                new Class[] { long.class }));
                _map.put("getCandidatesAddedCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getCandidatesAddedCount",
                                                                                new Class[] { long.class }));
                _map.put("getHWRRegisteredEmployeeCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getHWRRegisteredEmployeeCount",
                                                                                new Class[] { long.class }));
                _map.put("getHWRProfilesAddedCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getHWRProfilesAddedCount",
                                                                                new Class[] { long.class }));
                _map.put("getHWRPostsAnalyzedCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getHWRPostsAnalyzedCount",
                                                                                new Class[] { long.class }));
                _map.put("getHWRIssuesCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getHWRIssuesCount",
                                                                                new Class[] { long.class }));
                _map.put("getHWREmployeesAddedCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getHWREmployeesAddedCount",
                                                                                new Class[] { long.class }));
                _map.put("getHWRCandidatesAddedCount",
                         WorkforceReputationMetricServiceAMImpl.class.getMethod("getHWRCandidatesAddedCount",
                                                                                new Class[] { long.class }));
                _isInited = true;
            } catch (Exception t) {
                Diagnostic.printStackTrace(t);
            }
        }
    }


    /**
     * getRegisteredEmployeeCount: generated method. Do not modify.
     */
    public MetricResponseServiceData getRegisteredEmployeeCount(long pTimePeriod) throws ServiceException {
        return (MetricResponseServiceData)invokeCustom((Method)_map.get("getRegisteredEmployeeCount"),
                                                       new Object[] { pTimePeriod },
                                                       new String[] { null },
                                                       MetricResponseServiceData.class,
                                                       false);
    }

    /**
     * getProfilesAddedCount: generated method. Do not modify.
     */
    public MetricResponseServiceData getProfilesAddedCount(long pTimePeriod) throws ServiceException {
        return (MetricResponseServiceData)invokeCustom((Method)_map.get("getProfilesAddedCount"),
                                                       new Object[] { pTimePeriod },
                                                       new String[] { null },
                                                       MetricResponseServiceData.class,
                                                       false);
    }

    /**
     * getPostsAnalyzedCount: generated method. Do not modify.
     */
    public MetricResponseServiceData getPostsAnalyzedCount(long pTimePeriod) throws ServiceException {
        return (MetricResponseServiceData)invokeCustom((Method)_map.get("getPostsAnalyzedCount"),
                                                       new Object[] { pTimePeriod },
                                                       new String[] { null },
                                                       MetricResponseServiceData.class,
                                                       false);
    }

    /**
     * getIssuesCount: generated method. Do not modify.
     */
    public MetricResponseServiceData getIssuesCount(long pTimePeriod) throws ServiceException {
        return (MetricResponseServiceData)invokeCustom((Method)_map.get("getIssuesCount"),
                                                       new Object[] { pTimePeriod },
                                                       new String[] { null },
                                                       MetricResponseServiceData.class,
                                                       false);
    }

    /**
     * getEmployeesAddedCount: generated method. Do not modify.
     */
    public MetricResponseServiceData getEmployeesAddedCount(long pTimePeriod) throws ServiceException {
        return (MetricResponseServiceData)invokeCustom((Method)_map.get("getEmployeesAddedCount"),
                                                       new Object[] { pTimePeriod },
                                                       new String[] { null },
                                                       MetricResponseServiceData.class,
                                                       false);
    }

    /**
     * getCandidatesAddedCount: generated method. Do not modify.
     */
    public MetricResponseServiceData getCandidatesAddedCount(long pTimePeriod) throws ServiceException {
        return (MetricResponseServiceData)invokeCustom((Method)_map.get("getCandidatesAddedCount"),
                                                       new Object[] { pTimePeriod },
                                                       new String[] { null },
                                                       MetricResponseServiceData.class,
                                                       false);
    }

    /**
     * getHWRRegisteredEmployeeCount: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRRegisteredEmployeeCount(long pTimePeriod) throws ServiceException {
        return (List<MetricResponseServiceData>)invokeCustom((Method)_map.get("getHWRRegisteredEmployeeCount"),
                                                             new Object[] { pTimePeriod },
                                                             new String[] { null },
                                                             MetricResponseServiceData.class,
                                                             false);
    }

    /**
     * getHWRProfilesAddedCount: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRProfilesAddedCount(long pTimePeriod) throws ServiceException {
        return (List<MetricResponseServiceData>)invokeCustom((Method)_map.get("getHWRProfilesAddedCount"),
                                                             new Object[] { pTimePeriod },
                                                             new String[] { null },
                                                             MetricResponseServiceData.class,
                                                             false);
    }

    /**
     * getHWRPostsAnalyzedCount: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRPostsAnalyzedCount(long pTimePeriod) throws ServiceException {
        return (List<MetricResponseServiceData>)invokeCustom((Method)_map.get("getHWRPostsAnalyzedCount"),
                                                             new Object[] { pTimePeriod },
                                                             new String[] { null },
                                                             MetricResponseServiceData.class,
                                                             false);
    }

    /**
     * getHWRIssuesCount: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRIssuesCount(long pTimePeriod) throws ServiceException {
        return (List<MetricResponseServiceData>)invokeCustom((Method)_map.get("getHWRIssuesCount"),
                                                             new Object[] { pTimePeriod },
                                                             new String[] { null },
                                                             MetricResponseServiceData.class,
                                                             false);
    }

    /**
     * getHWREmployeesAddedCount: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWREmployeesAddedCount(long pTimePeriod) throws ServiceException {
        return (List<MetricResponseServiceData>)invokeCustom((Method)_map.get("getHWREmployeesAddedCount"),
                                                             new Object[] { pTimePeriod },
                                                             new String[] { null },
                                                             MetricResponseServiceData.class,
                                                             false);
    }

    /**
     * getHWRCandidatesAddedCount: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRCandidatesAddedCount(long pTimePeriod) throws ServiceException {
        return (List<MetricResponseServiceData>)invokeCustom((Method)_map.get("getHWRCandidatesAddedCount"),
                                                             new Object[] { pTimePeriod },
                                                             new String[] { null },
                                                             MetricResponseServiceData.class,
                                                             false);
    }

    /**
     * getHWRPostsAnalyzedCountAsync: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRPostsAnalyzedCountAsync(long pTimePeriod) throws ServiceException {
        return getHWRPostsAnalyzedCount(pTimePeriod);
    }

    /**
     * getCandidatesAddedCountAsync: generated method. Do not modify.
     */
    public MetricResponseServiceData getCandidatesAddedCountAsync(long pTimePeriod) throws ServiceException {
        return getCandidatesAddedCount(pTimePeriod);
    }

    /**
     * getHWRProfilesAddedCountAsync: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRProfilesAddedCountAsync(long pTimePeriod) throws ServiceException {
        return getHWRProfilesAddedCount(pTimePeriod);
    }

    /**
     * getPostsAnalyzedCountAsync: generated method. Do not modify.
     */
    public MetricResponseServiceData getPostsAnalyzedCountAsync(long pTimePeriod) throws ServiceException {
        return getPostsAnalyzedCount(pTimePeriod);
    }

    /**
     * getEmployeesAddedCountAsync: generated method. Do not modify.
     */
    public MetricResponseServiceData getEmployeesAddedCountAsync(long pTimePeriod) throws ServiceException {
        return getEmployeesAddedCount(pTimePeriod);
    }

    /**
     * getHWRRegisteredEmployeeCountAsync: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRRegisteredEmployeeCountAsync(long pTimePeriod) throws ServiceException {
        return getHWRRegisteredEmployeeCount(pTimePeriod);
    }

    /**
     * getIssuesCountAsync: generated method. Do not modify.
     */
    public MetricResponseServiceData getIssuesCountAsync(long pTimePeriod) throws ServiceException {
        return getIssuesCount(pTimePeriod);
    }

    /**
     * getHWRIssuesCountAsync: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRIssuesCountAsync(long pTimePeriod) throws ServiceException {
        return getHWRIssuesCount(pTimePeriod);
    }

    /**
     * getRegisteredEmployeeCountAsync: generated method. Do not modify.
     */
    public MetricResponseServiceData getRegisteredEmployeeCountAsync(long pTimePeriod) throws ServiceException {
        return getRegisteredEmployeeCount(pTimePeriod);
    }

    /**
     * getProfilesAddedCountAsync: generated method. Do not modify.
     */
    public MetricResponseServiceData getProfilesAddedCountAsync(long pTimePeriod) throws ServiceException {
        return getProfilesAddedCount(pTimePeriod);
    }

    /**
     * getHWRCandidatesAddedCountAsync: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWRCandidatesAddedCountAsync(long pTimePeriod) throws ServiceException {
        return getHWRCandidatesAddedCount(pTimePeriod);
    }

    /**
     * getHWREmployeesAddedCountAsync: generated method. Do not modify.
     */
    public List<MetricResponseServiceData> getHWREmployeesAddedCountAsync(long pTimePeriod) throws ServiceException {
        return getHWREmployeesAddedCount(pTimePeriod);
    }
}
