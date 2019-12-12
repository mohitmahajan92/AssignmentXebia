package com.example.assignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Model implements Parcelable {

    public String strTitle;
    public String strDate;
    public String strAbstract;
    public String strSource;
    public ArrayList<MediaModel> arrayListMedia;


    public Model(Parcel in) {
        super();
        readFromParcel(in);
    }

    public Model() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(strTitle);
        dest.writeString(strDate);
        dest.writeString(strAbstract);
        dest.writeString(strSource);


        dest.writeSerializable(arrayListMedia);
    }


    public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        public Model[] newArray(int size) {

            return new Model[size];
        }

    };

    public void readFromParcel(Parcel dest) {
        strTitle = dest.readString();
        strDate= dest.readString();
        strAbstract = dest.readString();
        strSource = dest.readString();
        arrayListMedia = (ArrayList<MediaModel>) dest.readSerializable();
    }
}
