package com.datalogics.pdf.web.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.datalogics.pdf.web.AddImagesRequest;
import com.datalogics.pdf.web.PropertiesRequest;
import com.datalogics.pdf.web.DecorateDocumentRequest;
import com.datalogics.pdf.web.RenderDocumentRequest;

public class PDFWebAPISampleActivity extends Activity {

    enum RequestType {
        RENDER,
        DECORATE,
        PROPERTIES
    }

    RequestType currentRequestType = RequestType.RENDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfweb_apisample);

        final TextView extra1Tag = (TextView) findViewById(R.id.extraTextView1);
        final TextView extra2Tag = (TextView) findViewById(R.id.extraTextView2);
        final EditText extra1TextField = (EditText) findViewById(R.id.extraTextField1);
        final EditText extra2TextField = (EditText) findViewById(R.id.extraTextField2);

        RadioButton rb = (RadioButton) findViewById(R.id.renderRadioButton);
        // Default to render option
        rb.setChecked(true);
        rb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // Render requests only require an input and an output file.
                extra1Tag.setVisibility(View.INVISIBLE);
                extra1TextField.setVisibility(View.INVISIBLE);

                extra2Tag.setVisibility(View.INVISIBLE);
                extra2TextField.setVisibility(View.INVISIBLE);

                currentRequestType = RequestType.RENDER;
            }
        });


        rb = (RadioButton) findViewById(R.id.propertiesRadioButton);
        rb.setOnClickListener(new OnClickListener (){
            public void onClick(View v) {

                // Properties requests have the option to specify a property to request.
                extra1Tag.setVisibility(View.VISIBLE);
                extra1Tag.setText("Property");
                extra1TextField.setVisibility(View.VISIBLE);
                extra1TextField.setHint("property name (optional)");

                extra2Tag.setVisibility(View.INVISIBLE);
                extra2TextField.setVisibility(View.INVISIBLE);

                currentRequestType = RequestType.PROPERTIES;
            }
        });


        rb = (RadioButton) findViewById(R.id.decorateRadioButton);
        rb.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                // Decorate document requires a data file to describe the decorations
                // and may require an image file as well.
                extra1Tag.setVisibility(View.VISIBLE);
                extra1Tag.setText("Decorate Data File");
                extra1TextField.setVisibility(View.VISIBLE);
                extra1TextField.setHint("data file path");

                extra2Tag.setVisibility(View.VISIBLE);
                extra2Tag.setText("Image");
                extra2TextField.setVisibility(View.VISIBLE);
                extra2TextField.setHint("image file path");

                currentRequestType = RequestType.DECORATE;
            }
        });

        Button button = (Button) findViewById(R.id.requestButton);
        button.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                String inputFile = ((EditText)findViewById(R.id.inputFileTextField)).getText().toString();
                String outputFile = ((EditText)findViewById(R.id.outputFileTextField)).getText().toString();

                String applicationID = "";
                String applicationKey = "";

                switch (currentRequestType) {
                    case RENDER:
                        RenderDocumentRequest renderRequest = new RenderDocumentRequest(applicationID, applicationKey, inputFile, outputFile);
                        renderRequest.execute();
                       break;

                    case PROPERTIES:
                        String requestedInfo = ((EditText)findViewById(R.id.extraTextField1)).getText().toString();
                        PropertiesRequest propertiesRequest = new PropertiesRequest(applicationID, applicationKey, inputFile, outputFile, requestedInfo);
                        propertiesRequest.execute();
                        break;

                    case DECORATE:

                        // TODO
                        break;
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pdfweb_apisample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
