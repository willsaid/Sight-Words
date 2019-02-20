package com.hearatale.sightwords.data.model.phonics;

import android.graphics.Bitmap;

import com.chad.library.adapter.base.entity.SectionEntity;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;

public class SectionQuiz extends SectionEntity<LetterModel> {

    private Bitmap puzzleCompleted;
    private boolean isCompleted;

    public SectionQuiz(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public SectionQuiz(LetterModel t, Bitmap puzzleCompleted) {
        super(t);
        this.puzzleCompleted = puzzleCompleted;
        isCompleted = true;
    }

    public SectionQuiz(LetterModel t) {
        super(t);
        isCompleted = false;
    }

    public Bitmap getPuzzleCompleted() {
        return puzzleCompleted;
    }

    public void setPuzzleCompleted(Bitmap puzzleCompleted) {
        this.puzzleCompleted = puzzleCompleted;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
