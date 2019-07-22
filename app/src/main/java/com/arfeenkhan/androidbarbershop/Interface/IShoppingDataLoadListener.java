package com.arfeenkhan.androidbarbershop.Interface;

import com.arfeenkhan.androidbarbershop.model.ShoppingItem;

import java.util.List;

public interface IShoppingDataLoadListener {

    void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList);
    void onShoppingDataLoadFailed(String message);

}
