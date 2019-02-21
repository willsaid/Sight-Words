package com.hearatale.sightword.utils;

import android.content.Context;
import android.graphics.Typeface;

public final class FontsHelper {

    public static final String COMIC_NEUE_REGULAR_FONT_NAME = "ComicNeue-Regular.otf";
    public static final String COMIC_NEUE_BOLD_FONT_NAME = "ComicNeue_Bold.otf";
    public static final String COMIC_SANS_FONT_NAME = "ComicSans.ttf";

    private static Typeface fontComicNeueRegular;
    private static Typeface fontComicNeueBold;
    private static Typeface fontComicSansBold;

    public static Typeface getFontComicNeueRegular(Context context) {
        if (fontComicNeueRegular == null) {
            fontComicNeueRegular = Typeface.createFromAsset(context.getAssets(), "fonts/" + COMIC_NEUE_REGULAR_FONT_NAME);
        }
        return fontComicNeueRegular;
    }

    public static Typeface getFontComicNeueBold(Context context) {
        if (fontComicNeueBold == null) {
            fontComicNeueBold = Typeface.createFromAsset(context.getAssets(), "fonts/" + COMIC_NEUE_BOLD_FONT_NAME);
        }
        return fontComicNeueBold;
    }

    public static Typeface getComicSans(Context context) {
        if (fontComicSansBold == null) {
            fontComicSansBold = Typeface.createFromAsset(context.getAssets(), "fonts/" + COMIC_SANS_FONT_NAME);
        }
        return fontComicSansBold;
    }
}
