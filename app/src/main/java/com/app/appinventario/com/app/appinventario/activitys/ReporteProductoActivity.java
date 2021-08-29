package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.adapters.AdapterReporteProducto;
import com.app.appinventario.com.app.appinventario.entitys.ReportePro;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReporteProductoActivity extends AppCompatActivity {

    private ListView lista;
    AdapterReporteProducto adapterReporteProducto;
    public static ArrayList<ReportePro> datosarray = new ArrayList<>();
    ReportePro reporteClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_producto);

        lista = findViewById(R.id.listaPro);
        adapterReporteProducto = new AdapterReporteProducto(this,datosarray);
        lista.setAdapter(adapterReporteProducto);
        showData();
    }

    public void showData(){
        String url ="http://192.168.0.6:80/AppInventory/ReporteProducto.php";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                datosarray.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            String codigo = object.getString("codigo");
                            String nombre = object.getString("nombre");
                            float precio_compra = Float.parseFloat(object.getString("precio_compra"));
                            float coste_pro = Float.parseFloat(object.getString("coste_pro"));
                            float ganancia = Float.parseFloat(object.getString("ganancia"));
                            String imagen = object.getString("imagen");

                            reporteClass = new ReportePro(codigo, nombre, precio_compra,coste_pro,ganancia,imagen);
                            datosarray.add(reporteClass);
                            adapterReporteProducto.notifyDataSetChanged();
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReporteProductoActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("action", "consult");
                params.put("action", "consult");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LlamadoReportesActivity.class);
        startActivity(intent);
    }

}
