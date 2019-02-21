package com.hearatale.sightword.data;

import android.support.v4.util.ArrayMap;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.hearatale.sightword.Application;
import com.hearatale.sightword.data.model.phonics.SightWordModel;
import com.hearatale.sightword.data.model.phonics.letters.LetterModel;
import com.hearatale.sightword.data.model.phonics.letters.PieceModel;
import com.hearatale.sightword.data.model.phonics.letters.PuzzlePieceModel;
import com.hearatale.sightword.data.model.phonics.letters.WordModel;
import com.hearatale.sightword.data.model.typedef.DifficultyDef;
import com.hearatale.sightword.data.model.typedef.SightWordsCategoryDef;
import com.hearatale.sightword.data.prefs.AppPreferencesHelper;
import com.hearatale.sightword.data.prefs.PreferencesHelper;
import com.hearatale.sightword.utils.Config;
import com.hearatale.sightword.utils.Helper;
import com.hearatale.sightword.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;


public class AppDataManager implements DataManager {

    private static volatile AppDataManager Instance = null;
    private PreferencesHelper mPreferencesHelper;

    private static volatile Map<String, List<LetterModel>> LETTERS = null;

    private static volatile List<LetterModel> LETTERS_FIRST = null;

    private static volatile List<LetterModel> ALL_SOUNDS = null;

    private static volatile List<WordModel> ALL_WORDS = null;

    private static volatile List<SightWordModel> PRE_K_SIGHT_WORDS = null;

    private static volatile List<SightWordModel> KINDERGARTEN_SIGHT_WORDS = null;

    public static final Map<String, List<String>> MAP_LETTER_BLACK_LIST = new HashMap<String, List<String>>() {{
        put("g", asList("j"));
        put("j", asList("g"));
        put("c", asList("s", "grapes"));
        put("s", asList("c", "x", "grapes")); // add x to list
        put("o", asList("a"));
        put("e", asList("ing"));
        put("z", asList("grapes", "s", "ge", "c"));
        put("y", asList("e", "ee", "i")); // add line y
        put("a", asList("unicorn")); // add line a
    }};

    public static final Map<String, List<String>> MAP_SOUND_ID_BLACK_LIST = new HashMap<String, List<String>>() {{
        put("schwa", asList("nail", "lion", "footstool"));
        put("sss", asList("ts"));
        put("sh", asList("c", "s", "x", "z", "ge"));
    }};

    public static final Map<String, int[]> EXPLICIT_EXCLUSIONS = new HashMap<String, int[]>() {{
        put("eagle", new int[]{2});
        put("skis", new int[]{1});
        put("footstool", new int[]{2});
        put("dune buggy", new int[]{2});
        put("ice cream", new int[]{2});
        put("excavator", new int[]{2});
        put("balance beam", new int[]{1, 3});
        put("tricycle", new int[]{2});
        put("unicycle", new int[]{2});
        put("motorcycle", new int[]{2});
        put("pretzel", new int[]{2});
        put("robot", new int[]{2});
        put("volcano", new int[]{1});
        put("seagulls", new int[]{2});
        put("cucumber", new int[]{2});
        put("diving", new int[]{2});
        put("present", new int[]{2});
        put("window", new int[]{2});
    }};

    private AppDataManager() {
        mPreferencesHelper = new AppPreferencesHelper(Application.Context);
    }

    public static AppDataManager getInstance() {
        AppDataManager localInstance = Instance;
        if (localInstance == null) {
            synchronized (AppDataManager.class) {
                localInstance = Instance;
                if (localInstance == null) {
                    Instance = localInstance = new AppDataManager();
                }
            }
        }
        return localInstance;
    }

    public void setAnswersWithoutMistake(String sightWord, int answersWithoutMistake) {
        mPreferencesHelper.setAnswersWithoutMistake(sightWord, answersWithoutMistake);
    }

    public int getAnswersWithoutMistake(String sightWord) {
        return mPreferencesHelper.getAnswersWithoutMistake(sightWord);
    }

    @Override
    public void removePref(String name) {
        mPreferencesHelper.removePref(name);
    }

    @Override
    public void clearPref() {
        mPreferencesHelper.clearPref();
    }

    @Override
    public Map<String, List<LetterModel>> getLetters() {
        Map<String, List<LetterModel>> localInstance = LETTERS;
        if (localInstance == null) {
            synchronized (AppDataManager.class) {
                localInstance = LETTERS;
                if (localInstance == null) {
                    LETTERS = localInstance = mPreferencesHelper.getLetters();
                }
            }
        }
        return localInstance;
    }

