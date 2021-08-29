package com.app.appinventario.com.app.appinventario.adapters;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterCategory extends ArrayAdapter<Category> implements Filterable {
    Context context;
    List<Category> arrayCategory;
    List<Category> arrayCategoryFilter;

    public AdapterCategory(Context context, List<Category> arrayCategory){
        super(context, R.layout.activity_custom_category , arrayCategory);
        this.context = context;
        this.arrayCategory =  arrayCategory;
        this.arrayCategoryFilter = arrayCategory;
    }

    @Override
    public int getCount() {
        return arrayCategoryFilter.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Category category = getItem(position);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_category, null, true);
        TextView tvname = view.findViewById(R.id.name);
        ImageView checkB = view.findViewById(R.id.checkB);
        ImageView editar = view.findViewById(R.id.editar);

        editar.setTag(category);
        checkB.setTag(category);

        //tvname.setText(category.getNombre());
        tvname.setText(arrayCategoryFilter.get(position).getNombre());
        if(category.getEstado()==1){
            checkB.setBackgroundResource(R.mipmap.ic_check2);
        }else if(category.getEstado()==0){
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
                    filterResults.count = arrayCategory.size();
                    filterResults.values = arrayCategory;
                }else {
                    String searchStr = charSequence.toString();
                    List<Category> resulData = new ArrayList<>();
                    for (Category category: arrayCategory){
                        if(category.getNombre().contains(searchStr)){
                            resulData.add(category);
                        }

                        filterResults.count =resulData.size();
                        filterResults.values = resulData;
                    }
                }

                return filterResults ;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayCategoryFilter = (List<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
