/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery.Method;
import oracle.apps.hcm.hwr.extwebclient.rest.apiuse.ConnectorApiUseage;
import oracle.apps.hcm.hwr.extwebclient.rest.query.QueryConfig;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONValue;

/**
 * The engine used to execute RESTful service calls
 * @author Yenal Kal
 * @author Ibtisam Haque
 * @author pglogows
 */
public class RestQueryEngine {

    /** The logger */
    private static final Log LOGGER = LogFactory.getLog(RestQueryEngine.class);

    /** The content length of the last response */
    private long mLastResponseContentLength = 0;

    /** REST query provider */
    private IRestQueryProvider mQueryProvider;

    /** The signature provider */
    private IRestQuerySignatureProvider mSignatureProvider;

    /** The proxy provider */
    private static final IProxyProvider PROXY_PROVIDER = new DefaultProxyProvider();

    /** The validation methods */
    private IRestQueryResultValidation mResultvalidater = null;

    private ConnectorApiUseage mConnectorApiUseage = null;

    /** Should use proxy or not */
    private Boolean mUseProxy = true;

    /** All the REST API providers */
    public enum Provider {
        Facebook, LinkedIn, Xing, Twitter, Jive
    }

    public static final String ARRAY_PARAM_ELEMENT_SEPARATOR = "||";

    private static final String ARRAY_PARAM_ELEMENT_SEPARATOR_REGEX = "\\|\\|";

    private static final String IDENTIFY_JIVE_URL = "sandbox.jiveon";

    private static final String IDENTIFY_OSN_URL = "osnvm.osnexternal";

    private static final int JIVE_HTTPS_PORT = 443;

    /**
     * Constructs a REST query engine with the given query provider
     * @param pQueryProvider
     * @throws RestQueryEngineException Exception thrown if no provider is supplied
     */
    public RestQueryEngine(IRestQueryProvider pQueryProvider) {
        this(pQueryProvider, null);
    }

    /**
     * Constructs a REST query engine with the given query provider
     * @param pQueryProvider The query provider
     * @param pSignatureProvider The signature provider
     * @throws RestQueryEngineException Exception thrown if no provider is supplied
     */
    public RestQueryEngine(IRestQueryProvider pQueryProvider, IRestQuerySignatureProvider pSignatureProvider) {

        mQueryProvider = pQueryProvider;
        mSignatureProvider = pSignatureProvider;
    }

    /**
     * @param pQueryProvider The query provider
     * @param pSignatureProvider The signature provider
     * @param pResultvalidater Validates the response and throws exception on error found.
     * @throws RestQueryEngineException Exception thrown if no provider is supplied
     * @throws Exception identified in the pResultvalidater
     */
    public RestQueryEngine(IRestQueryProvider pQueryProvider, IRestQuerySignatureProvider pSignatureProvider,
        IRestQueryResultValidation pResultvalidater) {

        mQueryProvider = pQueryProvider;
        mSignatureProvider = pSignatureProvider;
        mResultvalidater = pResultvalidater;
    }

    /**
     * Sets the proxy provider class used for HTTP(S) connections.
     * @param pProxyProvider The proxy provider.
     */
    @Deprecated
    public synchronized static void setProxyProvider(IProxyProvider pProxyProvider) {
        if (pProxyProvider == null) {
            throw new IllegalArgumentException("pProxyProvider can not be null.");
        }
        // PROXY_PROVIDER = pProxyProvider;
    }

    public DefaultHttpClient getThreadSafeClient() {

        DefaultHttpClient myClient = new DefaultHttpClient();
        ClientConnectionManager myManager = myClient.getConnectionManager();
        HttpParams myParams = myClient.getParams();
        myClient =
            new DefaultHttpClient(new ThreadSafeClientConnManager(myParams, myManager.getSchemeRegistry()), myParams);
        return myClient;
    }

    /**
     * @return the mSignatureProvider
     */
    public IRestQuerySignatureProvider getSignatureProvider() {
        return mSignatureProvider;
    }

    /**
     * @param mSignatureProvider the mSignatureProvider to set
     */
    public void setSignatureProvider(IRestQuerySignatureProvider mSignatureProvider) {
        this.mSignatureProvider = mSignatureProvider;
    }

    /**
     * @return the mLastResponseContentLength
     */
    public long getLastResponseContentLength() {
        return mLastResponseContentLength;
    }

