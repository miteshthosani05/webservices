package oracle.apps.hcm.hwr.csf;

import java.security.AccessController;
import java.security.PrivilegedAction;

import oracle.security.jps.JpsException;
import oracle.security.jps.service.JpsServiceLocator;
import oracle.security.jps.service.credstore.CredentialStore;
import oracle.security.jps.service.credstore.GenericCredential;
import oracle.security.jps.service.credstore.PasswordCredential;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 
public class CsfCredentialRetriever {
    
    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(CsfCredentialRetriever.class);
    
	final CredentialStore mStore;
	private PasswordCredential mPasswordCredential;
	private GenericCredential mGenericCredential;
	
    public CsfCredentialRetriever() {
        super();
        try {
	        final CredentialStore myStore =
	                JpsServiceLocator.getServiceLocator()
	                .lookup(CredentialStore.class);
	        this.mStore = myStore;
        } catch (JpsException e) {
            LOGGER.error(e);
            throw new RuntimeException("Failed to retrieve credential from the key store.", e);
        }
    }
    
    /**
     * Since this method performs a privileged operation, only current class or
     * jar containing this class needs CredentialAccessPermission
     * @param pMapName, pKeyName: See http://docs.oracle.com/cd/E17904_01/core.1111/e10043/devcsf.htm
     */
    public PasswordCredential privilegedRetrievePasswordCredential(String pMapName, String pKeyName) {
    	final String myMapName = pMapName;
    	final String myKeyName = pKeyName;
    	mPasswordCredential = null;
    	
        AccessController.doPrivileged(new PrivilegedAction<String>() {
        	
            public String run() {
                retrievePasswordCredential(myMapName, myKeyName);
                return "done";
            }
            
        });
        
        if(mPasswordCredential == null) {
        	LOGGER.warn("Credential '" + pKeyName + "' not available in map '" + pMapName + "'.");
        }
        
        return mPasswordCredential;
    }
    
    private void retrievePasswordCredential(String pMapName, String pKeyName) {
        try {
            // this call requires read privilege
        	mPasswordCredential = (PasswordCredential)mStore.getCredential(pMapName, pKeyName);
        } catch (JpsException e) {
            LOGGER.error(e);
        }
    }
    
    public GenericCredential privilegedRetrieveGenericCredential(String pMapName, String pKeyName) {
    	final String myMapName = pMapName;
    	final String myKeyName = pKeyName;
    	mGenericCredential = null;
    	
        AccessController.doPrivileged(new PrivilegedAction<String>() {
        	
            public String run() {
                retrieveGenericCredential(myMapName, myKeyName);
                return "done";
            }
            
        });
        
        if(mGenericCredential == null) {
        	LOGGER.warn("Credential '" + pKeyName + "' not available in map '" + pMapName + "'.");
        }
        
        return mGenericCredential;
    }
    
    private void retrieveGenericCredential(String pMapName, String pKeyName) {
        try {
            // this call requires read privilege
        	mGenericCredential = (GenericCredential)mStore.getCredential(pMapName, pKeyName);
        } catch (JpsException e) {
            LOGGER.error(e);
        }
    }
}
