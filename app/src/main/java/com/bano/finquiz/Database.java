package com.bano.finquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.db"; // название бд
    private static final int SCHEMA = 1; // версия
    static final String TABLE = "statistic"; // название таблицы
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_RECORD = "record";
    public static final String COLUMN_PERCENT = "percent";
    public static final String COLUMN_TOTAL = "total";

    public Database(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE statistic (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_RECORD + " INTEGER, " + COLUMN_PERCENT + " REAL, " + COLUMN_TOTAL + " INTEGER);");


        // добавление начальных данных
        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_RECORD + ", " + COLUMN_PERCENT + ", " + COLUMN_TOTAL + ") VALUES (0, 0, 0);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
}