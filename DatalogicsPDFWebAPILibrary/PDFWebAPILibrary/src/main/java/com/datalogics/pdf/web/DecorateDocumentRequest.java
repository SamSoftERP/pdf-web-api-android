package com.datalogics.pdf.web;

import com.squareup.mimecraft.Part;

import java.io.File;

/**
 * Created by bhaugen on 7/1/14.
 */
public class DecorateDocumentRequest extends BasicRequest {
    final static String REQUEST_PATH = "decorate/document";

    public String decorationDataFile;

    public DecorateDocumentRequest(String applicationID, String applicationKey, String inputFile, String decorationDataFile) {
        super(applicationID, applicationKey, inputFile);

        this.decorationDataFile = decorationDataFile;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        // build the parts of the multipart form that are specific to the decorate document request

        // first is the inputPart that contains the input file (the PDF) to decorate
        Part inputPart = new Part.Builder().body(new File(inputFile)).contentDisposition("form-data; name=\"input\"").contentType("application/pdf").build();

        // second is the decorationPart that contains the xml file to use to decorate the PDF
        Part decorationPart = new Part.Builder().body(new File(decorationDataFile)).contentDisposition("form-data; name=\"decorationData\"").contentType("text/xml").build();

        // create a new objects array to pass the info we need to down to the BasicRequest class
        objects = new Object[] {BasicRequest.WEB_API_URL + REQUEST_PATH, inputPart, decorationPart};

        // Then call BasicRequest.doInBackground
        return super.doInBackground(objects);
    }
}