    /**
     * Executes the REST query and returns the result as a Map<String, Object>
     * @param pQueryName The unique name of the query
     * @return Returns the RestQueryMapResult object.
     * @throws RestQueryEngineException The exception object describing the issue with the execution
     *         of the REST query
     */
    public RestQueryMapResult runMapQuery(String pQueryName) throws RestQueryEngineException {
        return runMapQuery(pQueryName, null, null, null, null);
    }

    /**
     * Executes the REST query and returns the result as a Map<String, Object>
     * @param pQuery The unique name of the query
     * @param pUrlPostfix The string appended to the end of the URL defined in the query
     * @param pHeaderFields The list of HTTP headers (optional)
     * @param pGetParams The list of get parameters (optional)
     * @param pPostParams The list of post parameters (optional)
     * @return Returns the RestQueryMapResult object.
     * @throws RestQueryEngineException RestQueryEngineException The exception object describing the
     *         issue with the execution of the REST query
     */
    public RestQueryMapResult runMapQuery(String pQueryName, String pUrlPostfix, Map<String, String> pHeaderFields,
        Map<String, String> pGetParams, Map<String, String> pPostParams) throws RestQueryEngineException {
        RestQuerySimpleResult result = runSimpleQuery(pQueryName, pUrlPostfix, pHeaderFields, pGetParams, pPostParams);

        return new RestQueryMapResult(result.getStatusCode(), mQueryProvider.procesQueryResponse(
            result.getStatusCode(), result.getResponse()), result.getResponseSize());
    }

    public RestQueryMapResult runMapQueryWithoutFlattening(String pQueryName, QueryConfig pQueryConfig)
        throws RestQueryEngineException {
        RestQuerySimpleResult mySimpleResult = runSimpleQuery(pQueryName, pQueryConfig);

        return new RestQueryMapResult(mySimpleResult.getStatusCode(),
            (Map<String, Object>) JSONValue.parse(mySimpleResult.getResponse()), mySimpleResult.getResponseSize());
    }

    /**
     * Executes REST Query and returns Map with only one key 'items' mapped to List of results.
     * Should be used when REST Query returns Collection of Objects
     * @param pQueryName Name of executed query.
     * @param pQueryConfig Object containing configuration for executing query
     * @return Map with mapping items --> List<Map<String, Object>>
     */
    @SuppressWarnings("unchecked")
    public RestQueryMapResult runListedMapQuery(String pQueryName, QueryConfig pQueryConfig) {
        RestQuerySimpleResult mySimpleResult = runSimpleQuery(pQueryName, pQueryConfig);

        Map<String, Object> myResult = new HashMap<String, Object>();
        List<Map<String, Object>> myResultList =
            (List<Map<String, Object>>) JSONValue.parse(mySimpleResult.getResponse());

        myResult.put("items", myResultList);

        return new RestQueryMapResult(mySimpleResult.getStatusCode(), myResult, mySimpleResult.getResponseSize());
    }

    /**
     * Executes the REST query AS SUCH and returns the result as a string
     * @param pQueryToRun Query to run directly without first finding Query
     * @return Returns The RestQuerySimpleResult object. May return null object.
     * @throws RestQueryEngineException The exception object describing the issue with the execution
     *         of the REST query
     */
    public RestQuerySimpleResult runSimpleQueryAsSuch(RestQuery pQueryToRun) throws RestQueryEngineException {
        return this.runSimpleQuery(pQueryToRun, null, null, null, null, null, null);
    }

    /**
     * Executes the REST query and returns the result as a string
     * @param pQueryName The unique name of the query
     * @return Returns the RestQuerySimpleResult object. Mat return null object.
     * @throws RestQueryEngineException The exception object describing the issue with the execution
     *         of the REST query
     */
    public RestQuerySimpleResult runSimpleQuery(String pQueryName) throws RestQueryEngineException {
        return this.runSimpleQuery(findQuery(pQueryName), null, null, null, null, null, null);
    }

    /**
     * Executes the REST query and returns the result as a string
     * @param pQueryName The unique name of the query
     * @param pUrlPostfix The string appended to the end of the URL defined in the query
     * @param pHeaderFields The list of HTTP headers (optional)
     * @param pGetParams The list of get parameters (optional)
     * @param pPostParams The list of post parameters (optional)
     * @return Returns the RestQuerySimpleResult object.
     * @throws RestQueryEngineException The exception object describing the issue with the execution
     *         of the REST query
     */
    public RestQuerySimpleResult runSimpleQuery(String pQueryName, String pUrlPostfix,
        Map<String, String> pHeaderFields, Map<String, String> pGetParams, Map<String, String> pPostParams)
        throws RestQueryEngineException {
        return this.runSimpleQuery(findQuery(pQueryName), pUrlPostfix, pHeaderFields, pGetParams, pPostParams, null,
            null);
    }

