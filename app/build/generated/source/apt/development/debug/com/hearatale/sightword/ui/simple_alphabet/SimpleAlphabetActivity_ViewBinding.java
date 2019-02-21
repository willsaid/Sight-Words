// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightword.ui.simple_alphabet;

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

public class SimpleAlphabetActivity_ViewBinding implements Unbinder {
  private SimpleAlphabetActivity target;

  private View view2131296384;

  private View view2131296385;

  private View view2131296380;

  private View view2131296382;

  private View view2131296386;

  @UiThread
  public SimpleAlphabetActivity_ViewBinding(SimpleAlphabetActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SimpleAlphabetActivity_ViewBinding(final SimpleAlphabetActivity target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recycler_view_grid, "field 'recyclerView'", RecyclerView.class);
    target.layoutActivity = Utils.findRequiredViewAsType(source, R.id.layout_activity, "field 'layoutActivity'", ConstraintLayout.class);
    view = Utils.findRequiredView(source, R.id.image_view_piggy, "field 'imageViewPiggy' and method 'bank'");
    target.imageViewPiggy = Utils.castView(view, R.id.image_view_piggy, "field 'imageViewPiggy'", ImageView.class);
    view2131296384 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bank();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_puzzle, "field 'imageViewPuzzle' and method 'pushQuizActivity'");
    target.imageViewPuzzle = Utils.castView(view, R.id.image_view_puzzle, "field 'imageViewPuzzle'", ImageView.class);
    view2131296385 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.pushQuizActivity();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_check, "method 'playIntructions'");
    view2131296380 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.playIntructions();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_home, "method 'backToHome'");
    view2131296382 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.backToHome();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_view_question, "method 'onPuzzle'");
    view2131296386 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPuzzle();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SimpleAlphabetActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.recyclerView = null;
    target.layoutActivity = null;
    target.imageViewPiggy = null;
    target.imageViewPuzzle = null;

    view2131296384.setOnClickListener(null);
    view2131296384 = null;
    view2131296385.setOnClickListener(null);
    view2131296385 = null;
    view2131296380.setOnClickListener(null);
    view2131296380 = null;
    view2131296382.setOnClickListener(null);
    view2131296382 = null;
    view2131296386.setOnClickListener(null);
    view2131296386 = null;
  }
}
