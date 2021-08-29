package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.app.appinventario.com.app.appinventario.adapters.AdapterProvider;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class ViewProviderActivity extends AppCompatActivity {
    int global_position = 0;
    private ListView lista;
    ImageView editar,call;
    static AdapterProvider adapterProvider;
    public static ArrayList<Provider> proveedor = new ArrayList<>();
    RequestParams parametros;
    public static AsyncHttpClient client;
    public static Provider proveedors;
    private final int PHONE_CALL_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_provider);

        Toolbar toolbar = findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);

        parametros = new RequestParams();
        client = new AsyncHttpClient();

        lista = findViewById(R.id.lista);
        editar = findViewById(R.id.editar);
        call = findViewById(R.id.call);
        adapterProvider = new AdapterProvider(this,proveedor);
        lista.setAdapter(adapterProvider);
        showData();
    }

    public void onMail(View view){
        Provider provider = (Provider) view.getTag();
        String email = provider.getCorreo();
        //Toast.makeText(this, "Posicion "+provider.getCorreo(), Toast.LENGTH_SHORT).show();

        Intent intentWed = new Intent();
        Intent intentMail = new Intent(Intent.ACTION_SEND, Uri.parse("lacalacasadecv@gmail.com"));
        //intentMail.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
        intentMail.setType("plaint/text");
        intentMail.putExtra(Intent.EXTRA_SUBJECT, "La Calaca");
        intentMail.putExtra(Intent.EXTRA_TEXT, "Saludos desde la empresa la calaca");
        intentMail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        startActivity(Intent.createChooser(intentMail, "Elige"));
    }

    public void onCall(View view){
        Provider provider = (Provider) view.getTag();
        String phoneNumber = provider.getTelefono().toString();
        //Toast.makeText(this, "Posicion "+provider.getId_proveedor(), Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //Comprobar si ha aceptado, no ha aceptado
            if(CheckPermission(Manifest.permission.CALL_PHONE)){
                Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel: " + phoneNumber));
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) return;
                startActivity(i);
            }else{
                //o no ha aceptado, o no es la primera vez
                if(!shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)){
                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PHONE_CALL_CODE);
                }else{
                    Toast.makeText(this, "Active los permisos por favor", Toast.LENGTH_LONG).show();
                                /*Intent s = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                s.addCategory(Intent.CATEGORY_DEFAULT);
                                s.setData(Uri.parse("package: " + getPackageName()));
                                s.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                s.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                s.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(s);*/
                }
            }

        } else {
            OlderVersions(phoneNumber);
        }

    }

    private boolean CheckPermission(String permission){
        int result = this.checkCallingOrSelfPermission(permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void OlderVersions(String phoneNumber) {
        Intent intentCall = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if (CheckPermission(Manifest.permission.CALL_PHONE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
            }
            startActivity(intentCall);
        } else {
            Toast.makeText(this, "Acceso declinado", Toast.LENGTH_SHORT).show();
        }

    }

    public  void showData(){
        StringRequest request = new StringRequest(Request.Method.POST, NewProviderActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                proveedor.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id_proveedor");
                            String nombre = object.getString("nombre");
                            String telefono = object.getString("telefono");
                            String correo = object.getString("correo");
                            String direccion = object.getString("direccion");
                            int estado = object.getInt("estado");

                            proveedors = new Provider(id, nombre, telefono, correo, direccion, estado);
                            proveedor.add(proveedors);
                            adapterProvider.notifyDataSetChanged();
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
        Intent intent = new Intent(getApplicationContext(), EditProviderActivity.class);
        intent.putExtra("position", (Provider) view.getTag());
        startActivity(intent);
    }

    public void onCheck(final View view){
        msg("¿Desea modificar el estado?","estado", view);
    }

    private void msg(String texto, String opcion, final View view) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Proveedor");
        dialogo.setMessage(texto);

        if(opcion.equals("estado")){
            dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final Provider provider = (Provider) view.getTag();
                    StringRequest request = new StringRequest(Request.Method.POST, NewProviderActivity.url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.isEmpty()){
                                Toast.makeText(ViewProviderActivity.this, response, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewProviderActivity.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(ViewProviderActivity.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ViewProviderActivity.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            if(provider.getEstado()==0){
                                params.put("action","activar");
                                params.put("id_proveedor", String.valueOf(provider.getId_proveedor()));
                            }else {
                                params.put("action","desactivar");
                                params.put("id_proveedor", String.valueOf(provider.getId_proveedor()));
                            }
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(ViewProviderActivity.this);
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
                adapterProvider.getFilter().filter(s);
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
            Intent intent = new Intent(this, NewProviderActivity.class);
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
        Intent intent = new Intent(this, NewProviderActivity.class);
        startActivity(intent);
    }
}
