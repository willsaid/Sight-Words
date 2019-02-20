package com.hearatale.sightwords.data;

import android.text.Spannable;

import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.WordModel;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightwords.data.prefs.PreferencesHelper;

import java.util.List;

public interface DataManager extends PreferencesHelper {
    boolean isPuzzleCompleted(LetterModel letter);

    boolean isPuzzleCompleted(String sourceSoundId);

    public void setAnswersWithoutMistake(String sightWord, int answersWithoutMistake);

    public int getAnswersWithoutMistake(String sightWord);

    List<WordModel> getAllWords();

    List<WordModel> getAllWordsNoduplicate();

    List<LetterModel> getLettersFirst();

    List<LetterModel> getAllLetters();

    LetterModel randomLetter(@DifficultyDef int diffDef);

    List<WordModel> getAllPossibleWords(LetterModel letter);

    boolean hasHomophoneConflict(SightWordModel primaryWord, List<SightWordModel> otherWords);

    boolean arrayHasHomophoneConflicts(List<SightWordModel> sightWords);

    List<SightWordModel> getSightWords(@SightWordsCategoryDef int category);

    public Spannable decorateWord(String wordText, String textHighlight, int color, String soundID);

}
