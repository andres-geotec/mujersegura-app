package com.geotec.mujersegura;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.preferences.SessionPreferences;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private TextInputEditText txtEmail, txtName, txtDateBirth, txtPassword;

    private SessionPreferences sessionPreferences;
    private Complements C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniComponents();
        sessionPreferences = new SessionPreferences(this);
        C = new Complements(this);

        actionBar.setSubtitle(getString(R.string.title_activity_profile) + " " + sessionPreferences.getName());

        fillData();
    }

    private void fillData() {
        txtEmail    .setText(sessionPreferences.getEmail());
        txtName     .setText(sessionPreferences.getName());
        txtDateBirth.setText(sessionPreferences.getDatebirth());
        txtPassword .setText("················");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_profile_email:
                break;
            case R.id.txt_profile_name:
                break;
            case R.id.txt_profile_date_birth:
                break;
            case R.id.txt_profile_password:
                break;
        }
    }

    private void iniComponents() {
        txtEmail            = (TextInputEditText) findViewById(R.id.txt_profile_email);
        txtName             = (TextInputEditText) findViewById(R.id.txt_profile_name);
        txtDateBirth        = (TextInputEditText) findViewById(R.id.txt_profile_date_birth);
        txtPassword         = (TextInputEditText) findViewById(R.id.txt_profile_password);

        txtEmail    .setOnClickListener(this);
        txtName     .setOnClickListener(this);
        txtDateBirth.setOnClickListener(this);
        txtPassword .setOnClickListener(this);
    }
}
