// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.sentence;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SentenceActivity_ViewBinding implements Unbinder {
  private SentenceActivity target;

  private View view2131296382;

  private View view2131296383;

  private View view2131296386;

  private View view2131296385;

  private View view2131296298;

  private View view2131296299;

  @UiThread
  public SentenceActivity_ViewBinding(SentenceActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SentenceActivity_ViewBinding(final SentenceActivity target, View source) {
    this.target = target;

    View view;
    target.layoutToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'layoutToolbar'", ConstraintLayout.class);
    target.imageCheck = Utils.findRequiredViewAsType(source, R.id.image_view_check, "field 'imageCheck'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.image_view_home, "field 'imageHome' and method 'onHome'");
    target.imageHome = Utils.castView(view, R.id.image_view_home, "field 'imageHome'", ImageView.class);
    view2131296382 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onHome();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_menu, "field 'imageMenu' and method 'onMenu'");
    target.imageMenu = Utils.castView(view, R.id.image_view_menu, "field 'imageMenu'", ImageView.class);
    view2131296383 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onMenu();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_question, "field 'imageQuestion' and method 'onSightWordQuizView'");
    target.imageQuestion = Utils.castView(view, R.id.image_view_question, "field 'imageQuestion'", ImageView.class);
    view2131296386 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSightWordQuizView();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_puzzle, "field 'imagePuzzle' and method 'onRepeat'");
    target.imagePuzzle = Utils.castView(view, R.id.image_view_puzzle, "field 'imagePuzzle'", ImageView.class);
    view2131296385 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onRepeat();
      }
    });
    target.textSightWord = Utils.findRequiredViewAsType(source, R.id.text_sight_word, "field 'textSightWord'", TextView.class);
    view = Utils.findRequiredView(source, R.id.button_next, "field 'buttonNext' and method 'onNext'");
    target.buttonNext = Utils.castView(view, R.id.button_next, "field 'buttonNext'", ImageButton.class);
    view2131296298 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onNext();
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
  }

  @Override
  @CallSuper
  public void unbind() {
    SentenceActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutToolbar = null;
    target.imageCheck = null;
    target.imageHome = null;
    target.imageMenu = null;
    target.imageQuestion = null;
    target.imagePuzzle = null;
    target.textSightWord = null;
    target.buttonNext = null;
    target.buttonPrev = null;

    view2131296382.setOnClickListener(null);
    view2131296382 = null;
    view2131296383.setOnClickListener(null);
    view2131296383 = null;
    view2131296386.setOnClickListener(null);
    view2131296386 = null;
    view2131296385.setOnClickListener(null);
    view2131296385 = null;
    view2131296298.setOnClickListener(null);
    view2131296298 = null;
    view2131296299.setOnClickListener(null);
    view2131296299 = null;
  }
}
