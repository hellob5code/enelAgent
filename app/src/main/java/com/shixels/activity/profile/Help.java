package com.shixels.activity.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shixels.R;
import com.shixels.activity.FirstMain;
import com.shixels.activity.Game.GameType;


/**
 * A simple {@link Fragment} subclass.
 */
public class Help extends Fragment {

     Button button;
    public Help() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((FirstMain)getActivity()).changeBack();
        View view = inflater.inflate(R.layout.fragment_help, container, false);
        button = (Button) view.findViewById(R.id.message);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((FirstMain)getContext()).openFragment(new Form(),R.id.container,"form");
            }
        });
        return  view;
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
