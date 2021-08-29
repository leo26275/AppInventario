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
import com.app.appinventario.com.app.appinventario.entitys.Product;

import java.util.ArrayList;
import java.util.List;

public class AdapterProduct extends ArrayAdapter<Product> {
    Context context;
    List<Product> arrayProduct;
    List<Product> arrayProductFilter;
    public AdapterProduct(Context context, List<Product> arrayProduct){
        super(context, R.layout.activity_custom_product , arrayProduct);
        this.context = context;
        this.arrayProduct =  arrayProduct;
        this.arrayProductFilter = arrayProduct;
    }

    @Override
    public int getCount() {
        return arrayProductFilter.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Product product = getItem(position);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_product, null, true);
        TextView code = view.findViewById(R.id.code);
        TextView name = view.findViewById(R.id.nombre);
        TextView stock = view.findViewById(R.id.stock);
        TextView category = view.findViewById(R.id.category);
        TextView provider = view.findViewById(R.id.provider);
        ImageView checkB = view.findViewById(R.id.checkB);
        ImageView editar = view.findViewById(R.id.editar);

        editar.setTag(product);
        checkB.setTag(product);

        code.setText(product.getCodigo());
        name.setText(product.getNombre());
        stock.setText(String.valueOf(product.getStock()));
        category.setText(String.valueOf(product.getId_categoria()));
        provider.setText(String.valueOf(product.getId_proveedor()));

        name.setText(arrayProduct.get(position).getNombre());
        if(product.getEstado()==1){
            checkB.setBackgroundResource(R.mipmap.ic_check2);
        }else if(product.getEstado()==0){
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
                    filterResults.count = arrayProduct.size();
                    filterResults.values = arrayProduct;
                }else {
                    String searchStr = charSequence.toString();
                    List<Product> resulData = new ArrayList<>();
                    for (Product product: arrayProduct){
                        if(product.getNombre().contains(searchStr)){
                            resulData.add(product);
                        }

                        filterResults.count =resulData.size();
                        filterResults.values = resulData;
                    }
                }

                return filterResults ;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayProductFilter = (List<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
