/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

import java.util.HashMap;
import java.util.Map;

import oauth.signpost.OAuth;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.http.HttpParameters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;

/**
 * @author Yenal Kal
 */
public class OAuthSignatureProvider implements IRestQuerySignatureProvider {

    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(OAuthSignatureProvider.class);

    /** The consumer key */
    private String mConsumerKey;

    /** The consumer secret */
    private String mConsumerSecret;

    /** The list of parameters used by OAuth */
    Map<String, String> mParams = new HashMap<String, String>();

    /**
     * Constructs an OAuth signature provider
     * @param pConsumerKey The consumer key
     * @param pConsumerSecret The consumer secret
     */
    public OAuthSignatureProvider(String pConsumerKey, String pConsumerSecret) {
        mConsumerKey = pConsumerKey;
        mConsumerSecret = pConsumerSecret;
    }

    /*
     * (non-Javadoc)
     * @see
     * oracle.apps.grc.restserviceprovider.IRESTQuerySignatureProvider#addParam(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void setParam(String pKey, String pValue) {
        mParams.put(pKey, pValue);
    }

    /*
     * (non-Javadoc)
     * @see
     * oracle.apps.grc.restserviceprovider.IRESTQuerySignatureProvider#removeParam(java.lang.String)
     */
    @Override
    public void removeParam(String pKey) {
        mParams.remove(pKey);
    }

    /*
     * (non-Javadoc)
     * @see oracle.apps.grc.restserviceprovider.IRESTQuerySignatureProvider#signRequest(java.net.
     * HttpURLConnection)
     */
    @Override
    public void signRequest(HttpRequest pRequest) throws RestQueryEngineException {
        CommonsHttpOAuthConsumer myConsumer =
            new CommonsHttpOAuthConsumer(mConsumerKey, mConsumerSecret);

        String myAccessToken = mParams.get("accessToken");
        String myTokenSecret = mParams.get("tokenSecret");
        String myVerifier = mParams.get("verifier");
        String myOAuthCallback = mParams.get("oauth_callback");

        if (myAccessToken != null && myTokenSecret != null) {
            myConsumer.setTokenWithSecret(myAccessToken, myTokenSecret);
        }

        if (myVerifier != null || myOAuthCallback != null) {
            HttpParameters myParams = new HttpParameters();
            if (myVerifier != null) {
                myParams.put(OAuth.OAUTH_VERIFIER, myVerifier, true);
            }
            
            if (myOAuthCallback != null) {
                myParams.put("oauth_callback", myOAuthCallback, true);
            }
            
            
            myConsumer.setAdditionalParameters(myParams);
        }

        try {
            myConsumer.sign(pRequest);
        } catch (Throwable e) {
            String myExceptionMessage =
                "Failed to sign the HTTP request with the OAuthSignatureProvider";
            LOGGER.error(myExceptionMessage, e);
            throw new RestQueryEngineException(myExceptionMessage);
        }
    }

}
