// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightword.ui.quiz;

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

public class QuizActivity_ViewBinding implements Unbinder {
  private QuizActivity target;

  private View view2131296383;

  private View view2131296382;

  @UiThread
  public QuizActivity_ViewBinding(QuizActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public QuizActivity_ViewBinding(final QuizActivity target, View source) {
    this.target = target;

    View view;
    target.layoutActivity = Utils.findRequiredViewAsType(source, R.id.layout_activity, "field 'layoutActivity'", ConstraintLayout.class);
    target.layoutToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_layout, "field 'layoutToolbar'", ConstraintLayout.class);
    target.check = Utils.findRequiredViewAsType(source, R.id.image_view_check, "field 'check'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.image_view_menu, "field 'imageMenu' and method 'onMenu'");
    target.imageMenu = Utils.castView(view, R.id.image_view_menu, "field 'imageMenu'", ImageView.class);
    view2131296383 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onMenu();
      }
    });
    target.imagePuzzle = Utils.findRequiredViewAsType(source, R.id.image_view_puzzle, "field 'imagePuzzle'", ImageView.class);
    target.imageQuestion = Utils.findRequiredViewAsType(source, R.id.image_view_question, "field 'imageQuestion'", ImageView.class);
    target.mRecyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'mRecyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.image_view_home, "method 'onHome'");
    view2131296382 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onHome();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    QuizActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutActivity = null;
    target.layoutToolbar = null;
    target.check = null;
    target.imageMenu = null;
    target.imagePuzzle = null;
    target.imageQuestion = null;
    target.mRecyclerView = null;

    view2131296383.setOnClickListener(null);
    view2131296383 = null;
    view2131296382.setOnClickListener(null);
    view2131296382 = null;
  }
}
