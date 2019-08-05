package com.arfeenkhan.androidbarbershop.Database;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;

import com.arfeenkhan.androidbarbershop.Common.Common;
import com.arfeenkhan.androidbarbershop.Interface.ICartItemLoadListener;
import com.arfeenkhan.androidbarbershop.Interface.ICountiteminCartListener;
import com.arfeenkhan.androidbarbershop.Interface.ISumCartListener;

import java.util.List;

public class DatabaseUtils {
    //Because all room handle need work on other thread

    public static void sumCart(CartDatabase db, ISumCartListener iSumCartListener) {
        SumCartAsync task = new SumCartAsync(db, iSumCartListener);
        task.execute();
    }


    public static void getAllCart(CartDatabase db, ICartItemLoadListener cartItemLoadListener) {
        GetAllCartAsync task = new GetAllCartAsync(db, cartItemLoadListener);
        task.execute();
    }

    public static void updateCart(CartDatabase db, CartItem cartItem) {
        UpdateCartAsync task = new UpdateCartAsync(db);
        task.execute(cartItem);
    }

    public static void insertToCart(CartDatabase db, CartItem... cartItems) {
        InsertToCartAsync task = new InsertToCartAsync(db);
        task.execute(cartItems);
    }

    public static void countItemInCart(CartDatabase db, ICountiteminCartListener iCountiteminCartListener) {
        CountItemInCartAsync task = new CountItemInCartAsync(db, iCountiteminCartListener);
        task.execute();
    }

    /*
    ==================================================================
    ASYNC TASK DEFINE
    ==================================================================
     */


    private static class SumCartAsync extends AsyncTask<CartItem, Void, Long> {

        private final CartDatabase db;
        private final ISumCartListener listener;

        public SumCartAsync(CartDatabase db, ISumCartListener listener) {
            this.db = db;
            this.listener = listener;
        }

        @Override
        protected Long doInBackground(CartItem... cartItems) {
            return db.cartDAO().sumPrice(Common.currentUser.getPhoneNumber());
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            listener.onSumCartSuccess(aLong);
        }
    }

    private static class UpdateCartAsync extends AsyncTask<CartItem, Void, Void> {

        private final CartDatabase db;

        public UpdateCartAsync(CartDatabase db) {
            this.db = db;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            db.cartDAO().update(cartItems[0]);
            return null;
        }
    }

    private static class GetAllCartAsync extends AsyncTask<String, Void, List<CartItem>> {

        CartDatabase db;
        ICartItemLoadListener listener;

        public GetAllCartAsync(CartDatabase cartDatabase, ICartItemLoadListener iCartItemLoadListener) {
            db = cartDatabase;
            listener = iCartItemLoadListener;
        }


        @Override
        protected List<CartItem> doInBackground(String... strings) {
            return db.cartDAO().getAllItemFromCart(Common.currentUser.getPhoneNumber());
        }

        @Override
        protected void onPostExecute(List<CartItem> cartItemList) {
            super.onPostExecute(cartItemList);
            listener.onGetAllItemFromCartSuccess(cartItemList);
        }
    }

    private static class InsertToCartAsync extends AsyncTask<CartItem, Void, Void> {

        CartDatabase db;

        public InsertToCartAsync(CartDatabase cartDatabase) {
            db = cartDatabase;
        }


        @Override
        protected Void doInBackground(CartItem... cartItems) {
            inserToCart(db, cartItems[0]);
            return null;
        }

        private void inserToCart(CartDatabase db, CartItem cartItem) {
            //If item already available in car, just increase quantity
            try {
                db.cartDAO().insert(cartItem);
            } catch (SQLiteConstraintException exception) {
                CartItem updateCartItem = db.cartDAO().getProductInCart(cartItem.getProductId(),
                        Common.currentUser.getPhoneNumber());
                updateCartItem.setProductQuantity(updateCartItem.getProductQuantity() + 1);
                db.cartDAO().update(updateCartItem);
            }
        }


    }

    private static class CountItemInCartAsync extends AsyncTask<Void, Void, Integer> {

        CartDatabase db;
        ICountiteminCartListener listener;

        public CountItemInCartAsync(CartDatabase cartDatabase, ICountiteminCartListener iCountiteminCartListener) {
            db = cartDatabase;
            listener = iCountiteminCartListener;
        }


        @Override
        protected Integer doInBackground(Void... voids) {
            return Integer.parseInt(String.valueOf(countItemInCartRun(db)));
        }

        private int countItemInCartRun(CartDatabase db) {
            return db.cartDAO().countItemInCart(Common.currentUser.getPhoneNumber());
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            listener.onCartItemCountSuccess(integer.intValue());
        }
    }
}
