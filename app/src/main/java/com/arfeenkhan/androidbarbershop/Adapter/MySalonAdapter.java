package com.arfeenkhan.androidbarbershop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arfeenkhan.androidbarbershop.Common.Common;
import com.arfeenkhan.androidbarbershop.Interface.IRecycleItemSelectedListener;
import com.arfeenkhan.androidbarbershop.Model.Salon;
import com.arfeenkhan.androidbarbershop.R;

import java.util.ArrayList;
import java.util.List;

public class MySalonAdapter extends RecyclerView.Adapter<MySalonAdapter.MySalonViewHolder> {
    Context context;
    List<Salon> salonList;
    List<CardView> cardViewList;
    LocalBroadcastManager localBroadcastManager;

    public MySalonAdapter(Context context, List<Salon> salonList) {
        this.context = context;
        this.salonList = salonList;
        cardViewList = new ArrayList<>();
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MySalonViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon,viewGroup,false);
        return new MySalonViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MySalonViewHolder mySalonViewHolder, int i) {
        mySalonViewHolder.txt_salon_name.setText(salonList.get(i).getName());
        mySalonViewHolder.txt_salon_address.setText(salonList.get(i).getAddress());
        if (!cardViewList.contains(mySalonViewHolder.card_salon))
            cardViewList.add(mySalonViewHolder.card_salon);

        mySalonViewHolder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int position) {
                //Set white background for all card not be selected
                for (CardView cardView:cardViewList)
                    cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));

                //Set selected BG for only selected item
                mySalonViewHolder.card_salon.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_dark));

                //Send Broadcast to tell Booking Activity enable Button next
                Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
                intent.putExtra(Common.KEY_SALON_STORE,salonList.get(position));
                localBroadcastManager.sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }

    public class MySalonViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_salon_name,txt_salon_address;
        CardView card_salon;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MySalonViewHolder(@NonNull View itemView) {
            super(itemView);
            card_salon = itemView.findViewById(R.id.card_salon);
            txt_salon_address = itemView.findViewById(R.id.txt_salon_address);
            txt_salon_name = itemView.findViewById(R.id.salon_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecycleItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }
}
