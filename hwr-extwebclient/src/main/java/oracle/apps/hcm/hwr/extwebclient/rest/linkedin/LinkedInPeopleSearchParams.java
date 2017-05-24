/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.linkedin;

/**
 * @author Yenal Kal
 */
public class LinkedInPeopleSearchParams {

    public static enum SortMethod {

        CONNECTIONS("connections"), RECOMMENDERS("recommenders"), DISTANCE("distance"), RELEVANCE(
            "relevance");

        /** The name. */
        private final String mName;

        SortMethod(String pName) {
            mName = pName;
        }

        /**
         * @return the mName
         */
        public String getName() {
            return mName;
        }
    }

    /** 0 based start index */
    private int mStartIndex;

    /** Count per page */
    private int mCountPerPage;

    /** Sort method */
    private SortMethod mSortMethod;

    /** Keywords */
    private String mKeywords;

    /** First name */
    private String mFirstName;

    /** Last name */
    private String mLastName;

    /** Company name */
    private String mCompanyName;

    /** Current company */
    private Boolean mCurrentCompany;

    /** Title */
    private String mTitle;

    /** Current title */
    private Boolean mCurrentTitle;

    /** School */
    private String mSchool;

    /** Current school */
    private Boolean mCurrentSchool;

    /** Country code */
    private String mCountryCode;

    /** Postal code */
    private String mPostalCode;

    /** Distance in miles */
    private Integer mDistance;

    public LinkedInPeopleSearchParams() {
        mSortMethod = SortMethod.RELEVANCE;
        mCountPerPage = 25;
    }

    /**
     * @return the mStartIndex
     */
    public int getStartIndex() {
        return mStartIndex;
    }

    /**
     * @param mStartIndex the mStartIndex to set
     */
    public void setStartIndex(int mStartIndex) {
        this.mStartIndex = mStartIndex;
    }

    /**
     * @return the mCountPerPage
     */
    public int getCountPerPage() {
        return mCountPerPage;
    }

    /**
     * @param mCountPerPage the mCountPerPage to set
     */
    public void setCountPerPage(int pCountPerPage) {

        if (mCountPerPage < 1 || mCountPerPage > 25) {
            throw new IllegalArgumentException("pCountPerPage must be between 1 and 25.");
        }

        mCountPerPage = pCountPerPage;
    }

    /**
     * @return the mSortMethod
     */
    public SortMethod getSortMethod() {
        return mSortMethod;
    }

    /**
     * @param mSortMethod the mSortMethod to set
     */
    public void setSortMethod(SortMethod mSortMethod) {
        this.mSortMethod = mSortMethod;
    }

    /**
     * @return the mKeywords
     */
    public String getKeywords() {
        return mKeywords;
    }

    /**
     * @param mKeywords the mKeywords to set
     */
    public void setKeywords(String mKeywords) {
        this.mKeywords = mKeywords;
    }

    /**
     * @return the mFirstName
     */
    public String getFirstName() {
        return mFirstName;
    }

    /**
     * @param mFirstName the mFirstName to set
     */
    public void setFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    /**
     * @return the mLastName
     */
    public String getLastName() {
        return mLastName;
    }

    /**
     * @param mLastName the mLastName to set
     */
    public void setLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    /**
     * @return the mCompanyName
     */
    public String getCompanyName() {
        return mCompanyName;
    }

    /**
     * @param mCompanyName the mCompanyName to set
     */
    public void setCompanyName(String mCompanyName) {
        this.mCompanyName = mCompanyName;
    }

    /**
     * @return the mCurrentCompany
     */
    public Boolean getCurrentCompany() {
        return mCurrentCompany;
    }

    /**
     * @param mCurrentCompany the mCurrentCompany to set
     */
    public void setCurrentCompany(Boolean mCurrentCompany) {
        this.mCurrentCompany = mCurrentCompany;
    }

    /**
     * @return the mTitle
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * @param mTitle the mTitle to set
     */
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * @return the mCurrentTitle
     */
    public Boolean getCurrentTitle() {
        return mCurrentTitle;
    }

    /**
     * @param mCurrentTitle the mCurrentTitle to set
     */
    public void setCurrentTitle(Boolean mCurrentTitle) {
        this.mCurrentTitle = mCurrentTitle;
    }

    /**
     * @return the mSchool
     */
    public String getSchool() {
        return mSchool;
    }

    /**
     * @param mSchool the mSchool to set
     */
    public void setSchool(String mSchool) {
        this.mSchool = mSchool;
    }

    /**
     * @return the mCurrentSchool
     */
    public Boolean getCurrentSchool() {
        return mCurrentSchool;
    }

    /**
     * @param mCurrentSchool the mCurrentSchool to set
     */
    public void setCurrentSchool(Boolean mCurrentSchool) {
        this.mCurrentSchool = mCurrentSchool;
    }

    /**
     * @return the mCountryCode
     */
    public String getCountryCode() {
        return mCountryCode;
    }

    /**
     * @param mCountryCode the mCountryCode to set
     */
    public void setCountryCode(String mCountryCode) {
        this.mCountryCode = mCountryCode;
    }

    /**
     * @return the mPostalCode
     */
    public String getPostalCode() {
        return mPostalCode;
    }

    /**
     * @param mPostalCode the mPostalCode to set
     */
    public void setPostalCode(String mPostalCode) {
        this.mPostalCode = mPostalCode;
    }

    /**
     * @return the mDistance
     */
    public Integer getDistance() {
        return mDistance;
    }

    /**
     * @param mDistance the mDistance to set
     */
    public void setDistance(Integer mDistance) {
        this.mDistance = mDistance;
    }
}
