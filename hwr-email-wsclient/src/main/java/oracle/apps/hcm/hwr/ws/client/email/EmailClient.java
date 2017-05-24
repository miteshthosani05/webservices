package oracle.apps.hcm.hwr.ws.client.email;

import javax.xml.ws.WebServiceRef;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import oracle.apps.hcm.hwr.appservices.IEmailService;
import oracle.apps.hcm.hwr.ws.client.email.autogen.BpelEmailNotification;
import oracle.apps.hcm.hwr.ws.client.email.autogen.Bpelemailnotification_client_ep;

/**
 * Client to interact with the BPEL e-mail service to send e-mails.
 * 
 * @author tgsnyder
 *
 */
public class EmailClient implements IEmailService {

    /** Logger. */
    private static final Log LOGGER = LogFactory.getLog(EmailClient.class);
    
    @WebServiceRef
    private static Bpelemailnotification_client_ep bpelemailnotification_client_ep;
    
    /** Location of the WSDL */
    private String mWsdl;
    
    /**
     * Default constructor.  Gets the WSDL location of the server dynamically.
     */
    public EmailClient() { 
        mWsdl = EmailServerUrl.getUrl();
    }
    
    /**
     * Override constructor.  Specify the location of the server WSDL.
     * 
     * @param pHostname - Hostname of the WSDL.
     * @param pPort - Port of the Wsdl.
     */
    public EmailClient(String pHostname, String pPort) {
        mWsdl = EmailServerUrl.getUrl(pHostname, pPort);
    }
    
    /**
     * Main method to request for the server to retreive the URL.
     */
    @Override
    public void sendEmail(String[] pTos, String pSubject, String pMessage) {

        LOGGER.debug("Sending e-mail to service at: " + mWsdl);
        
        try {
            bpelemailnotification_client_ep = new Bpelemailnotification_client_ep(mWsdl);
            BpelEmailNotification bpelEmailNotification = bpelemailnotification_client_ep.getBpelEmailNotification_pt();

            
            for (String pTo : pTos) {
                bpelEmailNotification.process(pTo, pSubject, pMessage);
            }
            
        } catch (Exception ex) {
            //@todo - error handling.  I.E. throw or format into another object to return.
            //Interface needs to be updated for this to take place.
            LOGGER.debug(ex);
        }
    }
    
}
