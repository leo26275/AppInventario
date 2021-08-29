package com.app.appinventario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.com.app.appinventario.activitys.DashboardActivity;
import com.app.appinventario.com.app.appinventario.activitys.MailActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    TextInputLayout user, password;
    CheckBox check;
    public static String url = "http://192.168.0.6:80/AppInventory/Login.php";
    String userv, passwordV;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        //setCredentialIfExist();

        user = findViewById(R.id.user);
        password = findViewById(R.id.password);
        check = findViewById(R.id.check);
        recuperarPreferencias();
    }


    public void onGetIn(View view) {

        if(user.getEditText().getText().toString().isEmpty()){
            user.setError("Complete usuario");
        }else if (password.getEditText().getText().toString().isEmpty()){
            password.setError("Complete clave");
        }else {
            userv = user.getEditText().getText().toString();
            passwordV = password.getEditText().getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(!response.isEmpty()){
                        guardarPreferencias();
                        Intent intent =  new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                        //savedPreferences(userv, passwordV);
                        Toast.makeText(MainActivity.this, "Bienvenido", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(MainActivity.this, "Usuario o contraseña incorrrecta", Toast.LENGTH_SHORT).show();
                        user.getEditText().setText("");
                        user.requestFocus();
                        password.getEditText().setText("");
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> parametros = new HashMap<String, String>();
                    parametros.put("option","login");
                    parametros.put("user", userv);
                    parametros.put("password", passwordV);
                    return parametros;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    /*private void savedPreferences(String usuario, String clave){
        if(check.isChecked()){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("usuario", usuario);
            editor.putString("clave", clave);
            editor.putBoolean("accion", true);
            //editor.commit();
            editor.apply();
        }
    }

    private void setCredentialIfExist() {
        String u = getUserPreferences();
        String c = getClavePreferences();
        if(!TextUtils.isEmpty(u) && !TextUtils.isEmpty(c)){
            user.getEditText().setText(u);
            password.getEditText().setText(c);
        }
    }

    private String getUserPreferences(){
        return preferences.getString("usuario", "");
    }

    private String getClavePreferences(){
        return preferences.getString("clave", "");
    }*/

    public void guardarPreferencias(){
       // SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        if(check.isChecked()) {
            editor.putString("user", userv);
            editor.putString("password", passwordV);
            editor.putBoolean("accion", true);
            editor.putBoolean("estado", check.isChecked());
            editor.apply();
        }
    }

    private void recuperarPreferencias(){
       // SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
        String txtUsuario = preferences.getString("user","");
        String txtClave = preferences.getString("password", "");
        boolean estado = preferences.getBoolean("estado", false);

        if(estado){
            user.getEditText().setText(txtUsuario);
            password.getEditText().setText(txtClave);
            check.setChecked(estado);
        }
    }

    public void onRecover(View view) {
        Intent intent =  new Intent(MainActivity.this, MailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
