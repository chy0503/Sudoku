package com.example.sudoku;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getSupportActionBar().hide();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        TextView easy = findViewById(R.id.easy_bestRecord);
        TextView normal = findViewById(R.id.normal_bestRecord);
        TextView hard = findViewById(R.id.hard_bestRecord);

        easy.setText(pref.getString("easy_BR", "0 : 00"));
        normal.setText(pref.getString("normal_BR", "0 : 00"));
        hard.setText(pref.getString("hard_BR", "0 : 00"));
    }
}