package com.bano.finquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class ResultActivity extends AppCompatActivity {

    Database database;
    SQLiteDatabase db;
    int currentCount;
    Cursor cursor;
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
        int currentAnticount = arg.getInt("Current anticount");

        record = findViewById(R.id.recordText);
        count = findViewById(R.id.countText);
        wins = findViewById(R.id.winsText);
        warning = findViewById(R.id.warningText);
        returnBut = findViewById(R.id.returnButton);

        count.setText("Текущий счет: " + currentCount);
        setWinsPercent(currentCount, currentAnticount);

        returnBut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(ResultActivity.this, MainActivity.class));
            }
        });

        database = new Database(this);
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + Database.TABLE, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_ID));
                    // Получение текущих значений из базы данных
                    int currentRecord = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_RECORD));
                    double currentPercent = cursor.getDouble(cursor.getColumnIndex(Database.COLUMN_PERCENT));
                    int currentTotal = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_TOTAL));

                    record.setText("Рекорд: " + currentRecord);
                    // Обновление значений в базе данных с использованием update
                    ContentValues cv = new ContentValues();
                    if(currentRecord < currentCount){
                        cv.put(Database.COLUMN_RECORD, currentCount);
                        db.update(Database.TABLE, cv, Database.COLUMN_ID + "=" + id, null);
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        // Закрытие базы данных
        db.close();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        cursor.close();
    }

    public void setWinsPercent(int currentCount, int currentAnticount){
        if(currentCount+currentAnticount == 0){
            wins.setText("Процент правильных ответов: " + 0 + " %");
        }
        else {
            double percent = ((double) currentCount / (currentCount+currentAnticount))*100;
            DecimalFormat df = new DecimalFormat("#.#");

            wins.setText("Процент правильных ответов: \n" +  df.format(percent) + "%");
            warning.setText(checkForAbuse(percent) ? "При проценте меньше 33.3 рекорд не обновится!" : "");
        }
    }

    public boolean checkForAbuse(double percent){
        return (percent < 33.3) ? true : false;
    }

}