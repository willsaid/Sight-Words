// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.main;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view2131296366;

  private View view2131296368;

  private View view2131296367;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.image_button_pre_k_sight_words, "method 'onPerKSightWordsClick'");
    view2131296366 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onPerKSightWordsClick();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_button_sight_words, "method 'onSightWords'");
    view2131296368 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSightWords();
      }
    });
    view = Utils.findRequiredView(source, R.id.image_button_secret_stuff, "method 'onSecretStuff'");
    view2131296367 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSecretStuff();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    target = null;


    view2131296366.setOnClickListener(null);
    view2131296366 = null;
    view2131296368.setOnClickListener(null);
    view2131296368 = null;
    view2131296367.setOnClickListener(null);
    view2131296367 = null;
  }
}
