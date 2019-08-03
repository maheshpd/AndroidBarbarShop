package com.arfeenkhan.androidbarbershop.Interface;

import com.arfeenkhan.androidbarbershop.Database.CartItem;

import java.util.List;

public interface ICartItemLoadListener {
    void onGetAllItemFromCartSuccess(List<CartItem> cartItemList);
}
