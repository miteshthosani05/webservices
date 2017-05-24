package oracle.apps.hcm.hwr.csf;

public class CredentialNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private CredentialNotFoundException(String message) {
		super(message);
	}
	
	public static CredentialNotFoundException getInstance(String pMapName, String pConfigKey, String pKeyName) {
		String message = "";
		
		if(pKeyName == null || pKeyName.equals("")) {
			message = "The Configuration Parameter '" + pConfigKey +
					"' is not set in the configuration file of the White Label App. " +
					"Please set that parameter to the name of the key in the Weblogic key store map '" + 
					pMapName + "'.";
		} else {
			message = "The Credential in the map '" +
					pMapName + "' with the key '" +
					pKeyName + "' could not be retrieved. Make sure the Configuration Parameter '" +
					pConfigKey + "' in the White Label App is set to the right key in the Weblogic key store.";
		}
		
		return new CredentialNotFoundException(message);
	}
}
