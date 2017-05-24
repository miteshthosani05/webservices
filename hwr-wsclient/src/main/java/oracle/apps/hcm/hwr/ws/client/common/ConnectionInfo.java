/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.ws.client.common;


/**
 * The Class ConnectionInfo.
 */
public class ConnectionInfo {
    
    /** Web services host name. */
    private String mHost = "";

    /** Web services port. */
    private int mPort = 0;

    /** Web services URL. */
    private String mServiceUrl = "";

    /** Web services username for authentication. */
    private String mUsername = "";

    /** Web services password for authentication. */
    private String mPassword = "";
    /** Web services protocol */
    private String mProtocol = "";
    
    
    /**
     * Instantiates a new connection info.
     *
     * @param pHost the host
     * @param pPort the port
     * @param pServiceUrl the service url
     * @param pUsername the username
     * @param pPassword the password
     */
    public ConnectionInfo(final String pHost, final int pPort,
        final String pServiceUrl, final String pUsername, 
        final String pPassword) {

        mHost = pHost;
        mPort = pPort;
        mServiceUrl = pServiceUrl;
        mUsername = pUsername;
        mPassword = pPassword;
    }

   
    
    
    
    
    
    /**
     * Gets the host.
     *
     * @return the host
     */
    public String getHost() {
        return mHost;
    }

    
    /**
     * Sets the host.
     *
     * @param pHost the new host
     */
    public void setHost(String pHost) {
        mHost = pHost;
    }

    
    /**
     * Gets the port.
     *
     * @return the port
     */
    public int getPort() {
        return mPort;
    }

    
    /**
     * Sets the port.
     *
     * @param pPort the new port
     */
    public void setPort(int pPort) {
        mPort = pPort;
    }

    
    /**
     * Gets the service url.
     *
     * @return the service url
     */
    public String getServiceUrl() {
        return mServiceUrl;
    }

    
    /**
     * Sets the service url.
     *
     * @param pServiceUrl the new service url
     */
    public void setServiceUrl(String pServiceUrl) {
        mServiceUrl = pServiceUrl;
    }

    
    /**
     * Gets the username.
     *
     * @return the username
     */
    public String getUsername() {
        return mUsername;
    }

    
    /**
     * Sets the username.
     *
     * @param pUsername the new username
     */
    public void setUsername(String pUsername) {
        mUsername = pUsername;
    }

    
    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return mPassword;
    }

    
    /**
     * Sets the password.
     *
     * @param pPassword the new password
     */
    public void setPassword(String pPassword) {
        this.mPassword = pPassword;
    }
    /**
     * Gets the protocol
     * 
     * @return the protocol
     */
    public String getProtocol() {
    	return mProtocol;
    	
    }
    
    /**
     * Sets the protocol
     */
    public void setProtocol(final String pProtocol) {
    	mProtocol = pProtocol;
    }
    
    /**
     * Determines if a protocol has been set.
     * 
     * @return true if protocol has been set, false if it's the default.
     */
    public boolean hasProtocol() {
        return !mProtocol.equals("");
    }
    
}
