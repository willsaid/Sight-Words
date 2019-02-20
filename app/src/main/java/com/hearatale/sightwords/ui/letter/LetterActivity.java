package com.hearatale.sightwords.ui.letter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.Constants;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.service.AudioPlayerHelper;
import com.hearatale.sightwords.ui.base.activity.ActivityMVP;
import com.hearatale.sightwords.ui.letter.letter_content.LetterContentFragment;
import com.hearatale.sightwords.ui.main.MainActivity;
import com.hearatale.sightwords.ui.quiz_puzzle.QuizPuzzleActivity;
import com.hearatale.sightwords.ui.simple_alphabet.SimpleAlphabetActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LetterActivity extends ActivityMVP<LetterPresenter, ILetter> implements ILetter {

    private static int REQUEST_CODE = 1028;

    @BindView(R.id.parent_layout)
    ConstraintLayout parentLayout;

    @BindView(R.id.layout_action)
    View layoutAction;

    @BindView(R.id.main_content)
    FrameLayout mainContent;

    @BindView(R.id.image_view_check)
    ImageView imageCheck;

    @BindView(R.id.image_view_menu)
    ImageView imageViewMenu;

    @BindView(R.id.image_view_puzzle)
    ImageView imageViewRepeat;

    @BindView(R.id.button_prev)
    ImageButton buttonPrev;

    @BindView(R.id.button_next)
    ImageButton buttonNext;

    @BindView(R.id.image_view_question)
    ImageView imageViewQuiz;

    @BindView(R.id.toolbar_layout)
    ConstraintLayout layoutToolbar;

    List<LetterModel> mLetterModels;

    @DifficultyDef
    private int mLetterModeDef = DifficultyDef.EASY;


    int currentPositionWord = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter);
        ButterKnife.bind(this);
        getArgument();
        initViews();
