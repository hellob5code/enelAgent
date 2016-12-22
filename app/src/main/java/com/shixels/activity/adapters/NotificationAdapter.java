package com.shixels.activity.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.shixels.activity.Game.Tickets;
import com.shixels.activity.Qblox.ConnectQB;
import com.shixels.activity.Qblox.SucessFunction;
import com.shixels.activity.model.NotificationModel;

import java.util.Arrays;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    List<NotificationModel> mData;
    private LayoutInflater inflater;
    ConnectQB connectQB = ConnectQB.getInstance();
    Context cont;
    Activity frag;
    View xView;
    String[] gamePlayedArr;

    public NotificationAdapter(Context context, List<NotificationModel> data, View fragment) {
        inflater = LayoutInflater.from(context);
        this.cont = context;
        this.mData = data;
        this.xView = fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_notification, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationModel current = mData.get(position);
        holder.setData(current, position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView gametype;
        TextView solddate;
        FlexboxLayout flexboxLayout;;;;;;;
        int position;
        LinearLayout viewItem;
        NotificationModel current;

        public MyViewHolder(View itemView) {
            super(itemView);
            solddate = (TextView) itemView.findViewById(R.id.soldDate);
            gametype = (ImageView) itemView.findViewById(R.id.gametypeImg);
            flexboxLayout = (FlexboxLayout) itemView.findViewById(R.id.gamesSeleted);
            viewItem = (LinearLayout) itemView.findViewById(R.id.viewItem);
        }

        public void setData(final NotificationModel current, int position) {
            Drawable drawable = null;
            if(current.getClassname() == "FiveStar"){
                this.gametype.setImageResource(R.drawable.fivestarlogo);
                drawable = cont.getResources().getDrawable(R.drawable.yellow_circle);
            }
            if(current.getClassname() == "SixStars"){
                this.gametype.setImageResource(R.drawable.sixstarlogo);
                drawable = cont.getResources().getDrawable(R.drawable.redcircle);

            }
            if(current.getClassname() == "ThreeStar"){
                this.gametype.setImageResource(R.drawable.threestarlogo);
                drawable = cont.getResources().getDrawable(R.drawable.blue_circle);
            }
            this.flexboxLayout.removeAllViews();
            String str = current.getGamesPlayed();
            int[] gamesPlayed = connectQB.toArray(str);
            gamePlayedArr = new String[gamesPlayed.length];
            for(int i =0; i < gamesPlayed.length; i++){
                Button btnAdd = new Button(cont);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40,40);
                btnAdd.setLayoutParams(params);
                btnAdd.setBackgroundDrawable(drawable);
                btnAdd.setText(gamesPlayed[i] + "");
                btnAdd.setTextSize(10);
                btnAdd.setTextColor(Color.parseColor("#ffffff"));
                this.flexboxLayout.addView(btnAdd);
            }
            for(int i = 0; i < gamesPlayed.length; i++) {
                gamePlayedArr[i] = gamesPlayed[i] + "";
            }
            this.position = position;
            this.current = current;
            this.solddate.setText(current.getDescription());
            this.viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProgressBar progressBar = (ProgressBar) xView.findViewById(R.id.progress);
                    RecyclerView recyclerView = (RecyclerView) xView.findViewById(R.id.recyclerView);
                    progressBar.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                  connectQB.runTimeSignin(cont, new SucessFunction() {
                      @Override
                      public void randomFuntion(final QBUser user) {
                          QBCustomObject customObject = new QBCustomObject(current.getClassname(), current.getQbCustomObject().getCustomObjectId());

                          QBCustomObjects.getObject(customObject, new QBEntityCallback<QBCustomObject>(){
                              @Override
                              public void onSuccess(QBCustomObject customObject, Bundle params) {
                                  Fragment fr=new Tickets();
                                  Bundle args = new Bundle();
                                  Gson gson = new Gson();
                                  String userinfo = gson.toJson(user,QBUser.class);
                                  String temp = gson.toJson(customObject,QBCustomObject.class);
                                  args.putString("info",temp);
                                  args.putString("user",userinfo);
                                  args.putCharSequenceArray("games",gamePlayedArr);
                                  fr.setArguments(args);
                                  ((FirstMain)cont).openFragment(fr,R.id.container,"tickets");
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
            });
        }
    }
}
