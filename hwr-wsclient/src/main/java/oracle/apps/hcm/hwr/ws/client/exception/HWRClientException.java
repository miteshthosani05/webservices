/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.ws.client.exception;


/**
 * The Class HWRClientException.
 */
public class HWRClientException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    /**
     * Instantiates a new hWR client exception.
     */
    public HWRClientException() {
        super();
      }

      /**
       * Instantiates a new hWR client exception.
       *
       * @param message the message
       * @param chainedException the chained exception
       */
      public HWRClientException(String message, Throwable chainedException) {
        super(message, chainedException);
      }

      /**
       * Instantiates a new hWR client exception.
       *
       * @param message the message
       */
      public HWRClientException(String message) {
        super(message);
      }

      /**
       * Instantiates a new hWR client exception.
       *
       * @param chainedException the chained exception
       */
      public HWRClientException(Throwable chainedException) {
        super(chainedException);
      }

      
      
}
