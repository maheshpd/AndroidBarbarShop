package com.arfeenkhan.androidbarbershop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arfeenkhan.androidbarbershop.Interface.IRecycleItemSelectedListener;
import com.arfeenkhan.androidbarbershop.R;
import com.arfeenkhan.androidbarbershop.model.Barber;
import com.arfeenkhan.androidbarbershop.model.EventBus.EnableNextButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyBarberAdapter extends RecyclerView.Adapter<MyBarberAdapter.MyBarberViewHolder> {

    Context context;
    List<Barber> barberLiat;
    List<CardView> cardViewList;
//    LocalBroadcastManager localBroadcastManager;

    public MyBarberAdapter(Context context, List<Barber> barberLiat) {
        this.context = context;
        this.barberLiat = barberLiat;
        cardViewList = new ArrayList<>();
//        localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyBarberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_barber,viewGroup,false);
        return new MyBarberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyBarberViewHolder myBarberViewHolder, int i) {
        myBarberViewHolder.txt_barber_name.setText(barberLiat.get(i).getName());
        if (barberLiat.get(i).getRatingTimes() != null)
            myBarberViewHolder.ratingBar.setRating(barberLiat.get(i).getRating().floatValue() / barberLiat.get(i).getRatingTimes());
        else
            myBarberViewHolder.ratingBar.setRating(0);
        if (!cardViewList.contains(myBarberViewHolder.card_barber))
            cardViewList.add(myBarberViewHolder.card_barber);

        myBarberViewHolder.setiRecycleItemSelectedListener((view, position) -> {
            //Set background for all item not choice
            for (CardView cardView: cardViewList) {
                cardView.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.white));
            }

            //Set background for choice
            myBarberViewHolder.card_barber.setCardBackgroundColor(context.getResources()
            .getColor(android.R.color.holo_orange_dark));

            //Send Local broadcast to enable button next
//            Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
//            intent.putExtra(Common.KEY_BARBER_SELECTED,barberLiat.get(position));
//            intent.putExtra(Common.KEY_SEMP,2);
//            localBroadcastManager.sendBroadcast(intent);

            //Eventbus
            EventBus.getDefault().postSticky(new EnableNextButton(2, barberLiat.get(i)));
        });
    }

    @Override
    public int getItemCount() {
        return barberLiat.size();
    }

    public class MyBarberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_barber_name;
        RatingBar ratingBar;
        CardView card_barber;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyBarberViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_barber_name = itemView.findViewById(R.id.txt_barber_name);
            ratingBar = itemView.findViewById(R.id.rtb_barber);
            card_barber = itemView.findViewById(R.id.card_barber);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            iRecycleItemSelectedListener.onItemSelectedListener(v,getAdapterPosition());
        }
    }

}
