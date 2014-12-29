package com.datalogics.pdf.web;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;

/**
 * Created by tsmith on 12/24/14.
 *
 * This class allows the user to create and send a request to the PDF Web API to get the properties
 * of a document as described by https://api.datalogics-cloud.com/docs#documentproperties
 */
public class PropertiesRequest extends BasicRequest {
    final static String REQUEST_PATH = "retrieve/document/properties";

    public String requestedInfo;

    // If requestedInfo is not provided it will default to an empty string, and all of the
    // document's information will be requested.
    public PropertiesRequest(String applicationID, String applicationKey, String inputFile, String outputFile){
        super(applicationID, applicationKey, inputFile, outputFile);

        this.requestedInfo = "";
    }

    // requestedInfo can be specified to request specific pieces of information about the document.
    public PropertiesRequest(String applicationID, String applicationKey, String inputFile, String outputFile, String requestedInfo){
        super(applicationID, applicationKey, inputFile, outputFile);

        this.requestedInfo = requestedInfo;
    }

    @Override
    protected String doInBackground(Object[] objects) {
        // build the parts of the multipart form that are specific to the properties request
        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        entity.addPart("input", new FileBody(new File(inputFile)));

        // create a new objects array to pass the info we need to down to the BasicRequest class
        String requestURL = BasicRequest.WEB_API_URL + REQUEST_PATH;
        if (!requestedInfo.isEmpty()) {
            requestURL = requestURL +"/" + requestedInfo;
        }
        objects = new Object[] {requestURL, entity};

        // Then call BasicRequest.doInBackground
        return super.doInBackground(objects);
    }
}