    /**
     * Executes the REST query and returns the result as a string
     * @param pQueryName The unique name of the query
     * @param pUrlPostfix The string appended to the end of the URL defined in the query
     * @param pHeaderFields The list of HTTP headers (optional)
     * @param pGetParams The list of get parameters (optional)
     * @param pPostParams The list of post parameters (optional)
     * @param pPostBody The post body. If this is passed pPostParams is ignored (optional)
     * @return Returns the RestQuerySimpleResult object.
     * @throws RestQueryEngineException The exception object describing the issue with the execution
     *         of the REST query
     */
    public RestQuerySimpleResult runSimpleQuery(String pQueryName, String pUrlPostfix,
        Map<String, String> pHeaderFields, Map<String, String> pGetParams, Map<String, String> pPostParams,
        String pPostBody) throws RestQueryEngineException {

        return runSimpleQuery(findQuery(pQueryName), pUrlPostfix, pHeaderFields, pGetParams, pPostParams, null,
            pPostBody);
    }

    public RestQuerySimpleResult runSimpleQuery(String pQueryName, String pUrlPostfix,
        Map<String, String> pHeaderFields, Map<String, String> pGetParams, Map<String, String> pPostParams,
        Map<String, String> pPutParams, String pPostBody) throws RestQueryEngineException {

        return runSimpleQuery(findQuery(pQueryName), pUrlPostfix, pHeaderFields, pGetParams, pPostParams, pPutParams,
            pPostBody);
    }

    /**
     * Returns the contents of the given URL.
     * @param pURL The URL to be returned.
     * @return The contents of the given URL.
     */
    public RestQuerySimpleResult getURL(String pURL) {

        if (pURL == null || pURL.isEmpty()) {
            throw new IllegalArgumentException("pURL can not be null or empty string.");
        }

        RestQuery myQuery = new RestQuery("getUrl", Method.GET, pURL);

        return runSimpleQuery(myQuery, null, null, null, null, null, null);
    }

    public RestQueryMapResult getURLMapResult(String pURL) {
        RestQuerySimpleResult mySimpleResult = getURL(pURL);
        return new RestQueryMapResult(mySimpleResult.getStatusCode(), mQueryProvider.procesQueryResponse(
            mySimpleResult.getStatusCode(), mySimpleResult.getResponse()), mySimpleResult.getResponseSize());
    }

    public RestQuerySimpleResult runSimpleQuery(String pQueryName, QueryConfig pQueryConfig)
        throws RestQueryEngineException {
        if (pQueryConfig != null) {
            return runSimpleQuery(findQuery(pQueryName), pQueryConfig.getUrlPostfix(), pQueryConfig.getHeaderFields(),
                pQueryConfig.getGetParams(), pQueryConfig.getPostParams(), pQueryConfig.getPutParams(),
                pQueryConfig.getPostBody());
        } else {
            return runSimpleQuery(pQueryName);
        }

    }

    public RestQueryMapResult runMapQuery(String pQueryName, QueryConfig pQueryConfig) throws RestQueryEngineException {
        if (pQueryConfig != null) {
            return runMapQuery(pQueryName, pQueryConfig.getUrlPostfix(), pQueryConfig.getHeaderFields(),
                pQueryConfig.getGetParams(), pQueryConfig.getPostParams());
        } else {
            return runMapQuery(pQueryName);
        }
    }

