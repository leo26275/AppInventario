package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.app.appinventario.com.app.appinventario.adapters.AdapterUser;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.app.appinventario.com.app.appinventario.entitys.User;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewUser extends AppCompatActivity {

    private ListView lista;
    ImageView editar;
    static AdapterUser adapterUser;
    public static ArrayList<User> user = new ArrayList<>();
    RequestParams parametros;
    public static AsyncHttpClient client;
    public static User users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        Toolbar toolbar = findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);

        parametros = new RequestParams();
        client = new AsyncHttpClient();

        lista = findViewById(R.id.lista);
        editar = findViewById(R.id.editar);
        adapterUser = new AdapterUser(this,user);
        lista.setAdapter(adapterUser);
        showData();
    }

    public  void showData(){
        StringRequest request = new StringRequest(Request.Method.POST, NewUserActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user.clear();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String succes = jsonObject.getString("succes");

                    JSONArray jsonArray = jsonObject.getJSONArray("datos");

                    if(succes.equals("1")){
                        for(int i =0; i<jsonArray.length(); i++){

                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id_usuario");
                            String usuario = object.getString("usuario");
                            String password = object.getString("password");
                            String correo = object.getString("correo");
                            int estado = object.getInt("estado");

                            users = new User(id, usuario, password, correo, estado);
                            user.add(users);
                            adapterUser.notifyDataSetChanged();
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
        Intent intent = new Intent(getApplicationContext(), EditUserActivity.class);
        intent.putExtra("position", (User) view.getTag());
        startActivity(intent);
    }

    public void onCheck(final View view){
        msg("¿Desea modificar el estado?","estado", view);
    }

    private void msg(String texto, String opcion, final View view) {
        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        dialogo.setTitle("Usuario");
        dialogo.setMessage(texto);

        if(opcion.equals("estado")){
            dialogo.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final User user = (User) view.getTag();
                    StringRequest request = new StringRequest(Request.Method.POST,NewUserActivity.url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if(!response.isEmpty()){
                                Toast.makeText(ViewUser.this, response, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ViewUser.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(ViewUser.this, response, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ViewUser.this, "error" + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            if(user.getEstado()==0){
                                params.put("action","activar");
                                params.put("id_usuario", String.valueOf(user.getId_usuario()));
                            }else {
                                params.put("action","desactivar");
                                params.put("id_usuario", String.valueOf(user.getId_usuario()));
                            }
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(ViewUser.this);
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
                adapterUser.getFilter().filter(s);
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
        }else  if(id == R.id.nuevo){
            Intent intent = new Intent(this, NewUserActivity.class);
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

    public void onMail(View view){
        User user = (User) view.getTag();
        String email = user.getCorreo();
        //Toast.makeText(this, "Posicion "+provider.getCorreo(), Toast.LENGTH_SHORT).show();

        Intent intentWed = new Intent();
        Intent intentMail = new Intent(Intent.ACTION_SEND, Uri.parse("lacalacasadecv@gmail.com"));
        //intentMail.setClassName("com.google.android.gm","com.google.android.gm.ComposeActivityGmail");
        intentMail.setType("plaint/text");
        intentMail.putExtra(Intent.EXTRA_SUBJECT, "La Calaca");
        intentMail.putExtra(Intent.EXTRA_TEXT, "Querido usuario por este medio se le informa: ");
        intentMail.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        startActivity(Intent.createChooser(intentMail, "Elige"));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, NewUserActivity.class);
        startActivity(intent);
    }
}
