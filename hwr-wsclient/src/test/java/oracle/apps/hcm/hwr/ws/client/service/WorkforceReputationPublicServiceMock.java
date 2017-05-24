package oracle.apps.hcm.hwr.ws.client.service;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import oracle.apps.hcm.hwr.ws.client.WorkforceReputationPublicServiceImpl;
import oracle.webservices.ClientConstants;

public class WorkforceReputationPublicServiceMock extends WorkforceReputationPublicServiceImpl {
	
	private String mKeystoreLocation;
	private String mUserName;
	private String mPassword;
	private String mKeystorePassword;
	private String mKeystoreType;
	
	private WorkforceReputationPublicServiceMock(Builder builder) {
		super();
		mKeystoreLocation = builder.mKeystoreLocation;
		mUserName = builder.mUserName;
		mPassword = builder.mPassword;
		mKeystorePassword = builder.mKeystorePassword;
		mKeystoreType = builder.mKeystoreType;
	}

	@Override
	protected void setupRequestContext() {
		Map<String, Object> reqContext = getReqContext();

		reqContext.put(BindingProvider.USERNAME_PROPERTY, mUserName);
		reqContext.put(BindingProvider.PASSWORD_PROPERTY, mPassword);
		reqContext.put(ClientConstants.WSSEC_KEYSTORE_TYPE, mKeystoreType);
		reqContext.put(ClientConstants.WSSEC_KEYSTORE_LOCATION, mKeystoreLocation);
		reqContext.put(ClientConstants.WSSEC_KEYSTORE_PASSWORD, mKeystorePassword);
	}
	
	public static class Builder {
		private String mKeystoreLocation;
		private String mUserName = "APPLICATION_IMPLEMENTATION_CONSULTANT";
		private String mPassword = "Welcome1";
		private String mKeystorePassword = "Welcome1";
		private String mKeystoreType = "JKS";
		
		public Builder(String pKeystoreLocation) {
			mKeystoreLocation = pKeystoreLocation;
		}
		
		public Builder setUserName(String pUserName) {
			mUserName = pUserName;
			return this;
		}
		
		public Builder setPassword(String pPassword) {
			mPassword = pPassword;
			return this;
		}
		
		public Builder setKeystorePassword(String pKeystorePassword) {
			mKeystorePassword = pKeystorePassword;
			return this;
		}
		
		public Builder setKeystoreType(String pKeystoreType) {
			mKeystoreType = pKeystoreType;
			return this;
		}
		
		public WorkforceReputationPublicServiceMock build() {
			return new WorkforceReputationPublicServiceMock(this);
		}
	}
}