    @Override
    public void setPiecesCompletedByLetter(String letter, List<PieceModel> piecesCompleted) {
        mPreferencesHelper.setPiecesCompletedByLetter(letter, piecesCompleted);
    }

    @Override
    public List<PieceModel> getPiecesCompletedByLetter(String letter) {
        return mPreferencesHelper.getPiecesCompletedByLetter(letter);
    }

    @Override
    public void savePuzzlePiece(String displayLetter, PuzzlePieceModel puzzlePiece) {
        mPreferencesHelper.savePuzzlePiece(displayLetter, puzzlePiece);
    }

    @Override
    public void savePuzzlePiece(String displayLetter, List<PuzzlePieceModel> puzzlePieces) {
        mPreferencesHelper.savePuzzlePiece(displayLetter, puzzlePieces);
    }

    @Override
    public List<PuzzlePieceModel> getCompletedPuzzlePieces(String displayLetter) {
        return mPreferencesHelper.getCompletedPuzzlePieces(displayLetter);
    }

    @Override
    public void savePuzzleBase64(String displayLetter, String base64) {
        mPreferencesHelper.savePuzzleBase64(displayLetter, base64);
    }

    @Override
    public String getPuzzleBase64(String displayLetter) {
        return mPreferencesHelper.getPuzzleBase64(displayLetter);
    }

    @Override
    public List<SightWordModel> getSightWords(@SightWordsCategoryDef int category) {
        if (category == SightWordsCategoryDef.PRE_K) {
            List<SightWordModel> localInstance = PRE_K_SIGHT_WORDS;
            if (localInstance == null) {
                synchronized (AppDataManager.class) {
                    localInstance = PRE_K_SIGHT_WORDS;
                    if (localInstance == null) {
                        List<SightWordModel> sightWords = mPreferencesHelper.getSightWords(category);
                        for(SightWordModel sightWord: sightWords){
                            sightWord.setStarCount(getSightWordStarCount(sightWord.getText()));
                        }
                        PRE_K_SIGHT_WORDS = localInstance = sightWords;
                    }
                }
            }
            return localInstance;
        } else {
            List<SightWordModel> localInstance = KINDERGARTEN_SIGHT_WORDS;
            if (localInstance == null) {
                synchronized (AppDataManager.class) {
                    localInstance = KINDERGARTEN_SIGHT_WORDS;
                    if (localInstance == null) {
                        List<SightWordModel> sightWords = mPreferencesHelper.getSightWords(category);
                        for(SightWordModel sightWord: sightWords){
                            sightWord.setStarCount(getSightWordStarCount(sightWord.getText()));
                        }
                        KINDERGARTEN_SIGHT_WORDS = localInstance = mPreferencesHelper.getSightWords(category);
                    }
                }
            }
            return localInstance;
        }
    }

    @Override
    public boolean isPuzzleCompleted(LetterModel letter) {
        String displayLetter = letter.getSourceLetter() + "-" + letter.getSoundId();
        List<PuzzlePieceModel> puzzlePieces = getCompletedPuzzlePieces(displayLetter);
        return puzzlePieces.size() == Config.PUZZLE_COLUMNS * Config.PUZZLE_ROWS;
    }

    @Override
    public boolean isPuzzleCompleted(String sourceSoundId) {
        return getCompletedPuzzlePieces(sourceSoundId).size() == Config.PUZZLE_COLUMNS * Config.PUZZLE_ROWS;
    }

    @Override
    public List<WordModel> getAllWords() {
        List<WordModel> localInstance = ALL_WORDS;
        if (localInstance == null) {
            synchronized (AppDataManager.class) {
                localInstance = ALL_WORDS;
                if (localInstance == null) {
                    localInstance = ALL_WORDS = new ArrayList<>();
                    for (String key : getLetters().keySet()) {
                        List<LetterModel> letters = getLetters().get(key);
                        for (LetterModel letter : letters) {
                            localInstance.addAll(letter.getPrimaryWords());
                            localInstance.addAll(letter.getQuizWords());
                        }
                    }
                }
            }
        }
        return localInstance;
    }

    @Override
    public List<WordModel> getAllWordsNoduplicate() {

        Map<String, WordModel> mapNoDuplicateWord = new ArrayMap<>();

        for (WordModel word : getAllWords()) {
            mapNoDuplicateWord.put(word.getText(), word);
        }

        return new ArrayList<>(mapNoDuplicateWord.values());
    }


