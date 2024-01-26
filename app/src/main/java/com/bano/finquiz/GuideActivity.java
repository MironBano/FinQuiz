package com.bano.finquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_activity);

        ListView guideList = findViewById(R.id.guideList);

        GlossaryDB glossaryDB = new GlossaryDB(this);
        SQLiteDatabase db = glossaryDB.getReadableDatabase();

        String[] items = new String[28];
        for(int i = 0; i <28; i++){
            items[i] = "";
        }

        Cursor cursor = db.rawQuery("SELECT * FROM " + GlossaryDB.TABLE_NAME, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(GlossaryDB.COLUMN_ID));
                    String term = cursor.getString(cursor.getColumnIndex(GlossaryDB.KEY_TERM));
                    String definition = cursor.getString(cursor.getColumnIndex(GlossaryDB.KEY_DEFINITION));

                    items[id - 1] = term + " - " + definition;

                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, items);

        guideList.setAdapter(adapter);

        guideList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                String selectedItem = items[position];
            }
        });

    }


}