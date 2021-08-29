package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class EditProviderActivity extends AppCompatActivity {

    TextInputLayout nombre, telefono, correo, direccion;
    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_provider);


        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        nombre = findViewById(R.id.nombre);
        telefono = findViewById(R.id.telefono);
        correo = findViewById(R.id.email);
        direccion = findViewById(R.id.password);

        Provider provider = (Provider) getIntent().getExtras().getSerializable("position");

        position = String.valueOf(provider.getId_proveedor());
        nombre.getEditText().setText(provider.getNombre());
        telefono.getEditText().setText(provider.getTelefono());
        correo.getEditText().setText(provider.getCorreo());
        direccion.getEditText().setText(provider.getDireccion());

    }

    public void onCancelar(View view){
        Intent intent = new Intent(getApplicationContext(), ViewProviderActivity.class);
        startActivity(intent);
    }

    public void onEdit(final View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, NewProviderActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Toast.makeText(EditProviderActivity.this, response, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewProviderActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(EditProviderActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProviderActivity.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action","editar");
                params.put("id_proveedor", position);
                params.put("nombre", nombre.getEditText().getText().toString());
                params.put("telefono", telefono.getEditText().getText().toString());
                params.put("correo", correo.getEditText().getText().toString());
                params.put("direccion", direccion.getEditText().getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditProviderActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewProviderActivity.class);
        startActivity(intent);
    }

}
