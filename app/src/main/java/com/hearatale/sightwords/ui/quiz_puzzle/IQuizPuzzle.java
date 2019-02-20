package com.hearatale.sightwords.ui.quiz_puzzle;

import android.support.annotation.IdRes;

import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.WordModel;
import com.hearatale.sightwords.ui.base.activity.IViewMVP;
import com.hearatale.sightwords.ui.quiz_puzzle.content.PuzzleLetterFragment;
import com.hearatale.sightwords.ui.quiz_puzzle.content.WordFragment;

import java.util.List;

public interface IQuizPuzzle extends IViewMVP {

    PuzzleLetterFragment getPuzzleLetterFragment(LetterModel letter, boolean puzzleRandom, @IdRes int puzzleColor);

    WordFragment getWordFragment(LetterModel letter, List<WordModel> selectedWords, boolean puzzleRandom, int[] wordSize);
}
