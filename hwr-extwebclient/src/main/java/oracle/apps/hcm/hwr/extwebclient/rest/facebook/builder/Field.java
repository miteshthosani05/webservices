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
 * Facebook graph api allows to retrieve objects ( like post or friend ), but sometimes we do not
 * want all data. We want to limit the data so that we could limit amount of retrieved data. Thats
 * what field is about. We could specify only the field we want to retrieve. We use comma separated
 * Field names as value of 'fields' param in HTTP request. Enum represents that field.
 * @author pglogows
 */
public enum Field {
        ID,
        FROM,
        MESSAGE,
        DESCRIPTION,
        CREATED_TIME,
        MESSAGE_TAGS,
        TYPE,
        STORY,
        STORY_TAGS,
        SHARES,
        NAME,
        LIKE_COUNT,
        LOCATION,
        PICTURE,
        WEBSITE,
        USERNAME,
        BIO,
        EMAIL,
        GENDER,
        FIRST_NAME,
        LAST_NAME,
        MIDDLE_NAME,
        BIRTHDAY,
        EDUCATION,
        LANGUAGES,
        WORK,
        // group specific
        ICON,
        OWNER,
        PRIVACY,
        UPDATED_TIME;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
