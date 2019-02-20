// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.secret_stuff;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SecretStuffActivity_ViewBinding implements Unbinder {
  private SecretStuffActivity target;

  private View view2131296382;

  @UiThread
  public SecretStuffActivity_ViewBinding(SecretStuffActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SecretStuffActivity_ViewBinding(final SecretStuffActivity target, View source) {
    this.target = target;

    View view;
    target.mainImage = Utils.findRequiredViewAsType(source, R.id.image_view_center_view, "field 'mainImage'", ImageView.class);
    target.linearLayout = Utils.findRequiredViewAsType(source, R.id.sub_layout_centerview, "field 'linearLayout'", LinearLayout.class);
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
    SecretStuffActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mainImage = null;
    target.linearLayout = null;

    view2131296382.setOnClickListener(null);
    view2131296382 = null;
  }
}
