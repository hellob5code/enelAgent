package com.shixels.activity.Game;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.icu.text.NumberFormat;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.customobjects.QBCustomObjects;
import com.quickblox.customobjects.model.QBCustomObject;
import com.quickblox.users.model.QBUser;
import com.shixels.R;
import com.shixels.activity.FirstMain;
import com.shixels.activity.Qblox.ConnectQB;
import com.shixels.activity.Qblox.SucessFunction;
import com.shixels.helper.printer.PrintService;
import com.shixels.helper.printer.PrinterClass;
import com.shixels.printer.PrinterClassSerialPort;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * A simple {@link Fragment} subclass.
 */
public class Tickets extends Fragment {

    public static PrinterClassSerialPort printerClass = null;
    public Tickets() {
        // Required empty public constructor
    }

    ImageView barcode;
    QBCustomObject object;
    Button confirm;
    FlexboxLayout picks;
    TextView soldby,soldDate, gameId, amount, jackpot,nextdraw;
    String[] gamesPlayed;
    ScrollView ticketsPan;
    QBUser userinfo;
    ConnectQB connectQB = ConnectQB.getInstance();
    ImageView gametypeImg;
    int statusConfirmed ;
    int gameInt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle mBundle = new Bundle();
        mBundle = getArguments();
        String temp = mBundle.getString("info");
        String userString = mBundle.getString("user");
        Gson gson = new Gson();
        object = gson.fromJson(temp,QBCustomObject.class);
        userinfo = gson.fromJson(userString,QBUser.class);
        gamesPlayed = (String[]) mBundle.getCharSequenceArray("games");
        Double tempConfirm = (Double) object.get("status");
        statusConfirmed = tempConfirm.intValue();
        View view = inflater.inflate(R.layout.fragment_tickets, container, false);
        picks = (FlexboxLayout) view.findViewById(R.id.picks);
        ticketsPan = (ScrollView) view.findViewById(R.id.tickets);
        soldby = (TextView) view.findViewById(R.id.soldby);
        soldDate = (TextView) view.findViewById(R.id.soldDate);
        gameId = (TextView) view.findViewById(R.id.gameid);
        jackpot = (TextView) view.findViewById(R.id.jackpot);
        amount = (TextView) view.findViewById(R.id.amount);
        gametypeImg = (ImageView) view.findViewById(R.id.gametypeImg);
        confirm = (Button) view.findViewById(R.id.confirm);
        nextdraw = (TextView) view.findViewById(R.id.nextDraw);

