package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.app.appinventario.com.app.appinventario.entitys.Client;
import com.app.appinventario.com.app.appinventario.entitys.Product;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vender_product extends AppCompatActivity {

    TextInputLayout code, cantidad;
    public static String url = "http://192.168.0.6:80/AppInventory/Product.php";
    TextView code_enc, name_enc, stock_enc, category_enc, provider_enc, precio_encs;
    ImageView img_enc;
    String codigo="";
    TextView error;
    String id_compra="";
    LinearLayout linel, descript_l;
    int compras_gir=0, i_pd;
    String price;

    String stockken="0";
    int vali_sal=0;
    Spinner cliente;
    public Client clients;
    public static ArrayList<Client> listaClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vender_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        code = findViewById(R.id.code_vend);
        code_enc = findViewById(R.id.code_enc);
        name_enc = findViewById(R.id.nombre_enc);
        stock_enc = findViewById(R.id.stock_enc);
        category_enc = findViewById(R.id.category_enc);
        provider_enc = findViewById(R.id.provider_enc);
        img_enc = findViewById(R.id.call_enc);
        error=findViewById(R.id.error_tet);
        linel=findViewById(R.id.content_product);
        descript_l = findViewById(R.id.layout_descrip);
        precio_encs=findViewById(R.id.precio_enc);
        cantidad=findViewById(R.id.cantidad);

        cliente=findViewById(R.id.cliente);
        listaClient = new ArrayList<>();


        showDataClient();
        cliente.setAdapter(new ArrayAdapter<>(Vender_product.this,android.R.layout.simple_spinner_dropdown_item,listaClient));


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
                Toast.makeText(Vender_product.this, "Cancelaste el escaneo", Toast.LENGTH_SHORT).show();
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
                                stockken=object.getString("stock");
                                precio_encs.setText(object.getString("precio"));
                                price=object.getString("precio");
                                String url_im="http://192.168.0.6:80/AppInventory/" + object.getString("imagen");
                                Picasso.with(Vender_product.this).load(url_im).error(R.drawable.producto3).fit().centerInside().into(img_enc);

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
        final Client pro = (Client) cliente.getSelectedItem();
        if(pro!=null){
            if(stockken.equals("0")){
                Toast.makeText(this, "El producto no se puede comprar ya que se encuentra agotado", Toast.LENGTH_SHORT).show();
            }else {
                linel.setVisibility(View.INVISIBLE);
                descript_l.setVisibility(View.VISIBLE);
                findViewById(R.id.error_spin).setVisibility(View.INVISIBLE);
                findViewById(R.id.save).setVisibility(View.INVISIBLE);
            }
        }else{
            findViewById(R.id.error_spin).setVisibility(View.VISIBLE);
        }

    }

    public void pay_more(View view){
        final int canti= Integer.parseInt(cantidad.getEditText().getText().toString());
        int cantimax=Integer.parseInt(stockken);
        if(capus_vacios(view)){
            Toast.makeText(this, "LLene todos los campos", Toast.LENGTH_SHORT).show();
        }else if(canti>cantimax){
            Toast.makeText(this, "No se puede comprar tantos productos, maximo " + cantimax + "productos", Toast.LENGTH_SHORT).show();
        }else{
            if(compras_gir==0){
                registro_primero();
                compras_gir++;
            }else{
                registrodos();
            }
        }


    }
    public void pay_finich(View view){
        vali_sal=-1;
        pay_more(view);
    }


    private boolean capus_vacios(View view){
        if(cantidad.getEditText().getText().toString().isEmpty()){
            cantidad.setError("Complete campo de la cantidad");
            return true;
        }
        return false;
    }


    private void registro_primero() {
        final Client pro = (Client) cliente.getSelectedItem();
        StringRequest request = new StringRequest(Request.Method.POST, Comprar_productos.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("registra")) {
                    registrodos();
                } else {
                    Toast.makeText(Vender_product.this, "No se registro la venta", Toast.LENGTH_SHORT).show();
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
                params.put("action", "create_vent");
                params.put("ed_cliente", String.valueOf(pro.getId_cliente()));
                params.put("user",   "1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);

    }


    private void registrodos(){

        final int canti= Integer.parseInt(cantidad.getEditText().getText().toString());
        int cantimax=Integer.parseInt(stockken);
        final int id_product = i_pd;


        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equalsIgnoreCase("registra")) {
                    Toast.makeText(Vender_product.this, "Venta Exitosa", Toast.LENGTH_SHORT).show();
                    cantidad.getEditText().setText("");
                    descript_l.setVisibility(View.INVISIBLE);
                    findViewById(R.id.save).setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                    if(vali_sal==-1){
                        Intent intent = new Intent(Vender_product.this,ViewVentas.class);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(Vender_product.this, "No se ha podido efectuar la compra" + response, Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Vender_product.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("action", "create_detalle_vent");
                params.put("cantidad", "" + canti);
                params.put("id_product", id_product + "");
                params.put("price", price);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Vender_product.this);
        requestQueue.add(request);

    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }




    public  void showDataClient(){
        StringRequest request = new StringRequest(Request.Method.POST, NewClientActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listaClient.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = Integer.parseInt(object.getString("id_cliente"));
                            String nombre = object.getString("nombre");
                            String direccion = object.getString("direccion");
                            String telefono = object.getString("telefono");
                            int estado = Integer.parseInt(object.getString("estado"));

                            clients = new Client(id, nombre, telefono,direccion, estado);
                            listaClient.add(clients);
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


}