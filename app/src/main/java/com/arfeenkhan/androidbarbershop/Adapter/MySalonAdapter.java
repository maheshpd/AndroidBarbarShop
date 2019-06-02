package com.arfeenkhan.androidbarbershop.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arfeenkhan.androidbarbershop.Model.Salon;
import com.arfeenkhan.androidbarbershop.R;

import java.util.List;

public class MySalonAdapter extends RecyclerView.Adapter<MySalonAdapter.MySalonViewHolder> {
    Context context;
    List<Salon> salonList;

    public MySalonAdapter(Context context, List<Salon> salonList) {
        this.context = context;
        this.salonList = salonList;
    }

    @NonNull
    @Override
    public MySalonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon,viewGroup,false);
        return new MySalonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MySalonViewHolder mySalonViewHolder, int i) {
        mySalonViewHolder.txt_salon_name.setText(salonList.get(i).getName());
        mySalonViewHolder.txt_salon_address.setText(salonList.get(i).getAddress());
    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }

    public class MySalonViewHolder extends RecyclerView.ViewHolder {
        TextView txt_salon_name,txt_salon_address;
        public MySalonViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_salon_address = itemView.findViewById(R.id.txt_salon_address);
            txt_salon_name = itemView.findViewById(R.id.txt_salon_name);
        }
    }
}
