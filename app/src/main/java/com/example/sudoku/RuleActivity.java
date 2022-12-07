package com.example.sudoku;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RuleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        getSupportActionBar().hide();
    }

    public void backBClick(View v) {
        finish();
    }

    public void ruleClick(View v) {
        int id = v.getId();
        TextView desc = findViewById(R.id.desc);
        ImageView img = findViewById(R.id.img);
        switch(id) {
            case R.id.r1:
                img.setImageResource(R.drawable.rule1);
                desc.setText("아홉 3×3 칸에 숫자가 1부터 9까지 하나씩만 들어가야 한다.");
                break;
            case R.id.r2:
                img.setImageResource(R.drawable.rule2);
                desc.setText("아홉 가로줄에 숫자가 1부터 9까지 하나씩만 들어가야 한다.");
                break;
            case R.id.r3:
                img.setImageResource(R.drawable.rule3);
                desc.setText("아홉 세로줄에 숫자가 1부터 9까지 하나씩만 들어가야 한다.");
                break;
        }
    }
}