package com.bano.finquiz;

import static com.bano.finquiz.SettingsActivity.APP_PREFERENCES;
import static com.bano.finquiz.SettingsActivity.TIME;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    double percent;
    Cursor cursor;
    TextView record, count, wins, warning;
    Button goBackButton;
    View goBackView;
    SharedPreferences mSettings;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ResultActivity.this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        Bundle arg = getIntent().getExtras();
        int currentCount = arg.getInt("Current count");
        int currentAnticount = arg.getInt("Current anticount");
        percent = ((double) currentCount / (currentCount+currentAnticount))*100;

        goBackView = findViewById(R.id.goBackView);
        goBackButton = goBackView.findViewById(R.id.goBackButton);
        record = findViewById(R.id.recordText);
        count = findViewById(R.id.countText);
        wins = findViewById(R.id.winsText);
        warning = findViewById(R.id.warningText);

        count.setText("Текущий счет: " + currentCount);
        setWinsPercent(currentCount, currentAnticount);

        goBackButton.setOnClickListener(new View.OnClickListener(){
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
                    int currentRecord = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_RECORD));
                    double currentPercent = cursor.getDouble(cursor.getColumnIndex(Database.COLUMN_PERCENT));
                    int currentTotal = cursor.getInt(cursor.getColumnIndex(Database.COLUMN_TOTAL));

                    record.setText("Рекорд: " + currentRecord);

                    ContentValues cv = new ContentValues();
                    if((currentRecord < currentCount) && !isRulesToUpdateViolated(percent)) cv.put(Database.COLUMN_RECORD, currentCount);
                    if(currentPercent < ((double)currentCount / (currentCount+currentAnticount))*100 && !isRulesToUpdateViolated(percent)) cv.put(Database.COLUMN_PERCENT, percent);
                    cv.put(Database.COLUMN_TOTAL, currentTotal + currentCount);
                    db.update(Database.TABLE, cv, Database.COLUMN_ID + "=" + id, null);

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
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
            percent = ((double) currentCount / (currentCount+currentAnticount))*100;
            DecimalFormat df = new DecimalFormat("#.#");

            wins.setText("Процент правильных ответов: \n" +  df.format(percent) + "%");
            warning.setText(isRulesToUpdateViolated(percent) ? "При проценте меньше 40 или времени на раунд больше 30 секунд рекорд не обновится!" : "");
        }
    }

    public boolean isRulesToUpdateViolated(double percent){
        return percent < 40 || mSettings.getFloat(TIME, 30) != 30;
    }

}