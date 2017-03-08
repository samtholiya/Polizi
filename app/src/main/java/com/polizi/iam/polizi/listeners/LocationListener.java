package com.polizi.iam.polizi.listeners;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.polizi.iam.polizi.models.PoliziUser;

import static com.polizi.iam.polizi.coordinators.SharedRuntimeContent.GPS_FILTER;
import static com.polizi.iam.polizi.coordinators.SharedRuntimeContent.GPS_LATITUDE;
import static com.polizi.iam.polizi.coordinators.SharedRuntimeContent.GPS_LONGITUDE;
/*
import static com.polizi.iam.polizi.service.GPSService.GPS_FILTER;
import static com.polizi.iam.polizi.service.GPSService.GPS_LATITUDE;
import static com.polizi.iam.polizi.service.GPSService.GPS_LONGITUDE;*/

/**
 * Created by shubh on 22-01-2017.
 */
public class LocationListener implements android.location.LocationListener {

    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Context mContext = null;
    public Location mPreviousBestLocation = null;

    public LocationListener(Context context){
        mContext = context;
    }


    public void onLocationChanged(Location loc) {
        //Log.i("**************************************", "Location changed");
        //Toast.makeText(mContext,"Got Location",Toast.LENGTH_LONG).show();
        if (isBetterLocation(loc, mPreviousBestLocation)) {
            ParseUser parseUser = PoliziUser.getCurrentUser();
            if (parseUser != null) {
                if (parseUser instanceof PoliziUser) {
                    ParseGeoPoint geoPoint = new ParseGeoPoint();
                    geoPoint.setLatitude(loc.getLatitude());
                    geoPoint.setLongitude(loc.getLongitude());
                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                    installation.put("Location", geoPoint);
                    installation.put("User", ((PoliziUser) parseUser));
                    installation.put("isLoggedIn",true);
                    installation.saveInBackground();
                    Intent intent = new Intent(GPS_FILTER);
                    Log.d("Location Service","Got Location ");
                    intent.putExtra(GPS_LATITUDE, loc.getLatitude());
                    intent.putExtra(GPS_LONGITUDE, loc.getLongitude());
                    mContext.sendBroadcast(intent);
                }
            }
        }
    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(mContext.getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }


    public void onProviderEnabled(String provider) {
        Toast.makeText(mContext, "Gps Enabled", Toast.LENGTH_SHORT).show();
    }


    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

}