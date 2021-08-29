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
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;

public class EditCategoryActivity extends AppCompatActivity {

    TextInputLayout name;
    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);

        Category category = (Category) getIntent().getExtras().getSerializable("position");

        position = String.valueOf(category.getId_categoria());
        name.getEditText().setText(category.getNombre());
    }

    public void onCancelar(View view){
        Intent intent = new Intent(getApplicationContext(), ViewCategoryActivity.class);
        startActivity(intent);
    }

    public void onEditCategory(final View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Cargando...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, NewCategoryActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Toast.makeText(EditCategoryActivity.this, response, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ViewCategoryActivity.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }else{
                    Toast.makeText(EditCategoryActivity.this, response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditCategoryActivity.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("action","editar");
                params.put("id_categoria", position);
                params.put("name", name.getEditText().getText().toString());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(EditCategoryActivity.this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), ViewCategoryActivity.class);
        startActivity(intent);
    }
}
