package com.example.aswer.gameapp;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

/*
 * Class responsible for JSON retrieval and canonization
 * some module-wide constants also stored here
 */

class Utils {
    static final String LOG_TAG = "GameApp";
    static final String EXTRA_VERSION = "SERVER_VERSION";
    static final String EXTRA_WORLDS = "WORLD_LIST";
    static final String SERVER_URL = "http://backend1.lordsandknights.com/XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds";

    static String canonizeJson(String jsonString) {
                /*try {
                    JSONTokener tokener = new JSONTokener(jsonString);
                    String str;
                    while (!(str = tokener.nextString('\"')).isEmpty()) {
                        Log.d(Utils.LOG_TAG, str);
                    }
                } catch (Exception e) {
                    Log.e(Utils.LOG_TAG, "Got exception", e);
                }*/
        return jsonString.
                replaceAll("\\s+","").
                replaceAll("\\(","[").
                replaceAll("\\)","]").
                replaceAll(";", ",").
                replaceAll("=", ":").
                replaceAll("\\,\\}", "\\}").
                replaceAll("\\\\U", "\\\\u");
    }

    static void connectionWrite(URLConnection connection, String value) throws IOException {
        OutputStream ostream = null;
        BufferedWriter writer = null;
        try {
            ostream = connection.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(ostream, "UTF-8")); // JSON is UTF-8
            writer.write(value);
            writer.flush();
            writer.close();
        } finally {
            if (writer != null) writer.close();
            if (ostream != null) ostream.close();
        }
    }

    static String connectionRead(URLConnection connection) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStream istream = null;
        BufferedReader reader = null;
        try {
            istream = connection.getInputStream();
            String line;
            reader = new BufferedReader(new InputStreamReader(istream, "UTF-8"));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } finally {
            if (reader != null) reader.close();
            if (istream != null) istream.close();
        }
        return builder.toString();
    }

    // TODO cover with unit tests
    static String getJsonFromServer(WorldRequest request) {
        HttpURLConnection conn;

        try { // init connection
            URL url = new URL(Utils.SERVER_URL);
            conn = (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            Log.wtf(Utils.LOG_TAG, "SERVER_URL is hard-coded. Should never be malformed");
            return null;
        } catch (IOException e) {
            Log.e(Utils.LOG_TAG, "Server is unreachable");
            return null;
        }

        try {
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length",
                    Integer.toString(request.toString().getBytes().length));
            conn.setRequestProperty("Content-Language", "en-GB");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            Utils.connectionWrite(conn, request.toString());

            Log.d(Utils.LOG_TAG, "request string: " + request); // debug

            int responseCode = conn.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK &&
                    responseCode != HttpsURLConnection.HTTP_CREATED) {
                // TODO dump error data for debug
                throw new IOException("Bad response code" + responseCode);
            }

            return Utils.connectionRead(conn);
        } catch (IOException e) {
            Log.e(Utils.LOG_TAG, "Could not retrieve remote game data", e);

            return null;
        } finally {
            conn.disconnect();
        }
    }

    static void printDebug(String debugInfo) { // debug
        try {
            File newFolder = new File(Environment.getExternalStorageDirectory(), "TestFolder");
            if (!newFolder.exists()) {
                newFolder.mkdir();
            }
            File file = new File(newFolder, "worlds.json");
            file.createNewFile();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(debugInfo);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
