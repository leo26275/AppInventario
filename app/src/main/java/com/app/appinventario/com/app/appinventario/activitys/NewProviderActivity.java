package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;//esta se copia para la barra

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewProviderActivity extends AppCompatActivity {

    Provider objProvider;
    ArrayList<Provider> listaProvider;
    TextInputLayout nombre, telefono, correo, direccion;
    public static String url = "http://192.168.0.6:80/AppInventory/Provider.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_provider);

        listaProvider = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        nombre = findViewById(R.id.nombre);
        telefono = findViewById(R.id.telefono);
        correo = findViewById(R.id.email);
        direccion = findViewById(R.id.password);

        showDataProvider();
    }

    public void savedProvider(View view) {
        final ProgressDialog progressDialog = new ProgressDialog(this);

        if(nombre.getEditText().getText().toString().isEmpty()){
            nombre.setError("Complete los campos");
        }else if(telefono.getEditText().getText().toString().isEmpty()){
            telefono.setError("Complete los campos");
        }else if(correo.getEditText().getText().toString().isEmpty()){
            correo.setError("Complete los campos");
        }else if(direccion.getEditText().getText().toString().isEmpty()){
            direccion.setError("Complete los campos");
        }else if(exist()){
            Toast.makeText(NewProviderActivity.this, "Este nombre de proveedor ya existe", Toast.LENGTH_LONG).show();
        }else{
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equalsIgnoreCase("registra")) {
                        Toast.makeText(NewProviderActivity.this, "Proveedor registrado", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        limpiar();
                    } else {
                        Toast.makeText(NewProviderActivity.this, "Proveedor no se registro" + response, Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(NewProviderActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", "create");
                    params.put("name", nombre.getEditText().getText().toString());
                    params.put("phone", telefono.getEditText().getText().toString());
                    params.put("email", correo.getEditText().getText().toString());
                    params.put("direction", direccion.getEditText().getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(NewProviderActivity.this);
            requestQueue.add(request);
        }
    }

    public  void showDataProvider(){
        StringRequest request = new StringRequest(Request.Method.POST, NewProviderActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaProvider.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id_proveedor");
                            String nombre = object.getString("nombre");
                            String telefono = object.getString("telefono");
                            String correo = object.getString("correo");
                            String direccion = object.getString("direccion");
                            int estado = object.getInt("estado");

                            objProvider = new Provider(id, nombre, telefono, correo, direccion, estado);
                            listaProvider.add(objProvider);
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
        for (Provider dato : listaProvider){
            if (dato.getNombre().equals(nombre.getEditText().getText().toString())){
                bandera =  true;
                break;
            }else{
                bandera = false;
            }
        }
        return bandera;
    }

    private void limpiar(){
        nombre.getEditText().setText("");
        telefono.getEditText().setText("");
        correo.getEditText().setText("");
        direccion.getEditText().setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.buscar){

        }else if(id == R.id.nuevo){
            Intent intent = new Intent(this, NewProviderActivity.class);
            startActivity(intent);
        }else if(id == R.id.lista){
            Intent intent = new Intent(this, ViewProviderActivity.class);
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
