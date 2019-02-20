// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.sentence.content;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SentenceContentFragment_ViewBinding implements Unbinder {
  private SentenceContentFragment target;

  private View view2131296370;

  private View view2131296375;

  @UiThread
  public SentenceContentFragment_ViewBinding(final SentenceContentFragment target, View source) {
    this.target = target;

    View view;
    target.layoutSentence = Utils.findRequiredViewAsType(source, R.id.layout_sentence, "field 'layoutSentence'", ConstraintLayout.class);
    target.layoutFirst = Utils.findRequiredViewAsType(source, R.id.layout_first, "field 'layoutFirst'", ConstraintLayout.class);
    view = Utils.findRequiredView(source, R.id.image_first, "field 'imageFirst' and method 'showFirstSentence'");
    target.imageFirst = Utils.castView(view, R.id.image_first, "field 'imageFirst'", ImageView.class);
    view2131296370 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showFirstSentence();
      }
    });
    target.textFirst = Utils.findRequiredViewAsType(source, R.id.text_first, "field 'textFirst'", TextView.class);
    target.layoutSecond = Utils.findRequiredViewAsType(source, R.id.layout_second, "field 'layoutSecond'", ConstraintLayout.class);
    view = Utils.findRequiredView(source, R.id.image_second, "field 'imageSecond' and method 'showSecondSentence'");
    target.imageSecond = Utils.castView(view, R.id.image_second, "field 'imageSecond'", ImageView.class);
    view2131296375 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showSecondSentence();
      }
    });
    target.textSecond = Utils.findRequiredViewAsType(source, R.id.text_second, "field 'textSecond'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    SentenceContentFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutSentence = null;
    target.layoutFirst = null;
    target.imageFirst = null;
    target.textFirst = null;
    target.layoutSecond = null;
    target.imageSecond = null;
    target.textSecond = null;

    view2131296370.setOnClickListener(null);
    view2131296370 = null;
    view2131296375.setOnClickListener(null);
    view2131296375 = null;
  }
}
