/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/

package oracle.apps.hcm.hwr.ws.client.common.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import oracle.apps.hcm.hwr.domain.webservices.WebservicesResult;
import oracle.apps.hcm.hwr.domain.webservices.serializer.WebservicesResultSerializer;

/**
 * @author tgsnyder
 *
 */
public class ResultFormatting {

    public static String exceptionToString(Throwable myException) {
        StringWriter myString = new StringWriter();
        myException.printStackTrace(new PrintWriter(myString));
        return myString.toString();
    }
    
    public static String serializeWebserviceResultObject(WebservicesResult pResult) {
        return new WebservicesResultSerializer().serialize(pResult);
    }
    
    public static WebservicesResult deserializeWebserviceResultObject(String pResult) {
        return new WebservicesResultSerializer().deserialize(pResult);
    }
}
