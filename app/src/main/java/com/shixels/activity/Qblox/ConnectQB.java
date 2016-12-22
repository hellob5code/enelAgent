package com.shixels.activity.Qblox;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;
import com.shixels.R;
import com.shixels.activity.Constant;
import com.shixels.activity.FirstMain;


import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * Created by thankgodrichard on 11/23/16.
 */
public class ConnectQB {

    private static ConnectQB ourInstance = new ConnectQB();

    public static ConnectQB getInstance() {
        return ourInstance;
    }

    private ConnectQB() {
    }
    public static final String APP_ID = "";
    public static final String AUTH_KEY = "";
    public static final String AUTH_SECRET = "";
    public static final String ACCOUNT_KEY = "";







    public void initQuickBlox(Context context) {
        //initializing quickblox with credentials
        QBSettings.getInstance().init(context, APP_ID, AUTH_KEY, AUTH_SECRET);
        QBSettings.getInstance().setAccountKey(ACCOUNT_KEY);
    }

    private void creatSession() {
        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success
                Log.i("sesion", "Created");
            }

            @Override
            public void onError(QBResponseException error) {
                // errors
                Log.i("session", "No created");
            }
        });
    }

    // register users
    public void Register(final String em, final String ps, final String username, final TextView v, final Context context) {
        // Register new
        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success
                Log.i("sesion", "Created");
                final QBUser user = new QBUser(username, ps);
                user.setEmail(em);
                final String[] temp = {username,ps};
                QBUsers.signUp(user, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser user, Bundle args) {
                        savePref(user,context,0,null);
                        savePref(null,context,1,temp);
                        Intent intent = new Intent(context, FirstMain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);

                    }

                    @Override
                    public void onError(QBResponseException error) {
                        // error
                        v.setText(String.valueOf(error).substring(49));
                        Log.e("Err", "error");
                    }
                });
            }

            @Override
            public void onError(QBResponseException error) {
                // errors
                Log.i("session", "No created");
            }
        });


    }


    public void startIntent(Context context1, Class aclass) {
        Intent intent = new Intent(context1, aclass);
        context1.startActivity(intent);
    }

    // to sign-in Users
    public void SignIn(final String username, final String ps, final TextView v, final Context context) {
        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success
                Log.i("sesion", "Created");
                final QBUser user = new QBUser(username, ps);
                 final String[] temp = {username,ps};
                QBUsers.signIn(user, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser user, Bundle args) {
                        v.setVisibility(View.VISIBLE);
                        v.setText("Please wiat....");
                        savePref(user,context,0,null);
                        savePref(null,context,1,temp);
                        Intent intent = new Intent(context, FirstMain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onError(QBResponseException error) {
                        // error
                        v.setVisibility(View.VISIBLE);
                        v.setTextSize(13);
                        v.setText("INVALID USERNAME OR PASSWORD");
                        Log.e("Err", String.valueOf(error));
                    }
                });
            }

            @Override
            public void onError(QBResponseException error) {
                // errors
                Log.i("session", "No created");
            }
        });





    }


    // PASS WRD VALIDATIONS

    interface PasswordRule {
        boolean passRule(String password);

        String failMessage();
    }

    abstract static class BaseRule implements PasswordRule {
        private final String message;

        BaseRule(String message) {
            this.message = message;
        }

        public String failMessage() {
            return message;
        }
    }

    private static final PasswordRule[] RULES = {
            new BaseRule("Password is too short. Needs to have 6 characters") {

                @Override
                public boolean passRule(String password) {
                    return password.length() >= 6;
                }
            },

            new BaseRule("Password needs an upper case") {

                private final Pattern ucletter = Pattern.compile(".*[\\p{Lu}].*");

                @Override
                public boolean passRule(String password) {
                    return ucletter.matcher(password).matches();
                }
            },

            /// .... more rules.
    };

    public static String passwordValidations(String pass1, String pass2) {
        if (pass1 == null || pass2 == null) {
            //logger.info("Passwords = null");
            return "Passwords Null \n ";
        }

        if (pass1.isEmpty() || pass2.isEmpty()) {
            return "Empty fields \n ";
        }

        if (!pass1.equals(pass2)) {
            //logger.info(pass1 + " != " + pass2);
            return "Passwords don't match \n";
        }

        StringBuilder retVal = new StringBuilder();

        boolean pass = true;
        for (PasswordRule rule : RULES) {
            if (!rule.passRule(pass1)) {
                // logger.info(pass + "<--- " + rule.failMessage());
                retVal.append(rule.failMessage()).append(" \n");
                pass = false;
            }
        }

        return pass ? "success" : retVal.toString();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }

    }


    public void forgotPassword(String email, final TextView msgSet) {
        QBUsers.resetPassword(email, new QBEntityCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid, Bundle bundle) {
                msgSet.setText(R.string.after_passwor_request_onsuccess);
            }

            @Override
            public void onError(QBResponseException e) {
                msgSet.setText("Error: " + String.valueOf(e).substring(49));
            }
        });

    }

    public static boolean isInternetAvailable(Context context) {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            return false;
        } else {
            if (info.isConnected()) {
                return true;
            } else {
                return true;
            }

        }
    }

    private void savePref(QBUser user, Context context , int  type, String[] login){
        SharedPreferences pref = context.getSharedPreferences(Constant.Pref, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        if(type == 1){
            String loginInfo = gson.toJson(login,String[].class);
            editor.putString("login",loginInfo);
            editor.commit();
        }
        else {
            String info = gson.toJson(user, QBUser.class);
            editor.putString("info", info);
            editor.commit();
        }
    }

    public void generateNumberPan(final int max, final FlexboxLayout flexboxLayout, final Activity activity, int btnHeight, int btnWidth, final int[] counter, final selected selc){
        Drawable background = activity.getResources().getDrawable(R.drawable.numbers_bakground);

        for(int i = 1; i < max; i++ ){
            final Button btnAddARoom = new Button(activity, null, android.R.attr.buttonStyleSmall);
            btnAddARoom.setText(i + "");
            btnAddARoom.setHeight(btnHeight);
            btnAddARoom.setWidth(btnWidth);
            btnAddARoom.setBackgroundDrawable(background);
            btnAddARoom.setTypeface(null, Typeface.BOLD);
            btnAddARoom.setTextSize(18);
            btnAddARoom.setTextColor(activity.getResources().getColor(R.color.blueText2));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(10,10,10,10);
           btnAddARoom.setLayoutParams(params);
            btnAddARoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selc.selectee(btnAddARoom.getText().toString());
                    btnAddARoom.setOnClickListener(null);

                }
            });
            flexboxLayout.addView(btnAddARoom);

        }

    }

    public void runTimeSignin(Context context, final SucessFunction function){
        SharedPreferences pref = context.getSharedPreferences(Constant.Pref, context.MODE_PRIVATE);
        Gson gson = new Gson();
        String temp = pref.getString("login","");
        final String[] loginInfo = gson.fromJson(temp,String[].class);
        initQuickBlox(context);
        QBAuth.createSession(new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession session, Bundle params) {
                // success
                Log.i("sesion", "Created");
                final QBUser user = new QBUser(loginInfo[0], loginInfo[1]);
                QBUsers.signIn(user, new QBEntityCallback<QBUser>() {
                    @Override
                    public void onSuccess(QBUser user, Bundle args) {
                        function.randomFuntion(user);
                    }

                    @Override
                    public void onError(QBResponseException error) {
                        function.error();
                    }
                });
            }

            @Override
            public void onError(QBResponseException error) {
                // errors
                Log.i("session", "No created");
            }
        });


    }
    public void clear(LinearLayout selectedNum){
        for(int i = 0; i < selectedNum.getChildCount(); i++){
            LinearLayout edit = (LinearLayout) selectedNum.getChildAt(i);
            edit.removeAllViews();
        }
    }
    public static void setBackgroundColorAndRetainShape(final int color, final Drawable background) {

        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background.mutate()).getPaint().setColor(color);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background.mutate()).setColor(color);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background.mutate()).setColor(color);
        }else{
            Log.w("burn","Not a valid background type");
        }

    }

    public Set randomGeneratorSet(int size,int range,String[] exclude){
        Random random = new Random();

        Set set = new HashSet<Integer>(size);

        while(set.size()< size) {
            if(exclude[0] != null) {
                    int r = random.nextInt(range - 1) + 1;
                    if ((r != Integer.parseInt(exclude[0]))) {

                        set.add(r);
                    }
                for(int i = 1; i < exclude.length; i++){
                    if(exclude[i] != null){
                        int s = random.nextInt(range - 1) + 1;
                        if ((s != Integer.parseInt(exclude[0]))) {
                            set.add(s);
                        }
                    }
                }
            }
            else {
                while (set.add(random.nextInt(range - 1) + 1) != true);
            }
        }
        assert set.size() == size;
        return set;
    }
    public void doClear(Fragment frag){


    }

    private  void readToArray(String line, int[] resultArray) {
        int index = 0;
        int number = 0;

        for (int i = 0, n = line.length(); i < n; i++) {
            char c = line.charAt(i);
            if (c == ',') {
                resultArray[index] = number;
                index++;
                number = 0;
            }
            else if (Character.isDigit(c)) {
                int digit = Character.getNumericValue(c);
                number = number * 10 + digit;
            }
        }

        if (index < resultArray.length) {
            resultArray[index] = number;
        }
    }

    public  int[] toArray(String line) {
        int[] result = new int[countOccurrences(line, ',') + 1];
        readToArray(line, result);
        return result;
    }

    private int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i=0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

    public void openAlert(final Context context, String message, String title){
        new AlertDialog.Builder(context) .setTitle(title)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();




    }
    public String getNextDrawDate(int gametype , Date date){
        DateFormat df = new SimpleDateFormat("kk");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        String time = df.format(date);
        if(gametype == 3){
            if(date.getDay() == 1){
                if(Integer.parseInt(time) < 20) {
                   return noNext(date);
                }
                return nextDate(Calendar.WEDNESDAY,date);

            }
            else if(date.getDay() == 2){
              return   nextDate(Calendar.WEDNESDAY,date);
            }
            else if(date.getDay() == 3){
                if(Integer.parseInt(time) < 20){
                   return noNext(date);
                }
              return nextDate(Calendar.MONDAY,date);
            }
            else if(date.getDay() == 4){
               return nextDate(Calendar.MONDAY,date);
            }
            else if(date.getDay() == 5){
                return nextDate(Calendar.MONDAY,date);
            }
            else if(date.getDay() == 6){
                return nextDate(Calendar.MONDAY,date);
            }
            else if(date.getDay() == 7){
                 return nextDate(Calendar.MONDAY,date);
            }
        }
        else if(gametype == 5){
            if(date.getDay() == 1){
                return nextDate(Calendar.TUESDAY,date);
            }
            else if(date.getDay() == 2){
                if(Integer.parseInt(time) < 20){
                    return noNext(date);
                }
               return nextDate(Calendar.THURSDAY,date);
            }
            else if(date.getDay() == 3){
                return  nextDate(Calendar.THURSDAY,date);
            }
            else if(date.getDay() == 4){
                if(Integer.parseInt(time) < 20){
                 return    noNext(date);
                }

            }
            else if(date.getDay() == 5){
               return nextDate(Calendar.TUESDAY,date);
            }
            else if(date.getDay() == 6){
                return  nextDate(Calendar.TUESDAY,date);
            }
            else if(date.getDay() == 7){
                return  nextDate(Calendar.TUESDAY,date);
            }
        }
        else if(gametype == 6){
             if(date.getDay() == 5){
                 if(Integer.parseInt(time) < 20){
                     return noNext(date);
                 }
                 return nextDate(Calendar.FRIDAY,date);
            }
            return nextDate(Calendar.FRIDAY,date);

        }

        return null;
    }

 private String nextDate(int calendarday, Date date){
     Calendar date1 = Calendar.getInstance();
     date1.setTime(date);
     DateFormatSymbols dfs = new DateFormatSymbols();
     String[] months = dfs.getMonths();


     while (date1.get(Calendar.DAY_OF_WEEK) != calendarday) {
         date1.add(Calendar.DATE, 1);
     }


     return date1.getTime().getDate() + " " + months[date1.getTime().getMonth()] + " " + date1.get(Calendar.YEAR);
 }

    private String noNext(Date date){
        Calendar date1 = Calendar.getInstance();
        date1.setTime(date);
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();

        return date1.getTime().getDate() + " " + months[date1.getTime().getMonth()] + " " + date1.get(Calendar.YEAR);

    }

}
