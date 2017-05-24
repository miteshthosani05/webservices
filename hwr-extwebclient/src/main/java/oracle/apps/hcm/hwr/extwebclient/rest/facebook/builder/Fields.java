/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder;

import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.BIO;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.BIRTHDAY;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.CREATED_TIME;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.DESCRIPTION;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.EDUCATION;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.EMAIL;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.FIRST_NAME;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.FROM;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.GENDER;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.ICON;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.ID;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.LANGUAGES;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.LAST_NAME;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.LIKE_COUNT;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.LOCATION;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.MESSAGE;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.MESSAGE_TAGS;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.MIDDLE_NAME;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.NAME;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.OWNER;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.PICTURE;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.PRIVACY;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.SHARES;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.STORY;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.STORY_TAGS;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.TYPE;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.UPDATED_TIME;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.WEBSITE;
import static oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder.Field.WORK;

/**
 * Static factory class to tables of {@link Field}.
 * @author pglogows
 */
public final class Fields {

    private static final Field[] POST = { ID, FROM, MESSAGE, DESCRIPTION, CREATED_TIME, MESSAGE_TAGS, TYPE, SHARES,
        STORY, STORY_TAGS };

    private static final Field[] PROFILE = { ID, LOCATION, NAME, PICTURE, WEBSITE, BIO, EMAIL, GENDER, FIRST_NAME,
        LAST_NAME, MIDDLE_NAME, BIRTHDAY, EDUCATION, LANGUAGES, WORK, UPDATED_TIME };

    private static final Field[] ID_AND_NAME = { ID, NAME };

    private static final Field[] GROUP = { ID, EMAIL, ICON, NAME, OWNER, PRIVACY, UPDATED_TIME };

    private static final Field[] COMMENT = { ID, FROM, MESSAGE, MESSAGE_TAGS, CREATED_TIME, LIKE_COUNT };

    private Fields() {

    }

    public static Field[] forPosts() {
        return POST;
    }

    public static Field[] forProfile() {
        return PROFILE;
    }

    public static Field[] forProfileLight() {
        return ID_AND_NAME;
    }

    public static Field[] forGroup() {
        return GROUP;
    }

    public static Field[] forComments() {
        return COMMENT;
    }

    public static Field[] forLikes() {
        return ID_AND_NAME;
    }
}
