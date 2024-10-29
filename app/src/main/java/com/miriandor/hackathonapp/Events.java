package com.miriandor.hackathonapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Events implements Parcelable {
    String name;
    String category;
    String address;
    LatLng latLng;
    Date date;
    String description;
    String imageURL;

    public Events(String name, String category, String address, LatLng latLng, Date date, String description, String imageUrl){
        this.name=name;
        this.category = category;
        this.address = address;
        this.latLng = latLng;
        this.date = date;
        this.description = description;
        this.imageURL = imageUrl;
    }

    protected Events(Parcel in) {
        name = in.readString();
        category = in.readString();
        address = in.readString();
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        date=new Date(in.readLong());
        description = in.readString();
        imageURL = in.readString();

    }

    public static final Creator<Events> CREATOR = new Creator<Events>() {
        @Override
        public Events createFromParcel(Parcel in) {
            return new Events(in);
        }

        @Override
        public Events[] newArray(int size) {
            return new Events[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeString(address);
        dest.writeParcelable(latLng,flags);
        dest.writeLong(date.getTime());
        dest.writeString(description);
        dest.writeString(imageURL);
    }
}
