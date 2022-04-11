package com.geotec.mujersegura.preferences;

import android.content.Context;

import com.geotec.mujersegura.R;

public class SettingPreferences extends Preferences {

    private Context c;

    public SettingPreferences(Context context) {
        super(context, context.getString(R.string.PREFERENCE_SETTING_ID));
        this.c = context;
    }

    public boolean getNotiAlertWPB() {
        return this.getBoolean(c.getString(R.string.PREFERENCE_SETTING_VAR_NOTI_ALERT_WPB));
    }
    public void setNotiAlertWPB(boolean notiAlertWPB) {
        this.setBoolean(c.getString(R.string.PREFERENCE_SETTING_VAR_NOTI_ALERT_WPB), notiAlertWPB);
    }
}
