/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.facebook;

import java.util.Date;

/**
 * Object that represent time frame using to retrieve posts, comments, likes. It contains FROM and
 * TO values as seconds from Epoch.
 * @author pglogows
 */
public class TimeFrame {

    public static final long NOT_SPECIFIED = 0L;
    
    /**
     * Time from when we want to search posts in second
     */
    private final long mFrom;

    /**
     * Time to which we want to search posts in second
     */
    private final long mTo;

    public TimeFrame(final Date pFrom, final Date pTo) {
        this.mFrom = pFrom == null ? NOT_SPECIFIED : pFrom.getTime() / 1000;
        this.mTo = pTo == null ? NOT_SPECIFIED : pTo.getTime() / 1000;
    }

    public TimeFrame(long pFrom, long pTo) {
        this.mFrom = pFrom;
        this.mTo = pTo;
    }

    public static TimeFrame notSpecified() {
        return new TimeFrame(NOT_SPECIFIED, NOT_SPECIFIED);
    }

    public long from() {
        return mFrom;
    }

    public long to() {
        return mTo;
    }

}
