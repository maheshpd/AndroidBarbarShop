package com.arfeenkhan.androidbarbershop.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.arfeenkhan.androidbarbershop.R;
import com.arfeenkhan.androidbarbershop.model.Barber;
import com.arfeenkhan.androidbarbershop.model.BookingInformation;
import com.arfeenkhan.androidbarbershop.model.MyToken;
import com.arfeenkhan.androidbarbershop.model.Salon;
import com.arfeenkhan.androidbarbershop.model.TimeSlot;
import com.arfeenkhan.androidbarbershop.model.User;
import com.arfeenkhan.androidbarbershop.service.MyFCMService;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    public static final String EVENT_URI_CACHE = "URI_EVENT_SAVE";
    public static final String TITLE_KEY = "title";
    public static final String CONTENT_KEY = "content";
    public static String IS_LOGIN = "IsLogin";
    public static User currentUser;
    public static Salon currentSalon;
    public static int step = 0; // Init first step is 0
    public static String city = "";
    public static Barber currentBarber;
    public static int currentTimeSlot = -1;
    public static Calendar bookingDate = Calendar.getInstance();
    public static SimpleDateFormat simpleFormatDate = new SimpleDateFormat("dd_MM_yyyy");  //Only use when need format key
    public static BookingInformation currentBooking;
    public static String currentBookingId = "";

    public static String convertTimeSlotToString(int slot) {
        switch (slot) {
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

    public static String convertTimestampTostring(Timestamp timestamp) {
        Date data = timestamp.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
        return simpleDateFormat.format(data);
    }

    public static String formatShoppingItemName(String name) {
        return name.length() > 13 ? new StringBuilder(name.substring(0, 10)).append("...").toString() : name;
    }

    public static void showNotification(Context context, int noti_id, String title, String content, Intent intent) {
        //Copy code from Staff app
        PendingIntent pendingIntent = null;
        if (intent != null)
            pendingIntent = PendingIntent.getActivity(context,
                    noti_id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);

            //Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder
                .setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));

        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification mNotification = builder.build();

        notificationManager.notify(noti_id, mNotification);


    }

    public static enum TOKEN_TYPE {
        CLIENT,
        BARBER,
        MANAGER
    }


    public static void updateToken(String s) {
        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if (accessToken != null) {
            AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                @Override
                public void onSuccess(Account account) {
                    MyToken myToken = new MyToken();
                    myToken.setToken(s);
                    myToken.setToken_type(TOKEN_TYPE.CLIENT); //Because token come from client app
                    myToken.setUserPhone(account.getPhoneNumber().toString());

                    FirebaseFirestore.getInstance()
                            .collection("Tokens")
                            .document(account.getPhoneNumber().toString())
                            .set(myToken)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            });

                }

                @Override
                public void onError(AccountKitError accountKitError) {

                }
            });
        }
    }
}
