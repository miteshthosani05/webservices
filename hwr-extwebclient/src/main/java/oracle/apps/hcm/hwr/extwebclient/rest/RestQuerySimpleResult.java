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
 * @author Ibtisam Haque
 */
public final class RestQuerySimpleResult extends AbstractRestQueryResult {

    /** The response */
    private String mResponse;

    /**
     * Constructs a result object
     * @param pStatusCode The HTTP status code e.g. 200
     * @param pResponse The response string
     */
    public RestQuerySimpleResult(int pStatusCode, String pResponse, long pResponseSize) {
        super(pStatusCode, pResponseSize);
        mResponse = pResponse;
    }

    /**
     * @return the mResponse
     */
    public String getResponse() {
        return mResponse;
    }

    public void trimMResponseFirstOccuranceOnly(String stringToTrim, String stringToReplaceWith) {
        if ((this.mResponse != null) && (this.mResponse.trim().length() != 0) && (stringToTrim != null)
            && (stringToTrim.trim().length() != 0)) {
            if (super.getResponseSize() >= stringToTrim.length()) {
                this.mResponse = this.mResponse.replaceFirst(stringToTrim, stringToReplaceWith);
                this.mResponse = this.mResponse.trim();

            }
        }

    }

    @Override
    public String toString() {
        return "RestQuerySimpleResult [" + "mResponseSize=" + super.getResponseSize() + ", mStatusCode="
            + super.getStatusCode() + ", mResponse=" + mResponse + "]";
    }

}
