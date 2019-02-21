// Generated code from Butter Knife. Do not modify!
package com.hearatale.sightword.ui.quiz_puzzle.content;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.hearatale.sightword.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PuzzleLetterFragment_ViewBinding implements Unbinder {
  private PuzzleLetterFragment target;

  private View view2131296413;

  private View view2131296374;

  private View view2131296526;

  @UiThread
  public PuzzleLetterFragment_ViewBinding(final PuzzleLetterFragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.layout_puzzle, "field 'layoutPuzzle' and method 'showIdiomActivity'");
    target.layoutPuzzle = Utils.castView(view, R.id.layout_puzzle, "field 'layoutPuzzle'", RelativeLayout.class);
    view2131296413 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showIdiomActivity();
      }
    });
    target.layoutPuzzleFake = Utils.findRequiredViewAsType(source, R.id.layout_puzzle_fake, "field 'layoutPuzzleFake'", RelativeLayout.class);
    view = Utils.findRequiredView(source, R.id.image_puzzle_completed, "field 'imagePuzzleCompleted' and method 'showIdiomActivity'");
    target.imagePuzzleCompleted = Utils.castView(view, R.id.image_puzzle_completed, "field 'imagePuzzleCompleted'", ImageView.class);
    view2131296374 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showIdiomActivity();
      }
    });
    view = Utils.findRequiredView(source, R.id.text_title, "field 'textTitle' and method 'playAudioAndAnimationLetter'");
    target.textTitle = Utils.castView(view, R.id.text_title, "field 'textTitle'", TextView.class);
    view2131296526 = view;
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
    PuzzleLetterFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.layoutPuzzle = null;
    target.layoutPuzzleFake = null;
    target.imagePuzzleCompleted = null;
    target.textTitle = null;

    view2131296413.setOnClickListener(null);
    view2131296413 = null;
    view2131296374.setOnClickListener(null);
    view2131296374 = null;
    view2131296526.setOnClickListener(null);
    view2131296526 = null;
  }
}
