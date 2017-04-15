package com.example.aswer.gameapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
            try {
                JSONObject worldList = new JSONObject(intent.getStringExtra("WORLD_LIST"));
                worldList.getString("serverVersion");
                JSONArray worlds = worldList.getJSONArray("allAvailableWorlds");
                Log.d("GameApp", "Worlds num: " + worlds.length());
                for (int i = 0; i < worlds.length(); i++) {
                    JSONObject world = worlds.getJSONObject(i);
                    String name = world.getString("name");

                    Log.d("GameApp", "Got name:" + name);
                    Button enterButton = new Button(this);
                    enterButton.setText(name);
                    content.addView(enterButton);
                }
            } catch (JSONException e) {
                Log.e("GameApp", "Failed to parse JSON string", e);
                Toast.makeText(this, "Could not retrieve world list", Toast.LENGTH_LONG);
            }
        } else {
            Log.e("GameApp", "Got no intent");
        }
    }
}
