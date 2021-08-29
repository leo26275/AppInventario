package com.app.appinventario.com.app.appinventario.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.activitys.EditProductActivity;
import com.app.appinventario.com.app.appinventario.entitys.Product;

import java.util.List;

public class AdapterProductImg extends RecyclerView.Adapter<AdapterProductImg.UsuariosHolder> {
    List<Product> listaProductos;
    List<Product> listaProductosFiltrar;
    RequestQueue request;
    Context context;
    Product product;

    public AdapterProductImg(List<Product> listaProductos, Context context){
        this.listaProductos = listaProductos;
        this.context = context;
        this.listaProductosFiltrar = listaProductos;
        request = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public UsuariosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom,parent,false);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new UsuariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosHolder holder, int position) {
        holder.code.setText(listaProductos.get(position).getCodigo());
        holder.nombre.setText(listaProductos.get(position).getNombre());
        holder.stock.setText(String.valueOf(listaProductos.get(position).getStock()));
        holder.category.setText(listaProductos.get(position).getNewcategoria());
        holder.provider.setText(listaProductos.get(position).getNewproveedor());
        holder.id.setText(String.valueOf(listaProductos.get(position).getId_producto()));

        product = new Product(listaProductos.get(position).getId_producto(),listaProductos.get(position).getEstado());
        holder.imagen2.setTag(product);

        if(listaProductos.get(position).getEstado()==1){
            holder.imagen2.setBackgroundResource(R.mipmap.ic_check2);
        }else{
            holder.imagen2.setBackgroundResource(R.mipmap.ic_nocheck);
        }
        if(listaProductos.get(position).getImagen()!=null){
            //holder.imagen.setImageBitmap(listaUsuarios.get(position).getImagen());
            cargarImagenWedService(listaProductos.get(position).getImagen(),holder);
        }else{
            holder.imagen.setImageResource(R.drawable.producto3);
        }

        holder.setOnClickListeners();
    }

    private void cargarImagenWedService(String rutaImagen,final UsuariosHolder holder) {
        String urlImagen = "http://192.168.0.6:80/AppInventory/"  +rutaImagen;
        //urlImagen = urlImagen.replace(" ", "%20");
        ImageRequest imageRequest = new ImageRequest(urlImagen, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                holder.imagen.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error al cargar la imagen" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        request.add(imageRequest);
    }

    @Override
    public int getItemCount() {
        return listaProductos.size();
    }



    static class UsuariosHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context context1;
        TextView code,nombre,stock,category,provider,estado,id;
        ImageView imagen,imagen2,editar;
        public UsuariosHolder(@NonNull View itemView) {
            super(itemView);

            context1 = itemView.getContext();

            code = itemView.findViewById(R.id.codigo);
            nombre = itemView.findViewById(R.id.nombre);
            stock = itemView.findViewById(R.id.stock);
            category = itemView.findViewById(R.id.categoria);
            provider = itemView.findViewById(R.id.proveedor);
            imagen = itemView.findViewById(R.id.imagen);
            imagen2 = itemView.findViewById(R.id.checkB);
            editar = itemView.findViewById(R.id.editar);
            id = itemView.findViewById(R.id.id);

        }


        void setOnClickListeners(){
            editar.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.editar:
                    Intent intent = new Intent(context1, EditProductActivity.class);
                    intent.putExtra("id", id.getText());
                    context1.startActivity(intent);
                    break;


            }
        }
    }
}
