package com.arfeenkhan.androidbarbershop.Interface;

import com.arfeenkhan.androidbarbershop.model.Banner;

import java.util.List;

public interface ILookbookLoadListener {
    void onLookbookLoadSuccess(List<Banner> banners);
    void onLookbookLoadFailed(String message);
}
