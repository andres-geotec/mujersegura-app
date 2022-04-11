    package com.geotec.mujersegura;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.preferences.SessionPreferences;
import com.geotec.mujersegura.preferences.SettingPreferences;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements
        RightsFragment.RightsFragmentListener,
        Complements.OnResponseSendData {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment;
    private AttentionFragment attentionFragment;
    private ReportsFragment reportsFragment;
    private RightsFragment rightsFragment;
    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (menuItem.getItemId()) {
                case R.id.id_tab_main_home:
                    fragmentTransaction.replace(R.id.main_content, homeFragment).commit();
                    return true;
                case R.id.id_tab_main_attention:
                    fragmentTransaction.replace(R.id.main_content, attentionFragment).commit();
                    return true;
                case R.id.ic_tab_main_reports:
                    fragmentTransaction.replace(R.id.main_content, reportsFragment).commit();
                    return true;
                case R.id.id_tab_main_rights:
                    fragmentTransaction.replace(R.id.main_content, rightsFragment).commit();
                    return true;
            }
            return false;
        }
    };

    private SettingPreferences settingsPreferences;
    private SessionPreferences sessionPreferences;
    private Complements C;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tbr_main);
        setSupportActionBar(toolbar);
        // Checando compatibilidad
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10.f);
        }

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_button_main);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        homeFragment = new HomeFragment();
        reportsFragment = new ReportsFragment();
        attentionFragment = new AttentionFragment();
        rightsFragment = new RightsFragment();
        fragmentTransaction.replace(R.id.main_content, homeFragment).commit();

        settingsPreferences = new SettingPreferences(this);
        sessionPreferences = new SessionPreferences(this);
        C = new Complements(this);
        C.iniOnResponseSendData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_popup_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popup_main_settings:
                C.GoOtherActivity(SettingActivity.class);
                break;
            case R.id.popup_main_tutorial:
                C.PrintDialog(getString(R.string.title_activity_tutorial), "Aún trabajando en esto");
                break;
            case R.id.popup_main_about:
                C.AboutDialog();
                break;
            case R.id.popup_main_porfile:
                C.GoOtherActivity(ProfileActivity.class);
                break;
            case R.id.popup_main_opinion:
                C.GoOtherActivity(MessageActivity.class);
                break;
            case R.id.popup_main_logout:
                SessionClose();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void SessionClose() {
        sessionPreferences.sessionClose();
        C.GoOtherActivity(LoginActivity.class);
        finish();
    }

    /**
     * RightsFragmentListener code
     */
    @Override
    public void requestData(JSONObject params, String url, boolean viewProgress) {
        C.sendData(params, url, viewProgress);
    }

    @Override
    public void onResponseSendData(JSONObject r) {
        try {
            returnData(r.getString(getString(R.string.rest_json_response_meta)), r);
        } catch (JSONException e) {
            C.PrintDialog(null, getString(R.string.error_try_json));
            e.printStackTrace();
        }
    }

    private void returnData(String type, JSONObject r) {
        if (type.equals(getString(R.string.rest_type_rights))) {
            rightsFragment.arrivedData(r);
        } else if (type.equals(getString(R.string.rest_type_user))) {
        } else {
            C.PrintDialog(null, "no coincidio con alguno");
        }
    }

    @Override
    public void onErrorResponseSendData() {

    }
}
