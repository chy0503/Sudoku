package com.example.sudoku;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class SudokuActivity extends AppCompatActivity {
    TableLayout table;
    double level;

    Chronometer chronometer;
    long pauseoffset;

    CustomButton clickedCustomButton;
    BoardGenerator board;
    CustomNumpad numpad;
    CustomButton[][] buttons;
    boolean[] selectedToggleButtons = new boolean[9];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        level = intent.getExtras().getDouble("level");
        table = (TableLayout)findViewById(R.id.tableLayout);
        bordGenerate(level);

        // 스톱워치 만들기
        chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        ImageView homeB = findViewById(R.id.home);
        homeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                builder.setMessage("지금 화면을 나간다면 진행하고 있는 게임은 저장되지 않습니다. 그래도 나가시겠습니까?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        TextView levelT = findViewById(R.id.level);
        if (level == 0.3)
            levelT.setText("EASY");
        else if (level == 0.5)
            levelT.setText("NORMAL");
        else if (level == 0.65)
            levelT.setText("HARD");

        Button chronoB = findViewById(R.id.chronoB);
        Button resetB = findViewById(R.id.resetB);
        Button clearB = findViewById(R.id.clearB);
        chronoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                pauseoffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                builder.setTitle("PAUSE")
                        .setPositiveButton("close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                chronometer.setBase(SystemClock.elapsedRealtime() - pauseoffset);
                                chronometer.start();
                                dialogInterface.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        resetB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.removeAllViews();
                bordGenerate(level);
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseoffset = 0;
            }
        });
        clearB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < 9; i ++) {
                    for (int j = 0; j < 9; j++) {
                        if (buttons[i][j].isBold == true) {
                            buttons[i][j].set(0);
                            buttons[i][j].clicked();
                        } else  // 충돌이 있는 상태에서 CLEAR 버튼 click 시
                            buttons[i][j].setTextColor("#000000");
                    }
                }
            }
        });
    }

    public void bordGenerate(double level) {
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
        table.setPadding(15, 15, 15, 15);
        buttons = new CustomButton[9][9];
        board = new BoardGenerator();
        for (int i=0; i<9; i++) {
            TableRow tableRow = new TableRow(this);
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
                            numpad = new CustomNumpad(SudokuActivity.this);
                            numpad.callFunction();
                        }
                    });
                    buttons[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            clickedCustomButton = (CustomButton) view;
                            View memo = (View) View.inflate(table.getContext(), R.layout.dialog_memo, null);
                            AlertDialog.Builder builder= new AlertDialog.Builder(table.getContext());
                            builder.setView(memo)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            TableLayout memoTable = (TableLayout) memo.findViewById(R.id.memoPad);
                                            int k = 0;
                                            for (int i = 0; i < 3; i++) {
                                                TableRow tableRow = (TableRow) memoTable.getChildAt(i);
                                                for (int j = 0; j < 3; j++, k++) {
                                                    ToggleButton toggleButton = (ToggleButton) tableRow.getChildAt(j);
                                                    if (toggleButton.isChecked()) {
                                                        selectedToggleButtons[k] = true;
                                                    } else {
                                                        selectedToggleButtons[k] = false;
                                                    }
                                                }
                                            }
                                            for (int i = 0; i < 9; i++) {
                                                if (selectedToggleButtons[i] == true && clickedCustomButton.value==0)
                                                    clickedCustomButton.memos[i].setVisibility(View.VISIBLE);
                                            }
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            dialogInterface.dismiss();
                                        }
                                    })
                                    .setNeutralButton("DELETE", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int which) {
                                            for (int i = 0; i < 9; i++) {
                                                selectedToggleButtons[i] = false;
                                                clickedCustomButton.memos[i].setVisibility(View.INVISIBLE);
                                            }
                                            dialogInterface.dismiss();
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            return true;
                        }
                    });
                }
                // width: 열에 값이 없을 때 값이 있는 곳과 너비가 달라지기 때문에 고정값로 설정
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics()),
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
            table.addView(tableRow);
        }
    }

    // numpad의 아이디("n"+i)를 받아 눌린 숫자 분리
    public void onClickNum(View v) {
        String str = v.getResources().getResourceEntryName(v.getId());
        if (clickedCustomButton.value != 0) {
            unsetConflict();
        }
        clickedCustomButton.set(Integer.valueOf(str.substring(1)));
        clickedCustomButton.clicked();

        // memo가 있는 상태에서 numpad를 click하면 memo를 지우고, 숫자를 입력하지 않고 del을 누른 경우에는 memo 유지
        if (Integer.valueOf(str.substring(1)) != 0) {
            for (int i = 0; i < 9; i++) {
                selectedToggleButtons[i] = false;
                clickedCustomButton.memos[i].setVisibility(View.INVISIBLE);
            }
        }
        setConflict();
        numpad.dlgDismiss();
        clickedCustomButton.setTextColor("#8C9EFF");
        if (completeCheck()) {
            chronometer.stop();
            long current = SystemClock.elapsedRealtime() - chronometer.getBase();
            int time = (int) (current / 1000);
            // 시간 구하기
            int hour = time / (60 * 60);
            int min = time % (60 * 60) / 60;
            int sec = time % 60;

            // format 설정
            String currentTime = min + " : " + sec;;
            if (hour != 0)
                currentTime = hour + " : " + currentTime;

            AlertDialog.Builder builder= new AlertDialog.Builder(v.getContext());
            builder.setTitle(currentTime)
                    .setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            table.removeAllViews();
                            bordGenerate(level);
                            chronometer.setBase(SystemClock.elapsedRealtime());
                            pauseoffset = 0;
                            chronometer.start();
                            dialogInterface.dismiss();
                        }
                    })
                    .setNeutralButton("Back to main", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            finish();
                            dialogInterface.dismiss();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
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

    boolean completeCheck() {
        for (int i = 0; i < 9; i ++) {
            for (int j = 0; j < 9; j++) {
                // 빈 칸이 있으면 검사 중단
                if (buttons[i][j].value == 0 )
                    return false;
                // 기입된 숫자가 답과 일치하지 않으면 중단
                else if (buttons[i][j].value != board.get(i, j))
                    return false;
                // 숫자가 모두 기입되어 있으며 그 답이 모두 일치한다면 완료
                else if (i == 8 & j == 8)
                    return true;
            }
        }
        return false;
    }
}

// complete dialog ui
// record
// undo, redo
// hint