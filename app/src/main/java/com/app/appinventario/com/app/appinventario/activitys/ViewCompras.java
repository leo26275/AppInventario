package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.adapters.AdapterCompras;
import com.app.appinventario.com.app.appinventario.adapters.AdapterProductImg;
import com.app.appinventario.com.app.appinventario.entitys.Compras;
import com.app.appinventario.com.app.appinventario.entitys.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCompras extends AppCompatActivity {

    RecyclerView reciclerCompras;
    ArrayList<Compras> listaCompras;
    ProgressDialog progressDialog;
    TextView comprobanre, fecha, esdo, total_tst;
    public static String url= "http://192.168.0.6:80/AppInventory/Product.php";
    float total=0;
    public Compras compras;

    AdapterCompras adapterCompras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_compras);


        comprobanre= findViewById(R.id.nombre_factura);
        fecha= findViewById(R.id.fecha);
        esdo= findViewById(R.id.estado);
        total_tst=findViewById(R.id.total_compr);

        listaCompras = new ArrayList<>();

        reciclerCompras = findViewById(R.id.recicler_compras);
        reciclerCompras.setLayoutManager(new LinearLayoutManager(ViewCompras.this));


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    String fecha_viene= jsonObject.getString("fecha");
                    String comprobante = jsonObject.getString("comprobante");
                    String estado= jsonObject.getString("estado");

                    fecha.setText("Fecha: " + fecha_viene);
                    comprobanre.setText("Compra Nª " + comprobante);
                    if(estado.equals("1")){
                        esdo.setText("Estado: Activo");
                    }else{
                        esdo.setText("Estado: Inactivo");
                    }



                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            String name = object.getString("name");
                            String preciocompra = object.getString("price_comp");
                            String precioventa = object.getString("price_vent");
                            String cantidad = object.getString("cantidad");
                            String imagen = object.getString("imagen");

                            total+=(Float.parseFloat(preciocompra)*Float.parseFloat(cantidad));
                            compras = new Compras(name, preciocompra, precioventa, cantidad, imagen);
                            listaCompras.add(compras);
                        }

                    }
                    total_tst.setText(total+ "");
                    adapterCompras = new AdapterCompras(listaCompras);
                    adapterCompras.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(ViewCompras.this, listaCompras.get(reciclerCompras.getChildAdapterPosition(view)).getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    reciclerCompras.setAdapter(adapterCompras);
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
                params.put("action", "consult_compras");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item= menu.findItem(R.id.search);
        androidx.appcompat.widget.SearchView serarch= (androidx.appcompat.widget.SearchView) item.getActionView();

        serarch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterCompras.getFilter().filter(newText);



                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }


    public void cerar(View view){
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }


}
