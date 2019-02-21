package com.hearatale.sightword.ui.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hearatale.sightword.R;

public class RatioLinearLayout extends LinearLayout {
    private float verticalRatio = 1;
    private float horizontalRatio = 1;
    private FixedAttribute fixedAttribute = FixedAttribute.WIDTH;

    public RatioLinearLayout(Context context) {
        super(context);
    }

    public RatioLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RatioLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(
                attrs, R.styleable.RatioLinearLayout, 0, 0);
        fixedAttribute = FixedAttribute.fromId(typedArray.getInt(R.styleable.RatioLinearLayout_fixed_attribute, 0));
        horizontalRatio = typedArray.getFloat(R.styleable.RatioLinearLayout_horizontal_ratio, 1);
        verticalRatio = typedArray.getFloat(R.styleable.RatioLinearLayout_vertical_ratio, 1);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int originalWidth = MeasureSpec.getSize(widthMeasureSpec);
        int originalHeight = MeasureSpec.getSize(heightMeasureSpec);
        if (fixedAttribute == FixedAttribute.WIDTH) {
            int calculatedHeight = (int) (originalWidth * (verticalRatio / horizontalRatio));
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(calculatedHeight, MeasureSpec.EXACTLY));
        } else {
            int calculatedWidth = (int) (originalHeight * (horizontalRatio / verticalRatio));
            super.onMeasure(MeasureSpec.makeMeasureSpec(calculatedWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
        }
    }

    public void setRatio(float horizontalRatio, float verticalRatio) {
        this.setRatio(fixedAttribute, horizontalRatio, verticalRatio);
    }

    public void setRatio(FixedAttribute fixedAttribute, float horizontalRatio, float verticalRatio) {
        this.fixedAttribute = fixedAttribute;
        this.horizontalRatio = horizontalRatio;
        this.verticalRatio = verticalRatio;
        this.invalidate();
        this.requestLayout();
    }

    public float getHorizontalRatio() {
        return horizontalRatio;
    }

    public float getVerticalRatio() {
        return verticalRatio;
    }

    public FixedAttribute getFixedAttribute() {
        return fixedAttribute;
    }
}
