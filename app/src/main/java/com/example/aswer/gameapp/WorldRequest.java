package com.example.aswer.gameapp;

import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

final class WorldRequest {
    public final String login;
    public final String password;
    public final String deviceType;
    public final String deviceId;

    WorldRequest(String email, String password) {
        this.login = email;
        this.password = password;
        this.deviceType = String.format("%s %s", Build.MODEL, Build.VERSION.RELEASE);
        this.deviceId = getDeviceId();
    }

    private static String getDeviceId() {
        List<NetworkInterface> interfaces;
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (interfaces == null || interfaces.size() == 0) {
                throw new IllegalStateException("Empty interfaces list");
            }
        } catch (SocketException | IllegalStateException e) {
            Log.e(Utils.LOG_TAG, "Could not get network interfaces", e);
            return null;
        }

        for (NetworkInterface iface : interfaces) {
            try {
                byte[] mac = iface.getHardwareAddress();
                if (mac == null || mac.length == 0) continue;
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < mac.length; ++i) {
                    builder.append(String.format("%02X:", mac[i]));
                }
                builder.deleteCharAt(builder.length()-1);

                return builder.toString();
            } catch (SocketException e) {
                Log.d(Utils.LOG_TAG, "could not retrieve mac from interface name: " + iface.getName());
            }
        }

        return null;
    }

    String getRequest() throws UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        builder.append("login=" + login);
        builder.append("&password=" + password);
        builder.append("&deviceType=" + deviceType);
        builder.append("&deviceId=" + deviceId);
        return builder.toString();
    }
}
