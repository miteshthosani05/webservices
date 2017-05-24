package com.oracle.xmlns.apps.hcm.hwr.coreservice;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import oracle.j2ee.ws.common.jaxws.ServiceDelegateImpl;
// !DO NOT EDIT THIS FILE!
// This source file is generated by Oracle tools
// Contents may be subject to change
// For reporting problems, use the following
// Version = Oracle WebServices (11.1.1.0.0, build 130224.1947.04102)

@WebServiceClient
public class WorkforceReputationService_Service
  extends ServiceDelegateImpl
{
  private static URL wsdlLocationURL;

  private static Logger logger;
  
  
  public WorkforceReputationService_Service(String pWsdlURL) {
	  super(createWsdlLocationURL(pWsdlURL),
			  new QName("http://xmlns.oracle.com/apps/hcm/hwr/coreService/",
					  "WorkforceReputationService"), WorkforceReputationService_Service.class);
  }
  
 public static URL createWsdlLocationURL(String pWsdlURL) {
    try
    {
      logger = Logger.getLogger("com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService_Service");
      URL baseUrl =
        WorkforceReputationService_Service.class.getResource(".");
      if (baseUrl == null)
      {
        wsdlLocationURL =
        		WorkforceReputationService_Service.class.getResource(pWsdlURL);
        if (wsdlLocationURL == null)
        {
          baseUrl = new File(".").toURL();
          wsdlLocationURL =
        		  new URL(baseUrl, pWsdlURL);
        }
      }
      else
      {
                if (!baseUrl.getPath().endsWith("/")) {
         baseUrl = new URL(baseUrl, baseUrl.getPath() + "/");
}
                wsdlLocationURL =
                		new URL(baseUrl, pWsdlURL);
      }
    }
    catch (MalformedURLException e)
    {
      logger.log(Level.ALL,
    		  "Failed to create wsdlLocationURL using " + pWsdlURL,
          e);
    }
    
    return wsdlLocationURL;
  }

  public WorkforceReputationService_Service()
  {
    super(wsdlLocationURL,
          new QName("http://xmlns.oracle.com/apps/hcm/hwr/coreService/",
        		  "WorkforceReputationService"), WorkforceReputationService_Service.class);
  }

  public WorkforceReputationService_Service(URL wsdlLocation,
                                            QName serviceName)
  {
	  super(wsdlLocation, serviceName, WorkforceReputationService_Service.class);
  }

  @WebEndpoint(name="WorkforceReputationServiceSoapHttpPort")
  public com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService getWorkforceReputationServiceSoapHttpPort()
  {
    return (com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService) super.getPort(new QName("http://xmlns.oracle.com/apps/hcm/hwr/coreService/",
    		                                                                                                          "WorkforceReputationServiceSoapHttpPort"),
    		                                                                                                com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService.class);
  }

  @WebEndpoint(name="WorkforceReputationServiceSoapHttpPort")
  public com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService getWorkforceReputationServiceSoapHttpPort(WebServiceFeature... features)
  {
    return (com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService) super.getPort(new QName("http://xmlns.oracle.com/apps/hcm/hwr/coreService/",
    		                                                                                                          "WorkforceReputationServiceSoapHttpPort"),
    		                                                                                                com.oracle.xmlns.apps.hcm.hwr.coreservice.WorkforceReputationService.class,
    		                                                                                                features);
  }
}