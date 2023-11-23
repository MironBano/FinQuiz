package com.bano.finquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    private Thread thread;
    TextView termText;
    Button def1Button;
    Button def2Button;
    Button def3Button;
    TextView timeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        termText = findViewById(R.id.term);
        timeText = findViewById(R.id.time);
        def1Button = findViewById(R.id.definition1);
        def2Button = findViewById(R.id.definition2);
        def3Button = findViewById(R.id.definition3);

        generateQuize();

        startCountdown();

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Остановка фонового потока при уходе с Activity
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }
    }

    public void startCountdown(){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i = 0; i <30; i++) {
                        Thread.sleep(1000); // отсчет 1 секундa
                        int finalI = 30 - i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeText.setText(finalI + " s");
                            }
                        });
                    }
                    Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                    startActivity(intent); // переход на другую Activity
                    finish(); // закрытие текущей Activity

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public void generateQuize(){
        String[] terms = getResources().getStringArray(R.array.terms_for_game);
        String[] defs = getResources().getStringArray(R.array.def_for_game);

        Random random = new Random();
        int randomTermIndex = random.nextInt(terms.length);

        termText.setText(terms[randomTermIndex]);

        int numberOfUsedButton;
        int randomButtonIndex = random.nextInt(3);
        switch (randomButtonIndex){
            case 0:{
                def1Button.setText(defs[randomTermIndex]);
                numberOfUsedButton = 1;
                break;
            }
            case 1:{
                def2Button.setText(defs[randomTermIndex]);
                numberOfUsedButton = 2;
                break;
            }
            case 2:{
                def3Button.setText(defs[randomTermIndex]);
                numberOfUsedButton = 3;
                break;
            }
        }
    }



}