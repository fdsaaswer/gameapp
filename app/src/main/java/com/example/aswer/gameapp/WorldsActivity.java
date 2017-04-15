package com.example.aswer.gameapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

                Log.d(Utils.LOG_TAG, "name: " + world.name + " status: " + world.status);

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
