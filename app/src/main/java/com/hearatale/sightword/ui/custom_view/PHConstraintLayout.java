package com.hearatale.sightword.ui.custom_view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hearatale.sightword.utils.DebugLog;

public class PHConstraintLayout extends ConstraintLayout {
    public PHConstraintLayout(Context context) {
        super(context);
    }

    public PHConstraintLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PHConstraintLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    boolean isBounceView = false;
    View mChildViewBound;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        DebugLog.e("EVENT: " + event.getAction());

        int xCoord = Math.round(event.getX());
        int yCoord = Math.round(event.getY());


        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);

            int left = childView.getLeft();
            int top = childView.getTop();
            int right = childView.getRight();
            int bot = childView.getBottom();

            if (childView instanceof LinearLayoutCompat && xCoord >= left &&
                    xCoord <= right &&
                    yCoord >= top &&
                    yCoord <= bot) {
//                DebugLog.e("viewId: " + childView.getId());
//                DebugLog.e("left:" + left + " right: " + right + " top: " + top + " bot: " + bot);

                childView.dispatchTouchEvent(event);
                isBounceView = true;
                mChildViewBound = childView;
                return true;

            }

        }

        // outside
        if (isBounceView && mChildViewBound != null) {
            event.setAction(MotionEvent.ACTION_CANCEL);
            mChildViewBound.dispatchTouchEvent(event);
            isBounceView = false;
            mChildViewBound = null;
        }

        return true;
    }
}
