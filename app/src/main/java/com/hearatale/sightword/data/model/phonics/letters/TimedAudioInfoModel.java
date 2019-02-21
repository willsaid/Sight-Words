package com.hearatale.sightword.data.model.phonics.letters;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hearatale.sightword.data.model.base.BaseModel;

public class TimedAudioInfoModel extends BaseModel {

    @SerializedName("fileName")
    @Expose
    private String fileName = "";
    @SerializedName("wordStart")
    @Expose
    private String wordStart = "";
    @SerializedName("wordDuration")
    @Expose
    private String wordDuration = "";

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getWordStart() {
        return wordStart;
    }

    public void setWordStart(String wordStart) {
        this.wordStart = wordStart;
    }

    public String getWordDuration() {
        return wordDuration;
    }

    public void setWordDuration(String wordDuration) {
        this.wordDuration = wordDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
        dest.writeString(this.wordStart);
        dest.writeString(this.wordDuration);
    }

    public TimedAudioInfoModel() {
    }

    protected TimedAudioInfoModel(Parcel in) {
        this.fileName = in.readString();
        this.wordStart = in.readString();
        this.wordDuration = in.readString();
    }

    public static final Creator<TimedAudioInfoModel> CREATOR = new Creator<TimedAudioInfoModel>() {
        @Override
        public TimedAudioInfoModel createFromParcel(Parcel source) {
            return new TimedAudioInfoModel(source);
        }

        @Override
        public TimedAudioInfoModel[] newArray(int size) {
            return new TimedAudioInfoModel[size];
        }
    };
}