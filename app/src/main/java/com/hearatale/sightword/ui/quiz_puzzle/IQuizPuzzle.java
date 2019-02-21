package com.hearatale.sightword.ui.quiz_puzzle;

import android.support.annotation.IdRes;

import com.hearatale.sightword.data.model.phonics.letters.LetterModel;
import com.hearatale.sightword.data.model.phonics.letters.WordModel;
import com.hearatale.sightword.ui.base.activity.IViewMVP;
import com.hearatale.sightword.ui.quiz_puzzle.content.PuzzleLetterFragment;
import com.hearatale.sightword.ui.quiz_puzzle.content.WordFragment;

import java.util.List;

public interface IQuizPuzzle extends IViewMVP {

    PuzzleLetterFragment getPuzzleLetterFragment(LetterModel letter, boolean puzzleRandom, @IdRes int puzzleColor);

    WordFragment getWordFragment(LetterModel letter, List<WordModel> selectedWords, boolean puzzleRandom, int[] wordSize);
}
