package com.hearatale.sightwords.data.model.phonics.letters;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hearatale.sightwords.data.model.base.BaseModel;

public class SourceLetterTimingModel extends BaseModel {

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

    public SourceLetterTimingModel() {
    }

    protected SourceLetterTimingModel(Parcel in) {
        this.fileName = in.readString();
        this.wordStart = in.readString();
        this.wordDuration = in.readString();
    }

    public static final Creator<SourceLetterTimingModel> CREATOR = new Creator<SourceLetterTimingModel>() {
        @Override
        public SourceLetterTimingModel createFromParcel(Parcel source) {
            return new SourceLetterTimingModel(source);
        }

        @Override
        public SourceLetterTimingModel[] newArray(int size) {
            return new SourceLetterTimingModel[size];
        }
    };
}