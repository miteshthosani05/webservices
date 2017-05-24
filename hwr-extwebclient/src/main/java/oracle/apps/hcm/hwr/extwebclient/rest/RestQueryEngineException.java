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
public class RestQueryEngineException extends RuntimeException {

    /** Required for serialization */
    private static final long serialVersionUID = -1345160218302170728L;

    /**
     * Instantiates a new RestQueryEngineException.
     * @param pMessage the message
     */
    public RestQueryEngineException(final String pMessage) {
        super(pMessage);
    }

    /**
     * Instantiates a new RestQueryEngineException.
     * @param pCause the cause
     */
    public RestQueryEngineException(final Throwable pCause) {
        super(pCause);
    }

    /**
     * Instantiates a new RestQueryEngineException.
     * @param pMessage the message
     * @param pCause the cause
     */
    public RestQueryEngineException(final String pMessage, final Throwable pCause) {
        super(pMessage, pCause);
    }
}
