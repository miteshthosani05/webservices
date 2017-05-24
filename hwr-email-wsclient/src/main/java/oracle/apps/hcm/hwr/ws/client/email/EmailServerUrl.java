package oracle.apps.hcm.hwr.ws.client.email;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * Methods to build the URL to the WSDL of the e-mail server.
 * 
 * @author tgsnyder
 *
 */
public class EmailServerUrl {
    /**
     * Default WSDL URL.
     */
    private static final String mDefaultWsdlUrl = "soa-infra/services/default/Bpel_Email_Notification/bpelemailnotification_client_ep?WSDL";

    /**
     * Default protocol.
     */
    private static final String mProtocol = "http";
    
    private EmailServerUrl() {}
    
    /**
     * Used in production, build the URL based on the server/port we are running on.
     * 
     * @return - string representation of the URL of the WSDL.
     */
    public static final String getUrl() {
        ExternalContext externalCtx = FacesContext.getCurrentInstance().getExternalContext();
        HttpServletRequest request = (HttpServletRequest)externalCtx.getRequest();
        
        return getUrl(request.getLocalName(), Integer.toString(request.getLocalPort()));
    }
    
    /**
     * Build the URL with specifying the hostname, and port.
     * 
     * @param pHostname - hostname of the WSDL.
     * @param pPort - port of the WSDL.
     * @return - string representation of the URL of the WSDL
     */
    public static final String getUrl(String pHostname, String pPort) {
        return getUrl(pHostname, pPort, mDefaultWsdlUrl);
    }
    
    /**
     * 
     * @param pHostname - hostname
     * @param pPort - port
     * @param pWsdlUrl - wsdl location
     * @return - string representation of the URL of the WSDL
     */
    public static final String getUrl(String pHostname, String pPort,
        String pWsdlUrl) {
        return mProtocol + "://" + pHostname + ":" + pPort + "/" + pWsdlUrl;
    }
}
