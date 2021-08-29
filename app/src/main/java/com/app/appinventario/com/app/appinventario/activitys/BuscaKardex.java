package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BuscaKardex extends AppCompatActivity {

    TextInputLayout code, cantidad, ganance, price_com;
    public static String url = "http://192.168.0.6:80/AppInventory/Product.php";
    TextView code_enc, name_enc, stock_enc, category_enc, provider_enc;
    ImageView img_enc;
    String codigo="";
    TextView error;
    String id_compra="";
    LinearLayout linel, descript_l;
    int compras_gir=0, i_pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_kardex);

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
                Toast.makeText(BuscaKardex.this, "Cancelaste el escaneo", Toast.LENGTH_SHORT).show();
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
                                Picasso.with(BuscaKardex.this).load(url_im).error(R.drawable.producto3).fit().centerInside().into(img_enc);

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
        Intent intent = new Intent(this,kardex.class);
        intent.putExtra("id", i_pd + "");
        startActivity(intent);
    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,DashboardActivity.class);
        startActivity(intent);
    }
}
