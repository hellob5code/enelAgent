package com.shixels.activity.Game;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * create an instance of this fragment.
 */
public class FiveStar extends Fragment {
    FlexboxLayout numberpan;
    ConnectQB connectQB = ConnectQB.getInstance();
    LinearLayout selectedNum;
    int counter = 0;
    String[] gamePlayed = new String[5];
    Button submit,clear,pick4me;
    public FiveStar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FiveStar.
     */



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_five_star, container, false);
        numberpan = (FlexboxLayout) view.findViewById(R.id.selecteNum);
        selectedNum = (LinearLayout) view.findViewById(R.id.gamesSeleted);
        submit = (Button) view.findViewById(R.id.submit);
        clear = (Button) view.findViewById(R.id.clear);
        pick4me = (Button) view.findViewById(R.id.pic4me);
        ((FirstMain)getActivity()).changeBack();
        connectQB.generateNumberPan(37, numberpan, getActivity(), 7, 15,new int[]{0},new selected() {
            @Override
            public void selectee(String number) {
                if(counter < 5){
                    LinearLayout edit = (LinearLayout) selectedNum.getChildAt(counter);
                    TextView se = new TextView(getContext());
                    se.setText(number);
                    se.setGravity(Gravity.CENTER_HORIZONTAL);
                    se.setTextSize(30);
                    se.setTextColor(getActivity().getResources().getColor(R.color.blueText2));
                    if(se.getText().length() == 1){
                        se.setPadding(7,5,0,0);
                    }
                    else{
                        se.setPadding(0,5,2,0);
                    }
                    edit.addView(se);
                    gamePlayed[counter] = number;
                    Button btnAddARoom = (Button) numberpan.getChildAt(Integer.parseInt(number) -1);
                    btnAddARoom.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.redcircle));
                    counter++;
                }
                else{
                    connectQB.openAlert(getContext(),getString(R.string.errorSelectfive),"Error");

                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gamePlayed[4] != null){
                    Fragment fr=new CashPage();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    Bundle args = new Bundle();
                    String[] info = new String[3];
                    info[0] = "FiveStar";
                    info[1] = "300";
                    info[2] = "300";
                    args.putCharSequenceArray("gameplayed",gamePlayed);
                    args.putCharSequenceArray("info",info);
                    fr.setArguments(args);
                    ft.replace(R.id.container, fr);
                    ft.commit();
                }
                else {
                 connectQB.openAlert(getContext(),"Please Select 5 numbers","Incomplete");
                }

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                tr.replace(R.id.container, new FiveStar());
                tr.commit();

            }
        });
        pick4me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = 5 - counter;
                if(counter < 6){
                    if(counter < 3){
                        Set set = connectQB.randomGeneratorSet(size,36,gamePlayed);
                        Iterator<Integer> iterator = set.iterator();
                        while (iterator.hasNext()){
                            LinearLayout edit = (LinearLayout) selectedNum.getChildAt(counter);
                            TextView se = new TextView(getContext());
                            se.setGravity(Gravity.CENTER_HORIZONTAL);
                            se.setTextSize(30);
                            se.setText(String.valueOf(iterator.next()));
                            se.setTextColor(getActivity().getResources().getColor(R.color.blueText2));
                            gamePlayed[counter] = se.getText().toString();
                            if(se.getText().length() == 1){
                                se.setPadding(7,5,0,0);
                            }
                            else{
                                se.setPadding(0,5,2,0);
                            }
                            edit.addView(se);


                            counter++;


                            Button layout = (Button) numberpan.getChildAt(Integer.parseInt(se.getText().toString())-1);
                            layout.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.redcircle));

                        }
                    }
                    else {
                        connectQB.openAlert(getContext(),getString(R.string.clearScrean),"Notice");
                    }

                }
                else {
                    connectQB.openAlert(getContext(),getString(R.string.errorSelectfive),"Error");
                }
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
