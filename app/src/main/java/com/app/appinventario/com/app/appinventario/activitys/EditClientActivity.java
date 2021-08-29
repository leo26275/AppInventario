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
import com.app.appinventario.com.app.appinventario.entitys.Client;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class EditClientActivity extends AppCompatActivity {

    TextInputLayout nombre, telefono, direccion;
    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        nombre = findViewById(R.id.nombre);
        direccion = findViewById(R.id.direccion);
        telefono = findViewById(R.id.telefono);

        Client cliente = (Client) getIntent().getExtras().getSerializable("position");

        position = String.valueOf(cliente.getId_cliente());
        nombre.getEditText().setText(cliente.getNombre());
        direccion.getEditText().setText(cliente.getDireccion());
        telefono.getEditText().setText(cliente.getTelefono());

    }

    public void onMOdificar(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, NewClientActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Toast.makeText(EditClientActivity.this, response, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewClientActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(EditClientActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditClientActivity.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action","editar");
                params.put("id_cliente", position);
                params.put("nombre", nombre.getEditText().getText().toString());
                params.put("direccion", direccion.getEditText().getText().toString());
                params.put("telefono", telefono.getEditText().getText().toString());

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditClientActivity.this);
        requestQueue.add(request);
    }

    public void onCancelar(View view) {
        Intent intent = new Intent(getApplicationContext(), ViewClientActivity.class);
        startActivity(intent);
    }
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ViewClientActivity.class);
        startActivity(intent);
    }
}

