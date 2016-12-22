package com.shixels.activity.profile;


import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.model.QBUser;
import com.shixels.R;
import com.shixels.activity.Qblox.ConnectQB;
import com.shixels.activity.Qblox.SucessFunction;
import com.shixels.activity.adapters.NotificationAdapter;
import com.shixels.activity.model.NotificationModel;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FivestarList extends Fragment {
  ConnectQB connectQB = ConnectQB.getInstance();
    TextView error;

    public FivestarList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_fivestar_list, container, false);
        if(connectQB.isInternetAvailable(getContext())) {

            connectQB.runTimeSignin(getContext(), new SucessFunction() {
                @Override
                public void randomFuntion(final QBUser user) {
                    QBRequestGetBuilder requestBuilder2 = new QBRequestGetBuilder();
                    requestBuilder2.eq("id", user.getId());
                    requestBuilder2.sortDesc("created_at");
                    QBCustomObjects.getObjects("FiveStar", requestBuilder2, new QBEntityCallback<ArrayList<QBCustomObject>>() {
                        @Override
                        public void onSuccess(ArrayList<QBCustomObject> customObjects2, Bundle params) {
                            Log.i("burn",customObjects2.size() + "");
                            if(customObjects2.size() == 0){
                                error.setVisibility(View.VISIBLE);
                                error.setText("No History");
                                error.setTextColor(getActivity().getResources().getColor(R.color.blueText2));
                            }
                            setUpRecyclerView(view, customObjects2, user);
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
        else {
            connectQB.openAlert(getContext(),"check Your internet connection","Connection Error");
        }
        error = (TextView) view.findViewById(R.id.error);
        return  view;

    }
    private void setUpRecyclerView(View v, ArrayList<QBCustomObject> object, QBUser user) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

    }
}
