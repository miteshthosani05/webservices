package oracle.apps.hcm.hwr.csf;

import oracle.security.jps.service.credstore.GenericCredential;
import oracle.security.jps.service.credstore.PasswordCredential;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class retrieves the credentials needed for the
 * Web Services client from the Credentials Store.
 * 
 * The keys must be created in the Weblogic Enterprise
 * Manager at host:port/em
 * 
 * THE NAME OF THE KEYS THAT NEED TO BE CREATED CAN BE
 * FOUND IN THE CONFIGURATION FILE OF THE WHITE LABEL APP.
 * 
 * @author cristobal.vergara.niedermayr@oracle.com
 *
 */
public class WSCredentials {
	
	private static final Log LOGGER = LogFactory.getLog(WSCredentials.class);
	
	private static final String sMapName = System.getProperty("csf.map");
	
	private static final String USERNAME_PASSWORD_KEY = 
			System.getProperty("csf.ws.username_and_password.key");
	private static final String KEYSTORE_PASSWORD_KEY = 
			System.getProperty("csf.ws.keystore_password.key");
	private static final String KEYSTORE_TYPE_KEY = 
			System.getProperty("csf.ws.keystore_type.key");
	
	private static final CsfCredentialRetriever sCsf = new CsfCredentialRetriever();
	
	/**
	 * @return The user name for authentication with the HWR server.
	 */
	public static String getUsername() {
        return getNameFromPasswordCredential(USERNAME_PASSWORD_KEY);
	}
	
	/**
	 * @return The password for authentication with the HWR server.
	 */
	public static String getPassword() {
		return getPasswordFromPasswordCredential(USERNAME_PASSWORD_KEY);
	}
	
	public static String getKeystorePassword() {
		return getValueFromGenericCredential(KEYSTORE_PASSWORD_KEY);
	}
	
	private static String getNameFromPasswordCredential(String pCredentialKey) {
		PasswordCredential myUsernameAndPassword = 
				retrievePasswordCredential(pCredentialKey);
        if(myUsernameAndPassword == null) {
        	return null;
        }
        return new String(myUsernameAndPassword.getName());
	}
	
	private static String getPasswordFromPasswordCredential(String pCredentialKey) {
		PasswordCredential myUsernameAndPassword = 
				retrievePasswordCredential(pCredentialKey);
        if(myUsernameAndPassword == null) {
        	return null;
        }
        return new String(myUsernameAndPassword.getPassword());
	}
	
	private static PasswordCredential retrievePasswordCredential(String pCredentialKey) {
		//validateCredential(pCredentialKey);
        PasswordCredential myUsernameAndPassword = 
        		sCsf.privilegedRetrievePasswordCredential(
        				sMapName, pCredentialKey);
        
        return myUsernameAndPassword;
	}
	
	public static String getKeystoreTypeKey() {
        return getValueFromGenericCredential(KEYSTORE_TYPE_KEY);
	}
	
	private static String getValueFromGenericCredential(String pCredentialKey) {
		GenericCredential myRecipientKeyAlias = sCsf.privilegedRetrieveGenericCredential(sMapName, pCredentialKey);
        if(myRecipientKeyAlias == null) {
        	return null;
        }
        return myRecipientKeyAlias.getCredential().toString();
	}
}
