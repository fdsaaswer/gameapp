package com.example.aswer.gameapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URLConnection;

class Utils {
    static final String LOG_TAG = "GameApp";
    static final String EXTRA_VERSION = "SERVER_VERSION";
    static final String EXTRA_WORLDS = "WORLD_LIST";
    static final String SERVER_URL = "http://backend1.lordsandknights.com/XYRALITY/WebObjects/BKLoginServer.woa/wa/worlds";

    static final String JSON_STRING_EXAMPLE =
            "{\n" +
                    "\"serverVersion\": \"1.0.\",\n" +
                    "\"allAvailableWorlds\": [\n" +
                    "{\n" +
                    "\"id\": \"118\",\n" +
                    "\"language\": \"de\",\n" +
                    "\"url\": \"http://backend2.lordsandknights.com/XYRALITY/WebObjects/LKWorldServer-DE-15.woa\",\n" +
                    "\"country\": \"DE\",\n" +
                    "\"worldStatus\": {\n" +
                    "\"id\": 3,\n" +
                    "\"description\": \"online\"\n" +
                    "},\n" +
                    "\"mapURL\": \"http://maps2.lordsandknights.com/v2/LKWorldServer-DE-15\",\n" +
                    "\"name\": \"Deutsch 15 (empfohlen)\"" +
                    "}\n" +
                    "]\n" +
                    "}\n";

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
            reader = new BufferedReader(new InputStreamReader(istream));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } finally {
            if (reader != null) reader.close();
            if (istream != null) istream.close();
        }
        return builder.toString();
    }
}
