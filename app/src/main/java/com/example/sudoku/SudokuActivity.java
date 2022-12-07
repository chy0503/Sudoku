package com.example.sudoku;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SudokuActivity extends AppCompatActivity {
    CustomButton clickedCustomButton;
    CustomDialog customDialog;
    CustomButton[][] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        double level = intent.getExtras().getDouble("level");
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        bordGenerate(level);

        // 스톱워치 만들기
        Chronometer chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        Button resetB = findViewById(R.id.resetB);
        resetB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.removeAllViews();
                bordGenerate(level);
                chronometer.setBase(SystemClock.elapsedRealtime());
            }
        });
    }

    public void bordGenerate(double level) {
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        table.setPadding(15, 15, 15, 15);
        buttons = new CustomButton[9][9];
        BoardGenerator board = new BoardGenerator();
        for (int i=0; i<9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new CustomButton(this, i, j);
                if (Math.random() >= level) {
                    buttons[i][j].set(board.get(i, j));
                    buttons[i][j].setClickable(false);
                }
                else {
                    buttons[i][j].set(0);
                    buttons[i][j].setClickable(true);
                    buttons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickedCustomButton = (CustomButton) view;
                            customDialog = new CustomDialog(SudokuActivity.this);
                            customDialog.callFunction();
                        }
                    });
                }
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);
                int left = 5, top = 5, right = 5, bottom = 5;
                if (i == 3 || i == 6) {
                    top = 10;
                }
                if (j == 3 || j == 6) {
                    left = 10;
                }
                layoutParams.setMargins(left, top, right, bottom);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);
            }
        }
    }

    public void HomeB_Click(View v) {
        finish();
    }

    // 눌린 numpad의 아이디를 받아 n+i에서 substring으로 숫자만 분리
    public void onClickNum(View v) {
        String str = v.getResources().getResourceEntryName(v.getId());
        if (clickedCustomButton.value != 0) {
            unsetConflict();
        }
        clickedCustomButton.set(Integer.valueOf(str.substring(1)));
        clickedCustomButton.clicked();
        setConflict();
        customDialog.dlgDismiss();
        clickedCustomButton.setTextColor("#8C9EFF");
    }

    public void setConflict() {
        int row = clickedCustomButton.row;
        int col = clickedCustomButton.col;
        int sqRow = row / 3;
        int sqCol = col / 3;

        for (int i = 0; i < 9 ; i++) {
            if (buttons[i][col].value == clickedCustomButton.value)
                buttons[i][col].setTextColor("#FF0066");
        }
        for (int j = 0; j < 9 ; j++) {
            if (buttons[row][j].value == clickedCustomButton.value)
                buttons[row][j].setTextColor("#FF0066");
        }
        for (int i = sqRow * 3; i < (sqRow + 1) * 3 ; i++) {
            for (int j = sqCol * 3; j < (sqCol + 1) * 3 ; j++) {
                if (buttons[i][j].value == clickedCustomButton.value)
                    buttons[i][j].setTextColor("#FF0066");
            }
        }
    }

    public void unsetConflict() {
        int row = clickedCustomButton.row;
        int col = clickedCustomButton.col;
        int sqRow = row / 3;
        int sqCol = col / 3;

        for (int i = 0; i < 9 ; i++) {
            if (buttons[i][col].value == clickedCustomButton.value) {
                if (buttons[i][col].isBold == true)
                    buttons[i][col].setTextColor("#8C9EFF");
                else
                    buttons[i][col].setTextColor("#000000");
            }
        }
        for (int j = 0; j < 9 ; j++) {
            if (buttons[row][j].value == clickedCustomButton.value) {
                if (buttons[row][j].isBold == true)
                    buttons[row][j].setTextColor("#8C9EFF");
                else
                    buttons[row][j].setTextColor("#000000");
            }
        }
        for (int i = sqRow * 3; i < (sqRow + 1) * 3 ; i++) {
            for (int j = sqCol * 3; j < (sqCol + 1) * 3 ; j++) {
                if (buttons[i][j].value == clickedCustomButton.value) {
                    if (buttons[i][j].isBold == true)
                        buttons[i][j].setTextColor("#8C9EFF");
                    else
                        buttons[i][j].setTextColor("#000000");
                }
            }
        }
    }
}

// 메모
// clear

// record 부가
// undo, redo
// hint