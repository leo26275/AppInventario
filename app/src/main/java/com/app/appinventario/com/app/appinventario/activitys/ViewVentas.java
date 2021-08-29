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
import com.app.appinventario.com.app.appinventario.adapters.AdapterVentas;
import com.app.appinventario.com.app.appinventario.entitys.Compras;
import com.app.appinventario.com.app.appinventario.entitys.Ventas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewVentas extends AppCompatActivity {


    RecyclerView reciclerVentas;
    ArrayList<Ventas> listaVentas;
    ProgressDialog progressDialog;
    TextView comprobanre, fecha, esdo, clien, usr, total_text;
    float totl=0;
    public static String url= "http://192.168.0.6:80/AppInventory/Product.php";

    public Ventas ventas;

    AdapterVentas adapterVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ventas);

        comprobanre= findViewById(R.id.nombre_factura);
        fecha= findViewById(R.id.fecha);
        esdo= findViewById(R.id.estado);
        clien=findViewById(R.id.name_clien);
        usr=findViewById(R.id.name_user);
        total_text=findViewById(R.id.total_vent);

        listaVentas = new ArrayList<>();

        reciclerVentas = findViewById(R.id.recicler_ventas);
        reciclerVentas.setLayoutManager(new LinearLayoutManager(ViewVentas.this));


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    String id_venta= jsonObject.getString("id_venta");
                    String fecha_es = jsonObject.getString("fecha");
                    String estado= jsonObject.getString("estado");
                    String  cliente= jsonObject.getString("cliente");
                    String username= jsonObject.getString("user");

                    fecha.setText("Fecha: " + fecha_es);
                    comprobanre.setText("venta Nª " + id_venta);
                    if(estado.equals("1")){
                        esdo.setText("Estado: Activo");
                    }else{
                        esdo.setText("Estado: Inactivo");
                    }
                    clien.setText("Cliente: " + cliente);
                    usr.setText("Usuario: " + username);



                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);

                            String cantidad = object.getString("cantidad");
                            String coste = object.getString("coste");
                            String name_product = object.getString("name_product");
                            float coste_uni= Float.parseFloat(coste)/Integer.parseInt(cantidad);
                            String imag=object.getString("imagen");
                            totl+=Float.parseFloat(coste);

                            ventas = new Ventas(name_product, coste, ""+coste_uni, cantidad, imag);
                            listaVentas.add(ventas);
                        }

                    }
                    total_text.setText(totl + "");
                    adapterVentas = new AdapterVentas(listaVentas);
                    adapterVentas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(ViewVentas.this, listaVentas.get(reciclerVentas.getChildAdapterPosition(view)).getName(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    reciclerVentas.setAdapter(adapterVentas);
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
                params.put("action", "consult_ventas");
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
                adapterVentas  .getFilter().filter(newText);



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
