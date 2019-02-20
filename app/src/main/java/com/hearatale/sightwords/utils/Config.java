package com.hearatale.sightwords.utils;

import com.hearatale.sightwords.BuildConfig;
import com.hearatale.sightwords.data.model.typedef.DevelopDef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class Config {

    public static final boolean PH_DEVELOPING = BuildConfig.ISDEVELOPING.equals(DevelopDef.WORKING);

    public static final boolean PH_PRODUCTION = BuildConfig.ISDEVELOPING.equals(DevelopDef.NONE);

    public static final String ASSETS_PATH = "file:///android_asset/";

    public static final String IMAGES_SIMPLE_LETTER_PATH = Config.ASSETS_PATH + "Images/Words/Easy Letter Icons/";

    public static final String IMAGES_WORD_BY_LETTER_PATH = Config.ASSETS_PATH + "Images/Words/";

    public static final String AUDIO_BY_LETTER_PATH = "Audio/Sounds/Extra Letters/";

    public static final String SOUND_PATH = "Sounds/";

    public static final String IMAGES_SIMPLE_PATH = Config.ASSETS_PATH + "Images/";

    public static final String AUDIO_ROOT_PATH = "Audio/";
    public static final String AUDIO_WORDS_PATH = "Audio/Words/";
    public static final String AUDIO_WORDS_SETS_PATH = "Audio/Word Sets/";
    public static final String AUDIO_SOUND_PATH = "Audio/Sounds/";
    public static final String AUDIO_SOUND_EXTRA_PATH = "Audio/Sounds/Extra Letters/";
    public static final String AUDIO_RHYMES_TEXT = "Audio/Rhymes/ Rhyme Text/";
    public static final String AUDIO_RHYMES = "Audio/Rhymes/";

    public static final String[] ALPHABET_LETTERS = {"A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L",
            "M", "N", "O", "P", "Q", "R",
            "S", "T", "U", "V", "W", "X", "Y", "Z"};

    public static final int PUZZLE_COLUMNS = 3;

    public static final int PUZZLE_ROWS = 4;

    public static final List<List<String>> HOMOPHONE_GROUPS = new ArrayList<>(Collections.singleton(Arrays.asList("to", "too", "two")));

    public static final int MAX_PUZZLE_COMPLETED = 2;
    public static final int MIN_STAR_CONSECUTIVE = 1;
    public static final int MAX_STAR_CONSECUTIVE = 5;
}
