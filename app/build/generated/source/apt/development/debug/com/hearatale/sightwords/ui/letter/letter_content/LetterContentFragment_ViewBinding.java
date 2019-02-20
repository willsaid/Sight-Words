// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightwords.ui.letter.letter_content;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightwords.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LetterContentFragment_ViewBinding implements Unbinder {
  private LetterContentFragment target;

  private View view2131296401;

  private View view2131296418;

  private View view2131296422;

  private View view2131296343;

  @UiThread
  public LetterContentFragment_ViewBinding(final LetterContentFragment target, View source) {
    this.target = target;

    View view;
    target.parentLayout = Utils.findRequiredViewAsType(source, R.id.parent_layout, "field 'parentLayout'", ConstraintLayout.class);
    target.textViewLetter = Utils.findRequiredViewAsType(source, R.id.text_view_letter, "field 'textViewLetter'", TextView.class);
    target.textViewFirst = Utils.findRequiredViewAsType(source, R.id.text_view_first, "field 'textViewFirst'", TextView.class);
    target.textViewSecond = Utils.findRequiredViewAsType(source, R.id.text_view_second, "field 'textViewSecond'", TextView.class);
    target.textViewThird = Utils.findRequiredViewAsType(source, R.id.text_view_third, "field 'textViewThird'", TextView.class);
    target.imageViewFirst = Utils.findRequiredViewAsType(source, R.id.image_first, "field 'imageViewFirst'", ImageView.class);
    target.imageViewSecond = Utils.findRequiredViewAsType(source, R.id.image_second, "field 'imageViewSecond'", ImageView.class);
    target.imageViewThird = Utils.findRequiredViewAsType(source, R.id.image_third, "field 'imageViewThird'", ImageView.class);
    target.checkMark = Utils.findRequiredViewAsType(source, R.id.check_mark, "field 'checkMark'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.layout_first_word, "field 'layoutFirstWord' and method 'playAudioAndAnimationFirstWord'");
    target.layoutFirstWord = Utils.castView(view, R.id.layout_first_word, "field 'layoutFirstWord'", LinearLayoutCompat.class);
    view2131296401 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.playAudioAndAnimationFirstWord();
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_second_word, "field 'layoutSecondWord' and method 'playAudioAndAnimationSecondWord'");
    target.layoutSecondWord = Utils.castView(view, R.id.layout_second_word, "field 'layoutSecondWord'", LinearLayoutCompat.class);
    view2131296418 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.playAudioAndAnimationSecondWord();
      }
    });
    view = Utils.findRequiredView(source, R.id.layout_third_word, "field 'layoutThirdWord' and method 'playAudioAndAnimationThirdWord'");
    target.layoutThirdWord = Utils.castView(view, R.id.layout_third_word, "field 'layoutThirdWord'", LinearLayoutCompat.class);
    view2131296422 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.playAudioAndAnimationThirdWord();
      }
    });
    view = Utils.findRequiredView(source, R.id.frame_layout_letter, "field 'layoutLetter' and method 'playAudioAndAnimationLetter'");
    target.layoutLetter = Utils.castView(view, R.id.frame_layout_letter, "field 'layoutLetter'", ConstraintLayout.class);
    view2131296343 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.playAudioAndAnimationLetter();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LetterContentFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.parentLayout = null;
    target.textViewLetter = null;
    target.textViewFirst = null;
    target.textViewSecond = null;
    target.textViewThird = null;
    target.imageViewFirst = null;
    target.imageViewSecond = null;
    target.imageViewThird = null;
    target.checkMark = null;
    target.layoutFirstWord = null;
    target.layoutSecondWord = null;
    target.layoutThirdWord = null;
    target.layoutLetter = null;

    view2131296401.setOnClickListener(null);
    view2131296401 = null;
    view2131296418.setOnClickListener(null);
    view2131296418 = null;
    view2131296422.setOnClickListener(null);
    view2131296422 = null;
    view2131296343.setOnClickListener(null);
    view2131296343 = null;
  }
}
