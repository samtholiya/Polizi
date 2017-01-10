package com.polizi.iam.polizi.user.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseUser;
import com.polizi.iam.polizi.R;
import com.polizi.iam.polizi.coordinators.OnFragmentInteractionListener;
import com.polizi.iam.polizi.models.PoliziUser;

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

    private OnFragmentInteractionListener mListener;
    private View mView;
    private TextView mProfileName;
    private TextView mDesignation;
    private Button mCallBackup;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_user_dashboard, container, false);
        mProfileName = (TextView) mView.findViewById(R.id.dash_name);
        mDesignation = (TextView) mView.findViewById(R.id.dash_designation);
        ParseUser parseUser = PoliziUser.getCurrentUser();
        if (parseUser instanceof PoliziUser) {
            PoliziUser poliziUser = (PoliziUser) parseUser;
            String profileName = poliziUser.getProfileName();
            String designation = poliziUser.getDesignation();
            if (!profileName.isEmpty())
                mProfileName.setText(profileName);
            if (!designation.isEmpty())
                mDesignation.setText(designation);
        }
        mCallBackup = (Button) mView.findViewById(R.id.call_backup);
        mCallBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Procedure for push notification to others.
            }
        });
        return mView;
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

}
