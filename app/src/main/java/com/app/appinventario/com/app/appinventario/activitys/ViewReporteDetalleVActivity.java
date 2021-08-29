package com.app.appinventario.com.app.appinventario.activitys;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.adapters.AdapterClient;
import com.app.appinventario.com.app.appinventario.adapters.AdapterProductImg;

import com.app.appinventario.com.app.appinventario.adapters.AdapterReporteDetalleV;
import com.app.appinventario.com.app.appinventario.entitys.Client;
import com.app.appinventario.com.app.appinventario.entitys.DetalleVenta;
import com.app.appinventario.com.app.appinventario.entitys.Product;
import com.app.appinventario.com.app.appinventario.entitys.Venta;
import com.app.appinventario.com.app.appinventario.entitys.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ViewReporteDetalleVActivity extends AppCompatActivity{
    ImageView editar;
    private ListView lista;
    AdapterReporteDetalleV adapterReporteDetalleV;
    public static ArrayList<DetalleVenta> detalleVentas = new ArrayList<>();
    DetalleVenta detalleClass;
    String position;
    TextView monto;
    float acumula, acumulador;
    DecimalFormat decimalFormat = new DecimalFormat("#.00");
    //desde aqui

    /*RecyclerView recyclerView;
    ArrayList<DetalleVenta> listaDetalle;

    ProgressDialog progressDialog;
    RequestQueue request;
    JsonObjectRequest jsonObjectRequest;
    String position;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reporte_detalle_v);

       /* listaDetalle = new ArrayList<>();
        recyclerView = findViewById(R.id.id_contenedor);
        recyclerView.setLayoutManager(new LinearLayoutManager(ViewReporteDetalleVActivity.this));
        recyclerView.setHasFixedSize(true);

        request = Volley.newRequestQueue(ViewReporteDetalleVActivity.this);
        cargarServicio();*/

        Venta venta = (Venta) getIntent().getExtras().getSerializable("position");
        position = String.valueOf(venta.getId_numero_venta());

        lista = findViewById(R.id.lista);
        adapterReporteDetalleV = new AdapterReporteDetalleV(this,detalleVentas);
        lista.setAdapter(adapterReporteDetalleV);
        showData();
        monto = findViewById(R.id.monto);
        // mostrarTotal();
    }
    /*private void cargarServicio() {
        progressDialog = new ProgressDialog(ViewReporteDetalleVActivity.this);
        progressDialog.setMessage("Consultar Imagenes");
        progressDialog.show();

        String url = "http://192.168.0.6:80/AppInventory/ReporteDetalleVenta.php?id_numero_venta=" + position;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,url,null,this,this);
        request.add(jsonObjectRequest);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(ViewReporteDetalleVActivity.this, "No se puede conectar" + error.toString(), Toast.LENGTH_LONG).show();
        progressDialog.hide();
        Log.d("ERROR: ", error.toString());
    }

    @Override
    public void onResponse(JSONObject response) {
        DetalleVenta detalleVenta = null;

        JSONArray json = response.optJSONArray("producto");
        try {
            for(int i=0; i<json.length(); i++){
                detalleVenta = new DetalleVenta();
                JSONObject jsonObject = null;
                jsonObject = json.getJSONObject(i);
                detalleVenta.setCodigo(jsonObject.optString("codigo"));
                detalleVenta.setNombre(jsonObject.optString("nombre"));
                detalleVenta.setImagen(jsonObject.optString("imagen"));
                detalleVenta.setCantdad(jsonObject.optInt("cantidad"));
                detalleVenta.setPrecio_venta(Float.parseFloat(jsonObject.optString("precio_venta")));
                detalleVenta.setTotal(Float.parseFloat(jsonObject.optString("total")));
                listaDetalle.add(detalleVenta);
            }
            progressDialog.hide();
            AdapterRecyclerView adapter = new AdapterRecyclerView(listaDetalle, ViewReporteDetalleVActivity.this);
            recyclerView.setAdapter(adapter);
        }catch (JSONException e){
            e.printStackTrace();
        }

    }*/

    public void showData(){
        String url ="http://192.168.0.6:80/AppInventory/ReporteDetalleVenta.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                detalleVentas.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            String codigo = object.getString("codigo");
                            String nombre = object.getString("nombre");
                            String imagen = object.getString("imagen");
                            int cantidad = object.getInt("cantidad");
                            float precio_venta = Float.parseFloat(object.getString("precio_venta"));
                            float total = Float.parseFloat(object.getString("total"));

                            detalleClass = new DetalleVenta(codigo, nombre,imagen, cantidad,precio_venta,total);
                            detalleVentas.add(detalleClass);
                            adapterReporteDetalleV.notifyDataSetChanged();
                            acumulador += Float.parseFloat(object.optString("total"));
                        }
                        monto.setText("Total ($): "+String.valueOf(decimalFormat.format(acumulador)));
                        // acumula += acumulador;
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewReporteDetalleVActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("action", "consult");
                params.put("id_numero_venta", position);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public  void mostrarTotal(){
        monto.setText(String.valueOf(acumulador));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(this, ReporteVentaActivity.class);
        startActivity(intent);
    }
}
