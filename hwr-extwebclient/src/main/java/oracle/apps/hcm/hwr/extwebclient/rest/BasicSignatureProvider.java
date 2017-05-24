/*******************************************************************************
 * Copyright 2013, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.extwebclient.rest;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpRequest;

/**
 * Provides Basic HTTP Authorization for Jive API
 * 
 * @author Tom Snyder
 * @author Ibtisam Haque
 * 
 */
public class BasicSignatureProvider implements IRestQuerySignatureProvider {

    public static final String mBasicAuthKey 	= "Authorization";
    public final String mBase64EncodedString;
    
    public BasicSignatureProvider(String pUsername, String pPassword) {
        String myUsernamePass 	= (pUsername != null?pUsername.trim():pUsername) + ":" +(pPassword != null?pPassword.trim():pPassword);
        mBase64EncodedString 	= "Basic " + getBase64EncodedString(myUsernamePass);
    }
    
    @Override
    public void signRequest(HttpRequest pRequest) throws RestQueryEngineException {
        pRequest.addHeader(mBasicAuthKey, mBase64EncodedString);
    }
    
    

    private String getBase64EncodedString(String pString) {
        return new String(new Base64().encode(pString.getBytes()));
    }
    
    /**
     * not supported
     */
    @Override
    public void setParam(String pKey, String pValue) {}


    /**
     * not supported
     */
    @Override
    public void removeParam(String pKey) {}

    
    
}
