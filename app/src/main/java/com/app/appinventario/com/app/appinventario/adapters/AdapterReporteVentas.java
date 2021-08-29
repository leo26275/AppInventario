package com.app.appinventario.com.app.appinventario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.Client;
import com.app.appinventario.com.app.appinventario.entitys.Venta;

import java.util.List;

public class AdapterReporteVentas extends ArrayAdapter<Venta> {
    Context context;
    List<Venta> arrayVenta;
    public AdapterReporteVentas(Context context, List<Venta> arrayVenta){
        super(context, R.layout.activity_custom_reporte_vent_a , arrayVenta);
        this.context = context;
        this.arrayVenta =  arrayVenta;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Venta venta = getItem(position);



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_reporte_vent_a, null, true);
        TextView numero = view.findViewById(R.id.numero);
        TextView nombre = view.findViewById(R.id.nombre);
        TextView fecha = view.findViewById(R.id.fecha);
        ImageView detalles = view.findViewById(R.id.detalle);

        detalles.setTag(venta);



        numero.setText(String.valueOf(venta.getId_numero_venta()));
        nombre.setText(String.valueOf(venta.getId_cliente()));
        fecha.setText(String.valueOf(venta.getFecha()));

        return view;
    }
}