    /**
     * Executes the REST query and returns the result as a string
     * @param pQuery The RestQuery instance.
     * @param pUrlPostfix The string appended to the end of the URL defined in the query
     * @param pHeaderFields The list of HTTP headers (optional)
     * @param pGetParams The list of get parameters (optional)
     * @param pPostParams The list of post parameters (optional)
     * @param pPostBody The post body. If this is passed pPostParams is ignored (optional)
     * @return Returns the RestQuerySimpleResult object.
     * @throws RestQueryEngineException The exception object describing the issue with the execution
     *         of the REST query
     */
    private RestQuerySimpleResult runSimpleQuery(RestQuery pQuery, String pUrlPostfix,
        Map<String, String> pHeaderFields, Map<String, String> pGetParams, Map<String, String> pPostParams,
        Map<String, String> pPutParams, String pPostBody) throws RestQueryEngineException {

        // Make sure we have a valid query
        if (pQuery == null) {
            throw new RestQueryEngineException("Query cannot be null");
        }

        // Get the URL
        String myUrl = pQuery.getURL();
        if (myUrl == null || myUrl.isEmpty()) {
            throw new RestQueryEngineException("No URL found in query: " + pQuery.getName());
        }

        // update useage
        updateUseage(pQuery.getName(), 1);

        // Set up proxy
        ProxyInfo myProxyInfo = null;
        if (mUseProxy) {

            ProxyType myProxyType;
            if (myUrl.toLowerCase().startsWith("https")) {
                myProxyType = ProxyType.HTTPS;
            } else {
                myProxyType = ProxyType.HTTP;
            }

            myProxyInfo = PROXY_PROVIDER.getProxy(myProxyType);
        }

        DefaultHttpClient myHttpClient = getThreadSafeClient();

        if (myProxyInfo != null) {
            String myUsername = myProxyInfo.getUsername();
            String myPassword = myProxyInfo.getPassword();

            if (myUsername != null && !myUsername.isEmpty()) {
                myHttpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(myProxyInfo.getHost(), myProxyInfo.getPort()),
                    new UsernamePasswordCredentials(myUsername, myPassword));
            }

            HttpHost proxy = new HttpHost(myProxyInfo.getHost(), myProxyInfo.getPort());
            myHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }

        // Add or replace all GET, POST parameters and headers using the input
        if (pHeaderFields != null) {
            pQuery.getHeaderFields().putAll(pHeaderFields);
        }

        if (pGetParams != null) {
            pQuery.getGetParams().putAll(pGetParams);
        }
        if (pPostParams != null) {
            pQuery.getPostParams().putAll(pPostParams);
        }
        if (pPutParams != null) {
            pQuery.getPutParams().putAll(pPutParams);
        }

        String myCharset = "UTF-8";

        StringBuilder myURLSB = new StringBuilder(myUrl);

        if (pUrlPostfix != null && !pUrlPostfix.isEmpty()) {
            myURLSB.append(pUrlPostfix);
        }

        if (pQuery.getUrlPreQueryStringText() != null) {
            myURLSB.append(pQuery.getUrlPreQueryStringText());
        }

        String myBaseUrl = myURLSB.toString();

        if (pQuery.getGetParams().size() > 0) {

            if (myBaseUrl.indexOf('?') == -1 && myBaseUrl.charAt(myBaseUrl.length() - 1) != '/') {
                myURLSB.append('?');
            }

            buildQueryParamsString(pQuery.getGetParams(), myURLSB, myCharset);
        }

        if (pQuery.getUrlPostQueryStringText() != null) {
            myURLSB.append(pQuery.getUrlPostQueryStringText());
        }
        myUrl = myURLSB.toString();
        HttpRequestBase myRequest;

        if (pQuery.getMethod() == RestQuery.Method.POST) {
            HttpPost myHttpPost = new HttpPost(myUrl);
            myRequest = myHttpPost;

            if (pPostBody != null && !pPostBody.isEmpty()) {
                try {
                    StringEntity myStringEntity = new StringEntity(pPostBody);
                    myHttpPost.setEntity(myStringEntity);
                } catch (UnsupportedEncodingException e) {
                    logAndThrow("Unsupported encoding is used for URL encoding.", e);
                }
            } else if (pQuery.getPostParams().size() > 0) {
                List<NameValuePair> myParams = new ArrayList<NameValuePair>();

                // Add post parameters
                for (Map.Entry<String, String> myEntry : pQuery.getPostParams().entrySet()) {
                    if (myEntry.getKey().endsWith("[]")) {
                        // Special array handling
                        String[] myArrayParamValues = myEntry.getValue().split(ARRAY_PARAM_ELEMENT_SEPARATOR_REGEX);
                        for (String myArrayParamValue : myArrayParamValues) {
                            myParams.add(new BasicNameValuePair(myEntry.getKey(), myArrayParamValue));
                        }
                    } else {
                        myParams.add(new BasicNameValuePair(myEntry.getKey(), myEntry.getValue()));
                    }
                }

                try {
                    myHttpPost.setEntity(new UrlEncodedFormEntity(myParams, myCharset));
                } catch (UnsupportedEncodingException e) {
                    logAndThrow("Unsupported encoding is used for URL encoding.", e);
                }
            }
        } else if (pQuery.getMethod() == RestQuery.Method.GET) {
            myRequest = new HttpGet(myUrl);

        } else if (pQuery.getMethod() == RestQuery.Method.PUT) {
            HttpPut myhttpPut = new HttpPut(myUrl);
            myRequest = myhttpPut;
            if (pPostBody != null && !pPostBody.isEmpty()) {
                try {
                    StringEntity myStringEntity = new StringEntity(pPostBody);
                    myhttpPut.setEntity(myStringEntity);
                } catch (UnsupportedEncodingException e) {
                    logAndThrow("Unsupported encoding is used for URL encoding.", e);
                }
            } else if (pQuery.getPutParams().size() > 0) {
                List<NameValuePair> myParams = new ArrayList<NameValuePair>();

                // Add post parameters
                for (Map.Entry<String, String> myEntry : pQuery.getPutParams().entrySet()) {
                    if (myEntry.getKey().endsWith("[]")) {
                        // Special array handling
                        String[] myArrayParamValues = myEntry.getValue().split(ARRAY_PARAM_ELEMENT_SEPARATOR_REGEX);
                        for (String myArrayParamValue : myArrayParamValues) {
                            myParams.add(new BasicNameValuePair(myEntry.getKey(), myArrayParamValue));
                        }
                    } else {
                        myParams.add(new BasicNameValuePair(myEntry.getKey(), myEntry.getValue()));
                    }
                }

                try {
                    myhttpPut.setEntity(new UrlEncodedFormEntity(myParams, myCharset));
                } catch (UnsupportedEncodingException e) {
                    logAndThrow("Unsupported encoding is used for URL encoding.", e);
                }
            }

        } else if (pQuery.getMethod() == RestQuery.Method.DELETE) {
            myRequest = new HttpDelete(myUrl);
        } else {
            throw new RestQueryEngineException("Unsupported HTTP Method");
        }

