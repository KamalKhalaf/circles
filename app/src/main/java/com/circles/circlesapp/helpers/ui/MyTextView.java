package com.circles.circlesapp.helpers.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "BalooBhaina-Regular.ttf");
        setTypeface(tf, Typeface.BOLD);

    }
}