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
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class NewClientActivity extends AppCompatActivity {
    TextInputLayout nombre, direccion, telefono;
    public static String url = "http://192.168.0.6:80/AppInventory/Client.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        nombre = findViewById(R.id.nombre);
        direccion = findViewById(R.id.direccion);
        telefono = findViewById(R.id.telefono);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public void onSave(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);

        if(nombre.getEditText().getText().toString().isEmpty()){
            nombre.setError("Complete los campos");
        }else  if(direccion.getEditText().getText().toString().isEmpty()){
            direccion.setError("Complete los campos");
        }else if(telefono.getEditText().getText().toString().isEmpty()){
            telefono.setError("Complete los campos");
        }else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("registra")) {
                        Toast.makeText(NewClientActivity.this, "Cliente registrado", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        limpiar();
                    } else {
                        Toast.makeText(NewClientActivity.this, "Cliente no se registro" + response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(NewClientActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", "create");
                    params.put("name", nombre.getEditText().getText().toString());
                    params.put("direction", direccion.getEditText().getText().toString());
                    params.put("phone", telefono.getEditText().getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(NewClientActivity.this);
            requestQueue.add(request);
        }
    }

    private void limpiar(){
        nombre.getEditText().setText("");
        direccion.getEditText().setText("");
        telefono.getEditText().setText("");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.lista){
            Intent intent = new Intent(this, ViewClientActivity.class);
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
