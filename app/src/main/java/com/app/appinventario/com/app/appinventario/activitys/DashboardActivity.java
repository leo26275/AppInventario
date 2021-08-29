package com.app.appinventario.com.app.appinventario.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.app.appinventario.MainActivity;
import com.app.appinventario.R;

import java.io.File;

public class DashboardActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.menu_logout:
                logOut();
                break;
            //logOut();
                /*SharedPreferences preferences = getSharedPreferences("preferenciasLogin", Context.MODE_PRIVATE);
                boolean estado = preferences.getBoolean("estado", false);

                if(!estado) {
                    preferences.edit().clear().commit();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                break;*/
            case R.id.olvidar:
                renoveSharedPreferences();
                logOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void renoveSharedPreferences(){
        preferences.edit().clear().apply();
    }


    public void onCategory(View view) {
        Intent intent = new Intent(this,NewCategoryActivity.class);
        startActivity(intent);
    }

    public void onProvider(View view) {
        Intent intent = new Intent(this,NewProviderActivity.class);
        startActivity(intent);
    }

    public void onClient(View view) {
        Intent intent = new Intent(this,NewClientActivity.class);
        startActivity(intent);
    }

    public void onUser(View view) {
        Intent intent = new Intent(this,NewUserActivity.class);
        startActivity(intent);
    }

    public void onProduct(View view) {
        Intent intent = new Intent(this,NewProductActivity.class);
        startActivity(intent);
    }
    public void onCompra(View view) {
        Intent intent = new Intent(this, Comprar_productos.class);
        startActivity(intent);
    }

    public void onAcerca(View view) {
        Intent intent = new Intent(this,About2Activity.class);
        startActivity(intent);
    }

    public void onPDF(View view) {
        Intent intent = new Intent(this,PDFActivity.class);
        startActivity(intent);
    }

    public void onVenta(View view) {
        Intent intent = new Intent(this,Vender_product.class);
        startActivity(intent);
    }

    public void oninfo(View view) {
        Intent intent = new Intent(this,LlamadoReportesActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }
}
