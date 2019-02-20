package com.hearatale.sightwords.data.model.event;

import android.os.Parcel;

import com.hearatale.sightwords.data.model.base.BaseModel;

public class CompletedPuzzleEvent extends BaseModel {

    String sourceLetterSoundId;

    public CompletedPuzzleEvent(String sourceSoundId) {
        this.sourceLetterSoundId = sourceSoundId;
    }

    public String getSourceLetterSoundId() {
        return sourceLetterSoundId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sourceLetterSoundId);
    }

    protected CompletedPuzzleEvent(Parcel in) {
        this.sourceLetterSoundId = in.readString();
    }

    public static final Creator<CompletedPuzzleEvent> CREATOR = new Creator<CompletedPuzzleEvent>() {
        @Override
        public CompletedPuzzleEvent createFromParcel(Parcel source) {
            return new CompletedPuzzleEvent(source);
        }

        @Override
        public CompletedPuzzleEvent[] newArray(int size) {
            return new CompletedPuzzleEvent[size];
        }
    };
}
