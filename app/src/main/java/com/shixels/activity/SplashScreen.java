package com.shixels.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.shixels.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{

                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    String device_id = tm.getDeviceId();

                    if(checkDevice(device_id)){
                        SharedPreferences sharedPreferences = getSharedPreferences(Constant.Pref,MODE_PRIVATE);
                        String info = (sharedPreferences.getString("login",""));
                        if(info.length() > 0){
                            openUpAction(2,info);
                        }
                        else{
                            Intent intent = new Intent(SplashScreen.this, Authentication.class);
                            startActivity(intent);
                        }
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (!isFinishing()){
                                    new AlertDialog.Builder(SplashScreen.this)
                                            .setTitle("Notice")
                                            .setMessage("You cant use this app on this device")
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    openUpAction(1,"");
                                                }
                                            }).show();
                                }
                            }
                        });

                    }
                }
            }
        };
        timerThread.start();






    }

    private boolean checkDevice(String id){
        long deviceImei = 3592610509L;
        long temId = Long.parseLong(id.substring(0,10));
        Log.i("burn", temId + "");

        if(temId == deviceImei){
            return true;
        }
        else{
            return false;
        }

    }
    private void openUpAction(int i, String ii){
        if(i == 1){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=com.shixels.enellotto"));
            startActivity(intent);
            finish();

        }
        else{
            Intent intent = new Intent(this,FirstMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if(ii.length() > 1){
                intent.putExtra("info",ii);
            }
            startActivity(intent);
            finish();

        }
    }


}
