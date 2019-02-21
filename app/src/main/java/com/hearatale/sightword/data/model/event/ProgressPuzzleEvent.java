package com.hearatale.sightword.data.model.event;

import android.os.Parcel;

import com.hearatale.sightword.data.model.base.BaseModel;

public class ProgressPuzzleEvent extends BaseModel {

    String sourceLetterSoundId;

    public ProgressPuzzleEvent(String sourceSoundId) {
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

    protected ProgressPuzzleEvent(Parcel in) {
        this.sourceLetterSoundId = in.readString();
    }

    public static final Creator<ProgressPuzzleEvent> CREATOR = new Creator<ProgressPuzzleEvent>() {
        @Override
        public ProgressPuzzleEvent createFromParcel(Parcel source) {
            return new ProgressPuzzleEvent(source);
        }

        @Override
        public ProgressPuzzleEvent[] newArray(int size) {
            return new ProgressPuzzleEvent[size];
        }
    };
}
