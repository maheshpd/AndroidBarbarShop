package com.arfeenkhan.androidbarbershop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arfeenkhan.androidbarbershop.Common.Common;
import com.arfeenkhan.androidbarbershop.Interface.IRecycleItemSelectedListener;
import com.arfeenkhan.androidbarbershop.R;
import com.arfeenkhan.androidbarbershop.model.EventBus.EnableNextButton;
import com.arfeenkhan.androidbarbershop.model.TimeSlot;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyTimeSlotViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;
//    LocalBroadcastManager localBroadcastManager;

    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
//        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
//        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyTimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_time_slot,parent,false);
        return new MyTimeSlotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTimeSlotViewHolder holder, int position) {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());
        if (timeSlotList.size() == 0) //If all position is available, just show list
        {
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));

        }else //If have position is full (booked)
        {
            for (TimeSlot slotValue: timeSlotList)
            {
                //Loop all time slot from server and set different color
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if (slot == position) //If slot == position
                {
                    //We will set tag for all time slot is full
                    //So base on tag, we set all remain card background without change full time slot
                    holder.card_time_slot.setTag(Common.DISABLE_TAG);
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_bright));
                    holder.txt_time_slot_description.setText("Full");
                    holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.white));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));

                }
            }
        }

        //Add all card to list (20 card because we have 20 time slot)
        //No add card already in cardViewList
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);

        //Check if card time slot is available

            holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
                @Override
                public void onItemSelectedListener(View view, int position) {
                    //Loop all card in card List
                    for (CardView cardView:cardViewList) {
                        if (cardView.getTag() == null) //Only available card time slot be change
                            cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                    }
                    //Our selected card will be change color
                    holder.card_time_slot.setCardBackgroundColor(context.getResources()
                            .getColor(android.R.color.holo_orange_dark));

                    //After that , send broadcast to enable button NEXt
//                    Intent intent = new Intent(Common.KEY_ENABLE_BUTTON_NEXT);
//                    intent.putExtra(Common.KEY_TIME_SLOT,position); //Put index of time slot we have selected
//                    intent.putExtra(Common.KEY_SEMP,3); //Go to step 3
//                    localBroadcastManager.sendBroadcast(intent);

                    EventBus.getDefault().postSticky(new EnableNextButton(3, position));
                }
            });
        }


    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyTimeSlotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot,txt_time_slot_description;
        CardView card_time_slot;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyTimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = itemView.findViewById(R.id.txt_time_slot_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            iRecycleItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
