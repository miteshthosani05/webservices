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

import oracle.apps.hcm.hwr.extwebclient.rest.query.QueryConfig;

import org.apache.commons.lang.StringUtils;

public class FacebookQueryConfigBuilder {

    private String mUrlPostfix;

    private String mPostBody;

    private String mAuthToken;

    private List<Field> mFields = new ArrayList<Field>();

    private List<Modifier> mModifiers = new ArrayList<Modifier>();

    private List<FacebookConnection> mConnections = new ArrayList<FacebookConnection>();

    private FacebookQueryConfigBuilder() {
        // User static method to ceate it.
    }

    public static FacebookQueryConfigBuilder get() {
        return new FacebookQueryConfigBuilder();
    }

    public FacebookQueryConfigBuilder withFields(final Field... pFields) {
        mFields = Arrays.asList(pFields);
        return this;
    }

    public FacebookQueryConfigBuilder withModifiers(final Modifier... pModifiers) {
        mModifiers = Arrays.asList(pModifiers);
        return this;
    }

    public FacebookQueryConfigBuilder withConnections(final FacebookConnection... pConnections) {
        mConnections = Arrays.asList(pConnections);
        return this;
    }

    public FacebookQueryConfigBuilder withUrlPostfix(final String pUrlPostfix) {
        mUrlPostfix = pUrlPostfix;
        return this;
    }

    public FacebookQueryConfigBuilder withPostBody(final String pPostBody) {
        mPostBody = pPostBody;
        return this;
    }

    public FacebookQueryConfigBuilder withAuthToken(final String pAuthToken) {
        mAuthToken = pAuthToken;
        return this;
    }

    public QueryConfig buildQueryConfig() {
        final QueryConfig pQueryConfig = new QueryConfig(mUrlPostfix, mPostBody);
        if (StringUtils.isNotBlank(mAuthToken)) {
            pQueryConfig.addGetParam("access_token", mAuthToken);
        }

        final StringBuilder myFields = new StringBuilder(StringUtils.join(mFields.iterator(), ','));
        if (!mConnections.isEmpty()) {
            myFields.append(',');
            myFields.append(StringUtils.join(mConnections.iterator(), ','));
        }

        pQueryConfig.addGetParam("fields", myFields.toString());
        for (final Modifier myModifier : mModifiers) {
            pQueryConfig.addGetParam(myModifier.name(), myModifier.value());
        }

        return pQueryConfig;
    }

}
