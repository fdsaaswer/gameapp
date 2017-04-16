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
    // TODO replace all arguments with map and hard-coded list of strings "FIELDS"

    final String id; // weird that id is not int, but oh well
    final String language;
    final String url; // add validity checks?
    final String country;
    final String statusId;
    final String status; // replace with enum?
    final String mapUrl; // add validity checks?
    final String name;

    WorldInfo(JSONObject worldDescription) throws JSONException {
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

    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof  WorldInfo))
            return super.equals(otherObject);
        WorldInfo other = (WorldInfo) otherObject;

        if (!this.id.equals(other.id)) return false;
        if (!this.language.equals(other.language)) return false;
        if (!this.url.equals(other.url)) return false;
        if (!this.country.equals(other.country)) return false;
        if (!this.statusId.equals(other.statusId)) return false;
        if (!this.status.equals(other.status)) return false;
        if (!this.mapUrl.equals(other.mapUrl)) return false;
        if (!this.name.equals(other.name)) return false;

        return true;
    }
}
