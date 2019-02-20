package com.hearatale.sightwords.ui.adapter.quiz;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.model.phonics.letters.LetterModel;

import java.util.List;

public class QuizAdapter extends BaseQuickAdapter<LetterModel, BaseViewHolder> {

    public QuizAdapter(@Nullable List<LetterModel> data) {
        super(R.layout.item_quiz, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LetterModel item) {
        helper.setText(R.id.text_title, item.getSourceLetter().toLowerCase());
    }
}
