package com.hearatale.sightwords.data.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.hearatale.sightwords.Application;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.PieceModel;
import com.hearatale.sightwords.data.model.phonics.letters.PuzzlePieceModel;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightwords.data.network.api.base.json.JsonArray;
import com.hearatale.sightwords.data.network.api.base.json.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_FILE_NAME = "Phonics";

    private final SharedPreferences mPref;
    private final SharedPreferences.Editor mEditor;

    private final Gson mGson;

    @SuppressLint("CommitPrefEdits")
    public AppPreferencesHelper(Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
        mGson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSz")
                .create();
    }

    public void setAnswersWithoutMistake(String sightWord, int answersWithoutMistake) {
        mEditor.putInt(sightWord, answersWithoutMistake);
        mEditor.apply();
    }

    public int getAnswersWithoutMistake(String sightWord) {
        return mPref.getInt(sightWord, 0);
    }

    @Override
    public void removePref(String name) {
        mEditor.remove(name).apply();
    }

    @Override
    public void clearPref() {
        mEditor.clear().apply();
    }

    @Override
    public Map<String, List<LetterModel>> getLetters() {
        return getMapLettersInternal();
    }

    private TreeMap<String, List<LetterModel>> getMapLettersInternal() {
        String jsonLetters = "";
        try {
            InputStream inputStream = Application.Context.getAssets().open("Files/Letters.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonLetters = new String(buffer, "UTF-8");

            // parse
            JsonArray jsonArray = new JsonArray(jsonLetters);

            TreeMap<String, List<LetterModel>> mapLetter = new TreeMap<>();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.get(i);


                if (jsonObject == null) {
                    continue;
                }

                // get letters via key
                Set<String> keys = jsonObject.getKeys();

                for (String key : keys) {
                    JsonArray jsonArr = jsonObject.getJsonArray(key);

                    Type listType = new TypeToken<List<LetterModel>>() {
                    }.getType();

                    List<LetterModel> letter = mGson.fromJson(jsonArr.toString(), listType);

                    mapLetter.put(key, letter);
                }
            }

            return mapLetter;
        } catch (Exception e) {
            return new TreeMap<>();
        }


    }

    @Deprecated
    @Override
    public void setPiecesCompletedByLetter(String letter, List<PieceModel> piecesCompleted) {
        String json = mGson.toJson(piecesCompleted);
        mEditor.putString(Constants.Preferences.SIMPLE_LETTERS + letter.toUpperCase(), json);
        mEditor.apply();
    }

    @Deprecated
    @Override
    public List<PieceModel> getPiecesCompletedByLetter(String letter) {
        Type listType = new TypeToken<List<PieceModel>>() {
        }.getType();
        List<PieceModel> listPiecesCompleted = new ArrayList<>();
        if (mPref.contains(Constants.Preferences.SIMPLE_LETTERS + letter)) {
            listPiecesCompleted = mGson.fromJson(mPref.getString(Constants.Preferences.SIMPLE_LETTERS + letter.toUpperCase(), ""), listType);
            if (!com.hearatale.sightwords.utils.Helper.isListValid(listPiecesCompleted)) {
                return new ArrayList<>();
            }
        }
        return listPiecesCompleted;
    }

    @Override
    public void savePuzzlePiece(String displayLetter, PuzzlePieceModel puzzlePiece) {
        List<PuzzlePieceModel> puzzlePieces = getCompletedPuzzlePieces(displayLetter);
        for (PuzzlePieceModel puzzle : puzzlePieces) {
            if (puzzle.getId().equals(puzzlePiece.getId())) {
                return;
            }
        }

        puzzlePieces.add(puzzlePiece);

        savePuzzlePiece(displayLetter, puzzlePieces);

    }

    @Override
    public void savePuzzlePiece(String displayLetter, List<PuzzlePieceModel> puzzlePieces) {
        String json = mGson.toJson(puzzlePieces);
        mEditor.putString(displayLetter, json);
        mEditor.apply();
    }

    @Override
    public List<PuzzlePieceModel> getCompletedPuzzlePieces(String displayLetter) {
        Type listType = new TypeToken<List<PuzzlePieceModel>>() {
        }.getType();
        List<PuzzlePieceModel> data = new ArrayList<>();
        if (mPref.contains(displayLetter)) {
            data = mGson.fromJson(mPref.getString(displayLetter, ""), listType);
        }
        return data;
    }

    @Override
    public void savePuzzleBase64(String displayLetter, String base64) {
        mEditor.putString(Constants.Preferences.PUZZLE_BASE_64 + displayLetter, base64);
        mEditor.apply();
    }

    @Override
    public String getPuzzleBase64(String displayLetter) {
        return mPref.getString(Constants.Preferences.PUZZLE_BASE_64 + displayLetter, "");
    }

    @Override
    public List<SightWordModel> getSightWords(@SightWordsCategoryDef int category) {
        return getSighWordsInternal(category);
    }

    private List<SightWordModel> getSighWordsInternal(@SightWordsCategoryDef int category) {

        String jsonLetters = "";
        try {
            String categoryName = category == SightWordsCategoryDef.PRE_K ? "preK" : "kindergarten";
            String filename = "Files/" + categoryName + ".json";
            InputStream inputStream = Application.Context.getAssets().open(filename);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            jsonLetters = new String(buffer, "UTF-8");
            Type listType = new TypeToken<List<SightWordModel>>() {
            }.getType();

            return mGson.fromJson(jsonLetters, listType);
        } catch (IOException e) {
            return new ArrayList<>();
        }

    }

    private void setSightWordsPrefInternal(String jsonString, @SightWordsCategoryDef int category) {
        String key = category == SightWordsCategoryDef.PRE_K
                ? Constants.Preferences.KEY_PRE_K_SIGHT_WORDS
                : Constants.Preferences.KEY_KINDERGARTEN_SIGHT_WORDS;
        mEditor.putString(key, jsonString);
        mEditor.apply();
    }

    private String getSightWordsPrefInternal(@SightWordsCategoryDef int category) {
        String key = category == SightWordsCategoryDef.PRE_K
                ? Constants.Preferences.KEY_PRE_K_SIGHT_WORDS
                : Constants.Preferences.KEY_KINDERGARTEN_SIGHT_WORDS;
        return mPref.getString(key, "");
    }

    @Override
    public void setTotalGoldCount(String appFeature, int count) {
        mEditor.putInt(Constants.Preferences.TOTAL_GOLD_COINS + appFeature, count);
        mEditor.apply();
    }

    @Override
    public int getTotalGoldCount(String appFeature) {
        return mPref.getInt(Constants.Preferences.TOTAL_GOLD_COINS + appFeature, 0);
    }

    @Override
    public void setTotalSilverCount(String appFeature, int count) {
        mEditor.putInt(Constants.Preferences.TOTAL_SILVER_COINS + appFeature, count);
        mEditor.apply();
    }

    @Override
    public int getTotalSilverCount(String appFeature) {
        return mPref.getInt(Constants.Preferences.TOTAL_SILVER_COINS + appFeature, 0);
    }

    @Override
    public void setSightWordStarCount(String text, int count) {
        String key = String.format(Constants.Preferences.SIGHT_WORD_STAR_COUNT, text);
        mEditor.putInt(key, count);
        mEditor.apply();
    }

    @Override
    public int getSightWordStarCount(String text) {
        String key = String.format(Constants.Preferences.SIGHT_WORD_STAR_COUNT, text);
        return mPref.getInt(key, 0);
    }

    @Override
    public int getStarConsecutive(String displayLetter) {
        return mPref.getInt(Constants.Preferences.PUZZLE_STAR_CONSECUTIVE + displayLetter, 0);
    }

    @Override
    public void saveStarConsecutive(String displayLetter, int starConsecutive) {
        mEditor.putInt(Constants.Preferences.PUZZLE_STAR_CONSECUTIVE + displayLetter, starConsecutive);
        mEditor.apply();
    }
}
