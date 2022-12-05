package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        Button start = findViewById(R.id.startB);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenSet("level");
            }
        });
        TextView close = (TextView) findViewById(R.id.closeB);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                screenSet("main");
            }
        });
    }

    public void screenSet(String screen) {
        TextView MS1 = findViewById(R.id.mainS1);
        LinearLayout MS2 = findViewById(R.id.mainS2);
        ImageView LS1 = findViewById(R.id.levelS1);
        LinearLayout LS2 = findViewById(R.id.levelS2);
        switch(screen) {
            case "main":
                MS1.setVisibility(View.VISIBLE);
                MS2.setVisibility(View.VISIBLE);
                LS1.setVisibility(View.GONE);
                LS2.setVisibility(View.GONE);
                break;
            case "level":
                MS1.setVisibility(View.GONE);
                MS2.setVisibility(View.GONE);
                LS1.setVisibility(View.VISIBLE);
                LS2.setVisibility(View.VISIBLE);
        }
    }

    public void levelClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SudokuActivity.class);
        int id = v.getId();
        switch(id) {
            case R.id.easyB:
                intent.putExtra("level", 0.3);
                break;
            case R.id.normalB:
                intent.putExtra("level", 0.5);
                break;
            case R.id.hardB:
                intent.putExtra("level", 0.65);
                break;
        }
        startActivity(intent);
        screenSet("main");
    }
}

// SudokuActivity 만들기
// level screen, activity 분리