package com.bano.finquiz;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity{
    public static int count = 0;
    public static int anticount = 0;
    public static String answer;
    private Thread thread;
    TextView termText;
    Button def1Button;
    Button def2Button;
    Button def3Button;
    TextView timeText;
    TextView counterText;
    GlossaryDB glossaryDB = new GlossaryDB(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        count = 0;
        anticount = 0;

        termText = findViewById(R.id.term);
        timeText = findViewById(R.id.time);
        counterText = findViewById(R.id.count);

        def1Button = findViewById(R.id.definition1);
        def2Button = findViewById(R.id.definition2);
        def3Button = findViewById(R.id.definition3);

        Toast answerWrongToast = Toast.makeText(GameActivity.this,"Неверно", Toast.LENGTH_SHORT);

        answer = generateQuize();

        def1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(def1Button.getText() == answer) {
                    count += 1;
                    counterText.setText("Счет: " + count);
                }
                else {
                    anticount += 1;
                    answerWrongToast.cancel();
                    answerWrongToast.show();
                }
                answer = generateQuize();
            }
        });

        def2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(def2Button.getText() == answer) {
                    count += 1;
                    counterText.setText("Счет: " + count);
                }
                else {
                    anticount += 1;
                    answerWrongToast.cancel();
                    answerWrongToast.show();
                }
                answer = generateQuize();
            }
        });

        def3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(def3Button.getText() == answer) {
                    count += 1;
                    counterText.setText("Счет: " + count);
                }
                else {
                    anticount += 1;
                    answerWrongToast.cancel();
                    answerWrongToast.show();
                }
                answer = generateQuize();
            }
        });

        startCountdown();

    }

    @Override
    protected void onStop() {
        super.onStop();
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
                        Thread.sleep(1000);
                        int timeLeft = 30 - i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timeText.setText(timeLeft + " s");
                            }
                        });
                    }
                    Intent intent = new Intent(GameActivity.this, ResultActivity.class);
                    intent.putExtra("Current count",count);
                    intent.putExtra("Current anticount",anticount);
                    startActivity(intent);
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    public String generateQuize(){
        SQLiteDatabase db = glossaryDB.getReadableDatabase();

        String[] terms = new String[28];
        for(int i = 0; i <28; i++){
            terms[i] = "";
        }

        String[] defs = new String[28];
        for(int i = 0; i <28; i++){
            defs[i] = "";
        }

        Cursor cursor = db.rawQuery("SELECT * FROM " + GlossaryDB.TABLE_NAME, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(GlossaryDB.COLUMN_ID));
                    String term = cursor.getString(cursor.getColumnIndex(GlossaryDB.KEY_TERM));
                    String definition = cursor.getString(cursor.getColumnIndex(GlossaryDB.KEY_DEFINITION));

                    terms[id - 1] = term;
                    defs[id - 1] = definition;

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        Random random = new Random();
        int randomTermIndex = random.nextInt(terms.length);

        termText.setText(terms[randomTermIndex]);

        int numberOfUsedButton;
        String buttonInside;
        int randomButtonIndex = random.nextInt(3);
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

        for(int j = 1; j <= 3; j++){
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