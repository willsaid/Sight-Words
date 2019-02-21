// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.letter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LetterActivity_ViewBinding implements Unbinder {
  private LetterActivity target;

  private View view2131296383;

  private View view2131296385;

  private View view2131296299;

  private View view2131296298;

  private View view2131296386;

  private View view2131296382;

  @UiThread
  public LetterActivity_ViewBinding(LetterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LetterActivity_ViewBinding(final LetterActivity target, View source) {
    this.target = target;

    View view;
    target.parentLayout = Utils.findRequiredViewAsType(source, R.id.parent_layout, "field 'parentLayout'", ConstraintLayout.class);
    target.layoutAction = Utils.findRequiredView(source, R.id.layout_action, "field 'layoutAction'");
    target.mainContent = Utils.findRequiredViewAsType(source, R.id.main_content, "field 'mainContent'", FrameLayout.class);
    target.imageCheck = Utils.findRequiredViewAsType(source, R.id.image_view_check, "field 'imageCheck'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.image_view_menu, "field 'imageViewMenu' and method 'backToAlphabetActivity'");
    target.imageViewMenu = Utils.castView(view, R.id.image_view_menu, "field 'imageViewMenu'", ImageView.class);
    view2131296383 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.backToAlphabetActivity();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_puzzle, "field 'imageViewRepeat' and method 'onRepeat'");
    target.imageViewRepeat = Utils.castView(view, R.id.image_view_puzzle, "field 'imageViewRepeat'", ImageView.class);
    view2131296385 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRepeat();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_prev, "field 'buttonPrev' and method 'onPrev'");
    target.buttonPrev = Utils.castView(view, R.id.button_prev, "field 'buttonPrev'", ImageButton.class);
    view2131296299 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPrev();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_next, "field 'buttonNext' and method 'onNext'");
    target.buttonNext = Utils.castView(view, R.id.button_next, "field 'buttonNext'", ImageButton.class);
    view2131296298 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNext();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_question, "field 'imageViewQuiz' and method 'onPuzzle'");
    target.imageViewQuiz = Utils.castView(view, R.id.image_view_question, "field 'imageViewQuiz'", ImageView.class);
    view2131296386 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPuzzle();
      }
    });
    target.layoutToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'layoutToolbar'", ConstraintLayout.class);
    view = Utils.findRequiredView(source, R.id.image_view_home, "method 'backToHome'");
    view2131296382 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.backToHome();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LetterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.parentLayout = null;
    target.layoutAction = null;
    target.mainContent = null;
    target.imageCheck = null;
    target.imageViewMenu = null;
    target.imageViewRepeat = null;
    target.buttonPrev = null;
    target.buttonNext = null;
    target.imageViewQuiz = null;
    target.layoutToolbar = null;

    view2131296383.setOnClickListener(null);
    view2131296383 = null;
    view2131296385.setOnClickListener(null);
    view2131296385 = null;
    view2131296299.setOnClickListener(null);
    view2131296299 = null;
    view2131296298.setOnClickListener(null);
    view2131296298 = null;
    view2131296386.setOnClickListener(null);
    view2131296386 = null;
    view2131296382.setOnClickListener(null);
    view2131296382 = null;
  }
}
