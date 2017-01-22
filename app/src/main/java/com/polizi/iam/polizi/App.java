package com.polizi.iam.polizi;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.polizi.iam.polizi.models.PoliziUser;

/**
 * Created by shubh on 04-01-2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(PoliziUser.class);
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("myAppPolizi")
                .enableLocalDataStore()
                .server("http://192.168.43.123:1337/parse/")
                .build());
    }
}
