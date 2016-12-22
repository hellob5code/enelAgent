package com.shixels.activity.Auth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.shixels.R;
import com.shixels.activity.Authentication;
import com.shixels.activity.Qblox.ConnectQB;


/**
 * A simple {@link Fragment} subclass.
 */
public class Register extends Fragment {
    EditText userName,email,password, password2;
    TextView signin, error;
    Button register;
    ConnectQB connectQB = ConnectQB.getInstance();
    public Register() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ((Authentication) getActivity())
                .setActionBarTitle("Register");
        connectQB.initQuickBlox(getContext());
        userName = (EditText) view.findViewById(R.id.email);
        email = (EditText) view.findViewById(R.id.email);
        password = (EditText) view.findViewById(R.id.password);
        password2 = (EditText) view.findViewById(R.id.password2);
        register = (Button) view.findViewById(R.id.register);
        error = (TextView) view.findViewById(R.id.error);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                error.setVisibility(View.VISIBLE);
                error.setText("Please wait....");
                String usN = userName.getText().toString(), em = email.getText().toString(), ps = password.getText().toString(),
                        ps2 = password2.getText().toString();
                if(connectQB.isInternetAvailable(getContext())){
                    if(connectQB.passwordValidations(ps,ps2) == "success"){
                        connectQB.Register(em,ps,usN,error,getContext());
                    }
                    else{
                        error.setText(connectQB.passwordValidations(ps,ps2));
                    }
                 }
                else{
                    error.setText("No internet conncetion");
                }

            }
        });
        signin = (TextView) view.findViewById(R.id.SigninBtn);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment frag = new Login();
                ((Authentication) getActivity()).openFragment(frag,R.id.container,"signin");
            }
        });
        return  view;
    }

}
