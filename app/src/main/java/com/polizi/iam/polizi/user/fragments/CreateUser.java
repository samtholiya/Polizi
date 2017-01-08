package com.polizi.iam.polizi.user.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.polizi.iam.polizi.R;
import com.polizi.iam.polizi.coordinators.OnFragmentInteractionListener;
import com.polizi.iam.polizi.models.PoliziUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateUser extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText mProfileName, mUserName, mAddress,
            mMobileNumber, mDesignation, mPassword,
            mRetypePassword, mPoliceStation;
    private View mView;
    private Button mCreateUser;
    private OnFragmentInteractionListener mListener;

    public CreateUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CreateUser.
     */
    public static CreateUser newInstance() {
        CreateUser fragment = new CreateUser();
        Bundle args = new Bundle();
        /*args.putString(ARG_PARAM1, param1);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_create_user, container, false);

        mProfileName = (EditText) mView.findViewById(R.id.profile_name);
        mAddress = (EditText) mView.findViewById(R.id.address);
        mMobileNumber = (EditText) mView.findViewById(R.id.mobile_number);
        mDesignation = (EditText) mView.findViewById(R.id.designation);
        mPassword = (EditText) mView.findViewById(R.id.password);
        mRetypePassword = (EditText) mView.findViewById(R.id.retype_password);
        mUserName = (EditText) mView.findViewById(R.id.user_name);
        mPoliceStation = (EditText) mView.findViewById(R.id.police_station);

        mCreateUser = (Button) mView.findViewById(R.id.create_user);
        mCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCreatePressed();
            }
        });

        return mView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onCreatePressed() {
        if (mListener != null && createUser()) {
            mListener.onFragmentInteraction(0);
        }
    }

    private boolean createUser() {
        PoliziUser user = new PoliziUser();
        if (!mProfileName.getText().toString().isEmpty())
            user.setProfileName(mProfileName.getText().toString());
        else {
            Snackbar.make(mView, R.string.name_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!mUserName.getText().toString().isEmpty())
            user.setUsername(mUserName.getText().toString());
        else {
            Snackbar.make(mView, R.string.user_name_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!mPassword.getText().toString().isEmpty()) {
            if (mRetypePassword.getText().toString().isEmpty()) {
                Snackbar.make(mView, R.string.retype_password_empty, Snackbar.LENGTH_LONG).show();
                return false;
            }
            if(mPassword.getText().toString().equals(mRetypePassword.getText().toString()))
                user.setPassword(mPassword.getText().toString());
            else {
                Snackbar.make(mView, R.string.password_mismatch, Snackbar.LENGTH_LONG).show();
                return false;
            }
        } else {
            Snackbar.make(mView, R.string.password_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }

        if (!mMobileNumber.getText().toString().isEmpty())
            user.setMobileNumber(mMobileNumber.getText().toString());
        else {
            Snackbar.make(mView, R.string.mobile_number_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!mAddress.getText().toString().isEmpty())
            user.setAddress(mAddress.getText().toString());
        else {
            Snackbar.make(mView, R.string.address_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!mDesignation.getText().toString().isEmpty())
            user.setDesignation(mDesignation.getText().toString());
        else {
            Snackbar.make(mView, R.string.designation_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }
        if (!mPoliceStation.getText().toString().isEmpty())
            user.setPoliceStation(mPoliceStation.getText().toString());
        else {
            Snackbar.make(mView, R.string.police_station_empty, Snackbar.LENGTH_LONG).show();
            return false;
        }
        user.signUpInBackground();
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            //throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
