package com.arfeenkhan.androidbarbershop.fragments;


import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.arfeenkhan.androidbarbershop.Common.Common;
import com.arfeenkhan.androidbarbershop.R;
import com.arfeenkhan.androidbarbershop.model.BookingInformation;
import com.arfeenkhan.androidbarbershop.model.TimeSlot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.CookieHandler;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingStep4Fragment extends Fragment {

    SimpleDateFormat simpleDateFormat;
    LocalBroadcastManager localBroadcastManager;
    Unbinder unbinder;

    AlertDialog dialog;

    @BindView(R.id.txt_booking_barber_text)
    TextView txt_booking_barber_text;
    @BindView(R.id.txt_booking_time_text)
    TextView txt_booking_time_text;
    @BindView(R.id.txt_salon_address)
    TextView txt_salon_address;
    @BindView(R.id.txt_salon_name)
    TextView txt_salon_name;
    @BindView(R.id.txt_salon_open_hours)
    TextView txt_salon_open_hours;
    @BindView(R.id.txt_salon_phone)
    TextView txt_salon_phone;
    @BindView(R.id.txt_salon_website)
    TextView txt_salon_website;

    @OnClick(R.id.btn_confirm)
    void confirmBookinf() {

        dialog.show();

        //Process Timestamp
        //We will use Timestamp to filter all booking with date is greater today
        //For only display all future booking
        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-"); // Split ex : 9:00 - 10:00


        //Get start time : get 9:00
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); //we get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim()); //we get 00

        Calendar bookingDateWithourHouse = Calendar.getInstance();
        bookingDateWithourHouse.setTimeInMillis(Common.bookingDate.getTimeInMillis());
        bookingDateWithourHouse.set(Calendar.HOUR_OF_DAY,startHourInt);
        bookingDateWithourHouse.set(Calendar.MINUTE,startMinInt);

        //Create timestamp object and apply to BookingInformation
        Timestamp timestamp = new Timestamp(bookingDateWithourHouse.getTime());

        //Create booking information
       final BookingInformation bookingInformation = new BookingInformation();
        bookingInformation.setCityBook(Common.city);
        bookingInformation.setTimestamp(timestamp);
        bookingInformation.setDone(false); //Always FALSE, because we will use this field to filter for display for user

        bookingInformation.setBarberId(Common.currentBarber.getBarberId());
        bookingInformation.setBarberName(Common.currentBarber.getName());
        bookingInformation.setCustomeName(Common.currentUser.getName());
        bookingInformation.setCustomerPhone(Common.currentUser.getPhoneNumber());
        bookingInformation.setSalonId(Common.currentSalon.getSalonId());
        bookingInformation.setSaloneAddress(Common.currentSalon.getAddress());
        bookingInformation.setSalonName(Common.currentSalon.getName());
        bookingInformation.setTime(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(bookingDateWithourHouse.getTime())).toString());
        bookingInformation.setSlot(Long.valueOf(Common.currentTimeSlot));

        //Submit to Barber document
        DocumentReference bookingDate = FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(Common.city)
                .collection("Branch")
                .document(Common.currentSalon.getSalonId())
                .collection("Barbers")
                .document(Common.currentBarber.getBarberId())
                .collection(Common.simpleFormatDate.format(Common.bookingDate.getTime()))
                .document(String.valueOf(Common.currentTimeSlot)); //bookDate is date simpleformat with dd_MM_yyyy = 02_07_2019

        //Write data
        bookingDate.set(bookingInformation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Here we can write an function to check
                        //If already exist an booking, we will prevent new booking

                        addTOUserBooking(bookingInformation);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addTOUserBooking(BookingInformation bookingInformation) {
        //First , create new collection
        CollectionReference userBooking = FirebaseFirestore.getInstance()
                .collection("User")
                .document(Common.currentUser.getPhoneNumber())
                .collection("Booking");

        //Check if exist document in this collection
        //Get Current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        Timestamp toDayTimestamp = new Timestamp(calendar.getTime());

        userBooking
                .whereGreaterThanOrEqualTo("timestamp", toDayTimestamp)
                .whereEqualTo("done", false)
                .limit(1) //Only take 1
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.getResult().isEmpty())
                        {
                            //Set data
                            userBooking.document()
                                    .set(bookingInformation)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            if (dialog.isShowing())
                                                dialog.dismiss();
                                            addToCalendar(Common.bookingDate,
                                                    Common.convertTimeSlotToString(Common.currentTimeSlot));
                                            resetStaticData();
                                            getActivity().finish();  //Close activity
                                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            if (dialog.isShowing())
                                dialog.dismiss();
                            resetStaticData();
                            getActivity().finish();  //Close activity
                            Toast.makeText(getContext(), "Success!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void addToCalendar(Calendar bookingDate, String startDate) {

        String startTime = Common.convertTimeSlotToString(Common.currentTimeSlot);
        String[] convertTime = startTime.split("-"); // Split ex : 9:00 - 10:00
        //Get start time : get 9L00
        String[] startTimeConvert = convertTime[0].split(":");
        int startHourInt = Integer.parseInt(startTimeConvert[0].trim()); //we get 9
        int startMinInt = Integer.parseInt(startTimeConvert[1].trim()); //we get 00

        String[] endTimeConvert = convertTime[1].split(":");
        int endHourInt = Integer.parseInt(endTimeConvert[0].trim()); //we get 10
        int endMinInt = Integer.parseInt(endTimeConvert[1].trim()); //we get 00

        Calendar startEvent = Calendar.getInstance();
        startEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        startEvent.set(Calendar.HOUR_OF_DAY,startHourInt); //Set event start hour
        startEvent.set(Calendar.MINUTE,startMinInt); //Set event start min

        Calendar endEvent = Calendar.getInstance();
        endEvent.setTimeInMillis(bookingDate.getTimeInMillis());
        endEvent.set(Calendar.HOUR_OF_DAY,endHourInt); //Set event end hour
        endEvent.set(Calendar.MINUTE,endMinInt); //Set event end min

        //After we have startEvent and endEvent , convert it to format String
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String startEventTime = calendarDateFormat.format(startEvent.getTime());
        String endEventTime = calendarDateFormat.format(endEvent.getTime());

        addToDeviceCalendar(startEventTime,endEventTime,"Haircut Booking",
                new StringBuilder(" Haircut from ")
        .append(startTime)
        .append(" with ")
        .append(Common.currentBarber.getName())
        .append(" at ")
        .append(Common.currentSalon.getName()).toString(),
                new StringBuilder("Address: ").append(Common.currentSalon.getAddress()).toString());

    }

    private void addToDeviceCalendar(String startEventTime, String endEventTime, String title, String description, String location) {
        SimpleDateFormat calendarDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        try{
            Date start = calendarDateFormat.parse(startEventTime);
            Date end = calendarDateFormat.parse(endEventTime);

            ContentValues event = new ContentValues();

            //Put
            event.put(CalendarContract.Events.CALENDAR_ID,getCalendar(getContext()));
            event.put(CalendarContract.Events.TITLE,title);
            event.put(CalendarContract.Events.DESCRIPTION,description);
            event.put(CalendarContract.Events.EVENT_LOCATION,location);

            //Time
            event.put(CalendarContract.Events.DTSTART,start.getTime());
            event.put(CalendarContract.Events.DTEND,end.getTime());
            event.put(CalendarContract.Events.ALL_DAY,0);
            event.put(CalendarContract.Events.HAS_ALARM,1);

            String timeZone = TimeZone.getDefault().getID();
            event.put(CalendarContract.Events.EVENT_TIMEZONE,timeZone);

            Uri calendars;
            if (Build.VERSION.SDK_INT >= 19)
            calendars= Uri.parse("content://com.android.calendar/events");
            else
                calendars= Uri.parse("content://calendar/events");
            Uri uri_save = getActivity().getContentResolver().insert(calendars,event);

            //Save to cache
            Paper.init(getActivity());
            Paper.book().write(Common.EVENT_URI_CACHE,uri_save.toString());


        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String getCalendar(Context context) {
        //GEt default calendar ID of Calendar of Gmail
        String gmailIdCAlendar = "";
        String projection[] = {"_id","calendar_displayName"};
        Uri calendars = Uri.parse("content://com.android.calendar/calendars");

        ContentResolver contentResolver = context.getContentResolver();
        //Select all calendar
        Cursor managedCursor = contentResolver.query(calendars,projection,null,null,null);
        if (managedCursor.moveToFirst())
        {
            String calName;
            int nameCol = managedCursor.getColumnIndex(projection[1]);
            int idCol = managedCursor.getColumnIndex(projection[0]);
            do{
                calName = managedCursor.getString(nameCol);
                if (calName.contains("@gmail.com")) {
                    gmailIdCAlendar = managedCursor.getString(idCol);
                    break; //Exit as soon as have id
                }
            }while (managedCursor.moveToNext());
            managedCursor.close();
        }

        return gmailIdCAlendar;
    }

    private void resetStaticData() {
        Common.step = 0;
        Common.currentTimeSlot = -1;
        Common.currentSalon = null;
        Common.currentBarber = null;
        Common.bookingDate.add(Calendar.DATE, 0);  //Current date added
    }

    BroadcastReceiver confirmBookingReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setData();
        }
    };

    private void setData() {
        txt_booking_barber_text.setText(Common.currentBarber.getName());
        txt_booking_time_text.setText(new StringBuilder(Common.convertTimeSlotToString(Common.currentTimeSlot))
                .append(" at ")
                .append(simpleDateFormat.format(Common.bookingDate.getTime())));

        txt_salon_address.setText(Common.currentSalon.getAddress());
        txt_salon_website.setText(Common.currentSalon.getWebsite());
        txt_salon_name.setText(Common.currentSalon.getName());
        txt_salon_open_hours.setText(Common.currentSalon.getOpenHours());
    }

    static BookingStep4Fragment instance;

    public static BookingStep4Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep4Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apply format for date display on confirm
        simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        localBroadcastManager.registerReceiver(confirmBookingReceiver, new IntentFilter(Common.KEY_CONFIRM_BOOKING));

        dialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false)
                .build();

    }

    @Override
    public void onDestroy() {
        localBroadcastManager.unregisterReceiver(confirmBookingReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_booking_step4, container, false);
        unbinder = ButterKnife.bind(this, itemView);
        return itemView;
    }

}