    // get first sound
    @Override
    public List<LetterModel> getLettersFirst() {
        List<LetterModel> localInstance = LETTERS_FIRST;
        if (localInstance == null) {
            synchronized (AppDataManager.class) {
                localInstance = LETTERS_FIRST;
                if (localInstance == null) {
                    localInstance = LETTERS_FIRST = new ArrayList<>();
                    for (String key : getLetters().keySet()) {
                        List<LetterModel> letters = getLetters().get(key);
                        localInstance.add(letters.get(0));
                    }
                }
            }
        }
        return localInstance;
    }

    // get all sounds
    @Override
    public List<LetterModel> getAllLetters() {
        List<LetterModel> localInstance = ALL_SOUNDS;
        if (localInstance == null) {
            synchronized (AppDataManager.class) {
                localInstance = ALL_SOUNDS;
                if (localInstance == null) {
                    localInstance = ALL_SOUNDS = new ArrayList<>();
                    for (String key : getLetters().keySet()) {
                        List<LetterModel> letters = getLetters().get(key);
                        localInstance.addAll(letters);
                    }
                }
            }
        }
        return localInstance;
    }

    @Override
    public LetterModel randomLetter(@DifficultyDef int diffDef) {
        List<LetterModel> randomList = new ArrayList<>();

        if (diffDef == DifficultyDef.STANDARD) {
            randomList.addAll(getAllLetters());
        } else {
            randomList.addAll(getLettersFirst());
        }

        // debug
//        LetterModel letterModel = Helper.randomItems(randomList);
//
//        while (!letterModel.getSoundId().equals("ee")) {
//            letterModel= Helper.randomItems(randomList);
//        }
//
//        return letterModel;

        return Helper.randomItems(randomList);
    }

    @Override
    public List<WordModel> getAllPossibleWords(LetterModel letter) {
        String displayStringLowerCase = letter.getDisplayString().toLowerCase();
        String soundIdStringLowerCase = letter.getSoundId().toLowerCase();

        // blacklist
        // black list
        String blackListedLetter = !TextUtils.isEmpty(letter.getIpaPronunciation())
                ? letter.getIpaPronunciation() : displayStringLowerCase;

        // split string in to char[]
        List<String> blackListLetters = new ArrayList<>();
        for (int i = 0; i < blackListedLetter.length(); i++) {
            blackListLetters.add(blackListedLetter.substring(i, i + 1));
        }

        // additional blacklist
        List<String> additionBlackList = new ArrayList<>();
        // split string in to char[]
        for (int i = 0; i < displayStringLowerCase.length(); i++) {
            additionBlackList.add(displayStringLowerCase.substring(i, i + 1));
        }

        // displayString
        if (AppDataManager.MAP_LETTER_BLACK_LIST.containsKey(displayStringLowerCase)) {
            additionBlackList.addAll(0, AppDataManager.MAP_LETTER_BLACK_LIST.get(displayStringLowerCase));
        }

        // add soundId
        if (AppDataManager.MAP_SOUND_ID_BLACK_LIST.containsKey(soundIdStringLowerCase)) {
            additionBlackList.addAll(AppDataManager.MAP_SOUND_ID_BLACK_LIST.get(soundIdStringLowerCase));
        }

        // get possible words
        List<WordModel> possibleWords = new ArrayList<>();
        for (WordModel word : getAllWordsNoduplicate()) {

            boolean blackListInWord = false;
            // remove  word if contains char in word
            for (String ch : blackListLetters) {

                if (word.getPronunciation().contains(ch)) {
                    blackListInWord = true;
                    break;
                }
            }

            if (blackListInWord) {
                continue;
            }

            boolean blacklistInAdd = false;
            // remove word if text contain addBlacklist
            for (String ch : additionBlackList) {
                if (word.getText().toLowerCase().contains(ch)) {
                    blacklistInAdd = true;
                    break;
                }
            }

            if (blacklistInAdd) {
                continue;
            }

            //  //special case: remove hatchet, basket,
            //                //and any other two- syllable word in which the vowel in the second syllable is a, e, i, or u.

            boolean specialCase = false;
            if (soundIdStringLowerCase.equals("schwa") && displayStringLowerCase.equals("i")) {
                int vowels = 0;
                for (int i = 0; i < word.getText().length(); i++) {

                    char lt = word.getText().toLowerCase().charAt(i);
                    if (lt == 'a' || lt == 'e' || lt == 'i' || lt == 'u'
                            || lt == 'o' && vowels == 0) {

                        vowels += 1;
                        if (vowels > 1) {
                            specialCase = true;
                            break;
                        }
                    }
                }
            }

            if (specialCase) {
                continue;
            }

            // prevent prev answer set is true
            word.setAnswer(false);
            possibleWords.add(word);
        }

        return possibleWords;
    }

