package com.app.appinventario.com.app.appinventario.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.app.appinventario.com.app.appinventario.entitys.Client;

import java.util.ArrayList;
import java.util.List;

public class AdapterClient extends ArrayAdapter<Client> {
    Context context;
    List<Client> arrayClient;
    List<Client> arrayClientFilter;

    public AdapterClient(Context context, List<Client> arrayClient){
        super(context, R.layout.activity_custom_client , arrayClient);
        this.context = context;
        this.arrayClient =  arrayClient;
        this.arrayClientFilter = arrayClient;
    }


    @Override
    public int getCount() {
        return arrayClientFilter.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Client client = getItem(position);



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_client, null, true);
        TextView tvnombre = view.findViewById(R.id.nombre);
        TextView tvdireccion = view.findViewById(R.id.direccion);
        TextView tvtelefono = view.findViewById(R.id.telefono);
        ImageView checkB = view.findViewById(R.id.checkB);
        ImageView editar = view.findViewById(R.id.editar);
        ImageView onCall = view.findViewById(R.id.call);

        editar.setTag(client);
        checkB.setTag(client);
        onCall.setTag(client);

        tvnombre.setText(arrayClientFilter.get(position).getNombre());
        tvtelefono.setText(client.getTelefono());
        tvdireccion.setText(client.getDireccion());

        if(client.getEstado()==1){
            checkB.setBackgroundResource(R.mipmap.ic_check2);
        }else{
            checkB.setBackgroundResource(R.mipmap.ic_nocheck);
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();

                if (charSequence == null || charSequence.length() == 0){
                    filterResults.count = arrayClient.size();
                    filterResults.values = arrayClient;
                }else {
                    String searchStr = charSequence.toString();
                    List<Client> resulData = new ArrayList<>();
                    for (Client client: arrayClient){
                        if(client.getNombre().contains(searchStr)){
                            resulData.add(client);
                        }

                        filterResults.count =resulData.size();
                        filterResults.values = resulData;
                    }
                }

                return filterResults ;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayClientFilter = (List<Client>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}