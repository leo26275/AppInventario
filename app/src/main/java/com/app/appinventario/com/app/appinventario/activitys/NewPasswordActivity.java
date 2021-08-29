package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewPasswordActivity extends AppCompatActivity {

    TextInputLayout password1, password2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        password1 = findViewById(R.id.password1);
        password2 = findViewById(R.id.password2);
    }

    public void OnSavePassword(View view) {
        if(password1.getEditText().getText().toString().isEmpty()){
            password1.setError("Complete el campo");
        }else  if(password2.getEditText().getText().toString().isEmpty()){
            password2.setError("Repita contraseña");
        }else{
            if(password1.getEditText().getText().toString().equals(password2.getEditText().getText().toString())){
                validateEmail();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void validateEmail(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Toast.makeText(NewPasswordActivity.this, "Contraseña modificada", Toast.LENGTH_LONG).show();
                    finish();
                }else{
                    Toast.makeText(NewPasswordActivity.this, "La contraseña no se puede modificar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(NewPasswordActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("option","update");
                parametros.put("password", password2.getEditText().getText().toString());
                parametros.put("id", MailActivity.valorLlave
                );
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


}
