package com.arfeenkhan.androidbarbershop.Database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cart")
public class CartItem {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "productId")
    private  String productId;

    @ColumnInfo(name = "productName")
    private String productName;

    @ColumnInfo(name = "productImage")
    private String productImage;

    @ColumnInfo(name = "productPrice")
    private Long productPrice;

    @ColumnInfo(name = "productQuantity")
    private Long productQuantity;

    @ColumnInfo(name = "userPhone")
    private Long userPhone;

    //Getter & Setter


    @NonNull
    public String getProductId() {
        return productId;
    }

    public void setProductId(@NonNull String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Long productPrice) {
        this.productPrice = productPrice;
    }

    public Long getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Long productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(Long userPhone) {
        this.userPhone = userPhone;
    }

}
