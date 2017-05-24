/*******************************************************************************
 * Copyright 2012, Oracle Corporation and/or its affiliates. All rights reserved. Title, ownership
 * rights, and intellectual property rights in and to this software remain with Oracle Corporation.
 * Oracle Corporation hereby reserves all rights in and to this software. This software may not be
 * copied, modified, or used without a license from Oracle Corporation. This software is protected
 * by international copyright laws and treaties, and may be protected by other laws. Violation of
 * copyright laws may result in civil liability and criminal penalties.
 ******************************************************************************/
package oracle.apps.hcm.hwr.extwebclient.rest.linkedin;

import java.util.Map;

import oracle.apps.hcm.hwr.extwebclient.rest.IRestQueryProvider;
import oracle.apps.hcm.hwr.extwebclient.rest.JsonHelper;
import oracle.apps.hcm.hwr.extwebclient.rest.RestQuery;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * The IRESTQueryProvider implementation for LinkedIn
 * @author Yenal Kal
 */
public class LinkedInQueryProvider implements IRestQueryProvider {

    /*
     * (non-Javadoc)
     * @see oracle.apps.grc.restserviceprovider.IRESTQueryProvider#executeCommonQueryTasks(java.net.
     * HttpURLConnection)
     */
    @Override
    public void executeCommonQueryTasks(HttpRequestBase pRequest) {
        // Always get JSON response back from LinkedIn API
        pRequest.setHeader("x-li-format", "json");
    }

    /*
     * (non-Javadoc)
     * @see oracle.apps.grc.restserviceprovider.IRESTQueryProvider#findQuery(java.lang.String)
     */
    @Override
    public RestQuery findQuery(String pQueryName) {
        // @todo For now we have hard-coded queries. For the final product these queries should be
        // loaded in a map

        if (pQueryName.equals("getAccessToken")) {
            RestQuery myQuery =
                new RestQuery("getAccessToken", RestQuery.Method.POST,
                    "https://api.linkedin.com/uas/oauth/accessToken");
            return myQuery;
        } else if (pQueryName.equals("peopleSearch")) {
            RestQuery myQuery =
                new RestQuery(
                    "peopleSearch",
                    RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people-search:(people:(id,picture-url,public-profile-url,formatted-name,location:(name),summary,first-name,last-name,email-address,languages:(language:(name),proficiency:(name)),associations,headline,honors-awards,industry,interests,main-address,phone-numbers,skills:(skill:(name)),specialties,last-modified-timestamp,educations,certifications:(id,name,authority:(name),number,start-date,end-date),courses,publications:(id,title,publisher:(name),authors:(id,name,person),date,url,summary),patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name,person),date,url),positions:(id,title,summary,start-date,end-date,company:(name)),imAccounts,volunteer),num-results)");
            return myQuery;
        } else if (pQueryName.equals("getProfile")) {
            RestQuery myQuery =
                new RestQuery("getProfile", RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people/");
            myQuery
            .setUrlPostQueryStringText(":(id,picture-url,public-profile-url,formatted-name,location:(name),summary,first-name,last-name,email-address,languages:(language:(name),proficiency:(name)),associations,headline,honors-awards,industry,interests,main-address,phone-numbers,skills:(skill:(name)),specialties,last-modified-timestamp,educations,certifications:(id,name,authority:(name),number,start-date,end-date),courses,publications:(id,title,publisher:(name),authors:(id,name,person),date,url,summary),recommendations-received,patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name,person),date,url),positions:(id,title,summary,start-date,end-date,company:(name)),imAccounts,volunteer)");
            return myQuery;
        } else if (pQueryName.equals("getProfileLight")) {
            RestQuery myQuery =
                new RestQuery("getProfile", RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people/");
            myQuery
                .setUrlPostQueryStringText(":(id,formatted-name)");
            return myQuery;
		} else if (pQueryName.equals("getProfileNewApi")) {
            RestQuery myQuery =
                new RestQuery("getProfile", RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people/");
            myQuery
            .setUrlPostQueryStringText(":(id,picture-url,public-profile-url,formatted-name,location:(name),summary,first-name,last-name,email-address,headline,industry,specialties,last-modified-timestamp,positions:(id,title,summary,start-date,end-date,company:(name)))");
            return myQuery;	
        } else if (pQueryName.equals("getShares")) {
            RestQuery myQuery =
                new RestQuery("getShares", RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people/");
            return myQuery;
        } else if (pQueryName.equals("getConnections")) {
            RestQuery myQuery =
                new RestQuery("getConnections", RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people/");
            myQuery
                .setUrlPostQueryStringText(":(id,picture-url,public-profile-url,formatted-name,location:(name),summary,first-name,last-name,email-address,languages:(language:(name),proficiency:(name)),associations,headline,honors-awards,industry,interests,main-address,phone-numbers,skills:(skill:(name)),specialties,last-modified-timestamp,educations,certifications:(id,name,authority:(name),number,start-date,end-date),courses,publications:(id,title,publisher:(name),authors:(id,name,person),date,url,summary),patents:(id,title,summary,number,status:(id,name),office:(name),inventors:(id,name,person),date,url),positions:(id,title,summary,start-date,end-date,company:(name)),imAccounts,volunteer)");
            return myQuery;
        } else if (pQueryName.equals("getGroups")) {
            RestQuery myQuery =
                new RestQuery("getGroups", RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people/");
            return myQuery;
        } else if (pQueryName.equals("sendMessage")) {
            return new RestQuery("sendMessage", RestQuery.Method.POST,
                "https://api.linkedin.com/v1/people/~/mailbox");
        } else if (pQueryName.equals("getRequestTokenURL")) {
            return new RestQuery("getRequestTokenURL", RestQuery.Method.POST,
                "https://api.linkedin.com/uas/oauth/requestToken");
        } else if (pQueryName.equals("getOneProfileUrl")) {
            RestQuery myQuery =
                new RestQuery("getConnections", RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people/");
            myQuery.setUrlPostQueryStringText(":(id,public-profile-url)");
            return myQuery;
        } else if (pQueryName.equals("getConnectionProfileUrl")) {
            RestQuery myQuery =
                new RestQuery("getConnections", RestQuery.Method.GET,
                    "https://api.linkedin.com/v1/people/");
            myQuery.setUrlPostQueryStringText(":(id,public-profile-url)");
            return myQuery;
        } else if(pQueryName.equals("getLastModifiedDate")) {
        	RestQuery myQuery =
                    new RestQuery("getLastModifiedDate", RestQuery.Method.GET,
                        "https://api.linkedin.com/v1/people/");
                myQuery
                .setUrlPostQueryStringText(":(last-modified-timestamp)");
                return myQuery;
        }
        
        return null;
    }

    /*
     * (non-Javadoc)
     * @see oracle.apps.hcm.hwr.client.rest.IRestQueryProvider#procesQueryResponse(int,
     * java.lang.String)
     */
    @Override
    public Map<String, Object> procesQueryResponse(int statusCode, String pQueryResponse) {
        return JsonHelper.jsonStringToMap(pQueryResponse);
    }

}
