package com.example.sudoku;

import static android.view.Gravity.CENTER;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

public class CustomButton extends FrameLayout {
    int row;
    int col;
    int value;
    TextView textView;
    TextView[] memos;
    boolean isBold = false; // 사용자가 숫자를 입력한 곳

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

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TableLayout memo = (TableLayout) layoutInflater.inflate(R.layout.layout_memo, null);
        memos  = new TextView[9];
        int k = 0;
        for(int i = 0; i < 3; i++) {
            TableRow tableRow = (TableRow) memo.getChildAt(i);
            for(int j = 0; j < 3; j++, k++) {
                memos[k] = (TextView) tableRow.getChildAt(j);
                memos[k].setVisibility(INVISIBLE);
            }
        }
        addView(memo);
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
}
