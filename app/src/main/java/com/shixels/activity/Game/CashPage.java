package com.shixels.activity.Game;


import android.graphics.Canvas;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.model.QBUser;
import com.shixels.R;
import com.shixels.activity.FirstMain;
import com.shixels.activity.Qblox.ConnectQB;
import com.shixels.activity.Qblox.SucessFunction;

import org.w3c.dom.Text;

import java.util.Arrays;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class CashPage extends Fragment {

    Button cash;
    String[] gamePlayed,info;
    FlexboxLayout picks;
    LinearLayout mainContent;
    TextView price, total, xprice;
    boolean xply;
    View xpline;
    RelativeLayout xtraplay;
    ConnectQB connectQB = ConnectQB.getInstance();
    public CashPage() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        gamePlayed = (String[]) getArguments().getCharSequenceArray("gameplayed");
        info = (String[]) getArguments().getCharSequenceArray("info");
        if(getArguments().getBoolean("xtra")){
            xply = getArguments().getBoolean("xtra");
        }

        View view = inflater.inflate(R.layout.fragment_cash_page, container, false);
        picks = (FlexboxLayout) view.findViewById(R.id.picks);
        price = (TextView) view.findViewById(R.id.price);
        mainContent = (LinearLayout) view.findViewById(R.id.mainContent);
        total = (TextView) view.findViewById(R.id.total);
        xtraplay = (RelativeLayout) view.findViewById(R.id.xpContainer);
        xpline = view.findViewById(R.id.xpline);
        xprice = (TextView) view.findViewById(R.id.xprice);
        price.setText(info[1]);
        total.setText(info[2]);
          if(xply){
              int main = Integer.parseInt(info[2]) - Integer.parseInt(info[1]);
              xprice.setText("" + main);
              xpline.setVisibility(View.VISIBLE);
              xtraplay.setVisibility(View.VISIBLE);
          }

        for(int i = 0; i < gamePlayed.length;i++){
            final Button btnAddARoom = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);
            btnAddARoom.setText(gamePlayed[i]);
            btnAddARoom.setTextColor(getActivity().getResources().getColor(R.color.white));
            btnAddARoom.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.redcircle));

            picks.addView(btnAddARoom);

        }

        cash = (Button) view.findViewById(R.id.cash);

        cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = getRotateDrawable(getActivity().getResources().getDrawable(R.drawable.spinner_i_c_o_n), 20);
                cash.setCompoundDrawables(drawable, null, null, null);
                cash.setText("Submiting");
                if (connectQB.isInternetAvailable(getContext())) {
                    connectQB.runTimeSignin(getContext(), new SucessFunction() {
                        @Override
                        public void randomFuntion(final QBUser user) {
                            QBCustomObject object = new QBCustomObject();
                            object.putString("gamesPlayed", Arrays.toString(gamePlayed));
                            object.putString("amount", info[2]);
                            object.putInteger("status", 0);
                            if (xply) {
                                object.putBoolean("xtraplay", xply);
                            }
                            //onk
                            object.setClassName(info[0]);
                            QBCustomObjects.createObject(object, new QBEntityCallback<QBCustomObject>() {
                                @Override
                                public void onSuccess(QBCustomObject qbCustomObject, Bundle bundle) {
                                    Fragment fr = new Tickets();
                                    FragmentManager fm = getFragmentManager();
                                    FragmentTransaction ft = fm.beginTransaction();
                                    Bundle args = new Bundle();
                                    Gson gson = new Gson();
                                    String userinfo = gson.toJson(user, QBUser.class);
                                    String temp = gson.toJson(qbCustomObject, QBCustomObject.class);
                                    args.putString("info", temp);
                                    args.putString("user", userinfo);
                                    args.putCharSequenceArray("games", gamePlayed);
                                    fr.setArguments(args);
                                    ft.replace(R.id.container, fr);
                                    ft.commit();
                                }

                                @Override
                                public void onError(QBResponseException e) {
                                    cash.setVisibility(View.GONE);
                                    TextView tv = new TextView(getContext());
                                    tv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    tv.setText(getActivity().getResources().getString(R.string.error_unknown));
                                    mainContent.addView(tv);

                                }
                            });
                        }

                        @Override
                        public void error() {
                            cash.setVisibility(View.GONE);
                            TextView tv = new TextView(getContext());
                            tv.setGravity(Gravity.CENTER_HORIZONTAL);
                            tv.setText(getActivity().getResources().getString(R.string.error_unknown));
                            mainContent.addView(tv);
                        }

                    });

                }
                else{
                    connectQB.openAlert(getContext(),"check Your internet connection","Connection Error");
                    cash.setText("Submit");
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
    private Drawable getRotateDrawable(final Drawable d, final float angle) {
        final Drawable[] arD = { d };
        return new LayerDrawable(arD) {
            @Override
            public void draw(final Canvas canvas) {
                canvas.save();
                canvas.rotate(angle, d.getBounds().width() / 2, d.getBounds().height() / 2);
                super.draw(canvas);
                canvas.restore();
            }
        };
    }


}
