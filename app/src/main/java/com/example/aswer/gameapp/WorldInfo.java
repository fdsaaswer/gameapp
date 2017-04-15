package com.example.aswer.gameapp;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

/*
 * Parcelable is natural Android way for marshalling
 * using parcelable allows sharing this data seamlessly with native code
 * and removes dependency on JSON parsing in WorldsActivity
 * This class also should be used for validity checks
 */
final class WorldInfo implements Parcelable {
    public final String id; // weird that id is not int, but oh well
    public final String language;
    public final String url; // add validity checks?
    public final String country;
    public final String statusId;
    public final String status; // replace with enum?
    public final String mapUrl; // add validity checks?
    public final String name;

    WorldInfo(JSONObject worldDescription) throws JSONException {
        // we can remove code duplication here by using some of the java 8 features
        // but it's not fully supported everywhere yet
        this.id       = worldDescription.getString("id");
        this.language = worldDescription.getString("language");
        this.url      = worldDescription.getString("url");
        this.country  = worldDescription.getString("country");
        JSONObject statusObject = worldDescription.getJSONObject("worldStatus");
        this.statusId = statusObject.getString("id");
        this.status   = statusObject.getString("description");
        this.mapUrl   = worldDescription.getString("mapURL");
        this.name     = worldDescription.getString("name");
    }

    private WorldInfo(Parcel from) {
        this.id       = from.readString();
        this.language = from.readString();
        this.url      = from.readString();
        this.country  = from.readString();
        this.statusId = from.readString();
        this.status   = from.readString();
        this.mapUrl   = from.readString();
        this.name     = from.readString();
    }

    @Override
    public int describeContents(){
        return 0; // no file descriptors here
    }

    @Override
    public void writeToParcel(Parcel to, int flags) {
        to.writeString(this.id);
        to.writeString(this.language);
        to.writeString(this.url);
        to.writeString(this.country);
        to.writeString(this.statusId);
        to.writeString(this.status);
        to.writeString(this.mapUrl);
        to.writeString(this.name);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WorldInfo createFromParcel(Parcel in) {
            return new WorldInfo(in);
        }

        public WorldInfo[] newArray(int size) {
            return new WorldInfo[size];
        }
    };
}
