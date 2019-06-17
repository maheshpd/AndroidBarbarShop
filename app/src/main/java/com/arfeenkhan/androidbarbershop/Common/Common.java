package com.arfeenkhan.androidbarbershop.Common;

import com.arfeenkhan.androidbarbershop.model.Salon;
import com.arfeenkhan.androidbarbershop.model.User;

public class Common {
    public static final String KEY_ENABLE_BUTTON_NEXT = "ENABLE_BUTTON_NEXT";
    public static final String KEY_SALON_STORE = "SALON_SAVE";
    public static final String KEY_BARBER_LOAD_DONE = "BARBER_LOAD_DONE";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Salon currentSalon;
    public static int step = 0; // Init first step is 0
    public static String city="";
}
