package com.arfeenkhan.androidbarbershop.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.arfeenkhan.androidbarbershop.Common.SpacesItemDecoration;
import com.arfeenkhan.androidbarbershop.Interface.IShoppingDataLoadListener;
import com.arfeenkhan.androidbarbershop.R;
import com.arfeenkhan.androidbarbershop.adapter.MyShoppingAdapter;
import com.arfeenkhan.androidbarbershop.model.ShoppingItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopingFragment extends Fragment implements IShoppingDataLoadListener {


    CollectionReference shoppingItemRef;
    IShoppingDataLoadListener iShoppingDataLoadListener;


    Unbinder unbinder;
    @BindView(R.id.chip_group)
    ChipGroup chipGroup;
    @BindView(R.id.chip_wax)
    Chip chip_wax;

    @OnClick(R.id.chip_wax)
    void waxChipClick() {
        setSelectedChip(chip_wax);
        loadShoppingItem("Wax");
    }

    @BindView(R.id.chip_spray)
    Chip chip_spray;
    @OnClick(R.id.chip_spray)
    void sprayhipClick() {
        setSelectedChip(chip_spray );
        loadShoppingItem("Spray");
    }

    @BindView(R.id.recycler_items)
    RecyclerView recycler_items;

    private void loadShoppingItem(String itemMenu) {
        shoppingItemRef = FirebaseFirestore.getInstance().collection("Shopping")
                .document(itemMenu)
                .collection("Item");
        //Get Data
        shoppingItemRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iShoppingDataLoadListener.onShoppingDataLoadFailed(e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<ShoppingItem> shoppingItems = new ArrayList<>();
                for (DocumentSnapshot itemSnapShot : task.getResult()) {
                    ShoppingItem shoppingItem = itemSnapShot.toObject(ShoppingItem.class);
                    shoppingItem.setId(itemSnapShot.getId()); //Remember add it if you don't want toget null reference
                    shoppingItems.add(shoppingItem);
                }
                iShoppingDataLoadListener.onShoppingDataLoadSuccess(shoppingItems);
            }
        });
    }

    private void setSelectedChip(Chip chip) {
        //Set color
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            Chip chipItem = (Chip) chipGroup.getChildAt(i);
            if (chipItem.getId() != chip.getId())  //If not selected
            {
                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            } else  //If selected
            {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
            }
        }
    }


    public ShopingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_shoping, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        iShoppingDataLoadListener = this;

        initView();

        //Default load
        loadShoppingItem("Wax");

        return itemView;
    }

    private void initView() {
        recycler_items.setHasFixedSize(true);
        recycler_items.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recycler_items.addItemDecoration(new SpacesItemDecoration(8));
    }

    @Override
    public void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList) {
        MyShoppingAdapter adapter = new MyShoppingAdapter(getContext(), shoppingItemList);
        recycler_items.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onShoppingDataLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
