package com.shixels.activity.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.quickblox.core.Consts;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.model.QBUser;
import com.shixels.R;
import com.shixels.activity.FirstMain;
import com.shixels.activity.Game.GameType;
import com.shixels.activity.Qblox.ConnectQB;
import com.shixels.activity.Qblox.SucessFunction;
import com.shixels.activity.adapters.NotificationAdapter;
import com.shixels.activity.model.NotificationModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class TicketsHistory extends Fragment implements AdapterView.OnItemSelectedListener {


    public TicketsHistory() {
        // Required empty public constructor
    }
    ConnectQB connectQB = ConnectQB.getInstance();
    private Spinner spinner;
    private static final String[]paths = {"TICKET HISTORY (3 STAR)", "TICKET HISTORY (5 STAR)", "TICKET HISTORY (6 STAR)"};
    LinearLayout container2;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((FirstMain)getActivity()).changeBack();
        final View view = inflater.inflate(R.layout.fragment_tickets_history, container, false);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        container2 = (LinearLayout) view.findViewById(R.id.containerFrag);
        connectQB.runTimeSignin(getContext(), new SucessFunction() {
            @Override
            public void randomFuntion(final QBUser user) {


                    final QBRequestGetBuilder requestBuilder = new QBRequestGetBuilder();
                    requestBuilder.eq("id", user.getId());
                    requestBuilder.sortDesc("created_at");
                    QBCustomObjects.getObjects("ThreeStar", requestBuilder, new QBEntityCallback<ArrayList<QBCustomObject>>() {
                        @Override
                        public void onSuccess(ArrayList<QBCustomObject> customObjects, Bundle params) {
                            setUpRecyclerView(view,customObjects,user);
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
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

    private void setUpRecyclerView(View v, ArrayList<QBCustomObject> object,QBUser user) {
        ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progress);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        NotificationAdapter adapter = new NotificationAdapter(getContext(), NotificationModel.getData(object),v);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(getContext()); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we dont use it then also our items shows default animation. #Check Docs
        progressBar.setVisibility(View.GONE);
    }
    public static final long MAGIC=86400000L;

    public int DateToDays (Date date){
        return (int) (date.getTime()/MAGIC);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                container2.removeAllViews();
              Fragment   fragment = new ThreestarList();
                ((FirstMain)getActivity()).openFragment(fragment,R.id.containerFrag,"threestarlist");
                break;
            case 1:
                container2.removeAllViews();
               Fragment fragment2 = new FivestarList();

                ((FirstMain)getActivity()).openFragment(fragment2,R.id.containerFrag,"fivestarlist");
                break;
            case 2:
                container2.removeAllViews();
               Fragment fragment3 = new SIxstarList();
                ((FirstMain)getActivity()).openFragment(fragment3,R.id.containerFrag,"sixstarlist");
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
