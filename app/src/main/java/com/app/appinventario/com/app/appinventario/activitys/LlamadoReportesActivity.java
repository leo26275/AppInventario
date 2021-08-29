package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.app.appinventario.R;

public class LlamadoReportesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llamado_reportes);
    }

    public void onVentas(View view) {
        Intent intent = new Intent(this, ReporteVentaActivity.class);
        startActivity(intent);
    }

    public void onProducto(View view) {
        Intent intent = new Intent(this, ReporteProductoActivity.class);
        startActivity(intent);
    }

    public void onKardex(View view) {
        Intent intent = new Intent(this, BuscaKardex.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    public void onCosto(View view) {
        Intent intent = new Intent(this, ReporteCostoProductoActivity.class);
        startActivity(intent);
    }
}
