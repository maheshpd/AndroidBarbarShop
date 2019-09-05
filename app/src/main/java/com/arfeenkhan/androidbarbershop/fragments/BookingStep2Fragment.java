package com.arfeenkhan.androidbarbershop.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arfeenkhan.androidbarbershop.Common.SpacesItemDecoration;
import com.arfeenkhan.androidbarbershop.R;
import com.arfeenkhan.androidbarbershop.adapter.MyBarberAdapter;
import com.arfeenkhan.androidbarbershop.model.EventBus.BarberDoneEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookingStep2Fragment extends Fragment {

    Unbinder unbinder;
//    LocalBroadcastManager localBroadcastManager;

    @BindView(R.id.recycler_barber)
    RecyclerView recycler_barber;

//    private BroadcastReceiver barberDoneReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            ArrayList<Barber> barberArrayList = intent.getParcelableArrayListExtra(Common.KEY_BARBER_LOAD_DONE);
//            //Create adapter late
//            MyBarberAdapter adapter = new MyBarberAdapter(getContext(),barberArrayList);
//            recycler_barber.setAdapter(adapter);
//        }
//    };

    //Event Bus START


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void setBarberAdapter(BarberDoneEvent event) {
        MyBarberAdapter adapter = new MyBarberAdapter(getContext(), event.getBarberList());
        recycler_barber.setAdapter(adapter);
    }

    static BookingStep2Fragment instance;
    public static BookingStep2Fragment getInstance() {
        if (instance == null)
            instance = new BookingStep2Fragment();
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
//        localBroadcastManager.registerReceiver(barberDoneReceiver,new IntentFilter(Common.KEY_BARBER_LOAD_DONE));
    }

    @Override
    public void onDestroy() {
//        localBroadcastManager.unregisterReceiver(barberDoneReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemView= inflater.inflate(R.layout.fragment_booking_step2, container, false);
        unbinder = ButterKnife.bind(this,itemView);
        
        initView();
        return itemView;
    }

    private void initView() {
        recycler_barber.setHasFixedSize(true);
        recycler_barber.setLayoutManager(new GridLayoutManager(getActivity(),2));
        recycler_barber.addItemDecoration(new SpacesItemDecoration(4));
    }

}
