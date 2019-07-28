package com.circles.circlesapp.helpers.record;
/**/

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.circles.circlesapp.R;
import com.tyorikan.voicerecordingvisualizer.VisualizerView;

import java.util.ArrayList;
import java.util.List;

public class RecorderVisualizerView extends View {
    private static final int LINE_WIDTH = 2; // width of visualizer lines
    private static final int LINE_SCALE = 100; // scales visualizer lines
    private List<Float> amplitudes; // amplitudes for line lengths
    private int width; // width of this View
    private int height; // height of this View
    private Paint linePaint; // specifies line drawing characteristics

    private static final int DEFAULT_NUM_COLUMNS = 20;
    private static final int RENDAR_RANGE_TOP = 0;
    private static final int RENDAR_RANGE_BOTTOM = 1;
    private static final int RENDAR_RANGE_TOP_BOTTOM = 2;


    private Canvas mCanvas;
    private Bitmap mCanvasBitmap;
    private Rect mRect = new Rect();
    private Paint mPaint = new Paint();
    private Paint mFadePaint = new Paint();

    private int mNumColumns;
    private int mRenderColor;
    private int mType;
    private int mRenderRange;

    private float mColumnWidth;
    private float mSpace;

    private int mBaseY;


    // constructor
    public RecorderVisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs); // call superclass constructor
        linePaint = new Paint(); // create Paint for lines
        linePaint.setColor(Color.BLUE); // set color to green
        linePaint.setStrokeWidth(LINE_WIDTH); // set stroke width

        TypedArray args = context.obtainStyledAttributes(attrs, R.styleable.visualizerView);
        mNumColumns = args.getInteger(R.styleable.visualizerView_numColumns, DEFAULT_NUM_COLUMNS);
        mRenderColor = args.getColor(R.styleable.visualizerView_renderColor, Color.BLUE);
        mType = args.getInt(R.styleable.visualizerView_renderType, VisualizerView.Type.PIXEL.getFlag());
        mRenderRange = args.getInteger(R.styleable.visualizerView_renderRange, RENDAR_RANGE_TOP);
        args.recycle();
    }

    // called when the dimensions of the View change
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w; // new width of this View
        height = h; // new height of this View
        amplitudes = new ArrayList<Float>(width / LINE_WIDTH);
    }

    // clear all amplitudes to prepare for a new visualization
    public void clear() {
        amplitudes.clear();
    }

    // add the given amplitude to the amplitudes ArrayList
    public void addAmplitude(float volume) {
        if (mCanvas == null) {
            return;
        }

        if (volume == 0) {
            mCanvas.drawColor(Color.BLUE, PorterDuff.Mode.CLEAR);
        } else if ((mType & VisualizerView.Type.FADE.getFlag()) != 0) {
            // Fade out old contents
            mFadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
            mCanvas.drawPaint(mFadePaint);
        } else {
            mCanvas.drawColor(Color.BLUE, PorterDuff.Mode.CLEAR);
        }

        if ((mType & VisualizerView.Type.BAR.getFlag()) != 0) {
            drawBar(volume);
        }
        if ((mType & VisualizerView.Type.PIXEL.getFlag()) != 0) {
            drawPixel(volume);
        }
        invalidate();
    }


    private void drawBar(float volume) {
        for (int i = 0; i < mNumColumns; i++) {
            float height = getRandomHeight(volume);
            float left = i * mColumnWidth + mSpace;
            float right = (i + 1) * mColumnWidth - mSpace;

            RectF rect = createRectF(left, right, height);
            mCanvas.drawRect(rect, mPaint);
        }
    }

    private void drawPixel(float volume) {
        for (int i = 0; i < mNumColumns; i++) {
            float height = getRandomHeight(volume);
            float left = i * mColumnWidth + mSpace;
            float right = (i + 1) * mColumnWidth - mSpace;

            int drawCount = (int) (height / (right - left));
            if (drawCount == 0) {
                drawCount = 1;
            }
            float drawHeight = height / drawCount;

            // draw each pixel
            for (int j = 0; j < drawCount; j++) {

                float top, bottom;
                RectF rect;

                switch (mRenderRange) {
                    case RENDAR_RANGE_TOP:
                        bottom = mBaseY - (drawHeight * j);
                        top = bottom - drawHeight + mSpace;
                        rect = new RectF(left, top, right, bottom);
                        break;

                    case RENDAR_RANGE_BOTTOM:
                        top = mBaseY + (drawHeight * j);
                        bottom = top + drawHeight - mSpace;
                        rect = new RectF(left, top, right, bottom);
                        break;

                    case RENDAR_RANGE_TOP_BOTTOM:
                        bottom = mBaseY - (height / 2) + (drawHeight * j);
                        top = bottom - drawHeight + mSpace;
                        rect = new RectF(left, top, right, bottom);
                        break;

                    default:
                        return;
                }
                mCanvas.drawRect(rect, mPaint);
            }
        }
    }

    private float getRandomHeight(float volume) {
        double randomVolume = Math.random() * volume + 1;
        float height = getHeight();
        switch (mRenderRange) {
            case RENDAR_RANGE_TOP:
                height = mBaseY;
                break;
            case RENDAR_RANGE_BOTTOM:
                height = (getHeight() - mBaseY);
                break;
            case RENDAR_RANGE_TOP_BOTTOM:
                height = getHeight();
                break;
        }
        return (height / 60f) * (float) randomVolume;
    }

    private RectF createRectF(float left, float right, float height) {
        switch (mRenderRange) {
            case RENDAR_RANGE_TOP:
                return new RectF(left, mBaseY - height, right, mBaseY);
            case RENDAR_RANGE_BOTTOM:
                return new RectF(left, mBaseY, right, mBaseY + height);
            case RENDAR_RANGE_TOP_BOTTOM:
                return new RectF(left, mBaseY - height, right, mBaseY + height);
            default:
                return new RectF(left, mBaseY - height, right, mBaseY);
        }
    }

    /**
     * visualizer type
     */
    public enum Type {
        BAR(0x1), PIXEL(0x2), FADE(0x4);

        private int mFlag;

        Type(int flag) {
            mFlag = flag;
        }

        public int getFlag() {
            return mFlag;
        }
    }

    // draw the visualizer with scaled lines representing the amplitudes
    @Override
    public void onDraw(Canvas canvas) {
        int middle = height / 2; // get the middle of the View
        float curX = 0; // start curX at zero

        // for each item in the amplitudes ArrayList
        for (float power : amplitudes) {
            float scaledHeight = power / LINE_SCALE; // scale the power
            curX += LINE_WIDTH; // increase X by LINE_WIDTH

            // draw a line representing this item in the amplitudes ArrayList
            canvas.drawLine(curX, middle + scaledHeight / 2, curX, middle
                    - scaledHeight / 2, linePaint);
        }

        // for each item in the amplitudes ArrayList

        mRect.set(0, 0, getWidth(), getHeight());

        if (mCanvasBitmap == null) {
            mCanvasBitmap = Bitmap.createBitmap(
                    canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        }

        if (mCanvas == null) {
            mCanvas = new Canvas(mCanvasBitmap);
        }

        if (mNumColumns > getWidth()) {
            mNumColumns = DEFAULT_NUM_COLUMNS;
        }

        mColumnWidth = (float) getWidth() / (float) mNumColumns;
        mSpace = mColumnWidth / 8f;

        if (mBaseY == 0) {
            mBaseY = getHeight() / 2;
        }

        canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
    }
}