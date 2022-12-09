package com.example.sudoku;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class SudokuActivity extends AppCompatActivity {
    // 스톱워치
    Chronometer chronometer;
    long pauseoffset;
    String currentTime;

    // 스도쿠 테이블
    TableLayout table;
    double level;
    BoardGenerator board;
    CustomButton[][] buttons;
    CustomButton clickedCustomButton;
    CustomNumpad numpad;
    boolean[] selectedToggleButtons = new boolean[9];

    // 기록 저장
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String myStr;
    String levelSP;
    View complete;

    // undo할 때, CustomButton을 저장할 List
    ArrayList<CustomButton> inputList = new ArrayList<CustomButton>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        getSupportActionBar().hide();

        // intent 값을 통해 level 값 받기
        Intent intent = getIntent();
        level = intent.getExtras().getDouble("level");

        // SharedPreferences 식별값 설정, 초기화
        if (level == 0.25)
            levelSP = "easy_BR";
        else if (level == 0.4)
            levelSP = "normal_BR";
        else
            levelSP = "hard_BR";

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        // RESET을 누를 시, 스도쿠 재생성
        table = (TableLayout)findViewById(R.id.tableLayout);
        bordGenerate(level);
        inputList.clear();

        // 스톱워치
        chronometer = findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        // actionbar - main으로 나가는 홈버튼
        ImageView homeB = findViewById(R.id.home);
        homeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder= new AlertDialog.Builder(view.getContext());
                builder.setMessage("진행 중인 게임은 저장되지 않습니다. 그래도 나가시겠습니까?")
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

        // actionbar - title 자리에 level 보여주기
        TextView levelT = findViewById(R.id.level);
        if (level == 0.25)
            levelT.setText("EASY");
        else if (level == 0.4)
            levelT.setText("NORMAL");
        else if (level == 0.65)
            levelT.setText("HARD");

        // 스톱워치 - 누르면 시간이 멈추고 스도쿠 테이블을 가림
        Button chronoB = findViewById(R.id.chronoB);
        FrameLayout pauseScreen = findViewById(R.id.pauseScreen);
        chronoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.stop();
                pauseoffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                pauseScreen.setVisibility(View.VISIBLE);
                table.setVisibility(View.GONE);
            }
        });

        // 스톱워치 - 스도쿠 테이블을 가린 레이아웃을 지우는 버튼
        TextView pauseB = findViewById(R.id.pauseB);
        pauseB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseoffset);
                chronometer.start();
                pauseScreen.setVisibility(View.GONE);
                table.setVisibility(View.VISIBLE);
            }
        });

        // reset - 스도쿠 테이블 재생성
        Button resetB = findViewById(R.id.resetB);
        resetB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                table.removeAllViews();
                bordGenerate(level);
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseoffset = 0;
            }
        });

        // clear - 사용자가 입력한 값 모두 지우기
        Button clearB = findViewById(R.id.clearB);
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

        // undo - 사용자가 입력한 값 순서대로 하나씩 지우기
        CircleImageView undo = findViewById(R.id.undoB);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!inputList.isEmpty()) {
                    CustomButton temp = inputList.get(inputList.size() - 1);
                    unsetConflict();
                    temp.set(0);
                    temp.clicked();
                    inputList.remove(inputList.size() - 1);
                }
            }
        });
    }

    // 스도쿠 생성 함수
    public void bordGenerate(double level) {
        table = (TableLayout)findViewById(R.id.tableLayout);
        table.setPadding(15, 15, 15, 15);
        buttons = new CustomButton[9][9];
        board = new BoardGenerator();

        for (int i=0; i<9; i++) {
            TableRow tableRow = new TableRow(this);
            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new CustomButton(this, i, j);
                // 초기에 생성된 곳 - 클릭 할 수 없도록 함
                if (Math.random() >= 0.05) {
                    buttons[i][j].set(board.get(i, j));
                    buttons[i][j].setClickable(false);
                }
                // 사용자가 입력할 곳
                else {
                    buttons[i][j].set(0);
                    buttons[i][j].setClickable(true);
                    // numpad
                    buttons[i][j].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clickedCustomButton = (CustomButton) view;
                            numpad = new CustomNumpad(SudokuActivity.this);
                            numpad.callFunction();
                        }
                    });
                    // memo
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
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,5,getResources().getDisplayMetrics()),
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f); // width -> 열에 값이 없을 때 값이 있는 곳과 너비가 달라지기 때문에 고정값로 설정함..
                // 3*3 부분 구분선
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

    // numpad 숫자를 버튼에 입력하는 함수
    public void onClickNum(View v) {
        // 눌린 numpad 버튼의 아이디("n"+i)를 받아 눌린 숫자 분리하여 버튼의 value로 입력
        // 기존에 숫자가 입력된 버튼일 경우, 기존 충돌을 삭제 후 사용
        if (clickedCustomButton.value != 0) {
            unsetConflict();
        }
        String str = v.getResources().getResourceEntryName(v.getId());
        clickedCustomButton.set(Integer.valueOf(str.substring(1)));
        clickedCustomButton.clicked();                  // 볼드체 설정
        setConflict();                                  // 충돌 검사
        numpad.dlgDismiss();                            // 다이얼로그 닫기
        clickedCustomButton.setTextColor("#8C9EFF");    // 사용자가 입력한 숫자를 구분하기 위한 색상 설정

        // 지금까지 입력된 input을 저장하여 undo 할 때
        // numpad의 del이 눌렸을 시 inpulList에서도 삭제
        if(Integer.valueOf(str.substring(1)) != 0) {
            inputList.add(clickedCustomButton);
        } else
            inputList.remove(clickedCustomButton);

        // memo가 있는 상태에서 numpad를 click하면 memo를 지움
        // 단, 숫자를 입력하지 않고 del을 누른 경우에는 memo를 유지할 수 있도록 if문 검사
        if (Integer.valueOf(str.substring(1)) != 0) {
            for (int i = 0; i < 9; i++) {
                selectedToggleButtons[i] = false;
                clickedCustomButton.memos[i].setVisibility(View.INVISIBLE);
            }
        }

        // 스도쿠를 완성했는지 검사
        if (completeCheck()) {
            chronometer.stop();
            long current = SystemClock.elapsedRealtime() - chronometer.getBase();

            // 시간 변환
            int time = (int) (current / 1000);
            int hour = time / (60 * 60);
            int min = time % (60 * 60) / 60;
            int sec = time % 60;

            // format 설정
            currentTime = min + " : " + sec;
            if (hour != 0)
                currentTime = hour + " : " + currentTime;

            // 게임 클리어 다이얼로그 - 다이얼로그 불러오기
            complete = (View) View.inflate(this, R.layout.dialog_complete, null);

            // 게임 클리어 다이얼로그 - 현재 기록 반영
            TextView record = complete.findViewById(R.id.recordView);
            record.setText(currentTime);

            // 게임 클리어 다이얼로그 - 최고 기록 불러오기
            TextView BestRecord = complete.findViewById(R.id.bestRecord);
            myStr = pref.getString(levelSP, "0 : 00");
            BestRecord.setText(myStr);

            // 게임 클리어 다이얼로그 - 현재 기록과 최고 기록 비교하여 업데이트
            recordUpdate(currentTime);

            // 게임 클리어 다이얼로그
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.setView(complete)
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

    // 충돌 검사 함수
    public void setConflict() {
        int row = clickedCustomButton.row;
        int col = clickedCustomButton.col;
        int sqRow = row / 3;
        int sqCol = col / 3;

        // 세로줄 검사
        for (int i = 0; i < 9 ; i++) {
            if (buttons[i][col].value == clickedCustomButton.value)
                buttons[i][col].setTextColor("#FF0066");
        }
        // 가로줄 검사
        for (int j = 0; j < 9 ; j++) {
            if (buttons[row][j].value == clickedCustomButton.value)
                buttons[row][j].setTextColor("#FF0066");
        }
        // 3*3 검사
        for (int i = sqRow * 3; i < (sqRow + 1) * 3 ; i++) {
            for (int j = sqCol * 3; j < (sqCol + 1) * 3 ; j++) {
                if (buttons[i][j].value == clickedCustomButton.value)
                    buttons[i][j].setTextColor("#FF0066");
            }
        }
    }

    // 충돌 푸는 함수
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

    // 스도쿠 완성 검사 함수
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

    // best record 비교, 업데이트 함수
    void recordUpdate(String record) {
        String bestRecord = pref.getString(levelSP, "0 : 00");
        String BR[] = bestRecord.split(" : ");
        String R[] = record.split(" : ");
        if (BR.length < R.length)
            return;
        else if(BR.length == R.length) {
            for (int i = 0; i < BR.length; i++) {
                if(Integer.valueOf(BR[0]) < Integer.valueOf(R[0]))
                    return;
            }
        }
        myStr = record;
        editor.putString(levelSP, myStr);
        editor.apply();
    }
}