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
    private final int DYNAMIC_VIEW_ID = 0x8000;

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
            }
        });
    }

    public void bordGenerate(double level) {
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        table.setPadding(15, 15, 15, 15);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f);
        layoutParams.setMargins(5, 5, 5, 5);
        CustomButton[][] buttons = new CustomButton[9][9];
        BoardGenerator board = new BoardGenerator();
        for (int i=0; i<9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new CustomButton(this, i, j);
                buttons[i][j].setLayoutParams(layoutParams);
//                buttons[i][j].setId(DYNAMIC_VIEW_ID + numButton);
                if (Math.random() >= level) {
                    buttons[i][j].set(this, board.get(i, j));
                    buttons[i][j].setClickable(false);
                }
                else {
                    buttons[i][j].set(this, 0);
                    buttons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CustomDialog customDialog = new CustomDialog(SudokuActivity.this);

                            // 커스텀 다이얼로그를 호출한다.
                            // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
//                            customDialog.callFunction(main_label);
                            int num = customDialog.callFunction();
//                            buttons[i][j].set(this, num); // 몰라잉 시발
                        }
                    });

                }
                tableRow.addView(buttons[i][j]);
            }
        }
    }

    public void HomeB_Click(View v) {
        finish();
    }

    public void numpadReturn(View v) {
        int num = 0;
        String str = v.getResources().getResourceEntryName(v.getId());
        num = Integer.valueOf(str.substring(1));
    }


    public void setConflict() {

    }

    public void unsetConflict() {

    }
}

// 배열버튼 넘기고 numpad 반환
// 충돌 검사
// 메모
// clear
// 3*3마다 진한 선

// rule 사진
// rule fix
// record 부가
// undo, redo
// hint