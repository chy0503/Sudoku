package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LevelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);
        getSupportActionBar().hide();

        TextView close = (TextView) findViewById(R.id.l_closeB);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void levelClick(View v) {
        finish();
        Intent intent = new Intent(getApplicationContext(), SudokuActivity.class);
        int id = v.getId();
        switch(id) {
            case R.id.l_easyB:
                intent.putExtra("level", 0.25);
                break;
            case R.id.l_normalB:
                intent.putExtra("level", 0.4);
                break;
            case R.id.l_hardB:
                intent.putExtra("level", 0.65);
                break;
        }
        startActivity(intent);
    }
}