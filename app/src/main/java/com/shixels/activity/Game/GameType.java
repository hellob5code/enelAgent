package com.shixels.activity.Game;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shixels.R;
import com.shixels.activity.FirstMain;

import mehdi.sakout.fancybuttons.FancyButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class GameType extends Fragment implements View.OnClickListener{

    FancyButton threestar, sixstar, fivestar;
    public GameType() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_type, container, false);
        threestar = (FancyButton) view.findViewById(R.id.threestar);
        threestar.setOnClickListener(this);
        fivestar = (FancyButton) view.findViewById(R.id.fivestar);
        fivestar.setOnClickListener(this);
        sixstar = (FancyButton) view.findViewById(R.id.sixstar);
        sixstar.setOnClickListener(this);

        ((FirstMain)getActivity()).resetMenu();

        return view;
    }
    @Override
    public void onClick(View view) {
        Fragment frag = null;
        switch (view.getId()){
            case R.id.threestar:
                frag = new ThreeStar();
                ((FirstMain)getActivity()).openFragment(frag,R.id.container,"Threestar");
                break;
            case R.id.fivestar:
                frag = new FiveStar();
                ((FirstMain)getActivity()).openFragment(frag,R.id.container,"FiveStar");
                break;
            case R.id.sixstar:
                frag = new SixStar();
                ((FirstMain)getActivity()).openFragment(frag,R.id.container,"sixStar");
                //log
                break;
        }
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
                    getActivity().finish();
                    return true;
                }
                else if(event.getAction() == KeyEvent.ACTION_UP && keyCode == 22){
                    ((FirstMain)getContext()).openDrawer(true);
                    return true;
                }
                else if(event.getAction() == KeyEvent.ACTION_UP && keyCode == 21){
                    ((FirstMain)getContext()).openDrawer(false);
                    return true;
                }

                return false;
            }
        });
    }

}
