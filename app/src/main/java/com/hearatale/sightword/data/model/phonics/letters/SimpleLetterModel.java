package com.hearatale.sightword.data.model.phonics.letters;

import android.os.Parcel;
import android.os.Parcelable;

public class SimpleLetterModel implements Parcelable{

    private String id;

    private String letter;

    private int progressCompleted;

    private TimedAudioInfoModel audioInfo;

    private String pathImage;

    private String colorString;

    private int starConsecutive;

    public SimpleLetterModel() {
    }

    public SimpleLetterModel(String id, String letter, int progressCompleted, TimedAudioInfoModel audioInfo, String pathImage, String colorString) {
        this.id = id;
        this.letter = letter;
        this.progressCompleted = progressCompleted;
        this.audioInfo = audioInfo;
        this.pathImage = pathImage;
        this.colorString = colorString;
    }


    public SimpleLetterModel(String id, String letter, int progressCompleted, TimedAudioInfoModel audioInfo, String pathImage, String colorString, int starConsecutive) {
        this.id = id;
        this.letter = letter;
        this.progressCompleted = progressCompleted;
        this.audioInfo = audioInfo;
        this.pathImage = pathImage;
        this.colorString = colorString;
        this.starConsecutive = starConsecutive;
    }


    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public int getProgressCompleted() {
        return progressCompleted;
    }

    public void setProgressCompleted(int progressCompleted) {
        this.progressCompleted = progressCompleted;
    }


    public TimedAudioInfoModel getAudioInfo() {
        return audioInfo;
    }

    public void setAudioInfo(TimedAudioInfoModel audioInfo) {
        this.audioInfo = audioInfo;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public int getStarConsecutive() {
        return starConsecutive;
    }

    public void setStarConsecutive(int starConsecutive) {
        this.starConsecutive = starConsecutive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.letter);
        dest.writeInt(this.progressCompleted);
        dest.writeParcelable(this.audioInfo, flags);
        dest.writeString(this.pathImage);
        dest.writeString(this.colorString);
        dest.writeInt(this.starConsecutive);
    }

    protected SimpleLetterModel(Parcel in) {
        this.id = in.readString();
        this.letter = in.readString();
        this.progressCompleted = in.readInt();
        this.audioInfo = in.readParcelable(TimedAudioInfoModel.class.getClassLoader());
        this.pathImage = in.readString();
        this.colorString = in.readString();
        this.starConsecutive = in.readInt();
    }

    public static final Creator<SimpleLetterModel> CREATOR = new Creator<SimpleLetterModel>() {
        @Override
        public SimpleLetterModel createFromParcel(Parcel source) {
            return new SimpleLetterModel(source);
        }

        @Override
        public SimpleLetterModel[] newArray(int size) {
            return new SimpleLetterModel[size];
        }
    };
}
