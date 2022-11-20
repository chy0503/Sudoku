package com.example.sudoku;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CustomButton extends FrameLayout {
    int row;
    int col;
    int value;

    public CustomButton(Context context) {
        super(context);
    }

    public CustomButton(Context context, int row, int col) {
        super(context);
        setClickable(true);
        setBackgroundResource(R.drawable.button_selector);
    }

    void set(Context context, int a) {
        TextView textView = new TextView(context);
        textView.setTextSize(30);
        textView.setGravity(CENTER);
        if (a == 0)
            textView.setText(" ");
        else
            textView.setText(String.valueOf(a));
        addView(textView);
    }
}
