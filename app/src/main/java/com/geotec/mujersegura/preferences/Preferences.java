package com.geotec.mujersegura.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Preferences(Context context, String id) {
        this.preferences = context.getSharedPreferences(id, Context.MODE_PRIVATE);
    }

    protected void setBoolean(String name, boolean value) {
        this.editor = preferences.edit();
        this.editor.putBoolean(name, value);
        this.editor.commit();
    }
    protected void setString(String name, String value) {
        this.editor = preferences.edit();
        this.editor.putString(name, value);
        this.editor.commit();
    }
    protected void setInteger(String name, int value) {
        this.editor = preferences.edit();
        this.editor.putInt(name, value);
        this.editor.commit();
    }

    protected boolean getBoolean(String name) {
        return this.preferences.getBoolean(name, false);
    }
    protected String getString(String name) {
        return this.preferences.getString(name, null);
    }
    protected int getInteger(String name) {
        return this.preferences.getInt(name, 0);
    }

    protected void Clear() {
        this.editor = preferences.edit();
        this.editor.clear();
        this.editor.commit();
    }
}
