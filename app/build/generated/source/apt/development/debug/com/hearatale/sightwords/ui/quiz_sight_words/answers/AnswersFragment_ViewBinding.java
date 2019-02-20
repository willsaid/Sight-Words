// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.quiz_sight_words.answers;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AnswersFragment_ViewBinding implements Unbinder {
  private AnswersFragment target;

  @UiThread
  public AnswersFragment_ViewBinding(AnswersFragment target, View source) {
    this.target = target;

    target.layoutAnswers = Utils.findRequiredViewAsType(source, R.id.layout_answers, "field 'layoutAnswers'", ConstraintLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    AnswersFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutAnswers = null;
  }
}
