package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.KardexItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class kardex extends AppCompatActivity {

    ArrayList<KardexItem> listaUsuarios = new ArrayList<>();
    TableLayout tlTabla;
    public static String url = "http://192.168.0.6:80/AppInventory/kardex.php";
    TableRow fila;
    float totant=0, cant=0, ulrima_price=0, price_man=0;
    TextView tvfecha, tvconcepr, tvcantidad1,cantidad2,cantidad3,valoru1,valoru2,valoru3, valort1,valort2,valort3, text_no ;
    String id_pase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kardex);

        text_no=findViewById(R.id.text_no);
        tlTabla = findViewById(R.id.tlTabla);

        id_pase= getIntent().getStringExtra("id");
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    int cont=0;
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if (succes.equals("1")) {

                        for (int i = 0; i < jsonArray.length(); i++) {
                            cont=1;
                            JSONObject object = jsonArray.getJSONObject(i);
                            String i_pd = object.getString("codigo");
                            String fecha = object.getString("fecha");
                            String what = object.getString("concepto");
                            String cantidad = object.getString("cantidad");
                            String precio = object.getString("valorunidad");
                            if(what.equals("compra")){
                                float tot=Float.parseFloat(precio)*Float.parseFloat(cantidad);
                                float caca=Float.parseFloat(cantidad);
                                caca+=cant;
                                float toto=tot+totant;
                                float price=toto/caca;
                                listaUsuarios.add(new KardexItem(fecha, what, cantidad, precio, tot+"", "", "", "", caca+"", price+"", toto+""));
                                totant=toto;
                                ulrima_price=Float.parseFloat(precio);
                                price_man=price;
                                cant=caca;
                            }else{
                                float total=Float.parseFloat(cantidad)*ulrima_price;
                                float ca=cant-Float.parseFloat(cantidad);
                                float toat=ca*price_man;
                                listaUsuarios.add(new KardexItem(fecha, what, "", "", "", cantidad, ulrima_price+"", total+"", ca+"", price_man+"", toat+""));
                                cant=ca;
                                totant=toat;
                            }



                        }
                        if(cont==0){
                            text_no.setVisibility(View.VISIBLE);
                        }else{

                        }
                        llenarTable();

                    }else{
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id_pase);
                return params;
            }
        };
        progressDialog.dismiss();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

    public void llenarTable(){
        TableRow.LayoutParams layoutFila = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutfecha = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutconcepto = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutcantidad1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutcantidad2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutcantidad3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutprecio_u1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutprescio_u2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutprecio_u3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutprecio_t1 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutprecio_t2 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams layoutprecio_t3 = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        // TABLA
        for(int i = -2 ; i < listaUsuarios.size() ; i++) {
            fila = new TableRow(this);
            fila.setLayoutParams(layoutFila);

            if(i == -2) {
                tvfecha = new TextView(this);
                tvfecha.setText("");
                tvfecha.setGravity(Gravity.CENTER);
                tvfecha.setBackgroundColor(Color.WHITE);
                tvfecha.setTextColor(Color.BLACK);
                tvfecha.setPadding(10, 10, 10, 10);
                tvfecha.setLayoutParams(layoutfecha);
                fila.addView(tvfecha);

                tvconcepr = new TextView(this);
                tvconcepr.setText("");
                tvconcepr.setGravity(Gravity.CENTER);
                tvconcepr.setBackgroundColor(Color.WHITE);
                tvconcepr.setTextColor(Color.BLACK);
                tvconcepr.setPadding(10, 10, 10, 10);
                tvconcepr.setLayoutParams(layoutconcepto);
                fila.addView(tvconcepr);

                tvcantidad1 = new TextView(this);
                tvcantidad1.setText("");
                tvcantidad1.setGravity(Gravity.CENTER);
                tvcantidad1.setBackgroundColor(Color.BLACK);
                tvcantidad1.setTextColor(Color.WHITE);
                tvcantidad1.setPadding(10, 10, 10, 10);
                tvcantidad1.setLayoutParams(layoutcantidad1);
                fila.addView(tvcantidad1);

                valoru1 = new TextView(this);
                valoru1.setText("Entradas");
                valoru1.setGravity(Gravity.CENTER);
                valoru1.setBackgroundColor(Color.BLACK);
                valoru1.setTextColor(Color.WHITE);
                valoru1.setPadding(10, 10, 10, 10);
                valoru1.setLayoutParams(layoutprecio_u1);
                fila.addView(valoru1);

                valort1 = new TextView(this);
                valort1.setText("");
                valort1.setGravity(Gravity.CENTER);
                valort1.setBackgroundColor(Color.BLACK);
                valort1.setTextColor(Color.WHITE);
                valort1.setPadding(10, 10, 10, 10);
                valort1.setLayoutParams(layoutprecio_t1);
                fila.addView(valort1);

                cantidad2 = new TextView(this);
                cantidad2.setText("");
                cantidad2.setGravity(Gravity.CENTER);
                cantidad2.setBackgroundColor(Color.WHITE);
                cantidad2.setTextColor(Color.BLACK);
                cantidad2.setPadding(10, 10, 10, 10);
                cantidad2.setLayoutParams(layoutcantidad2);
                fila.addView(cantidad2);

                valoru2 = new TextView(this);
                valoru2.setText("Salidas");
                valoru2.setGravity(Gravity.CENTER);
                valoru2.setBackgroundColor(Color.WHITE);
                valoru2.setTextColor(Color.BLACK);
                valoru2.setPadding(10, 10, 10, 10);
                valoru2.setLayoutParams(layoutprescio_u2);
                fila.addView(valoru2);

                valort2 = new TextView(this);
                valort2.setText("");
                valort2.setGravity(Gravity.CENTER);
                valort2.setBackgroundColor(Color.WHITE);
                valort2.setTextColor(Color.BLACK);
                valort2.setPadding(10, 10, 10, 10);
                valort2.setLayoutParams(layoutprecio_t2);
                fila.addView(valort2);

                cantidad3 = new TextView(this);
                cantidad3.setText("");
                cantidad3.setGravity(Gravity.CENTER);
                cantidad3.setBackgroundColor(Color.BLACK);
                cantidad3.setTextColor(Color.WHITE);
                cantidad3.setPadding(10, 10, 10, 10);
                cantidad3.setLayoutParams(layoutcantidad3);
                fila.addView(cantidad3);

                valoru3 = new TextView(this);
                valoru3.setText("Saldo");
                valoru3.setGravity(Gravity.CENTER);
                valoru3.setBackgroundColor(Color.BLACK);
                valoru3.setTextColor(Color.WHITE);
                valoru3.setPadding(10, 10, 10, 10);
                valoru3.setLayoutParams(layoutprecio_u3);
                fila.addView(valoru3);

                valort3 = new TextView(this);
                valort3.setText("");
                valort3.setGravity(Gravity.CENTER);
                valort3.setBackgroundColor(Color.BLACK);
                valort3.setTextColor(Color.WHITE);
                valort3.setPadding(10, 10, 10, 10);
                valort3.setLayoutParams(layoutprecio_t3);
                fila.addView(valort3);


                tlTabla.addView(fila);
            } else if(i == -1) {
                tvfecha = new TextView(this);
                tvfecha.setText("Fecha");
                tvfecha.setGravity(Gravity.CENTER);
                tvfecha.setBackgroundColor(Color.WHITE);
                tvfecha.setTextColor(Color.BLACK);
                tvfecha.setPadding(10, 10, 10, 10);
                tvfecha.setLayoutParams(layoutfecha);
                fila.addView(tvfecha);

                tvconcepr = new TextView(this);
                tvconcepr.setText("Concepto");
                tvconcepr.setGravity(Gravity.CENTER);
                tvconcepr.setBackgroundColor(Color.WHITE);
                tvconcepr.setTextColor(Color.BLACK);
                tvconcepr.setPadding(10, 10, 10, 10);
                tvconcepr.setLayoutParams(layoutconcepto);
                fila.addView(tvconcepr);

                tvcantidad1 = new TextView(this);
                tvcantidad1.setText("Cantidad");
                tvcantidad1.setGravity(Gravity.CENTER);
                tvcantidad1.setBackgroundColor(Color.BLACK);
                tvcantidad1.setTextColor(Color.WHITE);
                tvcantidad1.setPadding(10, 10, 10, 10);
                tvcantidad1.setLayoutParams(layoutcantidad1);
                fila.addView(tvcantidad1);

                valoru1 = new TextView(this);
                valoru1.setText("Valor Unitario");
                valoru1.setGravity(Gravity.CENTER);
                valoru1.setBackgroundColor(Color.BLACK);
                valoru1.setTextColor(Color.WHITE);
                valoru1.setPadding(10, 10, 10, 10);
                valoru1.setLayoutParams(layoutprecio_u1);
                fila.addView(valoru1);

                valort1 = new TextView(this);
                valort1.setText("Valor Total");
                valort1.setGravity(Gravity.CENTER);
                valort1.setBackgroundColor(Color.BLACK);
                valort1.setTextColor(Color.WHITE);
                valort1.setPadding(10, 10, 10, 10);
                valort1.setLayoutParams(layoutprecio_t1);
                fila.addView(valort1);

                cantidad2 = new TextView(this);
                cantidad2.setText("Cantidad");
                cantidad2.setGravity(Gravity.CENTER);
                cantidad2.setBackgroundColor(Color.WHITE);
                cantidad2.setTextColor(Color.BLACK);
                cantidad2.setPadding(10, 10, 10, 10);
                cantidad2.setLayoutParams(layoutcantidad2);
                fila.addView(cantidad2);

                valoru2 = new TextView(this);
                valoru2.setText("Valor Unitario");
                valoru2.setGravity(Gravity.CENTER);
                valoru2.setBackgroundColor(Color.WHITE);
                valoru2.setTextColor(Color.BLACK);
                valoru2.setPadding(10, 10, 10, 10);
                valoru2.setLayoutParams(layoutprescio_u2);
                fila.addView(valoru2);

                valort2 = new TextView(this);
                valort2.setText("Valor Total");
                valort2.setGravity(Gravity.CENTER);
                valort2.setBackgroundColor(Color.WHITE);
                valort2.setTextColor(Color.BLACK);
                valort2.setPadding(10, 10, 10, 10);
                valort2.setLayoutParams(layoutprecio_t2);
                fila.addView(valort2);

                cantidad3 = new TextView(this);
                cantidad3.setText("Cantidad");
                cantidad3.setGravity(Gravity.CENTER);
                cantidad3.setBackgroundColor(Color.BLACK);
                cantidad3.setTextColor(Color.WHITE);
                cantidad3.setPadding(10, 10, 10, 10);
                cantidad3.setLayoutParams(layoutcantidad3);
                fila.addView(cantidad3);

                valoru3 = new TextView(this);
                valoru3.setText("Valor Unitario");
                valoru3.setGravity(Gravity.CENTER);
                valoru3.setBackgroundColor(Color.BLACK);
                valoru3.setTextColor(Color.WHITE);
                valoru3.setPadding(10, 10, 10, 10);
                valoru3.setLayoutParams(layoutprecio_u3);
                fila.addView(valoru3);

                valort3 = new TextView(this);
                valort3.setText("Valor Total");
                valort3.setGravity(Gravity.CENTER);
                valort3.setBackgroundColor(Color.BLACK);
                valort3.setTextColor(Color.WHITE);
                valort3.setPadding(10, 10, 10, 10);
                valort3.setLayoutParams(layoutprecio_t3);
                fila.addView(valort3);


                tlTabla.addView(fila);
            } else {
                tvfecha = new TextView(this);
                tvfecha.setText(listaUsuarios.get(i).getFecha());
                tvfecha.setPadding(10, 10, 10, 10);
                tvfecha.setLayoutParams(layoutfecha);
                fila.addView(tvfecha);

                tvconcepr = new TextView(this);
                tvconcepr.setGravity(Gravity.CENTER);
                tvconcepr.setText(listaUsuarios.get(i).getConcepto());
                tvconcepr.setPadding(10, 10, 10, 10);
                tvconcepr.setLayoutParams(layoutconcepto);
                fila.addView(tvconcepr);

                tvcantidad1 = new TextView(this);
                tvcantidad1.setText(listaUsuarios.get(i).getCantidad1());
                tvcantidad1.setPadding(10, 10, 10, 10);
                tvcantidad1.setLayoutParams(layoutcantidad1);
                fila.addView(tvcantidad1);

                valoru1 = new TextView(this);
                valoru1.setText(listaUsuarios.get(i).getPrecio_u1());
                valoru1.setPadding(10, 10, 10, 10);
                valoru1.setLayoutParams(layoutprecio_u1);
                fila.addView(valoru1);

                valort1 = new TextView(this);
                valort1.setText(listaUsuarios.get(i).getPrecio_t1());
                valort1.setPadding(10, 10, 10, 10);
                valort1.setLayoutParams(layoutprecio_t1);
                fila.addView(valort1);


                cantidad2 = new TextView(this);
                cantidad2.setText(listaUsuarios.get(i).getCantidad2());
                cantidad2.setPadding(10, 10, 10, 10);
                cantidad2.setLayoutParams(layoutcantidad2);
                fila.addView(cantidad2);

                valoru2 = new TextView(this);
                valoru2.setText(listaUsuarios.get(i).getPrecio_u2());
                valoru2.setPadding(10, 10, 10, 10);
                valoru2.setLayoutParams(layoutprescio_u2);
                fila.addView(valoru2);

                valort2 = new TextView(this);
                valort2.setText(listaUsuarios.get(i).getPrecio_t2());
                valort2.setPadding(10, 10, 10, 10);
                valort2.setLayoutParams(layoutprecio_t2);
                fila.addView(valort2);

                cantidad3 = new TextView(this);
                cantidad3.setText(listaUsuarios.get(i).getCantidad3());
                cantidad3.setPadding(10, 10, 10, 10);
                cantidad3.setLayoutParams(layoutcantidad3);
                fila.addView(cantidad3);

                valoru3 = new TextView(this);
                valoru3.setText(listaUsuarios.get(i).getPrecio_u3());
                valoru3.setPadding(10, 10, 10, 10);
                valoru3.setLayoutParams(layoutprecio_u3);
                fila.addView(valoru3);

                valort3 = new TextView(this);
                valort3.setText(listaUsuarios.get(i).getPrecio_t3());
                valort3.setPadding(10, 10, 10, 10);
                valort3.setLayoutParams(layoutprecio_t3);
                fila.addView(valort3);


                tlTabla.addView(fila);
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, LlamadoReportesActivity.class);
        startActivity(intent);
    }

}
