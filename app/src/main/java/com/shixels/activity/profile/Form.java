package com.shixels.activity.profile;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.shixels.R;
import com.shixels.activity.FirstMain;
import com.shixels.activity.Game.GameType;


/**
 * A simple {@link Fragment} subclass.
 */
public class Form extends Fragment {

    Button button;
    EditText title,message;
    String email,emailpassword;
    ProgressBar progressBar;
    TextView error;


    public Form() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_form, container, false);
        button = (Button) view.findViewById(R.id.send);
        title = (EditText) view.findViewById(R.id.title);
        progressBar = (ProgressBar) view.findViewById(R.id.progerss);
        error = (TextView) view.findViewById(R.id.error);
        message = (EditText) view.findViewById(R.id.message);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
                String subject = title.getText().toString(), body = message.getText().toString();
                BackgroundMail.newBuilder(getContext())
                        .withUsername("richard@shixels.com")
                        .withPassword("Shadrachmyonly1")
                        .withMailto("bolaji@shixels.com")
                        .withSubject(subject)
                        .withBody(body)
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                progressBar.setVisibility(View.GONE);
                                error.setText("You message was sent successfully");
                                error.setTextColor(Color.parseColor("#00ff00"));
                                error.setVisibility(View.VISIBLE);
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                error.setVisibility(View.VISIBLE);
                                button.setVisibility(View.VISIBLE);
                            }
                        })
                        .send();
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
                    Fragment frag = new Help();
                    ((FirstMain)getActivity()).openFragment(frag,R.id.container,"help");
                    return true;
                }
                return false;
            }
        });
    }


}
