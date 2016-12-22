package com.shixels.activity.Game;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.model.QBUser;
import com.shixels.R;
import com.shixels.activity.FirstMain;
import com.shixels.activity.Qblox.ConnectQB;
import com.shixels.activity.Qblox.SucessFunction;
import com.shixels.activity.Qblox.selected;


import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class ThreeStar extends Fragment {
    FlexboxLayout numberpan;
    ConnectQB connectQB = ConnectQB.getInstance();
    LinearLayout selectedNum;
    int counter = 0;
    Button submit,clear, pick4me;
    String[] gamePlayed = new String[3];
    public ThreeStar() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_three_star, container, false);
        selectedNum = (LinearLayout) view.findViewById(R.id.gameSelected);
        numberpan = (FlexboxLayout) view.findViewById(R.id.selecteNum);
        submit = (Button) view.findViewById(R.id.submit);
        pick4me = (Button) view.findViewById(R.id.pic4me);
        clear = (Button) view.findViewById(R.id.clear);
        connectQB.generateNumberPan(42, numberpan, getActivity(), 30, 50,new int[]{0},new selected() {
            @Override
            public void selectee(String number) {
                if(counter < 3){
                    LinearLayout edit = (LinearLayout) selectedNum.getChildAt(counter);
                    TextView se = new TextView(getContext());
                    se.setText(number);
                    se.setGravity(Gravity.CENTER_HORIZONTAL);
                    se.setTextSize(30);
                    se.setTextColor(getActivity().getResources().getColor(R.color.blueText2));
                    if(se.getText().length() == 1){
                        se.setPadding(18,5,0,0);
                    }
                    else{
                        se.setPadding(10,5,0,0);
                    }
                    edit.addView(se);
                    gamePlayed[counter] = number;
                    Button btnAddARoom = (Button) numberpan.getChildAt(Integer.parseInt(number) -1);
                    btnAddARoom.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.yellow_circle));
                    Log.i("burn", Arrays.toString(gamePlayed));
                    counter++;
                }
                else{
                    connectQB.openAlert(getContext(),getString(R.string.errorSelectThree),"Error");
                }








            }
        });
        ((FirstMain)getActivity()).changeBack();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gamePlayed[2] != null){
                    Fragment fr=new CashPage();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    Bundle args = new Bundle();
                    String[] info = new String[3];
                    info[0] = "ThreeStar";
                    info[1] = "100";
                    info[2] = "100";
                    args.putCharSequenceArray("gameplayed",gamePlayed);
                    args.putCharSequenceArray("info",info);
                    fr.setArguments(args);
                    ft.replace(R.id.container, fr);
                    ft.commit();
                }
                else {
                    connectQB.openAlert(getContext(),"Please Select 3 numbers","Incomplete");
                }
            }
        });
        pick4me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = 3 - counter;
                if(counter == 2){
                    size = 1;
                }

                if(counter < 3){
                    if(counter < 2){
                        Set set = connectQB.randomGeneratorSet(size,41,gamePlayed);
                        Iterator<Integer> iterator = set.iterator();
                        while (iterator.hasNext()){
                            LinearLayout edit = (LinearLayout) selectedNum.getChildAt(counter);
                            TextView se = new TextView(getContext());
                            se.setGravity(Gravity.CENTER_HORIZONTAL);
                            se.setTextSize(30);
                            se.setText(String.valueOf(iterator.next()));
                            se.setTextColor(getActivity().getResources().getColor(R.color.blueText2));
                            if(size != 1) {
                                gamePlayed[counter] = se.getText().toString();
                            }
                            else {
                                gamePlayed[2] = se.getText().toString();
                            }
                            if(se.getText().length() == 1){
                                se.setPadding(18,5,0,0);
                            }
                            else{
                                se.setPadding(10,5,0,0);
                            }
                            edit.addView(se);

                            counter++;


                            Button layout = (Button) numberpan.getChildAt(Integer.parseInt(se.getText().toString())-1 );
                            layout.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.yellow_circle));
                        }
                        counter = 4;
                    }
                    else {
                        connectQB.openAlert(getContext(),getString(R.string.clearScrean),"Notice");
                    }

                }
                else {
                    connectQB.openAlert(getContext(),getString(R.string.errorSelectThree),"Error");
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                tr.replace(R.id.container, new ThreeStar());
                tr.commit();

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
