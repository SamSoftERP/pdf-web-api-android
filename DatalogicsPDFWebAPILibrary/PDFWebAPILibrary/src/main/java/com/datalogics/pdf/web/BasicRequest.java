package com.datalogics.pdf.web;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by bhaugen on 7/1/14.
 */
public class BasicRequest extends AsyncTask {

    final static String WEB_API_URL = "https://pdfprocess.datalogics-cloud.com/api/actions/";

    String applicationID;
    String applicationKey;

    String inputFile;
    String inputFilePassword;

    public BasicRequest(String applicationID, String applicationKey, String inputFile) {
        this.applicationID = applicationID;
        this.applicationKey = applicationKey;

        this.inputFile = inputFile;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getApplicationKey() {
        return applicationKey;
    }

    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getInputFilePassword() {
        return inputFilePassword;
    }

    public void setInputFilePassword(String inputFilePassword) {
        this.inputFilePassword = inputFilePassword;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        if (objects[1] instanceof MultipartEntityBuilder) {
            MultipartEntityBuilder entity = (MultipartEntityBuilder) objects[1];

            String applicationString = "\"{\"id\":\"" + this.applicationID + "\", \"key\":\"" + this.applicationKey + "\"}\"";

            StringBody application = null;
            try {
                application = new StringBody(applicationString);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            entity.addPart("application", application);

            HttpPost post = new HttpPost((String) objects[0]);
            post.setEntity(entity.build());

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = null;
            try {
                response = httpClient.execute(post);
                if (response != null) {
                    Log.d("PDF WEB API", response.getStatusLine().toString());
                    HttpEntity resEntity = response.getEntity();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // TODO return the file from the request to the user or write it out to disk somewhere and
        // return the path to the user?
        return null;
    }
}