    @Override
    public boolean hasHomophoneConflict(SightWordModel primaryWord, List<SightWordModel> otherWords) {
        List<String> foundHomophoneGroup = null;
        for (List<String> homophoneGroup : Config.HOMOPHONE_GROUPS) {
            if (homophoneGroup.contains(primaryWord.getText())) {
                foundHomophoneGroup = homophoneGroup;
                break;
            }
        }
        if (foundHomophoneGroup == null) {
            return false;
        }

        for (String homophone : foundHomophoneGroup) {
            if (!homophone.equals(primaryWord.getText())) {
                for (SightWordModel sightWord : otherWords) {
                    if (sightWord.getText().equals(homophone)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean arrayHasHomophoneConflicts(List<SightWordModel> sightWords) {
        for (SightWordModel sightWord : sightWords) {
            if (hasHomophoneConflict(sightWord, sightWords)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setTotalGoldCount(String appFeature, int count) {
        mPreferencesHelper.setTotalGoldCount(appFeature, count);
    }

    @Override
    public int getTotalGoldCount(String appFeature) {
        return mPreferencesHelper.getTotalGoldCount(appFeature);
    }

    @Override
    public void setTotalSilverCount(String appFeature, int count) {
        mPreferencesHelper.setTotalSilverCount(appFeature, count);
    }

    @Override
    public int getTotalSilverCount(String appFeature) {
        return mPreferencesHelper.getTotalSilverCount(appFeature);
    }

    @Override
    public int getStarConsecutive(String displayLetter) {
        return mPreferencesHelper.getStarConsecutive(displayLetter);
    }

    @Override
    public void saveStarConsecutive(String displayLetter, int starConsecutive) {
        mPreferencesHelper.saveStarConsecutive(displayLetter, starConsecutive);
    }

    @Override
    public Spannable decorateWord(String wordText, String textHighlight, int color, String soundId) {
        String soundText = textHighlight.toLowerCase();

        wordText = wordText.toLowerCase();
        //remove higlighting from second 'a' in afraid
        if (wordText.equals("afraid") && soundId.equals("schwa")) {
            wordText = "afraid";
            Spannable spannable = new SpannableString(wordText);
            spannable.setSpan(new ForegroundColorSpan(color), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            return spannable;
        }

        if (wordText.equals("saint bernard")) {
            wordText = "Saint Bernard";
            if (soundText.equals("s")) {
                soundText = "S";
            }else if(soundText.equals("b")){
                soundText = "B";
            }
        }

        String mutableWord = wordText;

        int matchIndex = 1;
        Spannable spannable = new SpannableString(wordText);
        int count = 0;
        while (mutableWord.contains(soundText)) {
            int firstIndex = mutableWord.indexOf(soundText);

            int[] exclusion = EXPLICIT_EXCLUSIONS.get(wordText);


            if (exclusion != null && (exclusion.length - count) < Utils.getMatchesCount(mutableWord, soundText)) {
                boolean found = false;
                for (int i : exclusion) {
                    if (i == matchIndex) {
                        found = true;
                        break;
                    }
                }

                //Required exclusion size < number of character matches in word
                if (!found) {
                    spannable.setSpan(new ForegroundColorSpan(color), firstIndex, firstIndex + soundText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else {
                spannable.setSpan(new ForegroundColorSpan(color), firstIndex, firstIndex + soundText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            StringBuilder replacement = new StringBuilder();
            for (int i = 0; i < soundText.length(); i++) {
                replacement.append("-");
            }
            mutableWord = mutableWord.replaceFirst(soundText, replacement.toString());
            matchIndex++;
            count++;
        }
        return spannable;
    }

    @Override
    public void setSightWordStarCount(String text, int count) {
        mPreferencesHelper.setSightWordStarCount(text, count);
    }

    @Override
    public int getSightWordStarCount(String text) {
        return mPreferencesHelper.getSightWordStarCount(text);
    }
}
