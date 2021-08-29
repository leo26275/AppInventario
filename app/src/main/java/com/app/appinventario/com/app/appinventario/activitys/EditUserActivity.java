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
import com.app.appinventario.com.app.appinventario.entitys.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends AppCompatActivity {
    TextInputLayout name, password,password2, email;
    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        password2 = findViewById(R.id.password2);
        email = findViewById(R.id.email);

        User user = (User) getIntent().getExtras().getSerializable("position");

        position = String.valueOf(user.getId_usuario());
        name.getEditText().setText(user.getUsuario());
        password.getEditText().setText("");
        password2.getEditText().setText("");
        email.getEditText().setText(user.getCorreo());
    }

    public void onCancelar(View view){
        Intent intent = new Intent(getApplicationContext(), ViewUser.class);
        startActivity(intent);
    }

    public void onEditUser(final View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, NewUserActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Toast.makeText(EditUserActivity.this, response, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewUser.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(EditUserActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditUserActivity.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action","editar");
                params.put("id_usuario", position);
                params.put("user", name.getEditText().getText().toString());
                params.put("password", password.getEditText().getText().toString());
                params.put("email", email.getEditText().getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditUserActivity.this);
        requestQueue.add(request);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, ViewUser.class);
        startActivity(intent);
    }
}
