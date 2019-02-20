package com.hearatale.sightwords.utils.glide;


import android.os.Parcel;
import android.os.Parcelable;

import com.hearatale.sightwords.data.model.base.BaseModel;


public class StreamModel extends BaseModel {
    private String streamUrl;

    public StreamModel(String streamUrl) {
        this.streamUrl = streamUrl;
    }

    public String getStreamUrl() {
        return streamUrl;
    }

    public boolean isLocal() {
        return !streamUrl.contains("http");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.streamUrl);
    }

    protected StreamModel(Parcel in) {
        this.streamUrl = in.readString();
    }

    public static final Parcelable.Creator<StreamModel> CREATOR = new Parcelable.Creator<StreamModel>() {
        @Override
        public StreamModel createFromParcel(Parcel source) {
            return new StreamModel(source);
        }

        @Override
        public StreamModel[] newArray(int size) {
            return new StreamModel[size];
        }
    };
}
