package com.polizi.iam.polizi.models;

import com.parse.ParseUser;

/**
 * Created by shubh on 07-01-2017.
 */

public class PoliziUser extends ParseUser {
    public void setName(String name){
        put("name",name);
    }

    public String getName(){
        return get("name").toString();
    }
    public void setMobileNumber(String number){
        put("number",number);
    }

    public String getMobileNumber(){
        return get("number").toString();
    }

    public void setDesignation(String number){
        put("designation",number);
    }

    public String getDesignation(){
        return get("designation").toString();
    }

    public void setAddress(String number){
        put("address",number);
    }

    public String getAddress(){
        return get("address").toString();
    }

    public void setPoliceStation(String number){
        put("policeStation",number);
    }

    public String getPoliceStation(){
        return get("policeStation").toString();
    }
}