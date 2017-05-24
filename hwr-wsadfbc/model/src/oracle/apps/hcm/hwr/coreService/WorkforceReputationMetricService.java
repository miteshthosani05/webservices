package oracle.apps.hcm.hwr.coreService;


import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;

import javax.xml.ws.Action;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

import oracle.jbo.service.errors.ServiceException;

import oracle.webservices.annotations.PortableWebService;
import oracle.webservices.annotations.SDODatabinding;
import oracle.webservices.annotations.async.AsyncWebService;
import oracle.webservices.annotations.async.CallbackMethod;
// ---------------------------------------------------------------------
// ---    File generated by Oracle ADF Business Components Design Time.
// ---    Sun Jun 02 22:11:17 PDT 2013
// ---------------------------------------------------------------------
@AsyncWebService
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.WRAPPED, style = SOAPBinding.Style.DOCUMENT)
@PortableWebService(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/",
    name="WorkforceReputationMetricService", wsdlLocation="oracle/apps/hcm/hwr/coreService/WorkforceReputationMetricService.wsdl")
@SDODatabinding(schemaLocation="oracle/apps/hcm/hwr/coreService/WorkforceReputationMetricService.xsd")
public interface WorkforceReputationMetricService {


    public static final String NAME = "{http://xmlns.oracle.com/apps/hcm/hwr/coreService/}WorkforceReputationMetricService";

