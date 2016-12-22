package com.shixels.activity.Game;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shixels.R;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class XtraPlay extends Fragment implements View.OnClickListener {

    String[] gamePlayed,info;
    FancyButton hundred,twohundred,threeHundred, nothanks;
    public XtraPlay() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gamePlayed = (String[]) getArguments().getCharSequenceArray("gameplayed");
        info = (String[]) getArguments().getCharSequenceArray("info");

        View view =  inflater.inflate(R.layout.fragment_xtra_play, container, false);
        hundred = (FancyButton) view.findViewById(R.id.xplayHundred);
        twohundred = (FancyButton) view.findViewById(R.id.xplaytTwoHundred);
        threeHundred = (FancyButton) view.findViewById(R.id.xplaytThreeHundred);
        nothanks = (FancyButton) view.findViewById(R.id.noThanks);

        hundred.setOnClickListener(this);
        twohundred.setOnClickListener(this);
        threeHundred.setOnClickListener(this);
        nothanks.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.xplayHundred){
            Fragment fr=new CashPage();
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            Bundle args = new Bundle();
            String[] info = new String[3];
            info[0] = "SixStars";
            info[1] = "500";
            info[2] = "600";
            args.putCharSequenceArray("gameplayed",gamePlayed);
            args.putCharSequenceArray("info",info);
            args.putBoolean("xtra",true);
            fr.setArguments(args);
            ft.replace(R.id.container, fr);
            ft.commit();
        }
        else if(id == R.id.xplaytTwoHundred){
            Fragment fr=new CashPage();
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            Bundle args = new Bundle();
            String[] info = new String[3];
            info[0] = "SixStars";
            info[1] = "500";
            info[2] = "700";
            args.putCharSequenceArray("gameplayed",gamePlayed);
            args.putCharSequenceArray("info",info);
            args.putBoolean("xtra",true);
            fr.setArguments(args);
            ft.replace(R.id.container, fr);
            ft.commit();
        }
        else if(id == R.id.xplaytThreeHundred){
            Fragment fr=new CashPage();
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            Bundle args = new Bundle();
            String[] info = new String[3];
            info[0] = "SixStars";
            info[1] = "500";
            info[2] = "800";
            args.putCharSequenceArray("gameplayed",gamePlayed);
            args.putCharSequenceArray("info",info);
            args.putBoolean("xtra",true);
            fr.setArguments(args);
            ft.replace(R.id.container, fr);
            ft.commit();
        }
        else if(id == R.id.noThanks){
            Fragment fr=new CashPage();
            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            Bundle args = new Bundle();
            String[] info = new String[3];
            info[0] = "SixStars";
            info[1] = "500";
            info[2] = "500";
            args.putCharSequenceArray("gameplayed",gamePlayed);
            args.putCharSequenceArray("info",info);
            args.putBoolean("xtra",false);
            fr.setArguments(args);
            ft.replace(R.id.container, fr);
            ft.commit();
        }
    }
}
