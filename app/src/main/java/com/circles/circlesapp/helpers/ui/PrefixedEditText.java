package com.circles.circlesapp.helpers.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;


public class PrefixedEditText extends AppCompatEditText {
    public float mOriginalLeftPadding = -1;
    private String prefixText;
    public String leftPadding = "";

    public PrefixedEditText(Context context) {
        super(context);
    }

    public PrefixedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PrefixedEditText(Context context, AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getPrefixText() {
        return (prefixText == null) ? "" : prefixText;
    }

    public void setPrefixText(String prefixText) {
        mOriginalLeftPadding = -1;
        this.prefixText = prefixText;
        requestLayout();
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mOriginalLeftPadding == -1) {
            String prefix = getPrefixText();
            float[] widths = new float[prefix.length()];
            getPaint().getTextWidths(prefix, widths);
            float textWidth = 0;
            for (float w : widths) {
                textWidth += w;
            }
            mOriginalLeftPadding = getLeftPadding(widths.length);
            setPadding((int) (textWidth + mOriginalLeftPadding + 8),
                    getPaddingRight(), getPaddingTop(),
                    getPaddingBottom());
        }
    }

    private float getLeftPadding(int length) {
        if (length <= 1) return pxFromDp(getContext(), 2);
        return pxFromDp(getContext(), length);
    }

    private void calculatePrefix() {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String prefix = (String) getPrefixText();
        canvas.drawText(prefix, mOriginalLeftPadding,
                getLineBounds(0, null), getPaint());
    }
    public  float dpFromPx(final Context context, final float px) {
        float v = px / context.getResources().getDisplayMetrics().density;

        return v;
    }

    public  float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}