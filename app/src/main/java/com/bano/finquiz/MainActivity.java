package com.bano.finquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton guidBut;

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Нажмите ещё раз чтобы выйти",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onClick1(View view) {
        Intent i = new Intent(MainActivity.this, GuideActivity.class);
        startActivity(i);
    }

    public void onClick2(View view) {
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        startActivity(i);
    }

    public void onClick3(View view) {
        Intent i = new Intent(MainActivity.this, StatisticActivity.class);
        startActivity(i);
    }

    public void onClick4(View view) {
        Intent i = new Intent(MainActivity.this, RulesActivity.class);
        startActivity(i);
    }

    public void onClick5(View view) {
        Intent i = new Intent(MainActivity.this, DetalisActivity.class);
        startActivity(i);
    }

}