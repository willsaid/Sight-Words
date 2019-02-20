// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.quiz_sight_words;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class QuizSightWordsActivity_ViewBinding implements Unbinder {
  private QuizSightWordsActivity target;

  private View view2131296383;

  private View view2131296384;

  private View view2131296381;

  private View view2131296378;

  private View view2131296387;

  @UiThread
  public QuizSightWordsActivity_ViewBinding(QuizSightWordsActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public QuizSightWordsActivity_ViewBinding(final QuizSightWordsActivity target, View source) {
    this.target = target;

    View view;
    target.layoutToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'layoutToolbar'", ConstraintLayout.class);
    target.layoutActivity = Utils.findRequiredViewAsType(source, R.id.layout_activity, "field 'layoutActivity'", ConstraintLayout.class);
    view = Utils.findRequiredView(source, R.id.image_view_menu, "field 'imageMenu' and method 'backToAlphabetActivity'");
    target.imageMenu = Utils.castView(view, R.id.image_view_menu, "field 'imageMenu'", ImageView.class);
    view2131296383 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.backToAlphabetActivity();
      }
    });
    target.imageStars = Utils.findRequiredViewAsType(source, R.id.image_stars, "field 'imageStars'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.image_view_piggy, "field 'imageViewPiggy' and method 'showBank'");
    target.imageViewPiggy = Utils.castView(view, R.id.image_view_piggy, "field 'imageViewPiggy'", ImageView.class);
    view2131296384 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showBank();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_forward, "field 'imageViewForward' and method 'forward'");
    target.imageViewForward = Utils.castView(view, R.id.image_view_forward, "field 'imageViewForward'", ImageView.class);
    view2131296381 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.forward();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_back, "method 'back'");
    view2131296378 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.back();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_replay, "method 'replay'");
    view2131296387 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.replay();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    QuizSightWordsActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutToolbar = null;
    target.layoutActivity = null;
    target.imageMenu = null;
    target.imageStars = null;
    target.imageViewPiggy = null;
    target.imageViewForward = null;

    view2131296383.setOnClickListener(null);
    view2131296383 = null;
    view2131296384.setOnClickListener(null);
    view2131296384 = null;
    view2131296381.setOnClickListener(null);
    view2131296381 = null;
    view2131296378.setOnClickListener(null);
    view2131296378 = null;
    view2131296387.setOnClickListener(null);
    view2131296387 = null;
  }
}
