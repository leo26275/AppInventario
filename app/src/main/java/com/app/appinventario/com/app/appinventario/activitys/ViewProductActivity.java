package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
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
import com.app.appinventario.com.app.appinventario.adapters.AdapterProduct;
import com.app.appinventario.com.app.appinventario.adapters.AdapterProductImg;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.app.appinventario.com.app.appinventario.entitys.Product;
import com.app.appinventario.com.app.appinventario.entitys.Provider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewProductActivity extends AppCompatActivity {
    RecyclerView recyclerProductos;

    ArrayList<Product> listaProduct;
    ProgressDialog progressDialog;

    public Product products;
    //static AdapterProduct adapterProduct;
    //ListView lista;
    AdapterProductImg adapterProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        listaProduct = new ArrayList<>();

        recyclerProductos = findViewById(R.id.idRecycler);
        recyclerProductos.setLayoutManager(new LinearLayoutManager(ViewProductActivity.this));
        recyclerProductos.setHasFixedSize(true);
       //
        //adapterProduct = new AdapterProduct(this,listaProduct);
        //lista.setAdapter(adapterProduct);

        showDataProduct();
    }

    public  void showDataProduct(){
        StringRequest request = new StringRequest(Request.Method.POST, NewProductActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaProduct.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id_producto");
                            String codigo = object.getString("codigo");
                            String nombre = object.getString("nombre");
                            int stock = object.getInt("stock");
                            String imagen = object.getString("imagen");
                            int id_categoria = object.getInt("id_categoria");
                            int id_proveedor = object.getInt("id_proveedor");
                            int estado = object.getInt("estado");

                            //products = new Product(id, codigo,nombre,stock,imagen,id_categoria ,id_proveedor, estado);
                            listaProduct.add(products);
                        }

                    }
                    adapterProduct = new AdapterProductImg(listaProduct,ViewProductActivity.this);
                    recyclerProductos.setAdapter(adapterProduct);
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

    public void onCkeckProduct(final View view){
        msg("¿Desea modificar el estado?","estado", view);
    }

    private void msg(String texto, String opcion, final View view) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Producto");
        dialogo.setMessage(texto);

        if(opcion.equals("estado")){
            dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final Product product = (Product) view.getTag();
                    StringRequest request = new StringRequest(Request.Method.POST, NewProductActivity.url2, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.isEmpty()){
                                Toast.makeText(ViewProductActivity.this, response, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewCategoryActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(ViewProductActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ViewProductActivity.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            if(product.getEstado()==0){
                                params.put("action","activar");
                                params.put("id_producto", String.valueOf(product.getId_producto()));
                            }else {
                                params.put("action","desactivar");
                                params.put("id_producto", String.valueOf(product.getId_producto()));
                            }
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(ViewProductActivity.this);
                    requestQueue.add(request);
                    showDataProduct();
                }
            });
            dialogo.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }
        dialogo.show();
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
                //adapterProduct.getFilter().filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.buscar){
            return true;
        }else if(id == R.id.nuevo){
            Intent intent = new Intent(this, NewProviderActivity.class);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NewProductActivity.class);
        startActivity(intent);
    }

}
