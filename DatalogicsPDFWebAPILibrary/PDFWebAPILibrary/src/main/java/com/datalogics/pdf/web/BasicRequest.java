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
 */
public class BasicRequest extends AsyncTask {

    /**
     * The URL for Datalogics PDF Web API
     */
    final static String WEB_API_URL = "https://pdfprocess.datalogics-cloud.com/api/actions/";

    /**
     * The applicationID from signing up for Datalogics PDF Web API
     */
    String applicationID;

    /**
     * The applicationKey from signing up for Datalogics PDF Web API
     */
    String applicationKey;

    /**
     * The path to the input PDF file
     */
    String inputFile;

    /**
     * The password to the input PDF file, if it requires one
     */
    String inputFilePassword;

    /**
     * The path to write the output PDF file to
     */
    String outputFile;

    /**
     * BasicRequest constructor, executing this ASyncTask will do nothing and will probably result in
     * a 500 HTTP error
     *
     * @param applicationID
     * @param applicationKey
     * @param inputFile
     * @param outputFile
     */
    public BasicRequest(String applicationID, String applicationKey, String inputFile, String outputFile) {
        this.applicationID = applicationID;
        this.applicationKey = applicationKey;

        this.inputFile = inputFile;

        this.outputFile = outputFile;
    }

    /**
     * Get the applicationID in use
     * @return applicationID
     */
    public String getApplicationID() {
        return applicationID;
    }

    /**
     * Set the applicationID to use with Datalogics PDF Web API. This should be called before execute()!
     * @param applicationID
     */
    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    /**
     * Get the applicationKey in use
     * @return
     */
    public String getApplicationKey() {
        return applicationKey;
    }

    /**
     * Set the applicationKey to use with Datalogics PDF Web API. This should be called before execute()!
     * @param applicationKey
     */
    public void setApplicationKey(String applicationKey) {
        this.applicationKey = applicationKey;
    }

    /**
     * Get the path to the inputFile to use during this request
     * @return
     */
    public String getInputFile() {
        return inputFile;
    }

    /**
     * Set the path to the inputFile to use during this request. This should be called before execute()!
     * @param inputFile
     */
    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    /**
     * Set the password for the inputFile. This should be called before execute()!
     * @param inputFilePassword
     */
    public void setInputFilePassword(String inputFilePassword) {
        // TODO should this even be a member of this class?
        this.inputFilePassword = inputFilePassword;
    }

    /**
     * Execute the HTTP request on a separate thread and write the output PDF to the file system
     * @param objects
     * @return A String containing the HTTP response status code
     */
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

    /**
     * Do any special processing of the HTTP response here
     * @param response
     */
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

    /**
     * Write the outputFile to disk
     * @param resStream
     */
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
