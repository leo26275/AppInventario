package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;//esta se copia para la barra

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.MainActivity;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewCategoryActivity extends AppCompatActivity {

    Category objCategory;
    ArrayList<Category> listaCategory;
    TextInputLayout name;
    public static String url = "http://192.168.0.6:80/AppInventory/Category.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        listaCategory = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.name);
        showDataCategory();
    }

    public void savedCategory(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);

        if(name.getEditText().getText().toString().isEmpty()){
            name.setError("Complete los campos");
        }else if(exist()){
            Toast.makeText(NewCategoryActivity.this, "El Nombre ya existe", Toast.LENGTH_LONG).show();
        }else{

                progressDialog.show();
                StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("registra")) {
                            Toast.makeText(NewCategoryActivity.this, "Categoria registrada", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            limpiar();
                        } else {
                            Toast.makeText(NewCategoryActivity.this, "Categoria no se registro" + response, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NewCategoryActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("action", "create");
                        params.put("name", name.getEditText().getText().toString());
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NewCategoryActivity.this);
                requestQueue.add(request);

        }
    }

    private void limpiar(){
        name.getEditText().setText("");
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
            Intent intent = new Intent(this, ViewCategoryActivity.class);
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

    public  void showDataCategory(){
        StringRequest request = new StringRequest(Request.Method.POST, NewCategoryActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaCategory.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id_categoria");
                            String nombre = object.getString("nombre");
                            int estado = object.getInt("estado");

                            objCategory = new Category(id, nombre, estado);
                            listaCategory.add(objCategory);
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "consult");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public boolean exist(){
        boolean bandera = true;
        for (Category dato : listaCategory){
            if (dato.getNombre().equals(name.getEditText().getText().toString())){
                bandera =  true;
                break;
            }else{
                bandera = false;
            }
        }
        return bandera;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}
