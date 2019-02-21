package com.hearatale.sightword.data.model.phonics.letters;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hearatale.sightword.data.model.base.BaseModel;

public class WordModel extends BaseModel {

    @SerializedName("text")
    @Expose
    private String text = "";
    @SerializedName("pronunciation")
    @Expose
    private String pronunciation = "";
    @SerializedName("timedAudioInfo")
    @Expose
    private TimedAudioInfoModel timedAudioInfo = new TimedAudioInfoModel();

    boolean isAnswer;

    public WordModel() {
    }

    public WordModel(String text, String pronunciation, TimedAudioInfoModel timedAudioInfo) {
        this.text = text;
        this.pronunciation = pronunciation;
        this.timedAudioInfo = timedAudioInfo;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public TimedAudioInfoModel getTimedAudioInfo() {
        return timedAudioInfo;
    }

    public void setTimedAudioInfo(TimedAudioInfoModel timedAudioInfo) {
        this.timedAudioInfo = timedAudioInfo;
    }

    public boolean isAnswer() {
        return isAnswer;
    }

    public void setAnswer(boolean answer) {
        isAnswer = answer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeString(this.pronunciation);
        dest.writeParcelable(this.timedAudioInfo, flags);
    }

    protected WordModel(Parcel in) {
        this.text = in.readString();
        this.pronunciation = in.readString();
        this.timedAudioInfo = in.readParcelable(TimedAudioInfoModel.class.getClassLoader());
    }

    public static final Creator<WordModel> CREATOR = new Creator<WordModel>() {
        @Override
        public WordModel createFromParcel(Parcel source) {
            return new WordModel(source);
        }

        @Override
        public WordModel[] newArray(int size) {
            return new WordModel[size];
        }
    };
}
