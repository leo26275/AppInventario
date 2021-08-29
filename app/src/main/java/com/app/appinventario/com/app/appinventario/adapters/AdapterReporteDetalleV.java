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
import com.app.appinventario.com.app.appinventario.entitys.DetalleVenta;
import com.app.appinventario.com.app.appinventario.entitys.VolleySingleton;

import java.util.List;

public class AdapterReporteDetalleV extends ArrayAdapter<DetalleVenta> {
    Context context;
    List<DetalleVenta> arrayDetalle;
    Bitmap bitmap;

    public AdapterReporteDetalleV(Context context, List<DetalleVenta> arrayDetalle) {
        super(context, R.layout.activity_custom_client, arrayDetalle);
        this.context = context;
        this.arrayDetalle = arrayDetalle;
    }


    @Override
    public int getCount() {
        return arrayDetalle.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DetalleVenta detalleVenta = getItem(position);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_reporte_detalle_v, null, true);
        TextView codigo = view.findViewById(R.id.codigo);
        TextView nombre = view.findViewById(R.id.nombre);
        TextView cantidad = view.findViewById(R.id.cantidad);
        TextView precio_venta = view.findViewById(R.id.precio);
        TextView total = view.findViewById(R.id.total);
        ImageView img = view.findViewById(R.id.img);

        codigo.setText(detalleVenta.getCodigo());
        nombre.setText(detalleVenta.getNombre());
        cantidad.setText(String.valueOf(detalleVenta.getCantdad()));
        precio_venta.setText(String.valueOf(detalleVenta.getPrecio_venta()));
        total.setText(String.valueOf(detalleVenta.getTotal()));


        String urlImagen="http://192.168.0.6:80/AppInventory/" + detalleVenta.getImagen().toString();
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

