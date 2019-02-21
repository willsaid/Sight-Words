package com.hearatale.sightword.data.model.phonics;

import android.view.View;

import com.hearatale.sightword.data.model.phonics.letters.TimedAudioInfoModel;

public class QuizPuzzleLetterModel {

    View view;
    TimedAudioInfoModel audioInfo;

    public QuizPuzzleLetterModel(View view, TimedAudioInfoModel audioInfo) {
        this.view = view;
        this.audioInfo = audioInfo;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TimedAudioInfoModel getAudioInfo() {
        return audioInfo;
    }

    public void setAudioInfo(TimedAudioInfoModel audioInfo) {
        this.audioInfo = audioInfo;
    }
}
