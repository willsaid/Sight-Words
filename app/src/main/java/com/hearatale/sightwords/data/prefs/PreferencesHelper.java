package com.hearatale.sightwords.data.prefs;


import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.PieceModel;
import com.hearatale.sightwords.data.model.phonics.letters.PuzzlePieceModel;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;

import java.util.List;
import java.util.Map;

public interface PreferencesHelper {

    void removePref(String name);

    void clearPref();

    public void setAnswersWithoutMistake(String sightWord, int answersWithoutMistake);

    public int getAnswersWithoutMistake(String sightWord);

    Map<String, List<LetterModel>> getLetters();

    void setPiecesCompletedByLetter(String letter, List<PieceModel> piecesCompleted);

    List<PieceModel> getPiecesCompletedByLetter(String letter);

    void savePuzzlePiece(String displayLetter, PuzzlePieceModel puzzlePiece);

    void savePuzzlePiece(String displayLetter, List<PuzzlePieceModel> puzzlePieces);

    List<PuzzlePieceModel> getCompletedPuzzlePieces(String displayLetter);

    void savePuzzleBase64(String displayLetter, String base64);

    String getPuzzleBase64(String displayLetter);

    List<SightWordModel> getSightWords(@SightWordsCategoryDef int category);

    void setTotalGoldCount(String appFeature, int count);

    int getTotalGoldCount(String appFeature);

    void setTotalSilverCount(String appFeature, int count);

    int getTotalSilverCount(String appFeature);

    void setSightWordStarCount(String text,int count);

    int getSightWordStarCount(String text);

    int getStarConsecutive(String displayLetter);

    void saveStarConsecutive(String displayLetter, int starConsecutive);

}
