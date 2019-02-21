// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightword.ui.quiz_puzzle;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightword.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class QuizPuzzleActivity_ViewBinding implements Unbinder {
  private QuizPuzzleActivity target;

  private View view2131296384;

  private View view2131296382;

  private View view2131296383;

  private View view2131296385;

  private View view2131296381;

  @UiThread
  public QuizPuzzleActivity_ViewBinding(QuizPuzzleActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public QuizPuzzleActivity_ViewBinding(final QuizPuzzleActivity target, View source) {
    this.target = target;

    View view;
    target.layoutActivity = Utils.findRequiredViewAsType(source, R.id.layout_activity, "field 'layoutActivity'", ConstraintLayout.class);
    target.layoutToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'layoutToolbar'", ConstraintLayout.class);
    target.imageCheck = Utils.findRequiredViewAsType(source, R.id.image_view_check, "field 'imageCheck'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.image_view_piggy, "field 'imageViewPiggy' and method 'bank'");
    target.imageViewPiggy = Utils.castView(view, R.id.image_view_piggy, "field 'imageViewPiggy'", ImageView.class);
    view2131296384 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bank();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_home, "field 'imageHome' and method 'back'");
    target.imageHome = Utils.castView(view, R.id.image_view_home, "field 'imageHome'", ImageView.class);
    view2131296382 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.back();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_menu, "field 'imageMenu' and method 'backToAlphabetActivity'");
    target.imageMenu = Utils.castView(view, R.id.image_view_menu, "field 'imageMenu'", ImageView.class);
    view2131296383 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.backToAlphabetActivity();
      }
    });
    target.imageQuestion = Utils.findRequiredViewAsType(source, R.id.image_view_question, "field 'imageQuestion'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.image_view_puzzle, "field 'imagePuzzle' and method 'repeat'");
    target.imagePuzzle = Utils.castView(view, R.id.image_view_puzzle, "field 'imagePuzzle'", ImageView.class);
    view2131296385 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.repeat();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_forward, "field 'imageForward' and method 'forward'");
    target.imageForward = Utils.castView(view, R.id.image_view_forward, "field 'imageForward'", ImageView.class);
    view2131296381 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.forward();
      }
    });
    target.frameLayoutLetter = Utils.findRequiredViewAsType(source, R.id.frame_layout_puzzle_letter, "field 'frameLayoutLetter'", FrameLayout.class);
    target.frameLayoutWord = Utils.findRequiredViewAsType(source, R.id.frame_layout_word, "field 'frameLayoutWord'", FrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    QuizPuzzleActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutActivity = null;
    target.layoutToolbar = null;
    target.imageCheck = null;
    target.imageViewPiggy = null;
    target.imageHome = null;
    target.imageMenu = null;
    target.imageQuestion = null;
    target.imagePuzzle = null;
    target.imageForward = null;
    target.frameLayoutLetter = null;
    target.frameLayoutWord = null;

    view2131296384.setOnClickListener(null);
    view2131296384 = null;
    view2131296382.setOnClickListener(null);
    view2131296382 = null;
    view2131296383.setOnClickListener(null);
    view2131296383 = null;
    view2131296385.setOnClickListener(null);
    view2131296385 = null;
    view2131296381.setOnClickListener(null);
    view2131296381 = null;
  }
}
