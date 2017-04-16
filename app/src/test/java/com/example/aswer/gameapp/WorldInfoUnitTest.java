package com.example.aswer.gameapp;

import android.os.Parcel;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for WorldInfo class
 * Basic construction tests
  */

public class WorldInfoUnitTest {

    @Mock
    JSONObject mockJson = mock(JSONObject.class);

    @Before
    public void setUp() throws Exception {
        when(mockJson.getString(any(String.class))).thenReturn("test");
        when(mockJson.getJSONObject(any(String.class))).thenReturn(mockJson);
    }

    @Test
    public void worldInfoConstruction() {
        try {
            WorldInfo info = new WorldInfo(mockJson);
        } catch (Exception e) {
            fail("No exception expected " + e.getMessage());
        }
    }

    @Test
    public void worldInfoConstructionFail() {
        try {
            when(mockJson.getJSONObject(any(String.class))).thenReturn(null);
            WorldInfo info = new WorldInfo(mockJson);
            fail("Exception expected");
        } catch (Exception e) {
            // nothing here
        }
    }

    @Test
    public void worldInfoEquals() {
        try {
            WorldInfo info = new WorldInfo(mockJson);
            assertEquals(info, info);
            String[] fields = {
                    "id",
                    "language",
                    "url",
                    "country",
                    "statusId",
                    "status",
                    "mapUrl",
                    "name"
            };
            for (String field : fields) {
                when(mockJson.getString(field)).thenReturn(null);
                WorldInfo modifiedInfo = new WorldInfo(mockJson);
                assertNotEquals(info, modifiedInfo);
            }
        } catch (Exception e) {
            fail("No exception expected " + e.getMessage());
        }
    }
}
