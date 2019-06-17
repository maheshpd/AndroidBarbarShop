package com.arfeenkhan.androidbarbershop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.arfeenkhan.androidbarbershop.model.Barber;
import com.arfeenkhan.androidbarbershop.R;

import java.util.List;

public class MyBarberAdapter extends RecyclerView.Adapter<MyBarberAdapter.MyBarberViewHolder> {

    Context context;
    List<Barber> barberLiat;

    public MyBarberAdapter(Context context, List<Barber> barberLiat) {
        this.context = context;
        this.barberLiat = barberLiat;
    }

    @NonNull
    @Override
    public MyBarberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_barber,viewGroup,false);
        return new MyBarberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBarberViewHolder myBarberViewHolder, int i) {
        myBarberViewHolder.txt_barber_name.setText(barberLiat.get(i).getName());
        myBarberViewHolder.ratingBar.setRating((float)barberLiat.get(i).getRating());
    }

    @Override
    public int getItemCount() {
        return barberLiat.size();
    }

    public class MyBarberViewHolder extends RecyclerView.ViewHolder {
        TextView txt_barber_name;
        RatingBar ratingBar;
        public MyBarberViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_barber_name = itemView.findViewById(R.id.txt_barber_name);
            ratingBar = itemView.findViewById(R.id.rtb_barber);
        }
    }

}
