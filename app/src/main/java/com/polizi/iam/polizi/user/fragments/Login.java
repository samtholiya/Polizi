package com.polizi.iam.polizi.user.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.polizi.iam.polizi.R;
import com.polizi.iam.polizi.coordinators.OnFragmentInteractionListener;
import com.polizi.iam.polizi.coordinators.OnLoginListener;
import com.polizi.iam.polizi.models.PoliziUser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View mView;
    private OnLoginListener mLoginListener;

    private EditText mUser, mPassword;
    private Button mLogin;

    private boolean isLoggedIn;
    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance() {
        Login fragment = new Login();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false);
        mUser = (EditText) mView.findViewById(R.id.login_user_name);
        mPassword = (EditText) mView.findViewById(R.id.login_password);
        mLogin = (Button) mView.findViewById(R.id.login_button);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginUser()) {

                    mListener.onFragmentInteraction(3);
                    Log.d("Login",PoliziUser.getCurrentUser().getUsername());
                }

            }
        });

        return mView;
    }

    private boolean loginUser() {
        isLoggedIn=false;
        String username = mUser.getText().toString();
        String password = mPassword.getText().toString();

        if (username.isEmpty()) {
            Snackbar.make(mView, R.string.user_name_empty, Snackbar.LENGTH_LONG).show();
            return isLoggedIn;
        }
        if (password.isEmpty()) {
            Snackbar.make(mView, R.string.password_empty, Snackbar.LENGTH_LONG).show();
            return isLoggedIn;
        }

        PoliziUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                if (e == null) {
                    if (user instanceof PoliziUser) {
                        isLoggedIn = true;
                        mLoginListener.onLoginUpdate();
                    }
                } else {
                    Snackbar.make(mView,R.string.incorrect_login_credentials,Snackbar.LENGTH_LONG).show();
                }
            }
        });
        return isLoggedIn;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void loggedIn() {
        if (mListener != null) {
            mListener.onFragmentInteraction(2);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnLoginListener) {
            mLoginListener = (OnLoginListener) context;
        }
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            /*throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");*/
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
