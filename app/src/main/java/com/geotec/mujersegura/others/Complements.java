package com.geotec.mujersegura.others;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
//import android.support.design.widget.TextInputEditText;
import com.google.android.material.textfield.TextInputEditText;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import com.geotec.mujersegura.R;

public class Complements {

    private Context context;
    private OnResponseDialogOptions listenerDialogOptions;
    public OnResponseSendData listenerSendData;
    private OnResponseSendDataBackground listenerSendDataBackground;
    public ProgressDialog progressDialog;

    public Complements(Context context) {
        this.context = context;
    }

    private String getApi() { return context.getString(R.string.URL_API); }
    public String Api(int ID) { return getApi() + context.getString(ID); }

    /*public String urlNewRegistry() {
        return context.getString(R.string.URL_DOMINIO) + context.getString(R.string.URL_registry);
    }*/

    public String getText(EditText editText) {
        return editText.getText().toString().trim();
    }
    public String getText(TextInputEditText textInputEditText) {
        return textInputEditText.getText().toString().trim();
    }
    public int BoleanToInt(boolean b) {
        return b ? 1 : 0;
    }

    public boolean checkIsFill(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString().trim())) {
            editText.setError(context.getString(R.string.error_field_required));
            editText.requestFocus();
            return false;
        } else return true;
    }
    public boolean checkIsFill(TextInputEditText textInputEditText) {
        if (TextUtils.isEmpty(textInputEditText.getText().toString().trim())) {
            textInputEditText.setError(context.getString(R.string.error_field_required));
            textInputEditText.requestFocus();
            return false;
        } else return true;
    }

    public boolean checkIsEmailValid(EditText editText) {
        boolean fill = checkIsFill(editText);
        if (fill) {
            if (!editText.getText().toString().trim().contains("@")) {
                editText.setError(context.getString(R.string.error_invalid_email));
                editText.requestFocus();
                return false;
            } else return true;
        } else return fill;
    }
    public boolean checkIsEmailValid(TextInputEditText textInputEditText) {
        boolean fill = checkIsFill(textInputEditText);
        if (fill) {
            if (!textInputEditText.getText().toString().trim().contains("@")) {
                textInputEditText.setError(context.getString(R.string.error_invalid_email));
                textInputEditText.requestFocus();
                return false;
            } else return true;
        } else return fill;
    }

    public boolean checkIsLengthValid(EditText editText, int length) {
        boolean fill = checkIsFill(editText);
        if (fill) {
            if (editText.getText().toString().trim().length() < length) {
                editText.setError(context.getString(R.string.error_invalid_password));
                editText.requestFocus();
                return false;
            } else return true;
        } else return fill;
    }
    public boolean checkIsLengthValid(TextInputEditText textInputEditText, int length) {
        boolean fill = checkIsFill(textInputEditText);
        if (fill) {
            if (textInputEditText.getText().toString().trim().length() < length) {
                textInputEditText.setError(context.getString(R.string.error_invalid_password));
                textInputEditText.requestFocus();
                return false;
            } else return true;
        } else return fill;
    }

    public boolean checkInRangeInt(EditText editText, int min, int max) {
        boolean fill = checkIsFill(editText);
        if (fill) {
            int data = Integer.valueOf(editText.getText().toString().trim());
            if (data < min || data > max) {
                editText.setError(context.getString(R.string.error_invalid_range));
                editText.requestFocus();
                return false;
            } else return true;
        } else return fill;
    }

    public void PrintDialog(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null)
            builder.setTitle(title);
        if (msg != null)
            builder.setMessage(msg);
        //builder.setPositiveButton("Ok", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void PrintProgressDialog(String title, String msg) {
        progressDialog = new ProgressDialog(context);
        if (title != null)
            progressDialog.setTitle(title);
        if (msg != null)
            progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void PrintToastShort(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public void AboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name));
        builder.setMessage(context.getString(R.string.build_by));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setIcon(context.getDrawable(R.drawable.ribbon_splash));
        }
        builder.setPositiveButton("Ok", null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void GoOtherActivity(Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

    public String[] ArrayStringRange(int ini, int fin) {
        String[] array = new String[fin - ini];
        for (int i = 0; i < array.length; i++, ini++) {
            array[i] = String.valueOf(ini);
        }
        return array;
    }



    /************** INTERFACES ****************/

    public void iniOnResponseDialogOptions(){
        if (context instanceof OnResponseDialogOptions) {
            listenerDialogOptions = (OnResponseDialogOptions) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement HomeFragmentListener");
        }
    }

    public void PrintDialogOptions(String title, String[] list, final View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setItems(list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listenerDialogOptions.onResponseDialogOptions(i, view);
                    }
                });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                listenerDialogOptions.onResponseDialogOptions(-1, view);
            }
        });
        builder.create().show();
    }

    public interface OnResponseDialogOptions {
        void onResponseDialogOptions(int i, View view);
    }

    public void iniOnResponseSendData() {
        if (context instanceof OnResponseSendData) {
            listenerSendData = (OnResponseSendData) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentListener");
        }
    }

    public void sendData(JSONObject params, String url, final boolean viewProgress) {
        if (viewProgress) { PrintProgressDialog(null, "Enviando..."); }
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject r) {
                        if (viewProgress) { progressDialog.dismiss(); }
                        listenerSendData.onResponseSendData(r);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (viewProgress) {
                            progressDialog.dismiss();
                            Log.e("VER", error.toString());
                            PrintDialog(null, context.getString(R.string.error_response_volley));
                        }
                        listenerSendData.onErrorResponseSendData();
                    }
                }
        );
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.add(mJsonObjectRequest);
    }

    public interface OnResponseSendData {
        void onResponseSendData(JSONObject r);
        void onErrorResponseSendData();
    }

    public void iniOnResponseSendDataBackground(){
        if (context instanceof OnResponseSendDataBackground) {
            listenerSendDataBackground = (OnResponseSendDataBackground) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentListener");
        }
    }

    public void sendDataBackground(JSONObject params, String url) {
        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject r) {
                        listenerSendDataBackground.onResponseSendDataBackground(r);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listenerSendDataBackground.onErrorResponseSendDataBackground();
                    }
                }
        );
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        mRequestQueue.add(mJsonObjectRequest);
    }

    public interface OnResponseSendDataBackground {
        void onResponseSendDataBackground(JSONObject r);
        void onErrorResponseSendDataBackground();
    }

}
