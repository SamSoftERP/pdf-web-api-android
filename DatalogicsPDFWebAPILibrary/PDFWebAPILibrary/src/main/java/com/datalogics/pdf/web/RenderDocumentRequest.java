package com.datalogics.pdf.web;

import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import java.io.File;

/**
 * Created by bhaugen on 7/3/14.
 *
 * This class allows the user to create and send a request to the PDF Web API to convert a document
 * as described by https://api.datalogics-cloud.com/docs#renderpages
 */
public class RenderDocumentRequest extends BasicRequest {
    final static String REQUEST_PATH = "render/pages";
    public RenderDocumentRequest(String applicationID, String applicationKey, String inputFile, String outputFile) {
        super(applicationID, applicationKey, inputFile, outputFile);
    }

    @Override
    protected String doInBackground(Object[] objects) {
        // build the parts of the multipart form that are specific to the decorate document request

        // first is the inputPart that contains the input file (the PDF) to decorate
//        Part inputPart = new Part.Builder().body("http://www.datalogics.com/pdf/doc/pdf2img.pdf").contentDisposition("form-data; name=\"inputURL\"").contentType("application/pdf").build();

        // second is the optionPart that contains the options for rendering the documents pages
//        Part optionsPart = new Part.Builder().body("{\"outputFormat\": \"jpg\", \"printPreview\": true}").contentDisposition("form-data; name=\"options\"").build();

        MultipartEntityBuilder entity = MultipartEntityBuilder.create();

        entity.addPart("input", new FileBody(new File(inputFile)));

        // create a new objects array to pass the info we need to down to the BasicRequest class
        objects = new Object[] {BasicRequest.WEB_API_URL + REQUEST_PATH, entity};

        // Then call BasicRequest.doInBackground
        return super.doInBackground(objects);
    }
}