        // Set the custom HTTP headers
        for (Map.Entry<String, String> myEntry : pQuery.getHeaderFields().entrySet()) {
            myRequest.setHeader(myEntry.getKey(), myEntry.getValue());
        }
        // Execute common tasks
        mQueryProvider.executeCommonQueryTasks(myRequest);

        // Sign the connection if there is a signature provider
        if (mSignatureProvider != null) {
            mSignatureProvider.signRequest(myRequest);
        }

        disableSSLVerificationAndHostValidationForJiveSandbox(myUrl, myHttpClient);

        HttpResponse myResponse = null;
        try {
            myResponse = myHttpClient.execute(myRequest);
        } catch (SSLPeerUnverifiedException e) {
            throw new RestQueryEngineException(
                "The SSL certificate of the data source was not authenticated. Please verify that the trust store being used by HWR has been configured properly. Data source URL is: "
                    + myUrl, e);
        } catch (Throwable e) {
            logAndThrow("Failed to create an HTTP(S) connection while executing the query.", e);
        }

        HttpEntity myEntity = myResponse.getEntity();
        String myResponseString = null;

        mLastResponseContentLength = 0;
        if (myEntity != null) {
            if (myEntity.getContentLength() > 0) {
                mLastResponseContentLength = myEntity.getContentLength();
            }

            try {
                myResponseString = EntityUtils.toString(myEntity);
                if (mLastResponseContentLength == 0) {
                    mLastResponseContentLength = myResponseString.getBytes().length;
                }
            } catch (Throwable e) {
                logAndThrow("Failed to read the response from the HTTP connection.", e);
            }
        }

        RestQuerySimpleResult myResult =
            new RestQuerySimpleResult(myResponse.getStatusLine().getStatusCode(), myResponseString,
                mLastResponseContentLength);

        if (mResultvalidater != null) {
            mResultvalidater.validateResponse(myResult);
        }

