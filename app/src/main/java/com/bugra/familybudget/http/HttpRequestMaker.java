package com.bugra.familybudget.http;

import com.bugra.familybudget.http.constant.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HttpRequestMaker {

    private static HttpRequestMaker httpRequestMaker;

    private HttpRequestMaker() { }

    public static HttpRequestMaker getInstance() {
        if(httpRequestMaker == null) {
            httpRequestMaker = new HttpRequestMaker();
        }

        return httpRequestMaker;
    }

    public int doPost(String url, String entity) {
        try {
            HttpClient client = getClient();

            HttpPost postRequest = new HttpPost(url);
            postRequest.setEntity(new StringEntity(entity));
            HttpResponse response = executeRequest(client, postRequest);
            return response.getStatusLine().getStatusCode();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int doDelete(String deleteUrl) {
        try {
            HttpClient client = getClient();

            HttpDelete deleteRequest = new HttpDelete(deleteUrl);
            HttpResponse response = executeRequest(client, deleteRequest);
            return response.getStatusLine().getStatusCode();

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public String doGet(String url) {
        String result = null;
        try {
            HttpClient client = getClient();
            HttpGet getRequest = new HttpGet(url);
            HttpResponse response = executeRequest(client, getRequest);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                result = convertInputStreamToString(entity.getContent());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return result;
    }

    private HttpClient getClient() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, Constants.SERVICE_CONNECTION_TIMEOUT);
        return new DefaultHttpClient(httpParams);
    }

    private HttpResponse executeRequest(HttpClient client, HttpUriRequest request) {
        try {
            request.setHeader("Content-type", "application/json");
            return client.execute(request);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

}
