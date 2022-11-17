package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TableLayout table = (TableLayout)findViewById(R.id.tableLayout);
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
            for (int j=0; j<9; j++) {
                buttons[i][j] = new CustomButton(this);
                buttons[i][j].setLayoutParams(layoutParams);
                if (Math.random() >= 0.5)
                    buttons[i][j].setTextNum(this, String.valueOf(board.get(i, j)));
                tableRow.addView(buttons[i][j]);
            }
        }
    }
}