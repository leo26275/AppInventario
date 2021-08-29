package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.MainActivity;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.adapters.AdapterProductImg;
import com.app.appinventario.com.app.appinventario.entitys.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ConsultarProductoActivity extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {

    RecyclerView recyclerProductos;
    ArrayList<Product> listaProductos;

    ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_producto);

        Toolbar toolbar = findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);

        listaProductos = new ArrayList<>();
        recyclerProductos = findViewById(R.id.idrec);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(ConsultarProductoActivity.this));
        recyclerProductos.setHasFixedSize(true);

        request = Volley.newRequestQueue(ConsultarProductoActivity.this);
        cargarServicio();
    }

    private void cargarServicio() {
        progressDialog = new ProgressDialog(ConsultarProductoActivity.this);
        progressDialog.setMessage("Consultar Imagenes");
        progressDialog.show();

        String url = "http://192.168.0.6:80/AppInventory/ProductImg.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(ConsultarProductoActivity.this, "No se puede conectar" + error.toString(), Toast.LENGTH_LONG).show();
        progressDialog.hide();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        Product product = null;

        JSONArray json = response.optJSONArray("producto");
        try {
            for(int i=0; i<json.length(); i++){
                product = new Product();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                product.setId_producto(jsonObject.optInt("id_producto"));
                product.setCodigo(jsonObject.optString("codigo"));
                product.setNombre(jsonObject.optString("nombre"));
                product.setImagen(jsonObject.optString("imagen"));
                product.setStock(jsonObject.optInt("stock"));
                product.setNewcategoria(jsonObject.optString("id_categoria"));
                product.setNewproveedor(jsonObject.optString("id_proveedor"));
                product.setEstado(jsonObject.optInt("estado"));
                listaProductos.add(product);
            }
            progressDialog.hide();
            AdapterProductImg adapter = new AdapterProductImg(listaProductos, ConsultarProductoActivity.this);
            recyclerProductos.setAdapter(adapter);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lista,menu);

        MenuItem menuItem = menu.findItem(R.id.buscar);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //adapterUser.getFilter().filter(s);
                try {

                }catch (Exception e){
                    e.printStackTrace();
                }
                return false;
            }


        });
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.buscar){
            //return true;
        }else  if(id == R.id.nuevo){
            Intent intent = new Intent(this, NewProductActivity.class);
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

    public void onCkeck(View view) {
        Intent intent = new Intent(this, ConsultarProductoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NewProductActivity.class);
        startActivity(intent);
    }
}
