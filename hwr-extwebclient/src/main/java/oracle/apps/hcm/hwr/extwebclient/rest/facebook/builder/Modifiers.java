/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder;

import java.util.ArrayList;
import java.util.List;

import oracle.apps.hcm.hwr.extwebclient.rest.facebook.TimeFrame;

/**
 * Static factory to create tables of {@link Modifier}
 * @author pglogows
 */
public final class Modifiers {

    /**
     * WRM handles dates as timestamps. "U" here means unix time date format, which is easier for us
     * to handle, so we are setting this as a default. Reference for values:
     * http://www.php.net/manual/en/function.date.php#refsect1-function.date-parameters
     */
    private static final Modifier DEFAULT_DATE_FORMAT = Modifier.dateFormat("U");

    private Modifiers() {

    }

    public static Modifier[] forPosts(int pLimit, TimeFrame pDuration) {
        return createModifiers(pLimit, pDuration, false);
    }

    public static Modifier[] forComments() {
        return createModifiers(Modifier.LIMIT_NOT_SPECIFIED, TimeFrame.notSpecified(), true);
    }

    public static Modifier[] forLikes() {
        return createModifiers(Modifier.LIMIT_NOT_SPECIFIED, TimeFrame.notSpecified(), false);
    }

    private static Modifier[] createModifiers(int pLimit, TimeFrame pDuration, boolean pSummary) {
        List<Modifier> myResult = new ArrayList<Modifier>();
        myResult.add(DEFAULT_DATE_FORMAT);
        if (pLimit == Modifier.LIMIT_NOT_SPECIFIED) {
            myResult.add(Modifier.limit(Modifier.DEFAULT_FB_POST_NUMBER));
        } else {
            myResult.add(Modifier.limit(pLimit));
        }

        long pSince = pDuration.from();
        if (pSince > 0) {
            myResult.add(Modifier.since(pSince));
        }

        long pTo = pDuration.to();
        if (pTo > 0) {
            myResult.add(Modifier.until(pTo));
        }

        if (pSummary) {
            myResult.add(Modifier.summary(true));
        }
        return myResult.toArray(new Modifier[0]);
    }

}
