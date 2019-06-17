package com.arfeenkhan.androidbarbershop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arfeenkhan.androidbarbershop.model.Banner;
import com.arfeenkhan.androidbarbershop.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class LookbookAdapter extends RecyclerView.Adapter<LookbookAdapter.MyLookBookViewHolder> {
    List<Banner> lookbook;
    Context context;

    public LookbookAdapter(List<Banner> lookbook, Context context) {
        this.lookbook = lookbook;
        this.context = context;
    }

    @NonNull
    @Override
    public MyLookBookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_look_layout,viewGroup,false);
        return new MyLookBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLookBookViewHolder myLookBookViewHolder, int i) {
        Picasso.get().load(lookbook.get(i).getImage()).into(myLookBookViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return lookbook.size();
    }

    public class MyLookBookViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public MyLookBookViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_look_book);
        }
    }
}
