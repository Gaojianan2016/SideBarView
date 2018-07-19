package com.gjn.sidebarviewlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @author gjn
 * @time 2018/7/18 16:15
 */

public class SideBarView extends View {
    private static final String TAG = "SideBarView";

    private static final String[] LETTER = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private String[] letter = LETTER;

    private Paint paint;
    private int choose = -1;

    private int selectColor = Color.RED;
    private int normalColor = Color.BLACK;

    private boolean isBold = true;
    private int textSize = -1;

    private TextView showTextView;
    private long delayTime = 500;

    private onChooseLetterListener onChooseLetterListener;

    public SideBarView(Context context) {
        this(context, null);
    }

    public SideBarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSideBarView();
    }

    private void initSideBarView() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
        invalidate();
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        invalidate();
    }

    public void setBold(boolean bold) {
        isBold = bold;
        invalidate();
    }

    public void setLetter(String[] letter) {
        this.letter = letter == null ? new String[0] : letter;
        invalidate();
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    public void setShowTextView(TextView textView) {
        showTextView = textView;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public void setOnChooseLetterListener(SideBarView.onChooseLetterListener onChooseLetterListener) {
        this.onChooseLetterListener = onChooseLetterListener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (letter.length == 0) {
            Log.w(TAG, "letter is null, reset default LETTER");
            letter = LETTER;
        }
        int w = getWidth();
        int h = getHeight() - getPaddingTop() - getPaddingBottom();
        int sizeW = w - getPaddingLeft() - getPaddingRight();
        int sizeH = h / letter.length;
        int size = Math.min(sizeW, sizeH);
        if (isBold) {
            paint.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            paint.setTypeface(Typeface.DEFAULT);
        }
        if (textSize > 0) {
            paint.setTextSize(textSize);
        } else {
            paint.setTextSize(size - 7);
        }
        for (int i = 0; i < letter.length; i++) {
            if (i == choose) {
                paint.setColor(selectColor);
            } else {
                paint.setColor(normalColor);
            }
            float xPos = w / 2 - paint.measureText(letter[i]) / 2;
            float textH = sizeH * (1 + i) - paint.measureText(letter[i]) / 2;
            float yPos = textH + getPaddingTop();
            canvas.drawText(letter[i], xPos, yPos, paint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y = event.getY() - getPaddingTop();
        int h = getHeight() - getPaddingTop() - getPaddingBottom();
        if (y > 0 && y < getHeight() - getPaddingBottom()) {
            int oldChoose = choose;
            int newChoose = (int) (y / h * letter.length);
            newChoose = Math.min(letter.length - 1, newChoose);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                reset();
            } else {
                if (newChoose != oldChoose) {
                    if (showTextView != null) {
                        showTextView.setText(letter[newChoose]);
                        showTextView.setVisibility(VISIBLE);
                    }
                    if (onChooseLetterListener != null) {
                        onChooseLetterListener.chooseLetter(letter[newChoose]);
                    }
                    choose = newChoose;
                    invalidate();
                }
            }
        } else {
            reset();
        }
        return true;
    }

    private void reset() {
        choose = -1;
        invalidate();
        if (showTextView != null) {
            showTextView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showTextView.setVisibility(GONE);
                }
            }, delayTime);
        }
    }

    public interface onChooseLetterListener {
        void chooseLetter(String letter);
    }
}
