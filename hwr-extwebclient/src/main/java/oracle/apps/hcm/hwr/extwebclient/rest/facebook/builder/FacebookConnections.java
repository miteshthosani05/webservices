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
 * Static factory to creates tables of {@link FacebookConnection}
 * @author pglogows
 */
public final class FacebookConnections {

    private FacebookConnections() {

    }

    public static FacebookConnection[] forPosts() {
        FacebookConnection myLikes = new FacebookConnection("likes");
        myLikes.withModifiers(Modifier.summary(true), Modifier.limit(1));
        
        FacebookConnection myComments = new FacebookConnection("comments");
        myComments.withModifiers(Modifier.summary(true), Modifier.limit(1));
        myComments.withFields(Field.ID);
        
        return new FacebookConnection[] { myLikes, myComments };
    }

    public static FacebookConnection forComments() {
        FacebookConnection myComments = new FacebookConnection("comments");
        myComments.withModifiers(Modifier.summary(true));
        myComments.withFields(Field.ID, Field.FROM, Field.MESSAGE, Field.MESSAGE_TAGS, Field.CREATED_TIME,
            Field.LIKE_COUNT);

        return myComments;
    }

    public static FacebookConnection[] forLikes() {
        return new FacebookConnection[] { new FacebookConnection("likes") };
    }

}
