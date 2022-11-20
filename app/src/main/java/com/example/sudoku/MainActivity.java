package com.example.sudoku;

import static android.view.Gravity.CENTER;
import static android.view.View.VISIBLE;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        layoutParams.setMargins(5, 5, 5, 5);
        CustomButton[][] buttons = new CustomButton[9][9];
        BoardGenerator board = new BoardGenerator();
        for (int i=0; i<9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new CustomButton(this, i, j);
                buttons[i][j].setLayoutParams(layoutParams);
                if (Math.random() >= 0.5)
                    buttons[i][j].set(this, board.get(i, j));
                else
                    buttons[i][j].set(this, 0);
//                buttons[i][j].setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        numberPad.setVisibility(view.VISIBLE);
//                    }
//                });
                tableRow.addView(buttons[i][j]);
            }
        }
    }

    public void setConflict() {

    }

    public void unsetConflict() {

    }
}