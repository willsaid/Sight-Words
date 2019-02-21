package com.hearatale.sightword.data.model.phonics;

import android.support.annotation.DrawableRes;

import com.hearatale.sightword.R;

public class InstructionsContent {
    public String text;

    @DrawableRes
    public int imageDrawable;

    private InstructionsContent(String text, int imagePath) {
        this.text = text;
        this.imageDrawable = imagePath;
    }

    public static InstructionsContent listen = new InstructionsContent("Listen...", R.mipmap.secrets);
    public static InstructionsContent chooseWord = new InstructionsContent("Choose the word", R.mipmap.question_big);
    public static InstructionsContent correct = new InstructionsContent("Correct! Good job!", R.mipmap.star);

}
