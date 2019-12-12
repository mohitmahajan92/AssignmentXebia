package com.example.assignment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class MediaModel implements Parcelable {

    public ArrayList<MediaMetaDataModel> mediaMetaDataModels;

    public MediaModel(Parcel in) {
        super();
        readFromParcel(in);
    }

    public MediaModel() {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {


        dest.writeSerializable(mediaMetaDataModels);
    }


    public static final Parcelable.Creator<MediaModel> CREATOR = new Parcelable.Creator<MediaModel>() {
        public MediaModel createFromParcel(Parcel in) {
            return new MediaModel(in);
        }

        public MediaModel[] newArray(int size) {

            return new MediaModel[size];
        }

    };

    public void readFromParcel(Parcel dest) {
        mediaMetaDataModels = (ArrayList<MediaMetaDataModel>) dest.readSerializable();
    }


}
