package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class SudokuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        getSupportActionBar().hide();

        Chronometer chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        Intent intent = getIntent();
        double level = intent.getExtras().getDouble("level");

//        FrameLayout frame = (FrameLayout)findViewById(R.id.frameLayout);
//        LinearLayout padLayout = new LinearLayout(this);
//        frame.addView(padLayout);
//        TextView padTitle = new TextView(this);
//        padTitle.setText("Input Number");
//        padLayout.addView(padTitle);
//        TableLayout numberPad = new TableLayout(this);
//        padLayout.addView(numberPad);
//        Button[][] numButtons = new Button[3][3];
//        int num = 1;
//        for (int i = 0; i < 3; i++) {
//            TableRow tableRow = new TableRow(this);
//            numberPad.addView(tableRow);
//            for (int j = 0; j < 3; j++) {
//                numButtons[i][j].setText(num);
//                num++;
//            }
//        }
//        numberPad.setVisibility(View.INVISIBLE);

        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        table.setPadding(15, 15, 15, 15);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f);
        layoutParams.setMargins(5, 5, 5, 5); // 이게 왜 공통적으로 적용이 되는가.. 했다 이거쥥
        CustomButton[][] buttons = new CustomButton[9][9];
        BoardGenerator board = new BoardGenerator();
        for (int i=0; i<9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new CustomButton(this, i, j);
                buttons[i][j].setLayoutParams(layoutParams);
                if (Math.random() >= level)
                    buttons[i][j].set(this, board.get(i, j));
                else
                    buttons[i][j].set(this, 0);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        numberPadSet("pad");
                    }
                });
                tableRow.addView(buttons[i][j]);
            }
        }

        Button padCancelB = findViewById(R.id.cancelB);
        padCancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberPadSet("back");
            }
        });
    }

    public void HomeB_Click(View v) {
        finish();
    }

    public void numberPadSet(String screen) {
        ImageView P1 = findViewById(R.id.padBG);
        LinearLayout P2 = findViewById(R.id.pad);
        Toolbar S1 = findViewById(R.id.sudokuBar);
        LinearLayout S2 = findViewById(R.id.timeBar);
        Chronometer S3 = findViewById(R.id.chronometer);
        TableLayout S4 = findViewById(R.id.tableLayout);
        CircleImageView S5 = findViewById(R.id.undoB);
        CircleImageView S6 = findViewById(R.id.hintB);
        CircleImageView S7 = findViewById(R.id.redoB);

        switch(screen) {
            case "pad":
                P1.setVisibility(View.VISIBLE);
                P2.setVisibility(View.VISIBLE);
                S1.setClickable(false);
                S2.setClickable(false);
                S3.setClickable(false);
                S4.setClickable(false);
                S5.setClickable(false);
                S6.setClickable(false);
                S7.setClickable(false);
                break;
            case "back":
                P1.setVisibility(View.GONE);
                P2.setVisibility(View.GONE);
                S1.setClickable(true);
                S2.setClickable(true);
                S3.setClickable(true);
                S4.setClickable(true);
                S5.setClickable(true);
                S6.setClickable(true);
                S7.setClickable(true);
                break;
        }
    }

    public void setConflict() {

    }

    public void unsetConflict() {

    }
}
// numberpad 문제