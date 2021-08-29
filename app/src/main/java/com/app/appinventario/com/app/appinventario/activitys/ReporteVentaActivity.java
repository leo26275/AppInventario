package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.app.appinventario.com.app.appinventario.adapters.AdapterReporteVentas;
import com.app.appinventario.com.app.appinventario.entitys.Venta;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReporteVentaActivity extends AppCompatActivity implements View.OnClickListener {
    private int año,año1;
    private int mes, mes1;
    private int dia, dia1;

    TextView inicio,fin;
    ImageView btnInicio,btnFin;
    ListView lista;

    AdapterReporteVentas adapterReporteVentas;
    public static ArrayList<Venta> venta = new ArrayList<>();
    Venta ventaClass;
    public String url = "http://192.168.0.6:80/AppInventory/ReporteVenta.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte_venta);

        inicio = findViewById(R.id.inicio);
        btnInicio = findViewById(R.id.btnInicio);
        fin = findViewById(R.id.fin);
        btnFin = findViewById(R.id.btnFinal);
        lista = findViewById(R.id.lista);

        btnInicio.setOnClickListener(this);
        btnFin.setOnClickListener(this);



        adapterReporteVentas = new AdapterReporteVentas(this,venta);
        lista.setAdapter(adapterReporteVentas);
    }


    @Override
    public void onClick(View view) {
        if(view==btnInicio){
            final Calendar c = Calendar.getInstance();
            dia = c.get(Calendar.DAY_OF_MONTH);
            mes = c.get(Calendar.MONTH);
            año = c.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    inicio.setText(i+"/"+(i1+1)+"/"+i2);
                }
            },año,mes,dia);
            datePickerDialog.show();
        }else if(view==btnFin){
            final Calendar c = Calendar.getInstance();
            dia1 = c.get(Calendar.DAY_OF_MONTH);
            mes1 = c.get(Calendar.MONTH);
            año1 = c.get(Calendar.YEAR);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    fin.setText(i+"/"+(i1+1)+"/"+i2);
                }
            },año1,mes1,dia1);
            datePickerDialog.show();
        }
    }

    public void showData(){
        String fecha1 = (String) inicio.getText();
        String fecha2 = (String) fin.getText();
        String fecha1n = fecha1.replace("/","");
        String fecha2n = fecha2.replace("/","");
        int vFecha1 = Integer.valueOf(fecha1n);
        int vfecha2 = Integer.valueOf(fecha2n);

        if(vFecha1>vfecha2){
            Toast.makeText(ReporteVentaActivity.this, "La fecha de inicio debe ser menor", Toast.LENGTH_LONG).show();
            inicio.setText("");
            fin.setText("");
        }else {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    venta.clear();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String succes = jsonObject.getString("succes");

                        JSONArray jsonArray = jsonObject.getJSONArray("datos");

                        if (succes.equals("1")) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id_numero_venta");
                                Date fecha = Date.valueOf(object.getString("fecha"));
                                String nombre = object.getString("nombre");

                                ventaClass = new Venta(id, nombre, fecha);
                                venta.add(ventaClass);
                                adapterReporteVentas.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(ReporteVentaActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("action", "consult");
                    params.put("fecha_inicio", inicio.getText().toString());
                    params.put("fecha_final", fin.getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    }


    public void onConsultar(View view) {
        showData();
    }

    public void onDetalle(View view){
        finish();
        Intent intent = new Intent(getApplicationContext(), ViewReporteDetalleVActivity.class);
        intent.putExtra("position", (Venta) view.getTag());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LlamadoReportesActivity.class);
        startActivity(intent);
    }
}
