package com.app.appinventario.com.app.appinventario.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.app.appinventario.com.app.appinventario.entitys.Compras;
import com.app.appinventario.com.app.appinventario.entitys.Product;
import com.app.appinventario.com.app.appinventario.entitys.Ventas;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AdapterVentas extends RecyclerView.Adapter<AdapterVentas.ViewHolderDatos> implements View.OnClickListener, Filterable {


    private final ArrayList<Ventas> listDa;
    private final ArrayList<Ventas> listDaAll;
    private View.OnClickListener listener;

    public AdapterVentas(ArrayList<Ventas> date) {
        this.listDa=date;
        listDaAll= new ArrayList<>(listDa);
    }


    @NonNull
    @Override
    public AdapterVentas.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vie= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_ventas,parent,false);
        vie.setOnClickListener(this);
        return new AdapterVentas.ViewHolderDatos(vie);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull AdapterVentas.ViewHolderDatos holder, final int position) {


        holder.name.setText(listDa.get(position).getName());
        holder.ptotak.setText(listDa.get(position).getPreciototal());
        holder.punitario.setText(listDa.get(position).getPreciounitarip());
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
            ArrayList<Ventas> filteredlist = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredlist.addAll(listDaAll);
            } else {
                for (Ventas fetch : listDaAll) {
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
            listDa.addAll((Collection<? extends Ventas>) results.values);

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
        final TextView name, punitario, ptotak, cantidad;
        final ImageView imagen;

        ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nombre_compra);
            ptotak = itemView.findViewById(R.id.precio_comp);
            punitario = itemView.findViewById(R.id.precio_vent);
            cantidad = itemView.findViewById(R.id.cant_comp);

            imagen = itemView.findViewById(R.id.imagen_compra);

        }


    }
}