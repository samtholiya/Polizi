package com.polizi.iam.polizi.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.polizi.iam.polizi.models.PoliziUser;

/**
 * Created by COMPU on 28-10-2016.
 */

public class GPSService extends Service {

    public static final String GPS_FILTER = "trak.gps.location";
    public static final String GPS_LONGITUDE = "trak.gps.location.longitude";
    public static final String GPS_LATITUDE = "trak.gps.location.latitude";
    private LocationListener listener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        Log.d("GPS Service", "Service started");
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(getApplicationContext(), "location received", Toast.LENGTH_LONG).show();
                ParseUser parseUser = PoliziUser.getCurrentUser();
                if (parseUser != null) {
                    if (parseUser instanceof PoliziUser) {
                        ParseGeoPoint geoPoint = new ParseGeoPoint();
                        geoPoint.setLatitude(location.getLatitude());
                        geoPoint.setLongitude(location.getLongitude());
                        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                        installation.put("Location", geoPoint);
                        installation.put("User", ((PoliziUser) parseUser));
                        installation.saveInBackground();
                        Intent intent = new Intent(GPS_FILTER);
                        Toast.makeText(getApplicationContext(),"Got Location",Toast.LENGTH_LONG).show();
                        intent.putExtra(GPS_LATITUDE, location.getLatitude());
                        intent.putExtra(GPS_LONGITUDE, location.getLongitude());
                        sendBroadcast(intent);
                    }
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, listener);
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 3000, 0, listener);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(listener);

        }
    }
}
