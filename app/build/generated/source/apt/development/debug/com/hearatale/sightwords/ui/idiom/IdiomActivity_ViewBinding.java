// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.idiom;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class IdiomActivity_ViewBinding implements Unbinder {
  private IdiomActivity target;

  private View view2131296382;

  private View view2131296385;

  private View view2131296386;

  @UiThread
  public IdiomActivity_ViewBinding(IdiomActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public IdiomActivity_ViewBinding(final IdiomActivity target, View source) {
    this.target = target;

    View view;
    target.layoutActivity = Utils.findRequiredViewAsType(source, R.id.layout_activity, "field 'layoutActivity'", ConstraintLayout.class);
    target.imageBlur = Utils.findRequiredViewAsType(source, R.id.image_blur, "field 'imageBlur'", ImageView.class);
    target.layoutContent = Utils.findRequiredViewAsType(source, R.id.layout_content, "field 'layoutContent'", ConstraintLayout.class);
    target.layoutContentText = Utils.findRequiredViewAsType(source, R.id.layout_content_text, "field 'layoutContentText'", LinearLayoutCompat.class);
    target.layoutToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'layoutToolbar'", ConstraintLayout.class);
    view = Utils.findRequiredView(source, R.id.image_view_home, "field 'imageHome' and method 'back'");
    target.imageHome = Utils.castView(view, R.id.image_view_home, "field 'imageHome'", ImageView.class);
    view2131296382 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.back();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_puzzle, "field 'imagePuzzle' and method 'bank'");
    target.imagePuzzle = Utils.castView(view, R.id.image_view_puzzle, "field 'imagePuzzle'", ImageView.class);
    view2131296385 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bank();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_question, "field 'imageQuestion' and method 'repeat'");
    target.imageQuestion = Utils.castView(view, R.id.image_view_question, "field 'imageQuestion'", ImageView.class);
    view2131296386 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.repeat();
      }
    });
    target.imagePuzzleCompleted = Utils.findRequiredViewAsType(source, R.id.image_puzzle_completed, "field 'imagePuzzleCompleted'", ImageView.class);
    target.textIdiom = Utils.findRequiredViewAsType(source, R.id.text_idiom, "field 'textIdiom'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    IdiomActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutActivity = null;
    target.imageBlur = null;
    target.layoutContent = null;
    target.layoutContentText = null;
    target.layoutToolbar = null;
    target.imageHome = null;
    target.imagePuzzle = null;
    target.imageQuestion = null;
    target.imagePuzzleCompleted = null;
    target.textIdiom = null;

    view2131296382.setOnClickListener(null);
    view2131296382 = null;
    view2131296385.setOnClickListener(null);
    view2131296385 = null;
    view2131296386.setOnClickListener(null);
    view2131296386 = null;
  }
}
