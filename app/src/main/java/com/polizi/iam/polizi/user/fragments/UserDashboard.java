package com.polizi.iam.polizi.user.fragments;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.polizi.iam.polizi.R;
import com.polizi.iam.polizi.coordinators.OnFragmentInteractionListener;
import com.polizi.iam.polizi.models.PoliziUser;
import com.polizi.iam.polizi.service.LocationService;

import java.util.HashMap;

import static com.polizi.iam.polizi.coordinators.SharedRuntimeContent.GPS_FILTER;
import static com.polizi.iam.polizi.coordinators.SharedRuntimeContent.GPS_LATITUDE;
import static com.polizi.iam.polizi.coordinators.SharedRuntimeContent.GPS_LONGITUDE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserDashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserDashboard extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private BroadcastReceiver mReciever;

    private OnFragmentInteractionListener mListener;
    private View mView;
    private TextView mProfileName;
    private TextView mDesignation;
    private Button mCallBackup;
    //private EditText mUnitCount;
    private AppCompatSpinner mUnitMessage;

    private ParseGeoPoint mGeoPoint;
    private TextView mContact;
    private TextView mAddress;

    public UserDashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment UserHelper.
     */
    // TODO: Rename and change types and number of parameters
    public static UserDashboard newInstance() {
        UserDashboard fragment = new UserDashboard();
        Bundle args = new Bundle();/*
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);*/
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mGeoPoint = new ParseGeoPoint();
                mGeoPoint.setLatitude(intent.getDoubleExtra(GPS_LATITUDE, 0));
                mGeoPoint.setLongitude(intent.getDoubleExtra(GPS_LONGITUDE, 0));
            }
        };

        //getActivity().registerReceiver(mReciever, new IntentFilter(GPS_FILTER));
    }

    @Override
    public void onResume() {
        super.onResume();
        checkPermissions();
        if (mReciever != null)
            getActivity().registerReceiver(mReciever, new IntentFilter(GPS_FILTER));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_user_dashboard, container, false);
        mProfileName = (TextView) mView.findViewById(R.id.dash_name);
        mDesignation = (TextView) mView.findViewById(R.id.dash_designation);
        mContact = (TextView) mView.findViewById(R.id.contact_value);
//        mContact.setText();
        mAddress = (TextView) mView.findViewById(R.id.address_value);

        ParseUser parseUser = PoliziUser.getCurrentUser();
        if (parseUser instanceof PoliziUser) {
            PoliziUser poliziUser = (PoliziUser) parseUser;
            String profileName = poliziUser.getProfileName();
            String designation = poliziUser.getDesignation();
            if (!profileName.isEmpty())
                mProfileName.setText(profileName);
            if (!designation.isEmpty())
                mDesignation.setText(designation);
            mContact.setText(poliziUser.getMobileNumber());
            mAddress.setText(poliziUser.getAddress());
        }
        //mUnitCount = (EditText) mView.findViewById(R.id.backup_count);
        mUnitMessage = (AppCompatSpinner) mView.findViewById(R.id.backup_message);
        mCallBackup = (Button) mView.findViewById(R.id.call_backup);
        mCallBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mUnitMessage.getSelectedItem().toString();

                int backupCount = 0;
                //if (count.length() > 0)
                //    backupCount = Integer.parseInt(count);
                //else {
                backupCount = 10;
                //}

                if (message.length() == 0)
                    message = "Need help";
                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                if (mGeoPoint == null) {
                    Snackbar.make(mView, "We are still trying to get your location", Snackbar.LENGTH_LONG).show();
                    return;
                    //mGeoPoint = new ParseGeoPoint(19, 72);
                }
                /*
                hashMap.put("location",mGeoPoint);
                Log.d("here","_________________GOES___NOTHING");
                ParseCloud.callFunctionInBackground("SendPush", hashMap);
                */
                try {
                    if (!checkPermissions()) {
                        HashMap<String, Object> map = new HashMap<String, Object>();
                        Log.d("User_Dashboard", String.valueOf(mGeoPoint == null));
                        map.put("location", mGeoPoint);
                        map.put("userId", ParseInstallation.getCurrentInstallation().getInstallationId());
                        map.put("count", backupCount);
                        map.put("message", message);
                        map.put("userName", ((PoliziUser) PoliziUser.getCurrentUser()).getProfileName());
                        ParseCloud.callFunctionInBackground("SendPush", map, new FunctionCallback<Object>() {
                            @Override
                            public void done(Object object, ParseException e) {
                                if (e != null)
                                    e.printStackTrace();
                            }
                        });
                    } else {
                        Snackbar.make(mView, "You din't provide enough permissions to access location", Snackbar.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        });
        return mView;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (mReciever != null)
                getActivity().unregisterReceiver(mReciever);
        } catch (Exception e) {
            Log.d("User Dashboard", e.getMessage());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(0);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
          /*  throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getActivity().startService(new Intent(getContext(), LocationService.class));
            } else {
                checkPermissions();
            }
        }
    }

}
