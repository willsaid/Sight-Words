package com.hearatale.sightword.data.model.phonics;

import android.os.Parcel;

import com.hearatale.sightword.data.model.base.BaseModel;
import com.hearatale.sightword.utils.Config;

public class SentenceModel extends BaseModel {

    private String text = "";
    private String highlightWord = "";
    private String audioFileName = "";
    private String imageFileName = "";

    public SentenceModel() {
    }

    public SentenceModel(String text, String highlightWord, String audioFileName, String imageFileName) {
        this.text = text;
        this.highlightWord = highlightWord;
        this.audioFileName = audioFileName;
        this.imageFileName = imageFileName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHighlightWord() {
        return highlightWord;
    }

    public void setHighlightWord(String highlightWord) {
        this.highlightWord = highlightWord;
    }

    public String getAudioFileName() {
        return Config.AUDIO_ROOT_PATH + audioFileName;
    }

    public void setAudioFileName(String audioFileName) {
        this.audioFileName = audioFileName;
    }

    public String getImageFileName() {
        return Config.IMAGES_SIMPLE_PATH + imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.highlightWord);
        dest.writeString(this.audioFileName);
        dest.writeString(this.imageFileName);
    }

    protected SentenceModel(Parcel in) {
        this.text = in.readString();
        this.highlightWord = in.readString();
        this.audioFileName = in.readString();
        this.imageFileName = in.readString();
    }

    public static final Creator<SentenceModel> CREATOR = new Creator<SentenceModel>() {
        @Override
        public SentenceModel createFromParcel(Parcel source) {
            return new SentenceModel(source);
        }

        @Override
        public SentenceModel[] newArray(int size) {
            return new SentenceModel[size];
        }
    };
}
