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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Connection is always part of 'fields' HTTP Params and indicates other large part of data that is
 * connected to main data. It could be comments or likes that are connected with particular post. We
 * can also specify {@link Modifier} to connections <br />
 * Example: <br />
 * /me/feed?fields=id,likes.summary(true).limit(5)
 * @author pglogows
 */
public final class FacebookConnection {

    private final String mName;

    private final List<Modifier> mModifiers = new ArrayList<Modifier>();

    private final List<Field> mFields = new ArrayList<Field>();

    public FacebookConnection(final String mName) {
        this.mName = mName;
    }

    public FacebookConnection withModifiers(final Modifier... pModifier) {
        mModifiers.addAll(Arrays.asList(pModifier));
        return this;
    }

    public FacebookConnection withFields(final Field... pField) {
        mFields.addAll(Arrays.asList(pField));
        return this;
    }

    public String name() {
        return mName;
    }

    @Override
    public String toString() {
        final StringBuilder myResult = new StringBuilder(mName);
        for (final Modifier myModifier : mModifiers) {
            myResult.append('.');
            myResult.append(myModifier.name());
            myResult.append('(');
            myResult.append(myModifier.value());
            myResult.append(')');
        }
        if (!mFields.isEmpty()) {
            myResult.append('{');
            myResult.append(StringUtils.join(mFields.iterator(), ','));
            myResult.append('}');
        }

        return myResult.toString();
    }

}
