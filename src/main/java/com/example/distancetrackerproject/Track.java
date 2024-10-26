package com.example.distancetrackerproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Track {
    private String address;
    private int timeSpent;

    public Track(String address, int timeSpent) {
        this.address = address;
        this.timeSpent = timeSpent;
    }

    protected Track(Parcel in) {
        address = in.readString();
        timeSpent = in.readInt();
    }


    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };



    public String getAddress() {
        return address;
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    //@Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeInt(timeSpent);
    }

}
