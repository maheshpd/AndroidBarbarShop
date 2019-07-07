package com.arfeenkhan.androidbarbershop.Interface;

import com.arfeenkhan.androidbarbershop.activity.BookingActivity;
import com.arfeenkhan.androidbarbershop.model.BookingInformation;

public interface IBookingInfoLoadListener {
    void onBookingInfoLoadEmpty();
    void onBookingInfoLoadSuccess(BookingInformation bookingInformation);
    void onBookingInfoLoadFailed(String message);
}
