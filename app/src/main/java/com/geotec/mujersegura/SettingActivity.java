package com.geotec.mujersegura;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.preferences.SettingPreferences;
import com.geotec.mujersegura.services.NotiAlertWPBService;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    private ActionBar actionBar;
    private Switch swtNotiAlertWPB;

    private SettingPreferences settingPreferences;
    private Complements C;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniComponents();
        settingPreferences = new SettingPreferences(this);
        C = new Complements(this);

        actionBar.setSubtitle(R.string.title_activity_setting);

        if (isNotiAlertWPBActive()) {
            activateNotiAlertWPB();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.swt_setting_noti_alert_wpb:
                toggleActiveNotiWPB();
                break;
        }
    }

    private void toggleActiveNotiWPB() {
        if (isNotiAlertWPBActive()) { // Si ya esta acivada
            C.PrintToastShort(getString(R.string.action_notify_alert_deactivate));
            deactivateNotiAlertWPB();
        } else {
            C.PrintToastShort(getString(R.string.action_notify_alert_activate));
            activateNotiAlertWPB();
        }
    }
    private boolean isNotiAlertWPBActive() {
        return settingPreferences.getNotiAlertWPB();
    }
    private void activateNotiAlertWPB() {
        startService(new Intent(this, NotiAlertWPBService.class));
        settingPreferences.setNotiAlertWPB(true);
    }
    private void deactivateNotiAlertWPB() {
        stopService(new Intent(this, NotiAlertWPBService.class));
        settingPreferences.setNotiAlertWPB(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        swtNotiAlertWPB.setChecked(isNotiAlertWPBActive());
    }

    private void iniComponents() {
        swtNotiAlertWPB = (Switch) findViewById(R.id.swt_setting_noti_alert_wpb);

        swtNotiAlertWPB.setOnClickListener(this);
    }
}
