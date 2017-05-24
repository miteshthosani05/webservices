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
 * Provides proxy information.
 * @author Yenal Kal
 */
public interface IProxyProvider {

    /**
     * Returns the proxy information if one is available
     * @param pProxyType The proxy type.
     * @return If a proxy is found for the given type a ProxyInfo instance is returned. Otherwise
     *         null is returned.
     */
    ProxyInfo getProxy(ProxyType pProxyType);
}
