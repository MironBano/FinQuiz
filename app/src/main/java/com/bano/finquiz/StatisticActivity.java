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
import android.widget.Toast;

import java.text.DecimalFormat;

public class StatisticActivity extends AppCompatActivity {
    Database database;
    SQLiteDatabase db;
    TextView record;
    TextView wins;
    TextView total;
    Button back;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        record = findViewById(R.id.record);
        wins = findViewById(R.id.winsBest);
        total = findViewById(R.id.total);
        back = findViewById(R.id.backButton);
        delete = findViewById(R.id.deleteButton);

        Toast deleteStatToast = Toast.makeText(StatisticActivity.this,"Статистика сброшена", Toast.LENGTH_LONG);

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(StatisticActivity.this, MainActivity.class));
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
                    DecimalFormat df = new DecimalFormat("#.#");
                    wins.setText("Лучший процент: " + df.format(currentPercent) + "%");
                    total.setText("Всего верных ответов: " + currentTotal);



                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();


        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = database.getWritableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + Database.TABLE, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            int id = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_ID));
                            int currentRecord = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_RECORD));
                            double currentPercent = cursor.getDouble(cursor.getColumnIndex(Database.COLUMN_PERCENT));
                            int currentTotal = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_TOTAL));

                            // Обновление значений в базе данных с использованием update
                            ContentValues cv = new ContentValues();
                            cv.put(Database.COLUMN_RECORD, 0);
                            cv.put(Database.COLUMN_PERCENT, 0);
                            cv.put(Database.COLUMN_TOTAL, 0);
                            db.update(Database.TABLE, cv, Database.COLUMN_ID + "=" + id, null);

                            deleteStatToast.cancel();
                            deleteStatToast.show();

                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                }
                db.close();
            }
        });
    }

}