package com.hearatale.sightwords.data;

public final class Constants {


    public static final class Preferences {
        public static final String SIMPLE_LETTERS = "SIMPLE_LETTERS_";
        public static final String PUZZLE_BASE_64 = "PUZZLE_BASE_64";
        public static final String PUZZLE_STAR_CONSECUTIVE = "PUZZLE_STAR_CONSECUTIVE";
        public static final String KEY_PRE_K_SIGHT_WORDS = "KEY_PRE_K_SIGHT_WORDS";
        public static final String KEY_KINDERGARTEN_SIGHT_WORDS = "KEY_KINDERGARTEN_SIGHT_WORDS";
        public static final String TOTAL_GOLD_COINS = "TOTAL_GOLD_COINS";
        public static final String TOTAL_SILVER_COINS = "TOTAL_SILVER_COINS";
        public static final String SIGHT_WORD_STAR_COUNT = "SIGHT_WORD_STAR_COUNT-%s";
    }


    public static final class Arguments {

        //ALPHABET LETTER - PHONICS
        public static final String ARG_MAIN_DEF = "ARG_MAIN_DEF";
        public static final String ARG_LETTER_MODE = "ARG_LETTER_MODE";
        public static final String ARG_LETTER_TEXT = "ARG_LETTER_TEXT";
        public static final String ARG_LETTER = "ARG_LETTER";
        public static final String ARG_PUZZLE_RANDOM = "ARG_PUZZLE_RANDOM";
        public static final String ARG_SOUND_ID = "ARG_SOUND_ID";
        public static final String ARG_SIGHT_WORD_MODE = "ARG_SIGHT_WORD_MODE";
        public static final String ARG_BLUR_BITMAP = "ARG_BLUR_BITMAP";
        public static final String ARG_REQUEST_CODE = "ARG_REQUEST_CODE";
        public static final String ARG_BACKGROUND_COLOR = "ARG_BACKGROUND_COLOR";

        //SIGHT WORD
        public static final String SIGHT_WORD_MODE = "SIGHT_WORD_MODE";
        public static final String SIGHT_WORD_CATEGORY = "SIGHT_WORD_CATEGORY";
        public static final String SPECIFIC_SIGHT_WORD = "SPECIFIC_SIGHT_WORD";

        public static final String ARG_SIGHT_WORDS = "ARG_SIGHT_WORDS";
        public static final String ARG_SIGHT_WORD_POSITION = "ARG_SIGHT_WORD_POSITION";

        public static final String ARG_PLAY_CELEBRATION = "ARG_PLAY_CELEBRATION";
    }

    public static final class ResultCode{
        public static final int RELOAD_LETTER_CODE = 223;
        public static final int SETUP_NEW_WORD_CODE = 293;
    }

}
