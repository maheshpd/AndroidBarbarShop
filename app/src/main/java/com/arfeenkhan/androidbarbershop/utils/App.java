package com.arfeenkhan.androidbarbershop.utils;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        //Default value
        Map<String,Object> defaultValue = new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE,false);
        defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION,"1.0");
        defaultValue.put(UpdateHelper.KEY_UPDATE_URL,"your app url on App Store");

        remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(60) //fetch data from Firebase every 1 minute
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            remoteConfig.activateFetched();
                        }
                    }
                });
    }
}
