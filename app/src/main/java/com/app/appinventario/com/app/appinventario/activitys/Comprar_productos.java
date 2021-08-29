package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.MainActivity;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.adapters.AdapterProductImg;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Comprar_productos extends AppCompatActivity {

    TextInputLayout code, cantidad, ganance, price_com;
    public static String url = "http://192.168.0.6:80/AppInventory/Product.php";
    TextView code_enc, name_enc, stock_enc, category_enc, provider_enc;
    ImageView img_enc;
    String codigo="";
    int vali_sal=0;
    TextView error;
    String id_compra="";
    LinearLayout linel, descript_l;
    int compras_gir=0, i_pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comprar_productos);

        code = findViewById(R.id.code_foot);
        code_enc = findViewById(R.id.code_enc);
        name_enc = findViewById(R.id.nombre_enc);
        stock_enc = findViewById(R.id.stock_enc);
        category_enc = findViewById(R.id.category_enc);
        provider_enc = findViewById(R.id.provider_enc);
        img_enc = findViewById(R.id.call_enc);
        error=findViewById(R.id.error_tet);
        linel=findViewById(R.id.content_product);
        descript_l = findViewById(R.id.layout_descrip);

        cantidad=findViewById(R.id.cantidad);
        ganance=findViewById(R.id.ganacia);
        price_com=findViewById(R.id.price_compra);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }


    public void onCodigo(View view) {
        escanear();
    }

    public void escanear() {
        IntentIntegrator intent = new IntentIntegrator(this);
        intent.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intent.setPrompt("ESCANEAR CODIGO");
        intent.setCameraId(0);
        intent.setOrientationLocked(false);
        intent.setBeepEnabled(false);
        intent.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(Comprar_productos.this, "Cancelaste el escaneo", Toast.LENGTH_SHORT).show();
            } else {
                code.getEditText().setText(result.getContents().toString());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }



    }


    public void onFound(View view) {
        codigo = code.getEditText().getText().toString();
        if (!codigo.isEmpty()) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.show();
            StringRequest request = new StringRequest(Request.Method.POST, Comprar_productos.url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String succes = jsonObject.getString("succes");

                        JSONArray jsonArray = jsonObject.getJSONArray("datos");

                        if (succes.equals("1")) {

                            linel.setVisibility(View.VISIBLE);
                            error.setVisibility(View.INVISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);
                                i_pd = object.getInt("id_producto");
                                code_enc.setText(object.getString("codigo"));
                                name_enc.setText(object.getString("nombre"));
                                stock_enc.setText(object.getString("stock"));
                                String url_im="http://192.168.0.6:80/AppInventory/" + object.getString("imagen");
                                Picasso.with(Comprar_productos.this).load(url_im).error(R.drawable.producto3).fit().centerInside().into(img_enc);

                                category_enc.setText(object.getString("id_categoria"));
                                provider_enc.setText(object.getString("id_proveedor"));
                                // int estado = object.getInt("estado");

                                //products = new Product(id, codigo,nombre,stock,imagen,id_categoria ,id_proveedor, estado);
                            }

                        }else{
                            linel.setVisibility(View.INVISIBLE);
                            error.setVisibility(View.VISIBLE);
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
                    params.put("action", "consult_prouct");
                    params.put("code", codigo);
                    return params;
                }
            };
            progressDialog.dismiss();
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    }

    public void check_comp(View view){
        linel.setVisibility(View.INVISIBLE);
        descript_l.setVisibility(View.VISIBLE);
        findViewById(R.id.save).setVisibility(View.INVISIBLE);
    }

    public void pay_more(View view){
        if(capus_vacios(view)){
            Toast.makeText(this, "LLene todos los campos", Toast.LENGTH_SHORT).show();
        }
        else{
            if(compras_gir==0){
                registro_primero();
                compras_gir++;
            }else{
                registrodos();
            }


            descript_l.setVisibility(View.INVISIBLE);
            findViewById(R.id.save).setVisibility(View.VISIBLE);

        }


    }
    public void pay_finich(View view){
        vali_sal=-1;
        pay_more(view);
    }


    private void runthread() {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private boolean capus_vacios(View view){
        if(price_com.getEditText().getText().toString().isEmpty()){
            price_com.setError("Complete campo precio de compra");
            return true;
        }else if(cantidad.getEditText().getText().toString().isEmpty()){
            cantidad.setError("Complete campo de la cantidad");
            return true;
        }else if(ganance.getEditText().getText().toString().isEmpty()){
            ganance.setError("Complete campo ganancias");
            return true;
        }
        return false;
    }


    private void registro_primero() {
        StringRequest request = new StringRequest(Request.Method.POST, Comprar_productos.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("registra")) {
                    registrodos();
                } else {
                    Toast.makeText(Comprar_productos.this, "No se registro la compra", Toast.LENGTH_SHORT).show();
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
                params.put("action", "create_pay");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    private void registrodos(){

        float ganancias= (Float.parseFloat(price_com.getEditText().getText().toString().trim())*Float.parseFloat(ganance.getEditText().getText().toString().trim()))/100;

        final float precioventa= ganancias+ Float.parseFloat(price_com.getEditText().getText().toString());
        final int canti= Integer.parseInt(cantidad.getEditText().getText().toString());
        final float precio_coompra=Float.parseFloat(price_com.getEditText().getText().toString());
        final int id_product=i_pd;



        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("registra")) {
                    cantidad.getEditText().setText("");
                    ganance.getEditText().setText("");
                    price_com.getEditText().setText("");
                    code.getEditText().setText("");
                    Toast.makeText(Comprar_productos.this, "Compra Exitosa", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    if(vali_sal==-1){
                        Intent intent = new Intent(Comprar_productos.this,ViewCompras.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(Comprar_productos.this, "No se ha podido efectuar la compra" + response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Comprar_productos.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "create_detalle");
                params.put("precioventa", "" + precioventa);
                params.put("preciocompra", "" + precio_coompra);
                params.put("cantidad", "" + canti);
                params.put("id_product", id_product + "");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Comprar_productos.this);
        requestQueue.add(request);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sencillo,menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu){
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
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }

}
