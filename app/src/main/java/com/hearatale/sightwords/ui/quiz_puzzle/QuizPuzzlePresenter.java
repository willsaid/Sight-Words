package com.hearatale.sightwords.ui.quiz_puzzle;

import com.hearatale.sightwords.data.AppDataManager;
import com.hearatale.sightwords.data.DataManager;
import com.hearatale.sightwords.data.model.event.StarEvent;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.phonics.letters.PuzzlePieceModel;
import com.hearatale.sightwords.data.model.phonics.letters.WordModel;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.ui.base.activity.PresenterMVP;
import com.hearatale.sightwords.utils.Config;
import com.hearatale.sightwords.utils.Helper;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class QuizPuzzlePresenter extends PresenterMVP<IQuizPuzzle> implements IQuizPuzzlePresenter {

    private DataManager mDataManager;

    public QuizPuzzlePresenter() {
        mDataManager = AppDataManager.getInstance();
    }

    private CopyOnWriteArrayList<WordModel> mAllPossibleWord = new CopyOnWriteArrayList<>();
    private LetterModel mLetter = new LetterModel();
    private List<WordModel> mAnswerWords = new ArrayList<>();
    private int mCurrentPosition = 0;

    @Override
    public void setAnswersWithoutMistake(String sightWord, int answersWithoutMistake) {
        mDataManager.setAnswersWithoutMistake(sightWord, answersWithoutMistake);
    }

    @Override
    public int getAnswersWithoutMistake(String sightWord) {
        return mDataManager.getAnswersWithoutMistake(sightWord);
    }

    @Override
    public void savePuzzlePiece(String displayLetter, PuzzlePieceModel puzzlePiece) {
        mDataManager.savePuzzlePiece(displayLetter, puzzlePiece);
    }

    @Override
    public void savePuzzlePiece(String displayLetter, List<PuzzlePieceModel> puzzlePieces) {
        mDataManager.savePuzzlePiece(displayLetter, puzzlePieces);
    }

    @Override
    public List<PuzzlePieceModel> getCompletedPuzzlePieces(String displayLetter) {
        return mDataManager.getCompletedPuzzlePieces(displayLetter);
    }

    @Override
    public void savePuzzleBase64(String displayLetter, String base64) {
        mDataManager.savePuzzleBase64(displayLetter, base64);
    }

    @Override
    public String getPuzzleBase64(String displayLetter) {
        return mDataManager.getPuzzleBase64(displayLetter);
    }

    @Override
    public LetterModel randomLetter(@DifficultyDef int diffDef) {
        return mDataManager.randomLetter(diffDef);
    }

    @Override
    public List<WordModel> getSelectedWords() {

        // TODO get selectedWords
        return new ArrayList<>();
    }

    @Override
    public CopyOnWriteArrayList<WordModel> getSelectedWords(LetterModel letter, boolean puzzleRandom) {

        if (mLetter != letter) {
            mLetter = letter;
            mAllPossibleWord = new CopyOnWriteArrayList(mDataManager.getAllPossibleWords(mLetter));

            //If soundID = "AW", remove incorrect answer choices that contain letter 'o'
            if (letter.getSoundId().equals("AW")) {
                for (WordModel word : mAllPossibleWord) {
                    if (word.getText().contains("o")) {
                        mAllPossibleWord.remove(word);
                    }
                }
            }

            //If soundID = "ET", remove incorrect answer choices that contain phrase 'et'
            if (letter.getSoundId().equals("ET")) {
                for (WordModel word : mAllPossibleWord) {
                    if (word.getText().contains("et")) {
                        mAllPossibleWord.remove(word);
                    }
                }
            }

            if (letter.getSourceLetter().equals("E") && letter.getSoundId().equals("schwa")) {
                for (WordModel word : mAllPossibleWord) {
                    if (word.getText().contains("dandelion")) {
                        mAllPossibleWord.remove(word);
                    }
                }
            }


            if (letter.getSoundId().equals("wuh")) {
                for (WordModel word : mAllPossibleWord) {
                    if (word.getText().contains("window")) {
                        mAllPossibleWord.remove(word);
                    }
                }
            }

            //If soundID = "zz", remove incorrect answer choices that contain letter 'z' or 'c'
            if (letter.getSoundId().equals("zz")) {
                for (WordModel word : mAllPossibleWord) {
                    if (word.getText().contains("z") || word.getText().contains("c")) {
                        mAllPossibleWord.remove(word);
                    }
                }
            }

            //If soundID = "i", remove incorrect answer choices that contain letter 'i' or 'e'
            if (letter.getSoundId().equals("i")) {
                for (WordModel word : mAllPossibleWord) {
                    if (word.getText().contains("i") || word.getText().contains("e")) {
                        mAllPossibleWord.remove(word);
                    }
                }
            }


            mAnswerWords = new ArrayList<>();
            mAnswerWords = letter.getPrimaryWords();
            mAnswerWords.addAll(letter.getQuizWords());
        }

        CopyOnWriteArrayList<WordModel> selectedWords = new CopyOnWriteArrayList<>();

        // get selectedWords puzzleRandom: true -> 4, false -> 3
        int totalWords = puzzleRandom ? 4 : 3;

        // add answerWord
        // get random answerWords
        WordModel answerWord;
        if (puzzleRandom) {
            answerWord = Helper.randomItems(mAnswerWords);
        } else {
            if (mCurrentPosition >= mAnswerWords.size()) {
                mCurrentPosition = 0;
            }
            answerWord = mAnswerWords.get(mCurrentPosition);
            mCurrentPosition++;
        }
        answerWord.setAnswer(true);
        selectedWords.add(answerWord);


        // add wrong possible word
        while (selectedWords.size() < totalWords) {

//            Collections.shuffle(mAllPossibleWord);

            WordModel word = Helper.randomItems(mAllPossibleWord);
//            if (!selectedWords.contains(word)) {
//                selectedWords.add(word);
//            }

            boolean find = false;
            for (WordModel w : selectedWords) {
                if (w.getText().equals(word.getText())) {
                    find = true;
                    break;
                }
            }

            if (!find) {
                // prevent prev answer set is true
                word.setAnswer(false);
                selectedWords.add(word);
            }

        }

        // shuffle selectedWords
        Collections.shuffle(selectedWords);
        return selectedWords;
    }

    @Override
    public int getStarConsecutive(String displayLetter) {
        return mDataManager.getStarConsecutive(displayLetter);
    }

    @Override
    public void saveStarConsecutive(String displayLetter, int starConsecutive) {
        int currentStarConsecutive = getStarConsecutive(displayLetter);

        if (starConsecutive > currentStarConsecutive && starConsecutive >= Config.MIN_STAR_CONSECUTIVE) {
            mDataManager.saveStarConsecutive(displayLetter, starConsecutive);

            boolean isCompleted = mDataManager.isPuzzleCompleted(displayLetter);
            if (isCompleted) {
                EventBus.getDefault().post(new StarEvent(displayLetter, starConsecutive));
            }

        }
    }

}
