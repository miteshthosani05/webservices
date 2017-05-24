package oracle.apps.hcm.hwr.ws.client.email.service;

import static org.junit.Assert.*;

import oracle.apps.hcm.hwr.appservices.IEmailService;
import oracle.apps.hcm.hwr.ws.client.email.EmailClient;

import org.junit.Test;


public class EmailClientIntegrationTest {

    @Test
    public void testEmailClient() {
        IEmailService myEmailClient = new EmailClient("slc01qmf.us.oracle.com", "7022");
        
        myEmailClient.sendEmail(new String[] {"yourname@yourdomain.com"},
            "junittest", "It works from pom file! new email client");
    }

}
