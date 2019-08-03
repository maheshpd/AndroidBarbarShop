package com.arfeenkhan.androidbarbershop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.arfeenkhan.androidbarbershop.Common.Common;
import com.arfeenkhan.androidbarbershop.Database.CartDatabase;
import com.arfeenkhan.androidbarbershop.Database.CartItem;
import com.arfeenkhan.androidbarbershop.Database.DatabaseUtils;
import com.arfeenkhan.androidbarbershop.Interface.IRecycleItemSelectedListener;
import com.arfeenkhan.androidbarbershop.R;
import com.arfeenkhan.androidbarbershop.model.ShoppingItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyShoppingAdapter extends RecyclerView.Adapter<MyShoppingAdapter.MyViewHolder> {

    Context context;
    List<ShoppingItem> shoppingItemList;
    CartDatabase cartDatabase;

    public MyShoppingAdapter(Context context, List<ShoppingItem> shoppingItemList) {
        this.context = context;
        this.shoppingItemList = shoppingItemList;
        cartDatabase = CartDatabase.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shopping_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(shoppingItemList.get(position).getImage()).into(holder.img_shopping_item);
        holder.txt_shopping_item_name.setText(Common.formatShoppingItemName(shoppingItemList.get(position).getName()));
        holder.txt_shopping_item_prince.setText(new StringBuilder("$").append(shoppingItemList.get(position).getPrice()));


        //Add To Cart
        holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelectedListener(View view, int position) {
                //Create cart Item
                CartItem cartItem = new CartItem();
                cartItem.setProductId(shoppingItemList.get(position).getId());
                cartItem.setProductName(shoppingItemList.get(position).getName());
                cartItem.setProductImage(shoppingItemList.get(position).getImage());
                cartItem.setProductQuantity(1);
                cartItem.setProductPrice(shoppingItemList.get(position).getPrice());
                cartItem.setUserPhone(Common.currentUser.getPhoneNumber());

                //Insert to db
                DatabaseUtils.insertToCart(cartDatabase,cartItem);
                Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_shopping_item_name,txt_shopping_item_prince,txt_add_to_cart;
        ImageView img_shopping_item;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_shopping_item = itemView.findViewById(R.id.img_shopping_item);
            txt_shopping_item_name = itemView.findViewById(R.id.txt_name_shopping_item);
            txt_shopping_item_prince = itemView.findViewById(R.id.txt_price_shopping_item);
            txt_add_to_cart = itemView.findViewById(R.id.txt_salon_address);

            txt_add_to_cart.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
                iRecycleItemSelectedListener.onItemSelectedListener(view,getAdapterPosition());
        }
    }
}
