package com.app.appinventario.com.app.appinventario.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.activitys.Comprar_productos;
import com.app.appinventario.com.app.appinventario.entitys.Compras;
import com.app.appinventario.com.app.appinventario.entitys.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class AdapterCompras extends RecyclerView.Adapter<AdapterCompras.ViewHolderDatos> implements View.OnClickListener, Filterable {


    private final ArrayList<Compras> listDa;
    private final ArrayList<Compras> listDaAll;
    // --Commented out by Inspection (27/06/2020 12:04 AM):private String positiv;
    private View.OnClickListener listener;
    public AdapterCompras(ArrayList<Compras> date) {
        this.listDa=date;
        listDaAll= new ArrayList<>(listDa);
    }


    @NonNull
    @Override
    public AdapterCompras.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vie= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_compras,parent,false);
        vie.setOnClickListener(this);
        return new AdapterCompras.ViewHolderDatos(vie);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull AdapterCompras.ViewHolderDatos holder, final int position) {


        holder.name.setText(listDa.get(position).getName());
        holder.pcompra.setText(listDa.get(position).getPreciocompra());
        holder.pventa.setText(listDa.get(position).getPrecioventa());
        holder.cantidad.setText(listDa.get(position).getCantidad());

        String url_im="http://192.168.0.6:80/AppInventory/" + listDa.get(position).getImagen();
        Picasso.with(holder.itemView.getContext()).load(url_im).error(R.drawable.producto3).fit().centerInside().into(holder.imagen);




    }

    @Override
    public int getItemCount() {

        return  listDa.size();

    }

    public void setOnClickListener(View.OnClickListener listener){

        this.listener=listener;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Compras> filteredlist = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredlist.addAll(listDaAll);
            } else {
                for (Compras fetch : listDaAll) {
                    if (fetch.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredlist.add(fetch);
                    }
                }
            }

            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredlist;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listDa.clear();
            listDa.addAll((Collection<? extends Compras>) results.values);

            notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        if(listener!=null){
            listener.onClick(v);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {
        final TextView name, pcompra, pventa, cantidad;
        final ImageView imagen;

        ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nombre_compra);
            pcompra = itemView.findViewById(R.id.precio_comp);
            pventa = itemView.findViewById(R.id.precio_vent);
            cantidad = itemView.findViewById(R.id.cant_comp);

            imagen = itemView.findViewById(R.id.imagen_compra);

        }


    }
}