        return myResult;
    }

    private void disableSSLVerificationAndHostValidationForJiveSandbox(String myUrl, DefaultHttpClient myHttpClient) {
        if (((myUrl.contains(IDENTIFY_JIVE_URL)) || (myUrl.contains(IDENTIFY_OSN_URL))) && myUrl.contains("https")) {
            LOGGER.info("Disabling SSL verification and hostname validation for (" + myUrl + ")...");
            SSLSocketFactory sf;
            try {
                TrustManager[] sslVerificationDisabled = new TrustManager[] { new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {
                        LOGGER.debug("getAcceptedIssuers() called.");
                        return null;
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        String kindOfCertificatesMessage = "Certificates found in client:";
                        logCertificates(kindOfCertificatesMessage, certs, authType);
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        String kindOfCertificatesMessage = "Jive certificate hierarchy:";
                        logCertificates(kindOfCertificatesMessage, certs, authType);
                    }

                    private void logCertificates(String kindOfCertificatesMessage, X509Certificate[] certs,
                        String authType) {
                        StringBuilder certificates = new StringBuilder();
                        certificates.append(kindOfCertificatesMessage + "\n");
                        for (int i = 0; i < certs.length; i++) {
                            certificates.append(certs[i].toString() + "\n");
                        }
                        certificates.append("Authentication type: " + authType + "\n");
                        LOGGER.info(certificates);
                    }
                } };

                SSLContext mySSLContext = SSLContext.getInstance("TLS");
                mySSLContext.init(null, sslVerificationDisabled, new SecureRandom());
                sf = new SSLSocketFactory(mySSLContext);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                Scheme mySchema = new Scheme("https", sf, JIVE_HTTPS_PORT);
                myHttpClient.getConnectionManager().getSchemeRegistry().register(mySchema);

                LOGGER.info("SSL verification and hostname validation for Jive was disabled successfully.");
            } catch (Exception e) {
                LOGGER.warn("Unable to disable SSL verification and hostname validation for Jive: ", e);
            }
        }
    }

    /**
     * Returns the query matching the supplied name.
     * @param pQueryName The unique query name.
     * @return The query matching the supplied name.
     * @throws RestQueryEngineException
     */
    public RestQuery findQuery(String pQueryName) throws RestQueryEngineException {

        if (pQueryName == null || pQueryName.isEmpty()) {
            throw new RestQueryEngineException("pQueryName can not be null or empty string.");
        }

        if (mQueryProvider == null) {
            throw new RestQueryEngineException("No query provider is supplied.");
        }

        return mQueryProvider.findQuery(pQueryName);
    }

    /**
     * @param pParams The list of parameters
     * @param pSB The stringbuilder instance to hold the output
     * @param pCharset The charset used for URL encoding
     * @throws RestQueryEngineException
     */
    private void buildQueryParamsString(Map<String, String> pParams, StringBuilder pSB, String pCharset)
        throws RestQueryEngineException {
        int i = 0;
        for (Map.Entry<String, String> myEntry : pParams.entrySet()) {
            if (myEntry.getKey().endsWith("[]")) {
                // Special array handling
                String[] myArrayParamValues = myEntry.getValue().split(ARRAY_PARAM_ELEMENT_SEPARATOR_REGEX);

                for (String myArrayParamValue : myArrayParamValues) {
                    if (i > 0) {
                        pSB.append("&");
                    }
                    pSB.append(myEntry.getKey());
                    pSB.append("=");
                    try {
                        pSB.append(URLEncoder.encode(myArrayParamValue, pCharset));
                    } catch (UnsupportedEncodingException e) {
                        logAndThrow("Invalid charset used for URL encoding.", e);
                    }
                    i++;
                }

            } else {
                if (i > 0) {
                    pSB.append("&");
                }
                pSB.append(myEntry.getKey());
                pSB.append("=");
                try {
                    pSB.append(URLEncoder.encode(myEntry.getValue(), pCharset));
                } catch (UnsupportedEncodingException e) {
                    logAndThrow("Invalid charset used for URL encoding.", e);
                }
                i++;
            }
        }
    }

    /**
     * @param pExceptionMessage The exception message
     * @param e The exception object
     * @throws RestQueryEngineException The exception object describing the issue with the execution
     *         of the REST query
     */
    private void logAndThrow(String pExceptionMessage, Throwable e) throws RestQueryEngineException {
        LOGGER.error(pExceptionMessage + "\n" + e.getMessage(), e);
        throw new RestQueryEngineException(pExceptionMessage, e);
    }

    @Override
    public String toString() {
        return "RestQueryEngine [" + "mLastResponseContentLength=" + mLastResponseContentLength + ", mQueryProvider="
            + mQueryProvider + ", mSignatureProvider=" + mSignatureProvider + ", mResultvalidater=" + mResultvalidater
            + "]";
    }

    private void updateUseage(String pCallName, int pIncrementNum) {
        if (mConnectorApiUseage != null) {
            mConnectorApiUseage.incrementApiUseage(pCallName, pIncrementNum);
        }
    }

    public void setConnectorApiUseage(ConnectorApiUseage pConnectorApiUseage) {
        mConnectorApiUseage = pConnectorApiUseage;
    }

    /**
     * Set whether to use proxy or not
     * @param pUseProxy Use proxy or not.
     */
    public void setUseProxy(Boolean pUseProxy) {
        mUseProxy = pUseProxy;
    }

}
