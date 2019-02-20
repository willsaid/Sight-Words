package com.hearatale.sightwords.data.model.phonics.letters;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hearatale.sightwords.data.model.base.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class LetterModel extends BaseModel {

    @SerializedName("sourceLetter")
    @Expose
    private String sourceLetter = "";
    @SerializedName("soundId")
    @Expose
    private String soundId = "";
    @SerializedName("ipaPronunciation")
    @Expose
    private String ipaPronunciation = "";
    @SerializedName("displayString")
    @Expose
    private String displayString = "";
    @SerializedName("primaryWords")
    @Expose
    private List<WordModel> primaryWords = new ArrayList<>();
    @SerializedName("quizWords")
    @Expose
    private List<WordModel> quizWords = new ArrayList<>();
    @SerializedName("sourceLetterTiming")
    @Expose
    private TimedAudioInfoModel sourceLetterTiming = new TimedAudioInfoModel();
    @SerializedName("pronunciationTiming")
    @Expose
    private TimedAudioInfoModel pronunciationTiming = new TimedAudioInfoModel();
    @SerializedName("colorString")
    @Expose
    private String colorString = "";

    private List<PuzzlePieceModel> puzzlePieces = new ArrayList<>();

    public String getSourceLetter() {
        return sourceLetter;
    }

    public void setSourceLetter(String sourceLetter) {
        this.sourceLetter = sourceLetter;
    }

    public String getSoundId() {
        return soundId;
    }

    public void setSoundId(String soundId) {
        this.soundId = soundId;
    }

    public String getIpaPronunciation() {
        return ipaPronunciation;
    }

    public void setIpaPronunciation(String ipaPronunciation) {
        this.ipaPronunciation = ipaPronunciation;
    }

    public String getDisplayString() {
        return displayString;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    public List<WordModel> getPrimaryWords() {
        return primaryWords;
    }

    public void setPrimaryWords(List<WordModel> primaryWords) {
        this.primaryWords = primaryWords;
    }

    public List<WordModel> getQuizWords() {
        return quizWords;
    }

    public void setQuizWords(List<WordModel> quizWords) {
        this.quizWords = quizWords;
    }

    public TimedAudioInfoModel getSourceLetterTiming() {
        return sourceLetterTiming;
    }

    public void setSourceLetterTiming(TimedAudioInfoModel sourceLetterTiming) {
        this.sourceLetterTiming = sourceLetterTiming;
    }

    public TimedAudioInfoModel getPronunciationTiming() {
        return pronunciationTiming;
    }

    public void setPronunciationTiming(TimedAudioInfoModel pronunciationTiming) {
        this.pronunciationTiming = pronunciationTiming;
    }

    public String getColorString() {
        return colorString;
    }

    public void setColorString(String colorString) {
        this.colorString = colorString;
    }

    public List<PuzzlePieceModel> getPuzzlePieces() {
        return puzzlePieces;
    }

    public void setPuzzlePieces(List<PuzzlePieceModel> puzzlePieces) {
        this.puzzlePieces = puzzlePieces;
    }

    public LetterModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sourceLetter);
        dest.writeString(this.soundId);
        dest.writeString(this.ipaPronunciation);
        dest.writeString(this.displayString);
        dest.writeTypedList(this.primaryWords);
        dest.writeTypedList(this.quizWords);
        dest.writeParcelable(this.sourceLetterTiming, flags);
        dest.writeParcelable(this.pronunciationTiming, flags);
        dest.writeString(this.colorString);
        dest.writeTypedList(this.puzzlePieces);
    }

    protected LetterModel(Parcel in) {
        this.sourceLetter = in.readString();
        this.soundId = in.readString();
        this.ipaPronunciation = in.readString();
        this.displayString = in.readString();
        this.primaryWords = in.createTypedArrayList(WordModel.CREATOR);
        this.quizWords = in.createTypedArrayList(WordModel.CREATOR);
        this.sourceLetterTiming = in.readParcelable(TimedAudioInfoModel.class.getClassLoader());
        this.pronunciationTiming = in.readParcelable(TimedAudioInfoModel.class.getClassLoader());
        this.colorString = in.readString();
        this.puzzlePieces = in.createTypedArrayList(PuzzlePieceModel.CREATOR);
    }

    public static final Creator<LetterModel> CREATOR = new Creator<LetterModel>() {
        @Override
        public LetterModel createFromParcel(Parcel source) {
            return new LetterModel(source);
        }

        @Override
        public LetterModel[] newArray(int size) {
            return new LetterModel[size];
        }
    };
}
