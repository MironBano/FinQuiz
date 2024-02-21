package com.bano.finquiz;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.slider.Slider;

public class SettingsActivity extends AppCompatActivity {
    public static final String APP_PREFERENCES = "GameSettings";
    public static final String SOUND = "Sound";
    public static final String TIME = "Time";
    SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SwitchCompat soundSwitch = findViewById(R.id.sound_settings_switch);
        Slider timeSlider = findViewById(R.id.timeSlider);
        TextView timeText = findViewById(R.id.timeText);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = mSettings.edit();

        soundSwitch.setChecked(mSettings.getBoolean(SOUND, true));
        soundSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefEditor.putBoolean(SOUND, isChecked);
                prefEditor.apply();
            }
        });

        timeSlider.setValue(mSettings.getFloat(TIME, 30));
        timeText.setText("Время на раунд " + mSettings.getFloat(TIME, 30) + " секунд");
        timeSlider.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                prefEditor.putFloat(TIME, value);
                prefEditor.apply();
                timeText.setText("Время на раунд " + value + " секунд" );
            }
        });


    }
}