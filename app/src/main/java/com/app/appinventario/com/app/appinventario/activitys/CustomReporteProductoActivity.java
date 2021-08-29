package com.app.appinventario.com.app.appinventario.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.adapters.AdapterReporteProducto;
import com.app.appinventario.com.app.appinventario.entitys.ReportePro;

import java.util.ArrayList;

public class CustomReporteProductoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_reporte_producto);
    }
}
