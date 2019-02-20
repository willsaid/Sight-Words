package com.hearatale.sightwords.ui.adapter.sight_word;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hearatale.sightwords.R;
import com.hearatale.sightwords.data.model.phonics.SightWordModel;
import com.hearatale.sightwords.ui.custom_view.RatioCardLayout;
import com.hearatale.sightwords.utils.glide.GlideApp;

import java.util.List;

public class SightWordAdapter extends BaseQuickAdapter<SightWordModel, BaseViewHolder> {

    int sizeImage;
    float radius;

    public SightWordAdapter(@Nullable List<SightWordModel> data, int widthCell) {
        super(R.layout.item_sight_word, data);
        this.sizeImage = (int) (widthCell * 0.25);
        radius = widthCell * 5 / 6 * 0.1f; // cornerRadius = height *0.1f
    }

    @Override
    protected void convert(BaseViewHolder helper, final SightWordModel item) {

        RatioCardLayout itemV = (RatioCardLayout) helper.itemView;
        itemV.setRadius(radius);

        helper.setText(R.id.text_title, item.getText());

        // firstSentence
        ImageView imageViewFirst = helper.getView(R.id.image_artwork_first);
        GlideApp.with(mContext)
                .load(item.getFirstSentence().getImageFileName())
                .override(sizeImage)
                .into(imageViewFirst);

        // secondSentence
        ImageView imageViewSecond = helper.getView(R.id.image_artwork_second);
        GlideApp.with(mContext)
                .load(item.getSecondSentence().getImageFileName())
                .override(sizeImage)
                .into(imageViewSecond);

        ImageView imageViewStars = helper.getView(R.id.image_stars);
        setImageDrawableResStar(imageViewStars, item);

        boolean isComplete = item.getStarCount() >= 5;

        helper.setVisible(R.id.image_done, isComplete);

    }

    private void setImageDrawableResStar(ImageView imageView, SightWordModel sightWordModel) {
        if(imageView.getVisibility() != View.VISIBLE){
            imageView.setVisibility(View.VISIBLE);
        }
        switch (sightWordModel.getStarCount()) {
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
