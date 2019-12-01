package com.droidlogic.mboxlauncher;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ViewFlipper;

public class MyViewFlipper extends ViewFlipper {
    public MyViewFlipper(Context context) {
        super(context);
    }

    public MyViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
