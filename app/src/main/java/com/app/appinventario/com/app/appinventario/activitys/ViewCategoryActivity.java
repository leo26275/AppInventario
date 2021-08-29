package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.app.appinventario.com.app.appinventario.adapters.AdapterCategory;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.app.appinventario.com.app.appinventario.entitys.User;
import com.google.android.material.textfield.TextInputLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCategoryActivity extends AppCompatActivity {
    private ListView lista;
    SearchView searchView;
    ImageView editar, checkB;
    static AdapterCategory adapterCategory;
    public static ArrayList<Category> category = new ArrayList<>();
    RequestParams parametros;
    public static Category categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        Toolbar toolbar = findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);

        parametros = new RequestParams();
        lista = findViewById(R.id.lista);
        editar = findViewById(R.id.editar);
        searchView = findViewById(R.id.buscar);
        adapterCategory = new AdapterCategory(this,category);
        lista.setAdapter(adapterCategory);

        checkB = findViewById(R.id.checkB);
        showData();
    }

    public  void showData(){
        StringRequest request = new StringRequest(Request.Method.POST, NewCategoryActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                category.clear();
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

                            categories = new Category(id, nombre, estado);
                            category.add(categories);
                            adapterCategory.notifyDataSetChanged();
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

    public void onEditar(View view){
        finish();
        Intent intent = new Intent(getApplicationContext(), EditCategoryActivity.class);
        intent.putExtra("position", (Category) view.getTag());
        startActivity(intent);
    }

    public void onCheck(final View view){
        msg("¿Desea modificar el estado?","estado", view);
    }

    private void msg(String texto, String opcion, final View view) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Categoria");
        dialogo.setMessage(texto);

        if(opcion.equals("estado")){
            dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final Category category = (Category) view.getTag();
                    StringRequest request = new StringRequest(Request.Method.POST, NewCategoryActivity.url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.isEmpty()){
                                Toast.makeText(ViewCategoryActivity.this, response, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewCategoryActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(ViewCategoryActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ViewCategoryActivity.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            if(category.getEstado()==0){
                                params.put("action","activar");
                                params.put("id_categoria", String.valueOf(category.getId_categoria()));
                            }else {
                                params.put("action","desactivar");
                                params.put("id_categoria", String.valueOf(category.getId_categoria()));
                            }
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(ViewCategoryActivity.this);
                    requestQueue.add(request);
                    showData();
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
                adapterCategory.getFilter().filter(s);
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
            Intent intent = new Intent(this, NewCategoryActivity.class);
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
        Intent intent = new Intent(this, NewCategoryActivity.class);
        startActivity(intent);
    }
}
