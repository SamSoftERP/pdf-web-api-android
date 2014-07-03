package com.datalogics.pdf.web;

import android.os.AsyncTask;
import android.util.Log;

import com.squareup.mimecraft.Multipart;
import com.squareup.mimecraft.Part;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
        // create the json string for the body of the request
        String applicationString = "\"{\"id\":\"" + applicationID + "\", \"key\":\"" + applicationKey + "\"}\"";
        Part applicationPart = new Part.Builder().body(applicationString).contentDisposition("form-data; name=\"application\"").build();

        // now add the application Part toe the request
        Multipart.Builder builder = new Multipart.Builder("test")
                .addPart(applicationPart);

        // for each object that is a Part in the objects array, add the Part to the multipart request
        for (Object object : objects) {
            if (object instanceof Part) {
                builder.addPart((Part) object);
            }
        }

        // write the multipart form data to an output stream
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            builder.build().writeBodyTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient();

        // build the Call using the output stream from the multipart form data
        // TODO think about ramifications of using objects[0] for passing the URL
        Call call = client.newCall(new Request.Builder().url((String) objects[0])
                                                        .post(RequestBody.create(MediaType.parse("multipart/form-data"), out.toByteArray()))
                                                        .build());

        Response response;
        try {
            // actually make the call now
            response = call.execute();

            // debugging information
            String responseString = response.toString();
            Log.d("RESPONSE", responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO return the file from the request to the user or write it out to disk somewhere and
        // return the path to the user?
        return null;
    }
}
