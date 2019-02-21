// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightword.ui.sight_word;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightword.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SightWordActivity_ViewBinding implements Unbinder {
  private SightWordActivity target;

  private View view2131296382;

  private View view2131296386;

  private View view2131296385;

  @UiThread
  public SightWordActivity_ViewBinding(SightWordActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SightWordActivity_ViewBinding(final SightWordActivity target, View source) {
    this.target = target;

    View view;
    target.layoutActivity = Utils.findRequiredViewAsType(source, R.id.layout_activity, "field 'layoutActivity'", ConstraintLayout.class);
    target.layoutToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'layoutToolbar'", ConstraintLayout.class);
    target.imageCheck = Utils.findRequiredViewAsType(source, R.id.image_view_check, "field 'imageCheck'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.image_view_home, "field 'imageHome' and method 'onBackHome'");
    target.imageHome = Utils.castView(view, R.id.image_view_home, "field 'imageHome'", ImageView.class);
    view2131296382 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackHome();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_question, "field 'imageQuestion' and method 'bank'");
    target.imageQuestion = Utils.castView(view, R.id.image_view_question, "field 'imageQuestion'", ImageView.class);
    view2131296386 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bank();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_puzzle, "field 'imagePuzzle' and method 'onQuizSightWord'");
    target.imagePuzzle = Utils.castView(view, R.id.image_view_puzzle, "field 'imagePuzzle'", ImageView.class);
    view2131296385 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onQuizSightWord();
      }
    });
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'mRecyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SightWordActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutActivity = null;
    target.layoutToolbar = null;
    target.imageCheck = null;
    target.imageHome = null;
    target.imageQuestion = null;
    target.imagePuzzle = null;
    target.mRecyclerView = null;

    view2131296382.setOnClickListener(null);
    view2131296382 = null;
    view2131296386.setOnClickListener(null);
    view2131296386 = null;
    view2131296385.setOnClickListener(null);
    view2131296385 = null;
  }
}
