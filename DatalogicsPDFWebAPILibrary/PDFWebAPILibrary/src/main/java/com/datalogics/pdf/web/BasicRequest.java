package com.datalogics.pdf.web;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by bhaugen on 7/1/14.
 *
 * This is the basic request template on top of which all other request types are built.
 */
public class BasicRequest extends AsyncTask {

    final static String WEB_API_URL = "https://pdfprocess.datalogics-cloud.com/api/actions/";

    String applicationID;
    String applicationKey;

    String inputFile;
    String inputFilePassword;

    String outputFile;

    public BasicRequest(String applicationID, String applicationKey, String inputFile, String outputFile) {
        this.applicationID = applicationID;
        this.applicationKey = applicationKey;

        this.inputFile = inputFile;

        this.outputFile = outputFile;
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
    protected String doInBackground(Object[] objects) {
        String responseStatus = null;
        if (objects[1] instanceof MultipartEntityBuilder) {
            MultipartEntityBuilder entity = (MultipartEntityBuilder) objects[1];

            String applicationString = "{\"id\":\"" + this.applicationID + "\", \"key\":\"" + this.applicationKey + "\"}";

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
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response != null) {
                responseStatus = response.getStatusLine().toString();
                Log.d("PDF WEB API", responseStatus);

                processResponse(response);
            }
        }

        return responseStatus;
    }

    private void processResponse(HttpResponse response) {
        if (response.getStatusLine().getStatusCode() == 200) {
            // get the response and write the file out to disk
            HttpEntity resEntity = response.getEntity();
            if (resEntity != null) {
                InputStream resStream = null;
                try {
                    resStream = resEntity.getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                writeFileToDisk(resStream);
            }
        }
    }

    private void writeFileToDisk(InputStream resStream) {
        // write the inputStream to a FileOutputStream
        OutputStream outputStream =
                null;
        try {
            outputStream = new FileOutputStream(new File(outputFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        int read = 0;
        byte[] bytes = new byte[1024];

        try {
            while ((read = resStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
