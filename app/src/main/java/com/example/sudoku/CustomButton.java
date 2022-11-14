package com.example.sudoku;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CustomButton extends FrameLayout {
    int row;
    int col;
    int value;

    public CustomButton(Context context) {

    }

    public CustomButton(Context context, int row, int col) {


        setClickable(true);
        setBackgroundResource(R.drawable.button_selector);
    }

    void setText(String text) {
        TextView textView = new TextView(context);
        addView(textView);
    }
}
