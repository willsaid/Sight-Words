package com.hearatale.sightword.ui.adapter.quiz;

import android.support.annotation.Px;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hearatale.sightword.R;
import com.hearatale.sightword.data.model.phonics.SectionQuiz;
import com.hearatale.sightword.ui.custom_view.RatioFrameLayout;
import com.hearatale.sightword.utils.ImageHelper;

import java.util.List;

public class QuizSectionAdapter extends BaseSectionQuickAdapter<SectionQuiz, BaseViewHolder> {

    private int mWidthTag;
    private int mHeightTag;

    public QuizSectionAdapter(List<SectionQuiz> data, @Px int heightItem) {
        super(R.layout.item_quiz, R.layout.item_header_quiz, data);
        mHeightTag = (int) (heightItem * 0.2);
        mWidthTag = (int) ((heightItem * 3 / 4) * 0.25);
    }

    @Override
    protected void convert(BaseViewHolder helper, SectionQuiz item) {
        String displayLetter = item.t.getDisplayString().toLowerCase();
        helper.setText(R.id.text_title, displayLetter);

        boolean isPuzzleCompleted = item.isCompleted();
        ImageView imagePuzzle = helper.getView(R.id.image_puzzle);
        TextView textTitle = helper.getView(R.id.text_title);
        RatioFrameLayout ratio = helper.getView(R.id.layout_content);
        if (isPuzzleCompleted) {
            ImageHelper.load(imagePuzzle, item.getPuzzleCompleted());
            imagePuzzle.setVisibility(View.VISIBLE);
            setLayoutParams(textTitle);
            textTitle.setVisibility(View.VISIBLE);
            ratio.setBackgroundResource(0);
        } else {
            helper.setText(R.id.text_view_text_letter, displayLetter);
            imagePuzzle.setVisibility(View.GONE);
            textTitle.setVisibility(View.GONE);
            ratio.setBackgroundColor(mContext.getResources().getColor(R.color.brown_light));
        }
        helper.setGone(R.id.text_view_text_letter, !isPuzzleCompleted);
    }

    @Override
    protected void convertHead(BaseViewHolder helper, SectionQuiz item) {

    }

    private void setLayoutParams(TextView tag) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) tag.getLayoutParams();
        layoutParams.width = mWidthTag;
        layoutParams.height = mHeightTag;
        tag.setLayoutParams(layoutParams);
    }
}
