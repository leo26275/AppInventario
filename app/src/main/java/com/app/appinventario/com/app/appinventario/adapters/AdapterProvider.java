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
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import java.util.ArrayList;
import java.util.List;


public class AdapterProvider extends ArrayAdapter<Provider> {

   Context context;
   List<Provider> arrayProvider;
   List<Provider> arrayProviderFilter;
    public AdapterProvider(Context context, List<Provider> arrayProvider){
        super(context, R.layout.activity_custom_provider , arrayProvider);
        this.context = context;
        this.arrayProvider =  arrayProvider;
        this.arrayProviderFilter = arrayProvider;
    }

    @Override
    public int getCount() {
        return arrayProviderFilter.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Provider provider = getItem(position);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_provider, null, true);
        TextView tvnombre = view.findViewById(R.id.nombre);
        TextView tvtelefono = view.findViewById(R.id.telefono);
        TextView tvcorreo = view.findViewById(R.id.email);
        TextView tvdireccion = view.findViewById(R.id.password);
        ImageView checkB = view.findViewById(R.id.checkB);
        ImageView editar = view.findViewById(R.id.editar);
        ImageView call = view.findViewById(R.id.call);
        ImageView mail = view.findViewById(R.id.mail);

        editar.setTag(provider);
        call.setTag(provider);
        checkB.setTag(provider);
        mail.setTag(provider);

        tvnombre.setText(provider.getNombre());
        tvtelefono.setText(provider.getTelefono());
        tvcorreo.setText(provider.getCorreo());
        tvdireccion.setText(provider.getDireccion());

        tvnombre.setText(arrayProviderFilter.get(position).getNombre());
        if(provider.getEstado()==1){
            checkB.setBackgroundResource(R.mipmap.ic_check2);
        }else if(provider.getEstado()==0){
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
                    filterResults.count = arrayProvider.size();
                    filterResults.values = arrayProvider;
                }else {
                    String searchStr = charSequence.toString();
                    List<Provider> resulData = new ArrayList<>();
                    for (Provider provider: arrayProvider){
                        if(provider.getNombre().contains(searchStr)){
                            resulData.add(provider);
                        }

                        filterResults.count =resulData.size();
                        filterResults.values = resulData;
                    }
                }

                return filterResults ;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayProviderFilter = (List<Provider>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
