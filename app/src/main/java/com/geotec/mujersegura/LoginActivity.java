package com.geotec.mujersegura;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.others.Internet;
import com.geotec.mujersegura.preferences.SessionPreferences;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Complements.OnResponseSendData {

    private TextInputEditText txtEmail, txtPassword;
    private Button btnLogin, btnRegistry, btnAbout;

    private SessionPreferences sessionPreferences;
    private Complements C;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        iniComponents();
        sessionPreferences = new SessionPreferences(this);
        C = new Complements(this);
        C.iniOnResponseSendData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login_login:
                if (validateFields())
                    if (Internet.isConnected(this))
                        consultData();
                    else C.PrintDialog(null, getString(R.string.error_connected_internet));
                break;
            case R.id.btn_login_registry:
                C.GoOtherActivity(RegistryActivity.class);
                break;
            case R.id.btn_login_about:
                C.AboutDialog();
                break;
        }
    }

    private boolean validateFields() {
        if (C.checkIsEmailValid(txtEmail) && C.checkIsLengthValid(txtPassword, 4)) {
            return true;
        } else return false;
    }

    private void consultData() {
        //if (Internet.isConnected(this)) {
            JSONObject params = new JSONObject();
            try {
                params.put(getString(R.string.rest_var_user_email),     C.getText(txtEmail));
                params.put(getString(R.string.rest_var_user_password),  C.getText(txtPassword));
            } catch (JSONException e) {
                C.PrintDialog(null, getString(R.string.error_try_json));
                e.printStackTrace();
            }
            C.sendData(params, C.Api(R.string.api_login), true);
        //} else C.PrintDialog(null, getString(R.string.error_connected_internet));
    }

    @Override
    public void onResponseSendData(JSONObject r) {
        try {
            if (r.getBoolean(getString(R.string.rest_json_response_result))) {
                sessionOpen(r.getJSONObject(getString(R.string.rest_json_response_data)));
            } else {
                switch (r.getInt(getString(R.string.rest_json_response_code))) {
                    case 0: // No se pudo
                        C.PrintDialog(null, r.getString(getString(R.string.rest_json_response_message)));
                        break;
                    case 1: // Correo no existe
                        txtEmail.setError(r.getString(getString(R.string.rest_json_response_message)));
                        txtEmail.requestFocus();
                        break;
                    case 2: // Contraseña incorrecta
                        txtPassword.setError(r.getString(getString(R.string.rest_json_response_message)));
                        txtPassword.setText("");
                        txtPassword.requestFocus();
                        break;
                }
            }
        } catch (JSONException e) {
            C.PrintDialog(null, getString(R.string.error_try_json));
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
        txtEmail    = (TextInputEditText) findViewById(R.id.txt_login_email);
        txtPassword = (TextInputEditText) findViewById(R.id.txt_login_password);
        btnLogin    = (Button) findViewById(R.id.btn_login_login);
        btnRegistry = (Button) findViewById(R.id.btn_login_registry);
        btnAbout    = (Button) findViewById(R.id.btn_login_about);

        btnLogin.   setOnClickListener(this);
        btnRegistry.setOnClickListener(this);
        btnAbout.   setOnClickListener(this);
    }
}
