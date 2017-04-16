package com.example.aswer.gameapp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Spy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for Utils helper utilities
 * Connection to/from server
 * Canonization of JSON
  */
public class UtilsUnitTest {

    @Spy
    InputStream mockIStream = spy(InputStream.class);

    @Spy
    OutputStream mockOStream = spy(OutputStream.class);

    @Mock
    URLConnection mockConnection = mock(URLConnection.class);

    @Before
    public void setUp() throws Exception {
        when(mockConnection.getInputStream()).thenReturn(mockIStream);
        when(mockConnection.getOutputStream()).thenReturn(mockOStream);
    }

    @Test
    public void utilsConnectionRead() {
        try {
            when(mockIStream.read()).
                    thenReturn(0x0054).
                    thenReturn(0x0045).
                    thenReturn(0x0053).
                    thenReturn(0x0054).
                    thenReturn(-1);
            String gotString = Utils.connectionRead(mockConnection);
            assertEquals("Strings modified during transfer", gotString,  "TEST");
            verify(mockConnection, never()).connect();
        } catch (IOException e) {
            fail("No exception expected " + e.getMessage());
        }
    }

    // unchanged
    @Test
    public void utilsCanonizeJson_0() {
        String jsonString = "{\"key\":\"value\"}";
        assertEquals(jsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_1() {
        String jsonString = "{\"key\" : \"value\"}";
        assertEquals(jsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_2() {
        String jsonString = "{\n\"key\"\n:\n\"value\"\n}";
        assertEquals(jsonString, Utils.canonizeJson(jsonString));
    }

    // some replacements
    @Test
    public void utilsCanonizeJson_replace_0() { // equals with colon
        String jsonString = "{\"key\"=\"value\"}";
        String canonicalJsonString = "{\"key\":\"value\"}";
        assertEquals(canonicalJsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_replace_1() { // semicolon at the end
        String jsonString = "{\"key\":\"value\";}";
        String canonicalJsonString = "{\"key\":\"value\"}";
        assertEquals(canonicalJsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_replace_2() { // semicolon in dict
        String jsonString = "{\"key1\":\"value1\";\"key2\":\"value2\"}";
        String canonicalJsonString = "{\"key1\":\"value1\",\"key2\":\"value2\"}";
        assertEquals(canonicalJsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_replace_3() { // remove comma at the end
        String jsonString = "{\"key\":\"value\",}";
        String canonicalJsonString = "{\"key\":\"value\"}";
        assertEquals(canonicalJsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_replace_4() { // change parentheses
        String jsonString = "{({\"key\":\"value\"})}";
        String canonicalJsonString = "{[{\"key\":\"value\"}]}";
        assertEquals(canonicalJsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_unicode() { // change parentheses
        String jsonString = "{\"key\":\"\\U0627\"}";
        String canonicalJsonString = "{\"key\":\"\\u0627\"}";
        assertEquals(canonicalJsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_no_replace_0() { // don't change values inside strings
        String jsonString = "{\"key()\":\"value[]\"}";
        assertEquals(jsonString, Utils.canonizeJson(jsonString));
    }

    @Test
    public void utilsCanonizeJson_no_replace_1() { // don't change spaces inside strings
        String jsonString = "{\"ke y\":\"va\tlue\"}";
        assertEquals(jsonString, Utils.canonizeJson(jsonString));
    }
}