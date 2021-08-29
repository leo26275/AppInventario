package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.MainActivity;
import com.app.appinventario.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class NewUserActivity extends AppCompatActivity {

    TextInputLayout name, password,password2, email;
    public static String url = "http://192.168.0.6:80/AppInventory/User.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        email = findViewById(R.id.email);
    }


    public void savedUser(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);

        if(name.getEditText().getText().toString().isEmpty()){
            name.setError("Complete los campos");
        }else if(password.getEditText().getText().toString().isEmpty()){
            password.setError("Complete los campos");
        }else if(password2.getEditText().getText().toString().isEmpty()){
            password2.setError("Complete los campos");
        }else if(email.getEditText().getText().toString().isEmpty()){
            email.setError("Complete los campos");
        }else{
            if (password.getEditText().getText().toString().equals(password2.getEditText().getText().toString())){
                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("registra")) {
                            Toast.makeText(NewUserActivity.this, "Usuario registrado", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            limpiar();
                        } else {
                            Toast.makeText(NewUserActivity.this, "Usuario no se registro" + response, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewUserActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("action", "create");
                        params.put("user", name.getEditText().getText().toString());
                        params.put("password", password.getEditText().getText().toString());
                        params.put("email", email.getEditText().getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NewUserActivity.this);
                requestQueue.add(request);
            }else {
                Toast.makeText(this, "Las contraseñas no coninciden", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void limpiar(){
        name.getEditText().setText("");
        password.getEditText().setText("");
        password2.getEditText().setText("");
        email.getEditText().setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.lista){
            Intent intent = new Intent(this, ViewUser.class);
            startActivity(intent);
        }else if(id == R.id.menu){
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }else if(id == R.id.salir){
            SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
            boolean estado = preferences.getBoolean("estado", false);
            if(!estado) {
                preferences.edit().clear().commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }
        return true;
    }

    public void onCancelar(View view) {
        limpiar();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
