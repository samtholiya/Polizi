package com.polizi.iam.polizi.notification.helper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.polizi.iam.polizi.R;

import org.json.JSONException;
import org.json.JSONObject;

public class DisplayNotification extends AppCompatActivity {

    private double latitude;
    private double longitude;
    private String mName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        TextView msgBox = (TextView) findViewById(R.id.msgBox);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.showLocation);
        Bundle extras = getIntent().getExtras();
        try {
            String jsonData = extras.getString("com.parse.Data");
            JSONObject jObject;
            jObject = new JSONObject(jsonData);
            String location = jObject.getString("location");
            String[] locationStr = location.split(",");
            latitude = Double.valueOf(locationStr[1].split(":")[1]);
            longitude = Double.valueOf(locationStr[2].split(":")[1].replace("}", ""));
            String message = jObject.getString("message");
            mName = jObject.getString("profile");
            msgBox.setText(message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Uri gmmIntentUri = Uri.parse("google.navigation:q=" + latitude + "," + longitude);

//                If the below does't work
                String label = mName;
                String uriBegin = "geo:" + latitude + "," + longitude;
                String query = latitude + "," + longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri gmmIntentUri = Uri.parse(uriString);

                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }

}
