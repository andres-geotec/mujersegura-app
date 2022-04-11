package com.geotec.mujersegura.preferences;

import android.content.Context;

import com.geotec.mujersegura.R;

public class SessionPreferences extends Preferences {

    private Context c;

    public SessionPreferences(Context context) {
        super(context, context.getString(R.string.PREFERENCE_SESSION_ID));
        this.c = context;
    }

    public void sessionOpen(String cve, String name, String email, String datebirth, String avatar, String timestamp) {
        this.setString(c.getString(R.string.PREFERENCE_SESSION_VAR_CVE), cve);
        this.setString(c.getString(R.string.PREFERENCE_SESSION_VAR_NAME), name);
        this.setString(c.getString(R.string.PREFERENCE_SESSION_VAR_EMAIL), email);
        this.setString(c.getString(R.string.PREFERENCE_SESSION_VAR_DATE_BIRTH), datebirth);
        this.setString(c.getString(R.string.PREFERENCE_SESSION_VAR_AVATAR), avatar);
        this.setString(c.getString(R.string.PREFERENCE_SESSION_VAR_TIMESTAMP), timestamp);
        this.setBoolean(c.getString(R.string.PREFERENCE_SESSION_VAR_STATUS), true);
    }
    public void sessionClose() {
        this.Clear();
    }
    public boolean getSession() {
        return this.getBoolean(c.getString(R.string.PREFERENCE_SESSION_VAR_STATUS));
    }

    public String getCveUser() {
        return this.getString(c.getString(R.string.PREFERENCE_SESSION_VAR_CVE));
    }

    public String getName() {
        return this.getString(c.getString(R.string.PREFERENCE_SESSION_VAR_NAME));
    }

    public String getEmail() {
        return this.getString(c.getString(R.string.PREFERENCE_SESSION_VAR_EMAIL));
    }

    public String getDatebirth() {
        return this.getString(c.getString(R.string.PREFERENCE_SESSION_VAR_DATE_BIRTH));
    }

    public String getAvatar() {
        return this.getString(c.getString(R.string.PREFERENCE_SESSION_VAR_AVATAR));
    }

    public String getTimestamp() {
        return this.getString(c.getString(R.string.PREFERENCE_SESSION_VAR_TIMESTAMP));
    }

}
