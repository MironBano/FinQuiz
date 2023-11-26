package com.bano.finquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class StatisticActivity extends AppCompatActivity {

    Database database;
    SQLiteDatabase db;
    TextView record;
    TextView wins;
    TextView total;
    Button back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        record = findViewById(R.id.record);
        wins = findViewById(R.id.winsBest);
        total = findViewById(R.id.total);
        back = findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(StatisticActivity.this, MainActivity.class));
            }
        });

        database = new Database(this);
        SQLiteDatabase db = database.getReadableDatabase();
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
                    DecimalFormat df = new DecimalFormat("#.#");
                    wins.setText("Лучший процент: " + df.format(currentPercent) + "%");
                    total.setText("Всего верных ответов: " + currentTotal);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        // Закрытие базы данных
        db.close();

    }



}