        if(statusConfirmed == 0){
            Log.i("burn", String.valueOf(object.get("status").getClass().getName()));
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirm.setText("Please Wait");
                    if(connectQB.isInternetAvailable(getContext())){
                        connectQB.runTimeSignin(getContext(), new SucessFunction() {
                            @Override
                            public void randomFuntion(QBUser user) {
                                QBCustomObject record = new QBCustomObject();
                                record.setCustomObjectId(object.getCustomObjectId());
                                record.setClassName(object.getClassName());
                                record.putInteger("status",1);
                                QBCustomObjects.updateObject(record, new QBEntityCallback<QBCustomObject>() {
                                    @Override
                                    public void onSuccess(QBCustomObject qbCustomObject, Bundle bundle) {
                                        statusConfirmed = 1;
                                        confirm.setText("Confirmed");
                                        confirm.setOnClickListener(null);
                                        confirm.setEnabled(false);
                                    }

                                    @Override
                                    public void onError(QBResponseException e) {
                                        statusConfirmed = 0;
                                        confirm.setText("Error");
                                    }
                                });
                            }

                            @Override
                            public void error() {

                            }
                        });
                    }
                    else{
                        connectQB.openAlert(getContext(),"check Your internet connection","Connection Error");
                        confirm.setText(R.string.unconfirm);
                    }

                }
            });
        }
        else if( ((Double) object.get("status")).intValue() == 1){
            confirm.setText("Confirmed");
            confirm.setEnabled(false);
            statusConfirmed = 1;
        }

        Log.i("burn",gamesPlayed.length + "");

        soldby.setText(getActivity().getString(R.string.solddby)+" ("+ userinfo.getId() +")");
        soldDate.setText(object.getCreatedAt().toGMTString());






        gameId.setText(object.getCustomObjectId());
        amount.setText(object.get("amount") + "");
        //babz

        if(gamesPlayed.length == 3){
            gametypeImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.threestarlogo));
            jackpot.setText(getActivity().getString(R.string.threestarjackpot));
            gameInt = 3;
        }
        else if(gamesPlayed.length == 5){
            gametypeImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.fivestarlogo));
            jackpot.setText(getActivity().getString(R.string.fivestarjackpot));
            gameInt = 5;

        }
        else {
            gametypeImg.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.sixstarlogo));
            //Echo
            jackpot.setText(getActivity().getString(R.string.sixstarjackpot));
            gameInt = 6;

        }

        for(int i = 0; i < gamesPlayed.length;i++){
            final Button btnAddARoom = new Button(getActivity(), null, android.R.attr.buttonStyleSmall);
            btnAddARoom.setText(gamesPlayed[i]);
            btnAddARoom.setTextColor(Color.parseColor("#000000"));
            btnAddARoom.setTextSize(15);
            //echo
            btnAddARoom.setBackgroundDrawable(getActivity().getResources().getDrawable(R.drawable.ticketsballs));

            picks.addView(btnAddARoom);

        }
        barcode = (ImageView) view.findViewById(R.id.barcode);
        try {
            Bitmap bode = Create2DCode(object.getCustomObjectId());
            barcode.setImageBitmap(bode);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        Handler mhandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PrinterClass.MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        Log.i("burn", "readBuf:" + readBuf[0]);
                        if (readBuf[0] == 0x13) {
                            // PrintService.isFUll = true;
                            // ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_bufferfull));
                        } else if (readBuf[0] == 0x11) {
                            // PrintService.isFUll = false;
                            // ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_buffernull));
                        } else if (readBuf[0] == 0x08) {
                            ShowMsg(getResources().getString(
                                    R.string.str_printer_state)
                                    + ":"
                                    + getResources().getString(
                                    R.string.str_printer_nopaper));
                        } else if (readBuf[0] == 0x01) {
                            // ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_printing));
                        } else if (readBuf[0] == 0x04) {
                            ShowMsg(getResources().getString(
                                    R.string.str_printer_state)
                                    + ":"
                                    + getResources().getString(
                                    R.string.str_printer_hightemperature));
                        } else if (readBuf[0] == 0x02) {
                            ShowMsg(getResources().getString(
                                    R.string.str_printer_state)
                                    + ":"
                                    + getResources().getString(
                                    R.string.str_printer_lowpower));
                        } else {
                            String readMessage = new String(readBuf, 0, msg.arg1);
                            if (readMessage.contains("800"))// 80mm paper
                            {
                                PrintService.imageWidth = 72;
                                Toast.makeText(getContext(), "80mm",
                                        Toast.LENGTH_SHORT).show();
                            } else if (readMessage.contains("580"))// 58mm paper
                            {
                                PrintService.imageWidth = 72;
                                Toast.makeText(getContext(), "58mm",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                        break;
                    case PrinterClass.MESSAGE_STATE_CHANGE:// 6??l???
                        switch (msg.arg1) {
                            case PrinterClass.STATE_CONNECTED:// ???l??
                                break;
                            case PrinterClass.STATE_CONNECTING:// ????l??
                                Toast.makeText(getContext(),
                                        "STATE_CONNECTING", Toast.LENGTH_SHORT).show();
                                break;
                            case PrinterClass.STATE_LISTEN:
                            case PrinterClass.STATE_NONE:
                                break;
                            case PrinterClass.SUCCESS_CONNECT:
                                printerClass.write(new byte[] { 0x1b, 0x2b });// ??????????
                                Toast.makeText(getContext(),
                                        "SUCCESS_CONNECT", Toast.LENGTH_SHORT).show();
                                break;
                            case PrinterClass.FAILED_CONNECT:
                                Toast.makeText(getContext(),
                                        "FAILED_CONNECT", Toast.LENGTH_SHORT).show();

                                break;
                            case PrinterClass.LOSE_CONNECT:
                                Toast.makeText(getContext(), "LOSE_CONNECT",
                                        Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case PrinterClass.MESSAGE_WRITE:

                        break;
                }
                super.handleMessage(msg);
            }
        };

        printerClass = new PrinterClassSerialPort(mhandler);
        printerClass.open(getContext());
        nextdraw.setText(connectQB.getNextDrawDate(gameInt,object.getCreatedAt()));
        return  view;
    }
    public Bitmap Create2DCode(String str) throws WriterException {
        BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
             for (int x = 0; x < width; x++) {
                 if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                     }
             }
             }
         Bitmap bitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_8888);
        // Generated bitmap through Pixel group ,Details,Plz refer api
         bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
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
                else if(event.getAction() == KeyEvent.ACTION_UP && keyCode == 135){
                   if(statusConfirmed == 1){
                       Log.i("burn",ticketsPan.getHeight() + "");
                       Bitmap ticket = getBitmapFromView(ticketsPan.getChildAt(0));
                       Log.i("burn", ticket.getHeight() + ", "+ ticket.getWidth());
                       printerClass.printImage(ticket);
                   }
                    else {
                       connectQB.openAlert(getContext(),"Please Confirm the Ticket before Print","Ticket Confirmation");
                   }
                    //Intent intent = new Intent(getContext(),testprint.class);
                    //intent.putExtra("bit",ticket);
                   // getActivity().startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    private void ShowMsg(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public Bitmap getBitmapFromView2(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth,totalHeight , Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


}
