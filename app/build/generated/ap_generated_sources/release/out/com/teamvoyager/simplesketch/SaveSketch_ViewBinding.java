// Generated code from Butter Knife. Do not modify!
package com.teamvoyager.simplesketch;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SaveSketch_ViewBinding implements Unbinder {
  private SaveSketch target;

  private View view7f07004a;

  private View view7f07004b;

  private View view7f07004c;

  private View view7f07004d;

  private View view7f07004e;

  @UiThread
  public SaveSketch_ViewBinding(SaveSketch target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SaveSketch_ViewBinding(final SaveSketch target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.button_1, "field 'button' and method 'onViewClicked'");
    target.button = Utils.castView(view, R.id.button_1, "field 'button'", Button.class);
    view7f07004a = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_2, "field 'button2' and method 'onButton2Clicked'");
    target.button2 = Utils.castView(view, R.id.button_2, "field 'button2'", Button.class);
    view7f07004b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onButton2Clicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_3, "field 'button3' and method 'onButton3Clicked'");
    target.button3 = Utils.castView(view, R.id.button_3, "field 'button3'", Button.class);
    view7f07004c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onButton3Clicked();
      }
    });
    target.imageView = Utils.findRequiredViewAsType(source, R.id.saveImage, "field 'imageView'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.button_4, "field 'button4' and method 'onButton4Clicked'");
    target.button4 = Utils.castView(view, R.id.button_4, "field 'button4'", Button.class);
    view7f07004d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onButton4Clicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_5, "field 'button5' and method 'onButton5Clicked'");
    target.button5 = Utils.castView(view, R.id.button_5, "field 'button5'", Button.class);
    view7f07004e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onButton5Clicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SaveSketch target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.button = null;
    target.button2 = null;
    target.button3 = null;
    target.imageView = null;
    target.button4 = null;
    target.button5 = null;

    view7f07004a.setOnClickListener(null);
    view7f07004a = null;
    view7f07004b.setOnClickListener(null);
    view7f07004b = null;
    view7f07004c.setOnClickListener(null);
    view7f07004c = null;
    view7f07004d.setOnClickListener(null);
    view7f07004d = null;
    view7f07004e.setOnClickListener(null);
    view7f07004e = null;
  }
}
