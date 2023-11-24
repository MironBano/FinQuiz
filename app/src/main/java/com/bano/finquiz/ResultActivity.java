package com.bano.finquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    TextView record;
    TextView count;
    TextView wins;
    TextView warning;
    Button returnBut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Bundle arg = getIntent().getExtras();
        int currentCount = arg.getInt("Current count");

        record = findViewById(R.id.recordText);
        count = findViewById(R.id.countText);
        wins = findViewById(R.id.winsText);
        warning = findViewById(R.id.warningText);
        returnBut = findViewById(R.id.returnButton);

        count.setText("Текущий счет: " + currentCount );

        returnBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
            }
        });
    }

}