package com.geotec.mujersegura;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.preferences.SessionPreferences;

public class MessageActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private SessionPreferences sessionPreferences;
    private Complements C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        sessionPreferences = new SessionPreferences(this);
        C = new Complements(this);

        actionBar.setSubtitle(R.string.title_activity_message);
    }
}
