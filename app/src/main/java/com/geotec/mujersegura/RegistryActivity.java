package com.geotec.mujersegura;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.others.Internet;
import com.geotec.mujersegura.preferences.SessionPreferences;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegistryActivity extends AppCompatActivity implements
        View.OnFocusChangeListener,
        View.OnClickListener,
        DatePickerDialog.OnDateSetListener,
        Complements.OnResponseSendData {

    private Toolbar toolbar;
    private ActionBar actionBar;
    private TextInputEditText txtEmail, txtName, txtDateBirth, txtPassword, txtPasswordVerify;
    private Button btnRegistry, btnAbout;

    private SessionPreferences sessionPreferences;
    private Complements C;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);

        toolbar = (Toolbar) findViewById(R.id.tbr_main);
        setSupportActionBar(toolbar);
        // Checando compatibilidad
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10.f);
        }
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        iniComponents();
        sessionPreferences = new SessionPreferences(this);
        C = new Complements(this);
        C.iniOnResponseSendData();
    }

    @Override
    public void onClick(View view) {
        Options(view);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) Options(view);
    }

    private void Options(View view) {
        switch (view.getId()) {
            case R.id.txt_registry_date_birth:
                showDatePikerDialog();
                break;
            case R.id.btn_registry_registry:
                if (validateFields()) consultData();
                break;
            case R.id.btn_registry_about:
                C.AboutDialog();
                break;
        }
    }

    private boolean validateFields() {
        if (C.checkIsEmailValid(txtEmail)
                && C.checkIsFill(txtName)
                && C.checkIsFill(txtDateBirth)
                && C.checkIsLengthValid(txtPassword, 4)
                && C.checkIsFill(txtPasswordVerify)) {
            return true;
        } else return false;
    }

    private void consultData() {
        if (Internet.isConnected(this)) {
            JSONObject params = new JSONObject();
            try {
                params.put(getString(R.string.rest_var_user_email),             C.getText(txtEmail));
                params.put(getString(R.string.rest_var_user_name),              C.getText(txtName));
                params.put(getString(R.string.rest_var_user_date_birth),        C.getText(txtDateBirth));
                params.put(getString(R.string.rest_var_user_avatar),            "");
                params.put(getString(R.string.rest_var_user_password),          C.getText(txtPassword));
                params.put(getString(R.string.rest_var_user_password_verify),   C.getText(txtPasswordVerify));
            } catch (JSONException e) {
                C.PrintDialog(null, getString(R.string.error_try_json));
                e.printStackTrace();
            }
            C.sendData(params, C.Api(R.string.api_registry), true);
        } else C.PrintDialog(null, getString(R.string.error_connected_internet));
    }

    @Override
    public void onResponseSendData(JSONObject r) {
        try {
            if (r.getBoolean(getString(R.string.rest_json_response_result))) {
                sessionOpen(r.getJSONObject(getString(R.string.rest_json_response_data)));
            } else {
                switch (r.getInt(getString(R.string.rest_json_response_code))) {
                    case 1: // contraseña no coincide
                        txtPassword.setText("");
                        txtPasswordVerify.setText("");
                        txtPassword.setError(r.getString(getString(R.string.rest_json_response_message)));
                        txtPassword.requestFocus();
                        break;
                    case 2: // Correo ya existe
                        txtEmail.setError(r.getString(getString(R.string.rest_json_response_message)));
                        txtEmail.requestFocus();
                        break;
                    case 3: // No se pudo
                        C.PrintDialog(null, r.getString(getString(R.string.rest_json_response_message)));
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponseSendData() {}

    private void sessionOpen(JSONObject json) {
        C.PrintProgressDialog(null, getString(R.string.action_accessing));
        try {
            sessionPreferences.sessionOpen(
                    json.getString(getString(R.string.rest_var_user_cve)),
                    json.getString(getString(R.string.rest_var_user_name)),
                    json.getString(getString(R.string.rest_var_user_email)),
                    json.getString(getString(R.string.rest_var_user_date_birth)),
                    "",
                    json.getString(getString(R.string.rest_var_user_timestamp))
            );
            C.GoOtherActivity(MainActivity.class);
            C.progressDialog.dismiss();
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
            C.progressDialog.dismiss();
            C.PrintDialog(null, getString(R.string.error_open_session));
        }
    }

    private void iniComponents() {
        txtEmail            = (TextInputEditText) findViewById(R.id.txt_registry_email);
        txtName             = (TextInputEditText) findViewById(R.id.txt_registry_name);
        txtDateBirth        = (TextInputEditText) findViewById(R.id.txt_registry_date_birth);
        txtPassword         = (TextInputEditText) findViewById(R.id.txt_registry_password);
        txtPasswordVerify   = (TextInputEditText) findViewById(R.id.txt_registry_password_verify);
        btnRegistry         = (Button) findViewById(R.id.btn_registry_registry);
        btnAbout            = (Button) findViewById(R.id.btn_registry_about);

        txtDateBirth    .setOnClickListener(this);
        btnRegistry     .setOnClickListener(this);
        btnAbout        .setOnClickListener(this);

        txtDateBirth    .setOnFocusChangeListener(this);
    }

    private void showDatePikerDialog() {
        int maxAge = 70, ago = 12;
        Calendar minDate = Calendar.getInstance(), maxDate = Calendar.getInstance();
        minDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - maxAge);
        maxDate.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR) - ago);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR) - (maxAge / 2),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR,           year);
        myCalendar.set(Calendar.MONTH,          month);
        myCalendar.set(Calendar.DAY_OF_MONTH,   dayOfMonth);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        txtDateBirth.setText(simpleDateFormat.format(myCalendar.getTime()));
    }
}
