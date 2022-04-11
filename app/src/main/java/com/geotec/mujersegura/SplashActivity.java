package com.geotec.mujersegura;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.geotec.mujersegura.preferences.SessionPreferences;

public class SplashActivity extends Activity {

    private SessionPreferences sessionPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionPreferences = new SessionPreferences(this);
        Intent intent;

        if (isSessionOpen()) {
            intent = new Intent(this, MainActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }

    private boolean isSessionOpen() {
        return sessionPreferences.getSession();
    }

}
