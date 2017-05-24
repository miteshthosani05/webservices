/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder;

/**
 * In facebook graph api it allows to modify result. Using modifiers we could limit result(param
 * 'limit'), get results from certain time frame ('since' and 'until' ), search query and more. We
 * need to pass it as HTTP GET params.
 * @author pglogows
 */
public class Modifier {

    public static final int LIMIT_NOT_SPECIFIED = 0;

	//default in API v2.3 is 25 per request, specify more than that.
    public static final int DEFAULT_FB_POST_NUMBER = 500;
	
    private final String mName;

    private final String mValue;

    public Modifier(String mName, String mValue) {
        this.mName = mName;
        this.mValue = mValue;
    }

    public String name() {
        return mName;
    }

    public String value() {
        return mValue;
    }

    public static Modifier limit(int pLimit) {
        return new Modifier("limit", String.valueOf(pLimit));
    }

    public static Modifier until(long pTimeInSeconds) {
        return new Modifier("until", String.valueOf(pTimeInSeconds));
    }

    public static Modifier since(long pTimeInSeconds) {
        return new Modifier("since", String.valueOf(pTimeInSeconds));
    }

    public static Modifier summary(boolean pWithSummary) {
        return new Modifier("summary", String.valueOf(pWithSummary));
    }

    public static Modifier type(String ptype) {
        return new Modifier("type", ptype);
    }

    public static Modifier query(String pQuery) {
        return new Modifier("q", pQuery);
    }
    
    public static Modifier dateFormat(String pDateFormat) {
        return new Modifier("date_format", pDateFormat);
    }

}
