package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

//        ImageView IV1 = findViewById(R.id.startAfter1);
//        LinearLayout IV2 = findViewById(R.id.startAfter2);

//        Button start = findViewById(R.id.startB);
//        start.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IV1.setVisibility(View.VISIBLE);
//                IV2.setVisibility(View.VISIBLE);
//            }
//        });
//
//        TextView close = findViewById(R.id.closeB);
//        close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                IV1.setVisibility(View.GONE);
//                IV2.setVisibility(View.GONE);
//            }
//        });
    }

    public void levelClick(View v) {
        Intent intent = new Intent(getApplicationContext(), SubActivity.class);
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
    }
}