//        layoutAction.setVisibility(View.GONE);
    }

    @Override
    protected void makeView() {
        mView = this;
    }

    @Override
    protected void makePresenter() {
        mPresenter = new LetterPresenter();
    }

    private void initViews() {

        // init toolbar
//        layoutAction.setVisibility(mLetterModeDef == DifficultyDef.EASY ? View.INVISIBLE : View.VISIBLE);

        if (mLetterModeDef == DifficultyDef.EASY) {
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(parentLayout);
            constraintSet.centerHorizontally(layoutAction.getId(), mainContent.getId());
            constraintSet.centerVertically(layoutAction.getId(), mainContent.getId());
            constraintSet.applyTo(parentLayout);
        }

        getSupportFragmentManager().beginTransaction().add(R.id.main_content, getLetterContentFragment()).commit();

        imageCheck.setVisibility(View.INVISIBLE);
        imageViewMenu.setVisibility(View.VISIBLE);
        imageViewRepeat.setImageResource(R.mipmap.repeat);

        disablePrevNext();

        animationButtonQuiz();

        int color = getResources().getColor(mLetterModeDef == DifficultyDef.EASY ? R.color.letter_action_bar: R.color.blue);
        layoutToolbar.setBackgroundColor(color);
    }

    private void getArgument() {
        Intent intent = getIntent();
        if (intent == null) return;
        mLetterModeDef = intent.getIntExtra(Constants.Arguments.ARG_LETTER_MODE, 0);
        String letterText = intent.getStringExtra(Constants.Arguments.ARG_LETTER_TEXT);
        mLetterModels = mPresenter.getLetter().get(letterText.toUpperCase());

        if (mLetterModeDef == DifficultyDef.EASY) {
            List<LetterModel> letters = new ArrayList<>();
            Map<String, List<LetterModel>> letterMap = mPresenter.getLetter();
            for (String letter: letterMap.keySet()) {
                letters.add(letterMap.get(letter).get(0));
            }
            mLetterModels = letters;
        }

        currentPositionWord = letterText.charAt(0) - 'A';

        //Find index by sound id;
        String soundId = intent.getStringExtra(Constants.Arguments.ARG_SOUND_ID);
        if (TextUtils.isEmpty(soundId)) return;

        for (int i = 0; i < mLetterModels.size(); i++) {
            if (mLetterModels.get(i).getSoundId().equals(soundId)) {
                currentPositionWord = i;
            }
        }
    }

    private void animationButtonQuiz() {
        Animation scaleAnimation = new ScaleAnimation(1f, 1.15f, 1f, 1.15f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        imageViewQuiz.startAnimation(scaleAnimation);
    }

    @NonNull
    private LetterContentFragment getLetterContentFragment() {
        LetterContentFragment fragment = LetterContentFragment.newInstance(mLetterModels.get(currentPositionWord), mLetterModeDef);
        fragment.setUpdateViewListener(new LetterContentFragment.UpdateViewListener() {
            @Override
            public void showButtonQuiz(boolean isShow) {
                imageViewQuiz.setAlpha(isShow ? 1f : 0f);
                imageViewQuiz.setEnabled(isShow);
                imageViewQuiz.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
            }
        });

        return fragment;
    }

    @OnClick(R.id.button_prev)
    void onPrev() {
        if (currentPositionWord == 0) {
            return;
        }
        currentPositionWord--;
        disablePrevNext();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.main_content, getLetterContentFragment())
                .commit();
    }

    @OnClick(R.id.button_next)
    void onNext() {
        if (currentPositionWord == mLetterModels.size() - 1) {
            return;
        }
        currentPositionWord++;
        disablePrevNext();
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.push_enter, R.anim.push_exit)
                .replace(R.id.main_content, getLetterContentFragment())
                .commit();
    }

    // go to puzzle
    @OnClick(R.id.image_view_question)
    void onPuzzle() {
        Intent intent = new Intent(LetterActivity.this, QuizPuzzleActivity.class);
        Bundle args = new Bundle();
        args.putInt(Constants.Arguments.ARG_LETTER_MODE, mLetterModeDef);
        args.putParcelable(Constants.Arguments.ARG_LETTER, mLetterModels.get(currentPositionWord));
        intent.putExtras(args);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Constants.ResultCode.RELOAD_LETTER_CODE == resultCode && requestCode == REQUEST_CODE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_content);

            if (fragment != null && fragment instanceof LetterContentFragment) {
                LetterContentFragment item = (LetterContentFragment) fragment;
                item.visibleView();
            }
        }
    }

    private void disablePrevNext() {

        // one letter
        if (mLetterModels.size() == 1) {
            enableButton(buttonPrev, false);
            enableButton(buttonNext, false);
            return;
        }

        // middle position
        if (currentPositionWord != 0 && currentPositionWord != mLetterModels.size() - 1) {
            enableButton(buttonPrev, true);
            enableButton(buttonNext, true);
            return;
        }

        // first position
        if (currentPositionWord == 0) {
            enableButton(buttonPrev, false);
            enableButton(buttonNext, true);
            return;
        }

        // last position
        if (currentPositionWord == mLetterModels.size() - 1) {
            enableButton(buttonPrev, true);
            enableButton(buttonNext, false);
        }

    }

    @OnClick(R.id.image_view_home)
    void backToHome() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    @OnClick(R.id.image_view_menu)
    void backToAlphabetActivity() {
        Intent intent = new Intent(LetterActivity.this, SimpleAlphabetActivity.class);
        if (mLetterModeDef == DifficultyDef.EASY) {
            intent.putExtra(Constants.Arguments.ARG_LETTER_MODE, DifficultyDef.EASY);
        } else {
            intent.putExtra(Constants.Arguments.ARG_LETTER_MODE, DifficultyDef.STANDARD);
        }
        pushIntent(intent);
        finish();
    }


    @OnClick(R.id.image_view_puzzle)
    void onRepeat() {
        LetterContentFragment item = (LetterContentFragment) getSupportFragmentManager().findFragmentById(R.id.main_content);
        item.onRepeat();
    }

    private void enableButton(ImageButton button, boolean show) {
        button.setEnabled(show);
        button.setFocusable(show);
        button.setAlpha(show ? 1 : 0.5f);
    }

    @Override
    public void onBackPressed() {
        AudioPlayerHelper.getInstance().stopPlayer();
        super.onBackPressed();
    }
}
