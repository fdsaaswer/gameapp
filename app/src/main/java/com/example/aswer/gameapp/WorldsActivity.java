package com.example.aswer.gameapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

/*
 * Activity to show list of available game worlds
 * Scrollable
 * List of worlds is received as array of parcelable objects
 * and can be sent to native/remote service
 * no JSON parsing is needed at this point
 * instead of showing just button, we can put more stuff here:
 * world status, map url
 * but more information is needed to determine whether that makes sense
 */

public class WorldsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worlds);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout content = (LinearLayout) findViewById(R.id.content_layout);

        Intent intent = getIntent();
        if (intent!= null) {
            String serverVersion = intent.getStringExtra(Utils.EXTRA_VERSION);
            Parcelable[] worlds = intent.getParcelableArrayExtra(Utils.EXTRA_WORLDS);
            if (worlds == null) {
                Log.e(Utils.LOG_TAG, "Got no worlds");
            } else
            for (Parcelable parcelable: worlds) {
                WorldInfo world = (WorldInfo) parcelable;
                Button enterButton = new Button(this);
                enterButton.setText(world.name); // TODO replace with custom layout with additional data

                // TODO change button style
                if (!world.status.equals("online")) {
                    enterButton.setEnabled(false);
                }
                content.addView(enterButton);
            }
        } else {
            Log.e(Utils.LOG_TAG, "Got no intent");
        }
    }
}
