package com.example.aswer.gameapp;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for WorldInfo class
 * Construction and Parcel tests
 */
@RunWith(AndroidJUnit4.class)
public class WorldInfoTest {

    static private final String VALID_JSON_STRING =
                    "{\n" +
                    "\"id\": \"118\",\n" +
                    "\"language\": \"de\",\n" +
                    "\"url\": \"http://backend2.lordsandknights.com/XYRALITY/WebObjects/LKWorldServer-DE-15.woa\",\n" +
                    "\"country\": \"DE\",\n" +
                    "\"worldStatus\": {\n" +
                    "\"id\": \"3\",\n" +
                    "\"description\": \"online\"\n" +
                    "},\n" +
                    "\"mapURL\": \"http://maps2.lordsandknights.com/v2/LKWorldServer-DE-15\",\n" +
                    "\"name\": \"Deutsch 15 (empfohlen)\"" +
                    "}\n";

    @Test
    public void worldInfoConstruction() {
        try {
            WorldInfo info = new WorldInfo(new JSONObject(VALID_JSON_STRING));
        } catch (Exception e) {
            fail("No exception expected " + e.getMessage());
        }
    }

    @Test
    public void worldInfoParcel() {
        try {
            WorldInfo info = new WorldInfo(new JSONObject(VALID_JSON_STRING));
            Parcel parcel = Parcel.obtain();
            info.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            WorldInfo infoParcel = (WorldInfo) WorldInfo.CREATOR.createFromParcel(parcel);
            assertEquals(info, infoParcel);
        } catch (JSONException e) {
            fail("No exception expected: " + e.toString());
        }
    }
}
