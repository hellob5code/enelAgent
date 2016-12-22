package com.shixels.activity.Game;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.print.PrintHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.shixels.printer.PrinterClassSerialPort;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 */
public class SixStar extends Fragment {
   FlexboxLayout numberpan;
    LinearLayout selectedNum;
    int counter = 0;
    Button submit,clear,pick4me;
    public  static PrinterClassSerialPort printerCLASS = null;
    String[] gamePlayed = new String[6];
    ConnectQB connectQB = ConnectQB.getInstance();

    public SixStar() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_six_star, container, false);
        numberpan = (FlexboxLayout) view.findViewById(R.id.selecteNum);
        selectedNum = (LinearLayout) view.findViewById(R.id.gameSeleted);
        pick4me = (Button) view.findViewById(R.id.pic4me);
        submit = (Button) view.findViewById(R.id.submit);
        printerCLASS = new PrinterClassSerialPort(new Handler());
        printerCLASS.open(getContext());
        clear = (Button) view.findViewById(R.id.clear);

        connectQB.generateNumberPan(50, numberpan, getActivity(), 7, 15,new int[]{0},new selected() {
            @Override
            public void selectee(String number) {
                if(counter < 6){
                    LinearLayout edit = (LinearLayout) selectedNum.getChildAt(counter);
                    TextView se = new TextView(getContext());
                    se.setText(number);
                    se.setGravity(Gravity.CENTER_HORIZONTAL);
                    se.setTextSize(30);
                    se.setTextColor(getActivity().getResources().getColor(R.color.blueText2));
                    if(se.getText().length() == 1){
                        se.setPadding(10,0,0,0);
                    }
                    else{
                        se.setPadding(5,0,0,0);
                    }
                    edit.addView(se);
                    gamePlayed[counter] = number;
                    Button btnAddARoom = (Button) numberpan.getChildAt(Integer.parseInt(number) -1);
                    btnAddARoom.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.blue_circle));
                    btnAddARoom.setTextColor(getActivity().getResources().getColor(R.color.white));
                    Log.i("burn", String.valueOf(gamePlayed[counter]));
                    counter++;
                }
                else{
                    connectQB.openAlert(getContext(),getString(R.string.errorSelectsix),"Error");
                }

            }
        });
        ((FirstMain)getActivity()).changeBack();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gamePlayed[5] != null){
                    Fragment fr=new XtraPlay();
                    FragmentManager fm=getFragmentManager();
                    FragmentTransaction ft=fm.beginTransaction();
                    Bundle args = new Bundle();
                    String[] info = new String[3];
                    info[0] = "SixStars";
                    info[1] = "500";
                    info[2] = "500";
                    args.putCharSequenceArray("gameplayed",gamePlayed);
                    args.putCharSequenceArray("info",info);
                    fr.setArguments(args);
                    ft.replace(R.id.container, fr);
                    ft.commit();
                }
                else {
                    connectQB.openAlert(getContext(),"Please Select 6 numbers","Incomplete");
                }

            }
        });
        pick4me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = 6 - counter;
                if(counter < 6){
                    if(counter < 4){
                        Set set = connectQB.randomGeneratorSet(size,41,gamePlayed);
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
                                se.setPadding(10,0,0,0);
                            }
                            else{
                                se.setPadding(5,0,0,0);
                            }
                            edit.addView(se);

                            counter++;


                            Button layout = (Button) numberpan.getChildAt(Integer.parseInt(se.getText().toString())-1 );
                            layout.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.blue_circle));
                            layout.setTextColor(Color.parseColor("#ffffff"));

                        }
                    }
                    else {
                        connectQB.openAlert(getContext(),getString(R.string.clearScrean),"Notice");
                    }
                }

                else {
                    connectQB.openAlert(getContext(),getString(R.string.errorSelectsix),"Error");
                }
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                tr.replace(R.id.container, new SixStar());
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
    private void doPhotoPrint() {
        PrintHelper photoPrinter = new PrintHelper(getActivity());
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.logo_big);
        photoPrinter.printBitmap("droids.jpg - test print", bitmap);
    }
}
