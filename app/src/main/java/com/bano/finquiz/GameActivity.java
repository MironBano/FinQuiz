package com.bano.finquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class GameActivity extends AppCompatActivity{

    public static int count = 0;  //переменная для счета
    public static String answer;
    private Thread thread;
    TextView termText;
    Button def1Button;
    Button def2Button;
    Button def3Button;
    TextView timeText;
    TextView counterText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        count = 0;

        termText = findViewById(R.id.term);
        timeText = findViewById(R.id.time);
        counterText = findViewById(R.id.count);

        def1Button = findViewById(R.id.definition1);
        def2Button = findViewById(R.id.definition2);
        def3Button = findViewById(R.id.definition3);

        answer = generateQuize();

        startCountdown();

        def1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(def1Button.getText() == answer) {
                    count += 1;
                    counterText.setText("Счет: " + count);
                    answer = generateQuize();
                }
                else {
                    Toast.makeText(GameActivity.this,"Неверно", Toast.LENGTH_SHORT).show();
                    answer = generateQuize();
                }
            }
        });
        def2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(def2Button.getText() == answer) {
                    count += 1;
                    counterText.setText("Счет: " + count);
                    answer = generateQuize();
                }
                else {
                    Toast.makeText(GameActivity.this,"Неверно", Toast.LENGTH_SHORT).show();
                    answer = generateQuize();
                }
            }
        });
        def3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(def3Button.getText() == answer) {
                    count += 1;
                    counterText.setText("Счет: " + count);
                    answer = generateQuize();
                }
                else {
                    Toast.makeText(GameActivity.this,"Неверно", Toast.LENGTH_SHORT).show();
                    answer = generateQuize();
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        // остановка фонового потока при уходе с Activity
        if (thread != null) {
            thread.interrupt();
            thread = null;
        }

        // Удалить Toasts которые продолжают показываться !!!!!!!!!!!!!!!!!!!!!!!!!!
    }

    public void startCountdown(){          // начало отсчета 30 сек
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
                    intent.putExtra("Current count",count);
                    startActivity(intent); // переход на другую Activity
                    finish(); // закрытие текущей Activity

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public String generateQuize(){
        String[] terms = getResources().getStringArray(R.array.terms_for_game);
        String[] defs = getResources().getStringArray(R.array.def_for_game);

        Random random = new Random();
        int randomTermIndex = random.nextInt(terms.length);  // получение случайного индекса термина

        termText.setText(terms[randomTermIndex]);

        int numberOfUsedButton;
        String buttonInside;
        int randomButtonIndex = random.nextInt(3);    // установка правильного ответа в случаную кнопку
        switch (randomButtonIndex){
            case 0:{
                def1Button.setText(defs[randomTermIndex]);
                numberOfUsedButton = 1;
                buttonInside = defs[randomTermIndex];
                break;
            }
            case 1:{
                def2Button.setText(defs[randomTermIndex]);
                numberOfUsedButton = 2;
                buttonInside = defs[randomTermIndex];
                break;
            }
            case 2:{
                def3Button.setText(defs[randomTermIndex]);
                numberOfUsedButton = 3;
                buttonInside = defs[randomTermIndex];
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + randomButtonIndex);
        }

        for(int j = 1; j <= 3; j++){                        // установка остальных кнопок
            if(j == numberOfUsedButton) continue;
            switch (j){
                case 1:{
                    int randomDefIndex = random.nextInt(defs.length);
                    while (randomDefIndex == randomTermIndex) randomDefIndex = random.nextInt(defs.length);
                    def1Button.setText(defs[randomDefIndex]);
                    break;
                }
                case 2:{
                    int randomDefIndex = random.nextInt(defs.length);
                    while (randomDefIndex == randomTermIndex) randomDefIndex = random.nextInt(defs.length);
                    def2Button.setText(defs[randomDefIndex]);
                    break;
                }
                case 3:{
                    int randomDefIndex = random.nextInt(defs.length);
                    while (randomDefIndex == randomTermIndex) randomDefIndex = random.nextInt(defs.length);
                    def3Button.setText(defs[randomDefIndex]);
                    break;
                }
            }

        }
        return buttonInside;
    }

}