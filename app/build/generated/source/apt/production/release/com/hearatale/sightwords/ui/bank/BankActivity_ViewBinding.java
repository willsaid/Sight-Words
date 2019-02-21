// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.bank;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BankActivity_ViewBinding implements Unbinder {
  private BankActivity target;

  private View view2131296378;

  @UiThread
  public BankActivity_ViewBinding(BankActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BankActivity_ViewBinding(final BankActivity target, View source) {
    this.target = target;

    View view;
    target.layoutRoot = Utils.findRequiredViewAsType(source, R.id.layout_root, "field 'layoutRoot'", FrameLayout.class);
    target.imageBlur = Utils.findRequiredViewAsType(source, R.id.image_blur, "field 'imageBlur'", ImageView.class);
    target.textViewNoCoins = Utils.findRequiredViewAsType(source, R.id.text_view_no_coin, "field 'textViewNoCoins'", TextView.class);
    target.layoutAvailableCoins = Utils.findRequiredViewAsType(source, R.id.layout_available_coins, "field 'layoutAvailableCoins'", ConstraintLayout.class);
    target.layoutForTrucks = Utils.findRequiredViewAsType(source, R.id.layout_for_trucks, "field 'layoutForTrucks'", LinearLayoutCompat.class);
    target.layoutForTrucksSecond = Utils.findRequiredViewAsType(source, R.id.layout_for_trucks_second, "field 'layoutForTrucksSecond'", LinearLayoutCompat.class);
    target.layoutForBags = Utils.findRequiredViewAsType(source, R.id.layout_for_bags, "field 'layoutForBags'", LinearLayoutCompat.class);
    target.layoutForStacks = Utils.findRequiredViewAsType(source, R.id.layout_for_stacks, "field 'layoutForStacks'", LinearLayoutCompat.class);
    target.layoutForCoins = Utils.findRequiredViewAsType(source, R.id.layout_for_coins, "field 'layoutForCoins'", LinearLayoutCompat.class);
    view = Utils.findRequiredView(source, R.id.image_view_back, "method 'back'");
    view2131296378 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.back();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    BankActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutRoot = null;
    target.imageBlur = null;
    target.textViewNoCoins = null;
    target.layoutAvailableCoins = null;
    target.layoutForTrucks = null;
    target.layoutForTrucksSecond = null;
    target.layoutForBags = null;
    target.layoutForStacks = null;
    target.layoutForCoins = null;

    view2131296378.setOnClickListener(null);
    view2131296378 = null;
  }
}
