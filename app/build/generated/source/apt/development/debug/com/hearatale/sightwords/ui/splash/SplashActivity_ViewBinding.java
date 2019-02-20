// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.splash;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SplashActivity_ViewBinding implements Unbinder {
  private SplashActivity target;

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SplashActivity_ViewBinding(SplashActivity target, View source) {
    this.target = target;

    target.imageArtwork = Utils.findRequiredViewAsType(source, R.id.image_artwork, "field 'imageArtwork'", ImageView.class);
    target.linkText = Utils.findRequiredViewAsType(source, R.id.linkText, "field 'linkText'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SplashActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageArtwork = null;
    target.linkText = null;
  }
}
