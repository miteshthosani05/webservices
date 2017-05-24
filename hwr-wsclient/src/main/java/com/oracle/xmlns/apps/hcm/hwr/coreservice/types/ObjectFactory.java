
package com.oracle.xmlns.apps.hcm.hwr.coreservice.types;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.oracle.xmlns.apps.hcm.hwr.coreservice.types package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Fault_QNAME = new QName("http://xmlns.oracle.com/oracleas/schema/oracle-fault-11_0", "Fault");
    private final static QName _Types_QNAME = new QName("commonj.sdo", "types");
    private final static QName _Type_QNAME = new QName("commonj.sdo", "type");
    private final static QName _DataObject_QNAME = new QName("commonj.sdo", "dataObject");
    private final static QName _ServiceErrorMessage_QNAME = new QName("http://xmlns.oracle.com/adf/svc/errors/", "ServiceErrorMessage");
    private final static QName _Datagraph_QNAME = new QName("commonj.sdo", "datagraph");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.oracle.xmlns.apps.hcm.hwr.coreservice.types
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceErrorMessage }
     * 
     */
    public ServiceErrorMessage createServiceErrorMessage() {
        return new ServiceErrorMessage();
    }

    /**
     * Create an instance of {@link ServiceMessage }
     * 
     */
    public ServiceMessage createServiceMessage() {
        return new ServiceMessage();
    }

    /**
     * Create an instance of {@link ServiceDMLErrorMessage }
     * 
     */
    public ServiceDMLErrorMessage createServiceDMLErrorMessage() {
        return new ServiceDMLErrorMessage();
    }

    /**
     * Create an instance of {@link ServiceRowValErrorMessage }
     * 
     */
    public ServiceRowValErrorMessage createServiceRowValErrorMessage() {
        return new ServiceRowValErrorMessage();
    }

    /**
     * Create an instance of {@link ServiceAttrValErrorMessage }
     * 
     */
    public ServiceAttrValErrorMessage createServiceAttrValErrorMessage() {
        return new ServiceAttrValErrorMessage();
    }

    /**
     * Create an instance of {@link JavaInfo }
     * 
     */
    public JavaInfo createJavaInfo() {
        return new JavaInfo();
    }

    /**
     * Create an instance of {@link DataGraphType }
     * 
     */
    public DataGraphType createDataGraphType() {
        return new DataGraphType();
    }

    /**
     * Create an instance of {@link Type }
     * 
     */
    public Type createType() {
        return new Type();
    }

    /**
     * Create an instance of {@link Types }
     * 
     */
    public Types createTypes() {
        return new Types();
    }

    /**
     * Create an instance of {@link ModelsType }
     * 
     */
    public ModelsType createModelsType() {
        return new ModelsType();
    }

    /**
     * Create an instance of {@link Property }
     * 
     */
    public Property createProperty() {
        return new Property();
    }

    /**
     * Create an instance of {@link XSDType }
     * 
     */
    public XSDType createXSDType() {
        return new XSDType();
    }

    /**
     * Create an instance of {@link ChangeSummaryType }
     * 
     */
    public ChangeSummaryType createChangeSummaryType() {
        return new ChangeSummaryType();
    }

    /**
     * Create an instance of {@link FaultType }
     * 
     */
    public FaultType createFaultType() {
        return new FaultType();
    }

    /**
     * Create an instance of {@link Detail }
     * 
     */
    public Detail createDetail() {
        return new Detail();
    }

    /**
     * Create an instance of {@link EndorseUserAsync }
     * 
     */
    public EndorseUserAsync createEndorseUserAsync() {
        return new EndorseUserAsync();
    }

    /**
     * Create an instance of {@link InitializeWLAAsync }
     * 
     */
    public InitializeWLAAsync createInitializeWLAAsync() {
        return new InitializeWLAAsync();
    }

    /**
     * Create an instance of {@link GetUserToEndorseAsyncResponse }
     * 
     */
    public GetUserToEndorseAsyncResponse createGetUserToEndorseAsyncResponse() {
        return new GetUserToEndorseAsyncResponse();
    }

    /**
     * Create an instance of {@link PollForJobAsyncResponse }
     * 
     */
    public PollForJobAsyncResponse createPollForJobAsyncResponse() {
        return new PollForJobAsyncResponse();
    }

    /**
     * Create an instance of {@link GetUserToEndorse }
     * 
     */
    public GetUserToEndorse createGetUserToEndorse() {
        return new GetUserToEndorse();
    }

    /**
     * Create an instance of {@link EndorseUserResponse }
     * 
     */
    public EndorseUserResponse createEndorseUserResponse() {
        return new EndorseUserResponse();
    }

    /**
     * Create an instance of {@link PollForJobResponse }
     * 
     */
    public PollForJobResponse createPollForJobResponse() {
        return new PollForJobResponse();
    }

    /**
     * Create an instance of {@link UpdateJobProgress }
     * 
     */
    public UpdateJobProgress createUpdateJobProgress() {
        return new UpdateJobProgress();
    }

    /**
     * Create an instance of {@link PollForJob }
     * 
     */
    public PollForJob createPollForJob() {
        return new PollForJob();
    }

    /**
     * Create an instance of {@link SearchUserToEndorseAsync }
     * 
     */
    public SearchUserToEndorseAsync createSearchUserToEndorseAsync() {
        return new SearchUserToEndorseAsync();
    }

    /**
     * Create an instance of {@link WriteConnectorData }
     * 
     */
    public WriteConnectorData createWriteConnectorData() {
        return new WriteConnectorData();
    }

    /**
     * Create an instance of {@link GetMyEndorsementsAsyncResponse }
     * 
     */
    public GetMyEndorsementsAsyncResponse createGetMyEndorsementsAsyncResponse() {
        return new GetMyEndorsementsAsyncResponse();
    }

    /**
     * Create an instance of {@link EndorseUser }
     * 
     */
    public EndorseUser createEndorseUser() {
        return new EndorseUser();
    }

    /**
     * Create an instance of {@link UpdateJobProgressResponse }
     * 
     */
    public UpdateJobProgressResponse createUpdateJobProgressResponse() {
        return new UpdateJobProgressResponse();
    }

    /**
     * Create an instance of {@link GetUserToEndorseAsync }
     * 
     */
    public GetUserToEndorseAsync createGetUserToEndorseAsync() {
        return new GetUserToEndorseAsync();
    }

    /**
     * Create an instance of {@link RegisterUserAsyncResponse }
     * 
     */
    public RegisterUserAsyncResponse createRegisterUserAsyncResponse() {
        return new RegisterUserAsyncResponse();
    }

    /**
     * Create an instance of {@link RegisterUserResponse }
     * 
     */
    public RegisterUserResponse createRegisterUserResponse() {
        return new RegisterUserResponse();
    }

    /**
     * Create an instance of {@link MergePersonaSyncDataAsyncResponse }
     * 
     */
    public MergePersonaSyncDataAsyncResponse createMergePersonaSyncDataAsyncResponse() {
        return new MergePersonaSyncDataAsyncResponse();
    }

    /**
     * Create an instance of {@link InitializeWLAAsyncResponse }
     * 
     */
    public InitializeWLAAsyncResponse createInitializeWLAAsyncResponse() {
        return new InitializeWLAAsyncResponse();
    }

    /**
     * Create an instance of {@link UpdateJobProgressAsync }
     * 
     */
    public UpdateJobProgressAsync createUpdateJobProgressAsync() {
        return new UpdateJobProgressAsync();
    }

    /**
     * Create an instance of {@link MergePersonaSyncData }
     * 
     */
    public MergePersonaSyncData createMergePersonaSyncData() {
        return new MergePersonaSyncData();
    }

    /**
     * Create an instance of {@link WriteConnectorDataAsyncResponse }
     * 
     */
    public WriteConnectorDataAsyncResponse createWriteConnectorDataAsyncResponse() {
        return new WriteConnectorDataAsyncResponse();
    }

    /**
     * Create an instance of {@link InitializeWLA }
     * 
     */
    public InitializeWLA createInitializeWLA() {
        return new InitializeWLA();
    }

    /**
     * Create an instance of {@link GetMyEndorsementsResponse }
     * 
     */
    public GetMyEndorsementsResponse createGetMyEndorsementsResponse() {
        return new GetMyEndorsementsResponse();
    }

    /**
     * Create an instance of {@link RegisterUser }
     * 
     */
    public RegisterUser createRegisterUser() {
        return new RegisterUser();
    }

    /**
     * Create an instance of {@link GetUserToEndorseResponse }
     * 
     */
    public GetUserToEndorseResponse createGetUserToEndorseResponse() {
        return new GetUserToEndorseResponse();
    }

    /**
     * Create an instance of {@link RegisterUserAsync }
     * 
     */
    public RegisterUserAsync createRegisterUserAsync() {
        return new RegisterUserAsync();
    }

    /**
     * Create an instance of {@link SearchUserToEndorseAsyncResponse }
     * 
     */
    public SearchUserToEndorseAsyncResponse createSearchUserToEndorseAsyncResponse() {
        return new SearchUserToEndorseAsyncResponse();
    }

    /**
     * Create an instance of {@link MergePersonaSyncDataAsync }
     * 
     */
    public MergePersonaSyncDataAsync createMergePersonaSyncDataAsync() {
        return new MergePersonaSyncDataAsync();
    }

    /**
     * Create an instance of {@link SearchUserToEndorse }
     * 
     */
    public SearchUserToEndorse createSearchUserToEndorse() {
        return new SearchUserToEndorse();
    }

    /**
     * Create an instance of {@link GetUserProfileResponse }
     * 
     */
    public GetUserProfileResponse createGetUserProfileResponse() {
        return new GetUserProfileResponse();
    }

    /**
     * Create an instance of {@link WriteConnectorDataAsync }
     * 
     */
    public WriteConnectorDataAsync createWriteConnectorDataAsync() {
        return new WriteConnectorDataAsync();
    }

    /**
     * Create an instance of {@link MergePersonaSyncDataResponse }
     * 
     */
    public MergePersonaSyncDataResponse createMergePersonaSyncDataResponse() {
        return new MergePersonaSyncDataResponse();
    }

    /**
     * Create an instance of {@link EndorseUserAsyncResponse }
     * 
     */
    public EndorseUserAsyncResponse createEndorseUserAsyncResponse() {
        return new EndorseUserAsyncResponse();
    }

    /**
     * Create an instance of {@link GetMyEndorsements }
     * 
     */
    public GetMyEndorsements createGetMyEndorsements() {
        return new GetMyEndorsements();
    }

    /**
     * Create an instance of {@link GetUserProfile }
     * 
     */
    public GetUserProfile createGetUserProfile() {
        return new GetUserProfile();
    }

    /**
     * Create an instance of {@link GetMyEndorsementsAsync }
     * 
     */
    public GetMyEndorsementsAsync createGetMyEndorsementsAsync() {
        return new GetMyEndorsementsAsync();
    }

    /**
     * Create an instance of {@link GetUserProfileAsyncResponse }
     * 
     */
    public GetUserProfileAsyncResponse createGetUserProfileAsyncResponse() {
        return new GetUserProfileAsyncResponse();
    }

    /**
     * Create an instance of {@link WriteConnectorDataResponse }
     * 
     */
    public WriteConnectorDataResponse createWriteConnectorDataResponse() {
        return new WriteConnectorDataResponse();
    }

    /**
     * Create an instance of {@link InitializeWLAResponse }
     * 
     */
    public InitializeWLAResponse createInitializeWLAResponse() {
        return new InitializeWLAResponse();
    }

    /**
     * Create an instance of {@link PollForJobAsync }
     * 
     */
    public PollForJobAsync createPollForJobAsync() {
        return new PollForJobAsync();
    }

    /**
     * Create an instance of {@link SearchUserToEndorseResponse }
     * 
     */
    public SearchUserToEndorseResponse createSearchUserToEndorseResponse() {
        return new SearchUserToEndorseResponse();
    }

    /**
     * Create an instance of {@link GetUserProfileAsync }
     * 
     */
    public GetUserProfileAsync createGetUserProfileAsync() {
        return new GetUserProfileAsync();
    }

    /**
     * Create an instance of {@link UpdateJobProgressAsyncResponse }
     * 
     */
    public UpdateJobProgressAsyncResponse createUpdateJobProgressAsyncResponse() {
        return new UpdateJobProgressAsyncResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FaultType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/oracleas/schema/oracle-fault-11_0", name = "Fault")
    public JAXBElement<FaultType> createFault(FaultType value) {
        return new JAXBElement<FaultType>(_Fault_QNAME, FaultType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Types }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "commonj.sdo", name = "types")
    public JAXBElement<Types> createTypes(Types value) {
        return new JAXBElement<Types>(_Types_QNAME, Types.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Type }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "commonj.sdo", name = "type")
    public JAXBElement<Type> createType(Type value) {
        return new JAXBElement<Type>(_Type_QNAME, Type.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "commonj.sdo", name = "dataObject")
    public JAXBElement<Object> createDataObject(Object value) {
        return new JAXBElement<Object>(_DataObject_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceErrorMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://xmlns.oracle.com/adf/svc/errors/", name = "ServiceErrorMessage")
    public JAXBElement<ServiceErrorMessage> createServiceErrorMessage(ServiceErrorMessage value) {
        return new JAXBElement<ServiceErrorMessage>(_ServiceErrorMessage_QNAME, ServiceErrorMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataGraphType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "commonj.sdo", name = "datagraph")
    public JAXBElement<DataGraphType> createDatagraph(DataGraphType value) {
        return new JAXBElement<DataGraphType>(_Datagraph_QNAME, DataGraphType.class, null, value);
    }

}
