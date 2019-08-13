package com.example.todot;

import android.os.Parcel;
import android.os.Parcelable;

public class InfoDesc implements Parcelable {
    private String name;
    private boolean isSelected;

    public static final Parcelable.Creator<InfoDesc> CREATOR = new Parcelable.Creator<InfoDesc>() {
        public InfoDesc createFromParcel(Parcel in) {
            return new InfoDesc(in);
        }

        public InfoDesc[] newArray(int size) {
            return new InfoDesc[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeByte((byte) (this.isSelected ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public InfoDesc(Parcel in) {
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }
    public InfoDesc(String name){
        name = name;
    }
}