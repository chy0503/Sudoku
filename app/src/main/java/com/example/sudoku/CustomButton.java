package com.example.sudoku;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CustomButton extends FrameLayout {
    int row;
    int col;
    int value;
    TextView textView;
    boolean isBold = false;

    public CustomButton(Context context, int row, int col) {
        super(context);
        this.row = row;
        this.col = col;

        textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.button_selector);
        textView.setTextSize(32);
        textView.setGravity(CENTER);
        textView.setTextColor(Color.BLACK);
        addView(textView);
    }

    void set(int a) {
        if (a == 0) {
            this.value = 0;
            textView.setText(" ");
        }
        else {
            this.value = a;
            textView.setText(String.valueOf(a));
        }
    }

    void setTextColor(String color) {
        textView.setTextColor(Color.parseColor(color));
    }

    void clicked() {
        if (value != 0) {
            textView.setTypeface(null, Typeface.BOLD);
            isBold = true;
        } else {
            textView.setTypeface(null, Typeface.NORMAL);
            isBold = false;
        }
    }

    int getTextColor() {
        return textView.getCurrentTextColor();
    }
}
