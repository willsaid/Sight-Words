package com.hearatale.sightword.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    //convert string to spanned include color
    public static Spanned getSpannedFromString(String text, String regex, int color) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        Spannable spannable = new SpannableString(text);

        // Check all occurrences
        while (matcher.find()) {
            spannable.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public static Spanned getMatchesSpannedFromString(String text, String regex, int color) {
        regex = "\\b" + regex.toLowerCase() +"\\b"; // Regex to match only whole word
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());
        Spannable spannable = new SpannableString(text);

        // Check all occurrences
        while (matcher.find()) {
            spannable.setSpan(new ForegroundColorSpan(color), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }


    public static int  getMatchesCount(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text.toLowerCase());
        Spannable spannable = new SpannableString(text);

        int count = 0;
        // Check all occurrences
        while (matcher.find()) {
           count++;
        }
        return count;
    }


    /**
     * Bitmap to bytes.
     *
     * @param bitmap The bitmap.
     * @param format The format of bitmap.
     * @return bytes
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, 100, baos);
        return baos.toByteArray();
    }

    /**
     * Bytes to bitmap.
     *
     * @param bytes The bytes.
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0)
                ? null
                : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static CharSequence setColorText(CharSequence text, String patternString, int color) {

        Pattern pattern = Pattern.compile(patternString);

        SpannableStringBuilder ssb = new SpannableStringBuilder(text);

        if (pattern != null) {
            Matcher matcher = pattern.matcher(text);
            int matchesSoFar = 0;
            while (matcher.find()) {
                int start = matcher.start() - (matchesSoFar * 2);
                int end = matcher.end() - (matchesSoFar * 2);
                CharacterStyle span = new ForegroundColorSpan(color);
                ssb.setSpan(span, start + 1, end - 1, 0);
                ssb.delete(start, start + 1);
                ssb.delete(end - 2, end - 1);
                matchesSoFar++;
            }
        }
        return ssb;
    }


    public static int countSuccessive(int[] values, int target) {
        int maxLength = 0;
        int tempLength = 0;

        for (int value : values) {
            tempLength = (value == target) ? 1 + tempLength : 0;
            if (tempLength > maxLength) {
                maxLength = tempLength;
            }
        }

        return maxLength;
    }

    public static int countSuccessive(List<Integer> values, int target) {
        int maxLength = 0;
        int tempLength = 0;

        for (int value : values) {
            tempLength = (value == target) ? 1 + tempLength : 0;
            if (tempLength > maxLength) {
                maxLength = tempLength;
            }
        }

        return maxLength;
    }
}
