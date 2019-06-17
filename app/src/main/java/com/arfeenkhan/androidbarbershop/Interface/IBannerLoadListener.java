package com.arfeenkhan.androidbarbershop.Interface;

import com.arfeenkhan.androidbarbershop.model.Banner;

import java.util.List;

public interface IBannerLoadListener {
    void onBannerLoadSuccess(List<Banner> banners);
    void onBannerLoadFailed(String message);
}
