package com.hearatale.sightwords.data.model.phonics.letters;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hearatale.sightwords.data.model.base.BaseModel;

public class PronunciationTimingModel extends BaseModel {

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

    public PronunciationTimingModel() {
    }

    protected PronunciationTimingModel(Parcel in) {
        this.fileName = in.readString();
        this.wordStart = in.readString();
        this.wordDuration = in.readString();
    }

    public static final Creator<PronunciationTimingModel> CREATOR = new Creator<PronunciationTimingModel>() {
        @Override
        public PronunciationTimingModel createFromParcel(Parcel source) {
            return new PronunciationTimingModel(source);
        }

        @Override
        public PronunciationTimingModel[] newArray(int size) {
            return new PronunciationTimingModel[size];
        }
    };
}