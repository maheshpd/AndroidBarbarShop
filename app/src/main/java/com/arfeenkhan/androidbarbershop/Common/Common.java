package com.arfeenkhan.androidbarbershop.Common;

import com.arfeenkhan.androidbarbershop.model.Barber;
import com.arfeenkhan.androidbarbershop.model.Salon;
import com.arfeenkhan.androidbarbershop.model.TimeSlot;
import com.arfeenkhan.androidbarbershop.model.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_SALON_STORE = "SALON_SAVE";
    public static final String KEY_BARBER_LOAD_DONE = "BARBER_LOAD_DONE";
    public static final String KEY_DISPLAK_TIME_SLOT = "DISPLAY_TIME_SLOT";
    public static final String KEY_SEMP = "STEP";
    public static final String KEY_BARBER_SELECTED = "BARBER_SELECTED";
    public static final int TIME_SLOT_TOTAL = 20;
    public static final Object DISABLE_TAG = "DISABLE";
    public static final String KEY_TIME_SLOT = "TIME_SLOT";
    public static final String KEY_CONFIRM_BOOKING = "CONFIRM_BOOKING";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Salon currentSalon;
    public static int step = 0; // Init first step is 0
    public static String city="";
    public static Barber currentBarber;
    public static int currentTimeSlot = -1;
    public static Calendar currentDate = Calendar.getInstance();
    public static SimpleDateFormat simpleFormatDate = new SimpleDateFormat("dd_MM_yyyy");  //Only use when need format key

    public static String convertTimeSlotToString(int slot) {
        switch (slot)
        {
            case 0:
                return "9:0-9:30";
            case 1:
                return "9:30-10:00";
            case 2:
                return "10:00-10:30";
            case 3:
                return "10:30-11:00";
            case 4:
                return "11:00-11:30";
            case 5:
                return "11:30-12:00";
            case 6:
                return "12:00-12:30";
            case 7:
                return "12:30-01:00";
            case 8:
                return "01:00-01:30";
            case 9:
                return "01:30-2:00";
            case 10:
                return "2:00-02:30";
            case 11:
                return "02:30-03:00";
            case 12:
                return "03:00-03:30";
            case 13:
                return "3:30-04:00";
            case 14:
                return "04:00-04:30";
            case 15:
                return "04:30-05:00";
            case 16:
                return "05:00-05:30";
            case 17:
                return "05:30-06:00";
            case 18:
                return "06:00-06:30";
            case 19:
                return "06:30-07:00";
           default:
               return "Closed";
        }
    }
}
