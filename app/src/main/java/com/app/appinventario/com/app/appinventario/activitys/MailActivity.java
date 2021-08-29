package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.util.Properties;


import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class MailActivity extends AppCompatActivity {
    String emailCalaca, codigo;
    String passwordCalaca;
    TextInputLayout email;
    Session session;
    public static String valorLlave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        email = findViewById(R.id.code);
        emailCalaca="lacalacasadecv@gmail.com";
        passwordCalaca="larncalaca";
    }

    public void onSend(View view) {
        if(email.getEditText().getText().toString().isEmpty()){
            email.setError("Complete campo email");
        }else {
            if (email.getEditText().getText().toString().contains("@") &&
                    (email.getEditText().getText().toString().contains(".com") ||
                            email.getEditText().getText().toString().contains(".es") ||
                            email.getEditText().getText().toString().contains("ues.edu.sv")))    {
                validateEmail();
            }else {
                Toast.makeText(this, "Correo no valido", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public  void sendMail(){
        codigo = generarCodigo();
        enviarMail();
        Intent intent =  new Intent(MailActivity.this, CheckCodeActivity.class);
        intent.putExtra("code",codigo);
        intent.putExtra("email",email.getEditText().getText().toString());
        startActivity(intent);
    }

    public void validateEmail(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    sendMail();
                    JSONObject jsonjObject = null;
                    try {
                        jsonjObject = new JSONObject(response);
                        valorLlave = jsonjObject.getString("id_usuario");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(MailActivity.this, "Codigo enviado exitosamente", Toast.LENGTH_LONG).show();
                   // flag = true;
                    finish();
                }else{
                    // flag = false;
                    Toast.makeText(MailActivity.this, "El correo no coincide", Toast.LENGTH_SHORT).show();
                    email.getEditText().setText("");
                    email.requestFocus();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MailActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String, String>();
                parametros.put("option","checkEmail");
                parametros.put("email", email.getEditText().getText().toString());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
       // return  flag;
    }


    private  String generarCodigo() {
        String  codigo = "";
        int a;
        for (int i = 0; i < 7; i++) {
            if (i < 4) {
                codigo = (int) (Math.random() * 9) + "" + codigo;

            } else {
                do {
                    a = (int) (Math.random() * 26 + 65);
                } while (a == 65 || a == 69 || a == 73 || a == 79 || a == 85);

                char letra = (char) a;
                if (i == 4) {
                    codigo = codigo + "-" + letra;
                } else {
                    codigo = codigo + "" + letra;
                }

            }
        }
        return codigo;
    }

    public void enviarMail(){
        String cod1 = "El codigo de recuperación es: "+codigo;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.googlemail.com");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        try{
            session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailCalaca,passwordCalaca);
                }
            });

            if (session !=null){
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(emailCalaca));
                message.setSubject("Recuperar contraseña");
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.getEditText().getText().toString()));
                message.setContent(cod1,"text/html; charset=utf-8");
                Transport.send(message);
            }

        }catch (Exception e){
           e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {

    }
}
