package com.polizi.iam.polizi;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by shubh on 04-01-2017.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("SammyAppId")
                .enableLocalDataStore()
                .server("http://10.196.9.166:1337/parse/")
                .build());
    }
}
