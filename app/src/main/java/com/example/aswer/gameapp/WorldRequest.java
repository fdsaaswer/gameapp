package com.example.aswer.gameapp;

import android.os.Build;
import android.util.Log;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

/*
 * Container for everything related to server request
 * deviceId/deviceType retrieval is subject to modification
 */

final class WorldRequest {
    private final String login;
    private final String password;
    private final String deviceType;
    private final String deviceId;

    WorldRequest(String email, String password) {
        this.login = email;
        this.password = password;
        this.deviceType = getDeviceType();
        this.deviceId = getDeviceId();
    }

    private static String getDeviceType() {
        return String.format("%s %s", Build.MODEL, Build.VERSION.RELEASE);
    }

    /*
     * BT and WiFi MACs are usually different
     * Some devices may not support all interfaces?
     * Anyway, it's unusual for device to support interface at one point of time
     * and not support in other
     * so just ask all of the interfaces, and send the first viable MAC found
     */
    private static String getDeviceId() {
        List<NetworkInterface> interfaces;
        try {
            interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            if (interfaces.size() == 0) {
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

    @Override
    public String toString() {
        String result = new String(); // safe to use String here, compiler is smart
        result += "login=" + login;
        result += "&password=" + password;
        result += "&deviceType=" + deviceType;
        result += "&deviceId=" + deviceId;
        return result;
    }
}
