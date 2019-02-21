// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightword.ui.quiz_puzzle.content;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.hearatale.sightword.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class WordFragment_ViewBinding implements Unbinder {
  private WordFragment target;

  @UiThread
  public WordFragment_ViewBinding(WordFragment target, View source) {
    this.target = target;

    target.layoutLetter = Utils.findRequiredViewAsType(source, R.id.layout_letter, "field 'layoutLetter'", ConstraintLayout.class);
    target.layoutStandardTopLeft = Utils.findRequiredView(source, R.id.layout_standard_top_left, "field 'layoutStandardTopLeft'");
    target.layoutStandardTopRight = Utils.findRequiredView(source, R.id.layout_standard_top_right, "field 'layoutStandardTopRight'");
    target.layoutTopEz = Utils.findRequiredView(source, R.id.layout_top_ez, "field 'layoutTopEz'");
    target.layoutLetterBotLeft = Utils.findRequiredView(source, R.id.layout_letter_bot_left, "field 'layoutLetterBotLeft'");
    target.layoutLetterBotRight = Utils.findRequiredView(source, R.id.layout_letter_bot_right, "field 'layoutLetterBotRight'");
  }

  @Override
  @CallSuper
  public void unbind() {
    WordFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutLetter = null;
    target.layoutStandardTopLeft = null;
    target.layoutStandardTopRight = null;
    target.layoutTopEz = null;
    target.layoutLetterBotLeft = null;
    target.layoutLetterBotRight = null;
  }
}
