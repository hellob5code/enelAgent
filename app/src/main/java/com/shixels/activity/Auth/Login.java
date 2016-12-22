package com.shixels.activity.Auth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.shixels.R;
import com.shixels.activity.Authentication;
import com.shixels.activity.Qblox.ConnectQB;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends Fragment {
    EditText password,userName;
    Button login;
    TextView error, register;
    ConnectQB connectQB = ConnectQB.getInstance();
    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ((Authentication) getActivity())
                .setActionBarTitle("Login");

        connectQB.initQuickBlox(getContext());
        //Init Views
        password = (EditText) view.findViewById(R.id.password);
        userName = (EditText) view.findViewById(R.id.email);
        login = (Button) view.findViewById(R.id.signin);
        error = (TextView) view.findViewById(R.id.error);
        register = (TextView) view.findViewById(R.id.registerBtn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new Register();
                ((Authentication) getActivity()).openFragment(frag,R.id.container,"Register");
            }
        });



        //Init Listeners
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().length() <= 0){
                    error.setVisibility(View.VISIBLE);
                    error.setText("Please Enter Password");
                }
                else if(userName.getText().length() <= 0) {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Please Enter Username");
                }
                else {
                    error.setVisibility(View.VISIBLE);
                    error.setText("Please wait....");
                    String usr = userName.getText().toString();
                    String pswd = password.getText().toString();
                    if (connectQB.isInternetAvailable(getContext())) {
                        connectQB.SignIn(usr, pswd, error, getContext());
                    } else {
                        error.setText("No internet conncetion");
                    }
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        return  view;
    }

    private void login(String username, String password){
        QBUser user = new QBUser(username, password);

        QBUsers.signIn(user, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {

            }

            @Override
            public void onError(QBResponseException errors) {

            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    android.os.Process.killProcess(android.os.Process.myPid());
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }


}
