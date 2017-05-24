/*************************************************************************************************
 * Copyright 2015, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 *************************************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.facebook.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Fields needed to publish a post according to:
 * https://developers.facebook.com/docs/graph-api/reference/v2.2/user/feed#publish . Some fields are
 * not used, therefore not a part of this class. This data type is immutable and constructed
 * Builder.
 * @author Krzysztof Nirski - krzysztof.nirski@oracle.com
 */
public class Post {

    private final String mMessage;

    private final String mLink;

    /**
     * Custom preview picture for the link.
     */
    private final String mLinkPicture;

    /**
     * Custom Link title.
     */
    private final String mLinkName;

    /**
     * Custom link caption.
     */
    private final String mLinkCaption;

    /**
     * Custom link description;
     */
    private final String mLinkDescription;

    private final String mPlace;

    private final String[] mTags;

    private Post(final String mMessage, final String mLink, final String mLinkPicture, final String mLinkName, final String mLinkCaption,
        final String mLinkDescription, final String mPlace, final String[] mTags) {
        this.mMessage = mMessage;
        this.mLink = mLink;
        this.mLinkPicture = mLinkPicture;
        this.mLinkName = mLinkName;
        this.mLinkCaption = mLinkCaption;
        this.mLinkDescription = mLinkDescription;
        this.mPlace = mPlace;
        this.mTags = mTags;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getLink() {
        return mLink;
    }

    public String getLinkPicture() {
        return mLinkPicture;
    }

    public String getLinkName() {
        return mLinkName;
    }

    public String getLinkCaption() {
        return mLinkCaption;
    }

    public String getLinkDescription() {
        return mLinkDescription;
    }

    public String getPlace() {
        return mPlace;
    }

    public String[] getTags() {
        return mTags;
    }

    /**
     * Builder class for creating immutable Post instances.
     * @author Krzysztof Nirski - krzysztof.nirski@oracle.com
     */
    public static class PostBuilder {

        private String mMessage;

        private String mLink;

        private String mLinkPicture;

        private String mLinkName;

        private String mLinkCaption;

        private String mLinkDescription;

        private String mPlace;

        private List<String> mTags;

        public PostBuilder message(final String pMessage) {
            mMessage = pMessage;
            return this;
        }

        public PostBuilder link(final String pLink) {
            mLink = pLink;
            return this;
        }

        public PostBuilder linkPicture(final String pLinkPicture) {
            mLinkPicture = pLinkPicture;
            return this;
        }

        public PostBuilder linkName(final String pLinkName) {
            mLinkName = pLinkName;
            return this;
        }

        public PostBuilder linkCaption(final String pLinkCaption) {
            mLinkCaption = pLinkCaption;
            return this;
        }

        public PostBuilder linkDescription(final String pLinkDescription) {
            mLinkDescription = pLinkDescription;
            return this;
        }

        public PostBuilder place(final String pPlace) {
            mPlace = pPlace;
            return this;
        }

        public PostBuilder addTag(final String pTag) {
            if (mTags == null) {
                mTags = new ArrayList<String>();
            }
            mTags.add(pTag);
            return this;
        }

        public Post build() {
            return new Post(mMessage, mLink, mLinkPicture, mLinkName, mLinkCaption, mLinkDescription, mPlace,
                mTags == null ? null : mTags.toArray(new String[0]));
        }

    }

}
