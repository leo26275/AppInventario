package com.app.appinventario.com.app.appinventario.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.ReportePro;
import com.app.appinventario.com.app.appinventario.entitys.VolleySingleton;

import java.util.List;

public class AdapterReporteProducto extends ArrayAdapter<ReportePro> {
    Context context;
    List<ReportePro> arrayPro;
    Bitmap bitmap;

    public AdapterReporteProducto(Context context, List<ReportePro> arrayPro) {
        super(context, R.layout.activity_custom_reporte_producto, arrayPro);
        this.context = context;
        this.arrayPro = arrayPro;
    }


    @Override
    public int getCount() {
        return arrayPro.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReportePro repor = getItem(position);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_reporte_producto, null, true);
        TextView codigo = view.findViewById(R.id.codigo);
        TextView nombre = view.findViewById(R.id.nombre);
        TextView precio_compre = view.findViewById(R.id.precio_compra);
        TextView coste_pro = view.findViewById(R.id.coste_pro);
        TextView ganancia = view.findViewById(R.id.ganancia);
        ImageView img = view.findViewById(R.id.imagen);

        codigo.setText(repor.getCodigo());
        nombre.setText(repor.getNombre());
        precio_compre.setText(String.valueOf(repor.getPrecio_compra()));
        coste_pro.setText(String.valueOf(repor.getCoste_pro()));
        ganancia.setText(String.valueOf(repor.getGanancia()));


        String urlImagen="http://192.168.0.6:80/AppInventory/" + repor.getImagen().toString();
        cargarWebServiceImagen(urlImagen, img);

        return view;
    }

    private void cargarWebServiceImagen(String urlImagen, final ImageView img) {
        urlImagen=urlImagen.replace(" ","%20");

        ImageRequest imageRequest=new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                bitmap=response;//SE MODIFICA
                img.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error al cargar la imagen",Toast.LENGTH_SHORT).show();
                Log.i("ERROR IMAGEN","Response -> "+error);
            }
        });
        //  request.add(imageRequest);
        VolleySingleton.getIntanciaVolley(context).addToRequestQueue(imageRequest);
    }
}
