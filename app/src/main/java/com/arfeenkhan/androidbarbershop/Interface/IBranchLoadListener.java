package com.arfeenkhan.androidbarbershop.Interface;

import com.arfeenkhan.androidbarbershop.Model.Salon;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> salonList);
    void onBranchLoadFailed(String message);
}