    /**
     * Exported method getRegisteredEmployeeCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getRegisteredEmployeeCount",
        operationName="getRegisteredEmployeeCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getRegisteredEmployeeCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getRegisteredEmployeeCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    MetricResponseServiceData getRegisteredEmployeeCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getProfilesAddedCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getProfilesAddedCount",
        operationName="getProfilesAddedCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getProfilesAddedCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getProfilesAddedCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    MetricResponseServiceData getProfilesAddedCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getPostsAnalyzedCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getPostsAnalyzedCount",
        operationName="getPostsAnalyzedCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getPostsAnalyzedCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getPostsAnalyzedCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    MetricResponseServiceData getPostsAnalyzedCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getIssuesCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getIssuesCount",
        operationName="getIssuesCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getIssuesCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getIssuesCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    MetricResponseServiceData getIssuesCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getEmployeesAddedCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getEmployeesAddedCount",
        operationName="getEmployeesAddedCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getEmployeesAddedCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getEmployeesAddedCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    MetricResponseServiceData getEmployeesAddedCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getCandidatesAddedCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getCandidatesAddedCount",
        operationName="getCandidatesAddedCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getCandidatesAddedCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getCandidatesAddedCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    MetricResponseServiceData getCandidatesAddedCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getHWRRegisteredEmployeeCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRRegisteredEmployeeCount",
        operationName="getHWRRegisteredEmployeeCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRRegisteredEmployeeCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRRegisteredEmployeeCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    List<MetricResponseServiceData> getHWRRegisteredEmployeeCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getHWRProfilesAddedCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRProfilesAddedCount",
        operationName="getHWRProfilesAddedCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRProfilesAddedCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRProfilesAddedCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    List<MetricResponseServiceData> getHWRProfilesAddedCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getHWRPostsAnalyzedCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRPostsAnalyzedCount",
        operationName="getHWRPostsAnalyzedCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRPostsAnalyzedCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRPostsAnalyzedCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    List<MetricResponseServiceData> getHWRPostsAnalyzedCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getHWRIssuesCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRIssuesCount",
        operationName="getHWRIssuesCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRIssuesCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRIssuesCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    List<MetricResponseServiceData> getHWRIssuesCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getHWREmployeesAddedCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWREmployeesAddedCount",
        operationName="getHWREmployeesAddedCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWREmployeesAddedCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWREmployeesAddedCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    List<MetricResponseServiceData> getHWREmployeesAddedCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    /**
     * Exported method getHWRCandidatesAddedCount from WorkforceReputationMetricServiceAM.
     */
    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRCandidatesAddedCount",
        operationName="getHWRCandidatesAddedCount")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRCandidatesAddedCount")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRCandidatesAddedCountResponse")
    @WebResult(name="result")
    @CallbackMethod(exclude=true)
    List<MetricResponseServiceData> getHWRCandidatesAddedCount(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRPostsAnalyzedCountAsync",
        operationName="getHWRPostsAnalyzedCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRPostsAnalyzedCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRPostsAnalyzedCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRPostsAnalyzedCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRPostsAnalyzedCountAsyncResponse")
    List<MetricResponseServiceData> getHWRPostsAnalyzedCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getIssuesCountAsync",
        operationName="getIssuesCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getIssuesCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getIssuesCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getIssuesCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getIssuesCountAsyncResponse")
    MetricResponseServiceData getIssuesCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRIssuesCountAsync",
        operationName="getHWRIssuesCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRIssuesCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRIssuesCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRIssuesCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRIssuesCountAsyncResponse")
    List<MetricResponseServiceData> getHWRIssuesCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getCandidatesAddedCountAsync",
        operationName="getCandidatesAddedCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getCandidatesAddedCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getCandidatesAddedCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getCandidatesAddedCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getCandidatesAddedCountAsyncResponse")
    MetricResponseServiceData getCandidatesAddedCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getRegisteredEmployeeCountAsync",
        operationName="getRegisteredEmployeeCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getRegisteredEmployeeCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getRegisteredEmployeeCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getRegisteredEmployeeCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getRegisteredEmployeeCountAsyncResponse")
    MetricResponseServiceData getRegisteredEmployeeCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRProfilesAddedCountAsync",
        operationName="getHWRProfilesAddedCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRProfilesAddedCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRProfilesAddedCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRProfilesAddedCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRProfilesAddedCountAsyncResponse")
    List<MetricResponseServiceData> getHWRProfilesAddedCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getPostsAnalyzedCountAsync",
        operationName="getPostsAnalyzedCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getPostsAnalyzedCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getPostsAnalyzedCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getPostsAnalyzedCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getPostsAnalyzedCountAsyncResponse")
    MetricResponseServiceData getPostsAnalyzedCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getProfilesAddedCountAsync",
        operationName="getProfilesAddedCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getProfilesAddedCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getProfilesAddedCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getProfilesAddedCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getProfilesAddedCountAsyncResponse")
    MetricResponseServiceData getProfilesAddedCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRCandidatesAddedCountAsync",
        operationName="getHWRCandidatesAddedCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRCandidatesAddedCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRCandidatesAddedCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRCandidatesAddedCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRCandidatesAddedCountAsyncResponse")
    List<MetricResponseServiceData> getHWRCandidatesAddedCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWREmployeesAddedCountAsync",
        operationName="getHWREmployeesAddedCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWREmployeesAddedCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWREmployeesAddedCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWREmployeesAddedCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWREmployeesAddedCountAsyncResponse")
    List<MetricResponseServiceData> getHWREmployeesAddedCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRRegisteredEmployeeCountAsync",
        operationName="getHWRRegisteredEmployeeCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRRegisteredEmployeeCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getHWRRegisteredEmployeeCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRRegisteredEmployeeCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getHWRRegisteredEmployeeCountAsyncResponse")
    List<MetricResponseServiceData> getHWRRegisteredEmployeeCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;

    @WebMethod(action="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getEmployeesAddedCountAsync",
        operationName="getEmployeesAddedCountAsync")
    @RequestWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getEmployeesAddedCountAsync")
    @ResponseWrapper(targetNamespace="http://xmlns.oracle.com/apps/hcm/hwr/coreService/types/",
        localName="getEmployeesAddedCountAsyncResponse")
    @WebResult(name="result")
    @Action(input="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getEmployeesAddedCountAsync",
        output="http://xmlns.oracle.com/apps/hcm/hwr/coreService/getEmployeesAddedCountAsyncResponse")
    MetricResponseServiceData getEmployeesAddedCountAsync(@WebParam(mode = WebParam.Mode.IN,
            name="pTimePeriod")
        long pTimePeriod) throws ServiceException;
}