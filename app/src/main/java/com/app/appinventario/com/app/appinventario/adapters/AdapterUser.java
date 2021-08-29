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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.appinventario.R;
import com.app.appinventario.com.app.appinventario.entitys.Category;
import com.app.appinventario.com.app.appinventario.entitys.Provider;
import com.app.appinventario.com.app.appinventario.entitys.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdapterUser extends ArrayAdapter<User> {
    Context context;
    List<User> arrayUser;
    List<User> arrayUserFilter;
    public AdapterUser(Context context, List<User> arrayUser){
        super(context, R.layout.activity_custom_user , arrayUser);
        this.context = context;
        this.arrayUser =  arrayUser;
        this.arrayUserFilter = arrayUser;
    }

    @Override
    public int getCount() {
        return arrayUserFilter.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final User user = getItem(position);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_custom_user, null, true);
        TextView tvuser = view.findViewById(R.id.user);
        TextView tvcorreo = view.findViewById(R.id.email);
        ImageView checkB = view.findViewById(R.id.checkB);
        ImageView editar = view.findViewById(R.id.editar);
        ImageView mail = view.findViewById(R.id.mail);

        editar.setTag(user);
        checkB.setTag(user);
        mail.setTag(user);

        tvuser.setText(arrayUserFilter.get(position).getUsuario());

       /* tvuser.setText(user.getUsuario());*/
        tvcorreo.setText(user.getCorreo());

        if(user.getEstado()==1){
            checkB.setBackgroundResource(R.mipmap.ic_check2);
        }else if(user.getEstado()==0){
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
                    filterResults.count = arrayUser.size();
                    filterResults.values = arrayUser;
                }else {
                    String searchStr = charSequence.toString();
                    List<User> resulData = new ArrayList<>();
                    for (User user: arrayUser){
                        if(user.getUsuario().contains(searchStr)){
                            resulData.add(user);
                        }

                        filterResults.count =resulData.size();
                        filterResults.values = resulData;
                    }
                }

                return filterResults ;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arrayUserFilter = (List<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

}
