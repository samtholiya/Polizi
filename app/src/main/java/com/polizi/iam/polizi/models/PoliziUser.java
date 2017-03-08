package com.polizi.iam.polizi.models;

import com.parse.LogInCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by shubh on 07-01-2017.
 */

public class PoliziUser extends ParseUser {

    public void setProId(String proId) {
        put("proId", proId);
    }

    public String getProId() {
        return get("proId").toString();
    }

    public void setProfileName(String name) {
        put("profileName", name);
    }

    public String getProfileName() {
        String profileName = getString("profileName");
        if (profileName != null && !profileName.isEmpty())
            return profileName;
        else
            return "No Name";
    }

    public void setMobileNumber(String number) {
        put("number", number);
    }

    public String getMobileNumber() {
        String number = getString("number");
        if (number != null && !number.isEmpty())
            return number;
        else
            return "No Number";
    }

    public void setDesignation(String number) {
        put("designation", number);
    }

    public String getDesignation() {
        String designation = getString("designation");
        if (designation != null && !designation.isEmpty())
            return designation;
        else
            return "No Designation";
    }

    public void setAddress(String number) {
        put("address", number);
    }

    public String getAddress() {
        String address = getString("address");
        if (address != null && !address.isEmpty())
            return address;
        else
            return "No Address";
    }

    public void setPoliceStation(String number) {
        put("policeStation", number);
    }

    public String getPoliceStation() {
        return get("policeStation").toString();
    }

    public static void checkInInBackground(String username, String password, final LogInCallback logInCallback) {

        PoliziUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    ParseObject parseObject = new ParseObject("UserLogs");
                    parseObject.put("LogType", "CheckedIn");
                    parseObject.put("user", user);
                    parseObject.saveEventually();
                    poliziUser = (PoliziUser) user;
                }
                logInCallback.done(user, e);
            }
        });

    }

    private static PoliziUser poliziUser;

    public static void checkOutInBackground(final LogOutCallback logOutCallback) {
        PoliziUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    ParseObject parseObject = new ParseObject("UserLogs");
                    parseObject.put("LogType", "CheckedOut");
                    parseObject.put("user", poliziUser);
                    parseObject.saveEventually();
                }
                logOutCallback.done(e);
            }
        });
    }


}
