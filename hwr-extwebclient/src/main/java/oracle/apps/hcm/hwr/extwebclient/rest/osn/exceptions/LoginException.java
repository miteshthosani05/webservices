/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.osn.exceptions;

/**
 * @author Yiwei Zhang
 */
public class LoginException extends RuntimeException {

	/** Required for serialization */
	private static final long serialVersionUID = -5231476766097690284L;

	/**
     * Instantiates a new LoginException.
     * @param pMessage the message
     */
    public LoginException(final String pMessage) {
        super(pMessage);
    }

	/**
     * Instantiates a new LoginException.
     * @param pCause the cause
     */
    public LoginException(final Throwable pCause) {
        super(pCause);
    }
    
    /**
     * Instantiates a new LoginException.
     * @param pMessage the message
     * @param pCause the cause
     */
    public LoginException(final String pMessage, final Throwable pCause) {
        super(pMessage, pCause);
    }
}

