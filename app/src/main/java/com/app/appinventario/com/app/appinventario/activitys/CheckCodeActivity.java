package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.app.appinventario.MainActivity;
import com.app.appinventario.R;
import com.google.android.material.textfield.TextInputLayout;

public class CheckCodeActivity extends AppCompatActivity {

    TextInputLayout code;
    Bundle receiveCode, receiveImail;
    String compCode,email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_code);

        code = findViewById(R.id.code);
        receiveCode = getIntent().getExtras();
        compCode = receiveCode.getString("code");

        receiveImail = getIntent().getExtras();
        email = receiveCode.getString("email");
      //  Toast.makeText(this, "codigo: "+compCode, Toast.LENGTH_LONG).show();

    }

    public void onCheck(View view) {
        if(code.getEditText().getText().toString().isEmpty()){
            code.setError("Ingrese el codigo");
            code.getEditText().requestFocus();
        }else{

            if(code.getEditText().getText().toString().equals(compCode)){
                Intent intent =  new Intent(CheckCodeActivity.this, NewPasswordActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(this, "El codigo no es correcto", Toast.LENGTH_SHORT).show();
            }
        }

    }

 }
