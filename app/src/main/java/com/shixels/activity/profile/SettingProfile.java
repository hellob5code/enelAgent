package com.shixels.activity.profile;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.shixels.R;
import com.shixels.activity.Authentication;
import com.shixels.activity.Constant;
import com.shixels.activity.FirstMain;
import com.shixels.activity.Game.GameType;
import com.shixels.activity.Qblox.ConnectQB;
import com.shixels.activity.Qblox.SucessFunction;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingProfile extends Fragment {
    ConnectQB connectQB = ConnectQB.getInstance();
    EditText userName,email,fullame,phone;
    FancyButton chanpwd, logout, update;
    ScrollView holder;
    ProgressBar progressBar;

    public SettingProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((FirstMain)getActivity()).changeBack();
        View view=  inflater.inflate(R.layout.fragment_setting_profile, container, false);
        userName = (EditText) view.findViewById(R.id.username);
        fullame= (EditText) view.findViewById(R.id.fullname);
        phone = (EditText) view.findViewById(R.id.phon);
        email = (EditText) view.findViewById(R.id.email);
        chanpwd = (FancyButton) view.findViewById(R.id.changepwd);
        update = (FancyButton) view.findViewById(R.id.update);
        logout = (FancyButton) view.findViewById(R.id.logout);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        holder = (ScrollView) view.findViewById(R.id.holder);
        if(connectQB.isInternetAvailable(getContext())) {
            connectQB.runTimeSignin(getContext(), new SucessFunction() {
                @Override
                public void randomFuntion(QBUser user) {
                    userName.setText(user.getLogin());
                    fullame.setText(user.getFullName());
                    phone.setText(user.getPassword());
                    email.setText(user.getEmail());
                    progressBar.setVisibility(View.GONE);
                    holder.setVisibility(View.VISIBLE);

                }

                @Override
                public void error() {

                }
            });
        }
        else{
            connectQB.openAlert(getContext(),"check Your internet connection","Connection Error");

        }
        userName.setEnabled(false);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                holder.setVisibility(View.GONE);
                connectQB.runTimeSignin(getContext(), new SucessFunction() {
                    @Override
                    public void randomFuntion(QBUser user1) {
                        String usN,fuN,phN,em;
                        usN = userName.getText().toString();
                        fuN = fullame.getText().toString();
                        phN = phone.getText().toString();
                        em = email.getText().toString();
                        QBUser user = new QBUser();
                        user.setId(user1.getId());
                        user.setFullName(fuN);
                        user.setEmail(em);
                        user.setLogin(usN);
                        user.setPhone(phN);

                        QBUsers.updateUser(user, new QBEntityCallback<QBUser>(){
                            @Override
                            public void onSuccess(QBUser user, Bundle args) {
                                progressBar.setVisibility(View.GONE);
                                holder.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError(QBResponseException errors) {

                            }
                        });
                    }

                    @Override
                    public void error() {

                    }
                });
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constant.Pref,getContext().MODE_PRIVATE);
                String info = (sharedPreferences.getString("login",""));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("login","");
                editor.putString("info", "");
                editor.commit();
                Intent intent = new Intent(getContext(), Authentication.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finish();

            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();

        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){
                    Fragment frag = new GameType();
                    ((FirstMain)getActivity()).openFragment(frag,R.id.container,"GameType");
                    return true;
                }
                return false;
            }
        });
    }

}
