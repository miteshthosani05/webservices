/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

/**
 * Contains information about the proxy.
 * @author Yenal Kal
 */
public class ProxyInfo {

    /** The host */
    private String mHost;

    /** The port */
    private int mPort;

    /** The username used for authentication (optional) */
    private String mUsername;

    /** The password used for authentication (optional) */
    private String mPassword;

    public ProxyInfo(String pHost, int pPort) {
        this(pHost, pPort, null, null);
    }

    /**
     * Creates a ProxyInfo instance.
     * @param pHost The host.
     * @param pPort The port.
     * @param pUsername The username.
     * @param pPassword The password.
     */
    public ProxyInfo(String pHost, int pPort, String pUsername, String pPassword) {
        mHost = pHost;
        mPort = pPort;
        mUsername = pUsername;
        mPassword = pPassword;
    }

    /**
     * @return the mHost
     */
    public String getHost() {
        return mHost;
    }

    /**
     * @return the mPort
     */
    public int getPort() {
        return mPort;
    }

    /**
     * @return the mUsername
     */
    public String getUsername() {
        return mUsername;
    }

    /**
     * @return the mPassword
     */
    public String getPassword() {
        return mPassword;
    }

}
