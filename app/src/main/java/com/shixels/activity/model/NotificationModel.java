package com.shixels.activity.model;


import android.util.Log;
import android.view.View;

import com.quickblox.customobjects.model.QBCustomObject;
import com.shixels.R;

import java.util.ArrayList;

/**
 * Created by thankgodrichard on 10/5/16.
 */
public class NotificationModel {


    private String title;
    private String description;
    private String gamesPlayed;
    private String classname;
    private QBCustomObject qbCustomObject;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArrayList<NotificationModel> getData(ArrayList<QBCustomObject> objects) {

        ArrayList<NotificationModel> dataList = new ArrayList<>();

        if(objects != null){
            if(objects.size() >= 1){

                for (int i = 0; i < objects.size(); i++) {

                    NotificationModel landscape = new NotificationModel();
                    Log.i("burn",objects.get(i).getClassName());
                    landscape.setGamesPlayed((String) objects.get(i).get("gamesPlayed"));
                    landscape.setDescription(objects.get(i).getCreatedAt().toGMTString());
                    landscape.setClassname(objects.get(i).getClassName());
                    landscape.setQbCustomObject(objects.get(i));
                    dataList.add(landscape);

                }

            }
        }
        return dataList;
    }

    public String getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(String gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public QBCustomObject getQbCustomObject() {
        return qbCustomObject;
    }

    public void setQbCustomObject(QBCustomObject qbCustomObject) {
        this.qbCustomObject = qbCustomObject;
    }
}
