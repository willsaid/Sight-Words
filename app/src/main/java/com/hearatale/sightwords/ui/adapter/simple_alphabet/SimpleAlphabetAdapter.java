package com.hearatale.sightwords.ui.adapter.simple_alphabet;

import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.model.phonics.letters.SimpleLetterModel;
import com.hearatale.sightwords.data.model.typedef.DifficultyDef;
import com.hearatale.sightwords.ui.quiz_puzzle.QuizPuzzlePresenter;
import com.hearatale.sightwords.utils.Config;
import com.hearatale.sightwords.utils.glide.GlideApp;

import java.util.List;

public class SimpleAlphabetAdapter extends BaseQuickAdapter<SimpleLetterModel, BaseViewHolder> {

    int sizeImage;
    @DifficultyDef
    int mDifficulty;

    QuizPuzzlePresenter mPresenter = new QuizPuzzlePresenter();

    public SimpleAlphabetAdapter(@Nullable List<SimpleLetterModel> data, int sizeImage, @DifficultyDef int difficulty) {
        super(R.layout.item_alphabet, data);
        this.sizeImage = sizeImage;
        mDifficulty = difficulty;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    protected void convert(BaseViewHolder helper, SimpleLetterModel item) {
        ImageView imageView = helper.getView(R.id.image_avatar);
        if (mDifficulty == DifficultyDef.EASY) {
            ProgressBar progressBar = helper.getView(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
        }

        String text;
        if (mDifficulty == DifficultyDef.EASY) {
            String letterUpperCase = item.getLetter().toUpperCase();
            String letterLowerCase = item.getLetter().toLowerCase();
            text = letterUpperCase + " " + letterLowerCase;
        } else {
            @ColorRes int colorRes = R.color.black_alphabet;
            switch (item.getColorString()) {
                case "orange":
                    colorRes = R.color.orange_alphabet;
                    break;
                case "green":
                    colorRes = R.color.green_alphabet;
                    break;
                case "blue":
                    colorRes = R.color.blue;
                    break;
                case "purple":
                    colorRes = R.color.purple_alphabet;
                    break;
            }
            helper.setTextColor(R.id.text_view_alphabet, mContext.getResources().getColor(colorRes));
            text = item.getLetter().toLowerCase();
        }

        helper.setText(R.id.text_view_alphabet, text);

        helper.setProgress(R.id.progress_bar, item.getProgressCompleted());
        int totalProgress = Config.PUZZLE_COLUMNS * Config.PUZZLE_ROWS;
        boolean isComplete = item.getProgressCompleted() == totalProgress;
        helper.setMax(R.id.progress_bar, totalProgress);


        ImageView imageViewStars = helper.getView(R.id.image_stars);

//        if (isComplete) {
        int starCount;
        if (mDifficulty == DifficultyDef.EASY) {
            starCount = mPresenter.getStarConsecutive(item.getId() + "ALPHABET_LETTERS_Stars");
        } else {
            starCount = mPresenter.getStarConsecutive(item.getId() + "PHONICS_Stars");
        }
        setImageDrawableResStar(imageViewStars, starCount);

        if (mDifficulty == DifficultyDef.STANDARD) {
            helper.setVisible(R.id.image_done, isComplete);
        } else {
            if (starCount >= 5) {
                helper.setVisible(R.id.image_done, true);
            }
        }

//        } else {
//            imageViewStars.setVisibility(View.INVISIBLE);
//        }

        if (mDifficulty == DifficultyDef.STANDARD) {
            GlideApp.with(mContext)
                    .load(Uri.parse(item.getPathImage()))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(sizeImage)
                    .into(imageView);
        }
    }

    private void setImageDrawableResStar(ImageView imageView, int starConsecutive) {

        if(imageView.getVisibility() != View.VISIBLE){
            imageView.setVisibility(View.VISIBLE);
        }

        switch (starConsecutive) {

            case 0:
                imageView.setVisibility(View.INVISIBLE);
                break;
            case 1:
                GlideApp.with(mContext).load(R.mipmap.star).into(imageView);
                break;
            case 2:
                GlideApp.with(mContext).load(R.mipmap.star_2).into(imageView);
                break;
            case 3:
                GlideApp.with(mContext).load(R.mipmap.star_3).into(imageView);
                break;
            case 4:
                GlideApp.with(mContext).load(R.mipmap.star_4).into(imageView);
                break;
            default:
                GlideApp.with(mContext).load(R.mipmap.star_5).into(imageView);
                break;
        }
    }
}
