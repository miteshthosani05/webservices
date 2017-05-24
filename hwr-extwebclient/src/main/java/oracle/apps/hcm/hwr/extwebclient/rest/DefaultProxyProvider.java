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
 * @author Yenal Kal
 */
 
 /** Make singleton to get around jaudit errors */
public class DefaultProxyProvider implements IProxyProvider {

    /**
     * Returns the HTTP proxy information if one is available.
     * @return Returns the HTTP proxy information if one is available. If not returns null.
     */
    protected ProxyInfo getHttpProxy() {
        String myProxySet = System.getProperty("http.proxySet");

        if (myProxySet != null && myProxySet.equalsIgnoreCase("true")) {
            String myHost = System.getProperty("http.proxyHost");
            String myPort = System.getProperty("http.proxyPort");
            String myUsername = System.getProperty("http.proxyUser");
            String myPassword = System.getProperty("http.proxyPassword");

            if (myHost != null && !myHost.isEmpty()) {
                if (myPort == null || myPort.isEmpty()) {
                    myPort = "80";
                }

                return new ProxyInfo(myHost, Integer.parseInt(myPort), myUsername, myPassword);
            }
        }

        return null;
    }

    /**
     * Returns the HTTPS proxy information if one is available.
     * @return Returns the HTTPS proxy information if one is available. If not returns null.
     */
    protected ProxyInfo getHttpsProxy() {
        String myProxySet = System.getProperty("https.proxySet");

        if (myProxySet != null && myProxySet.equalsIgnoreCase("true")) {
            String myHost = System.getProperty("https.proxyHost");
            String myPort = System.getProperty("https.proxyPort");
            String myUsername = System.getProperty("https.proxyUser");
            String myPassword = System.getProperty("https.proxyPassword");

            if (myHost != null && !myHost.isEmpty()) {
                if (myPort == null || myPort.isEmpty()) {
                    myPort = "443";
                }

                return new ProxyInfo(myHost, Integer.parseInt(myPort), myUsername, myPassword);
            }
        }

        return null;
    }

    /*
     * (non-Javadoc)
     * @see
     * oracle.apps.hcm.hwr.extwebclient.rest.IProxyProvider#getProxy(oracle.apps.hcm.hwr.extwebclient
     * .rest.ProxyType)
     */
    @Override
    public ProxyInfo getProxy(ProxyType pProxyType) {
        if (pProxyType == ProxyType.HTTPS) {

            ProxyInfo myHttpsProxyInfo = getHttpsProxy();

            if (myHttpsProxyInfo == null) {
                return getHttpProxy();
            } else {
                return myHttpsProxyInfo;
            }

        } else if (pProxyType == ProxyType.HTTP) {
            return getHttpProxy();
        }

        return null;
    }

}
