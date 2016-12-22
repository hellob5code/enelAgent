package com.shixels.activity;

import java.io.DataOutputStream;
import java.io.IOException;

import com.shixels.R;
import com.shixels.Service.CaptureService;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity {
	private GridView gridView;
	TextView textViewBat;

	private int[] mImageIds = { R.drawable.printer, R.drawable.qrcode,
			R.drawable.camera, R.drawable.serialport,R.drawable.finger_print

	};

	private int[] TitleTexts = { R.string.str_print, R.string.str_qrcode,
			R.string.str_camera, R.string.str_wireless,R.string.str_finger_print };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textViewBat = (TextView) findViewById(R.id.textViewBat);

		gridView = (GridView) findViewById(R.id.GridViewId);
		gridView.setAdapter(new gridViewAdapter(mImageIds, TitleTexts));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == 0) {
					startActivity(new Intent(MainActivity.this, PrintDemo.class));
				} else if (arg2 == 1) {
					startActivity(new Intent(MainActivity.this,
							com.shixels.barvodeScan.MainActivity.class));
				} else if (arg2 == 2) {
					startActivity(new Intent(MainActivity.this,
							CameraDemo.class));
				} else if (arg2 == 3) {
					startActivity(new Intent(MainActivity.this,
							SerialPortActivity.class));
				}else if (arg2 == 4) {
					startActivity(new Intent(MainActivity.this,
							FingerActivity.class));
				}
			}

		});
		////new ThreadShowBat().start();
		
		////shutdown();
	}

	private class ThreadShowBat extends Thread {
		@Override
		public void run() {
			while (!isInterrupted()) {
				textViewBat.post(new Runnable() {
					@Override
					public void run() {
						textViewBat.setText("BATTERY:"
								+ CaptureService.batLevel);
					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public class gridViewAdapter extends BaseAdapter {
		private View[] itemViews;

		public gridViewAdapter(int[] mImageIds, int[] TitleTexts) {
			itemViews = new View[mImageIds.length];

			for (int i = 0; i < itemViews.length; i++) {
				itemViews[i] = makeItemView(mImageIds[i], TitleTexts[i]);

			}

		}

		private View makeItemView(int mImageIds, int titleTexts) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) MainActivity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View itemView = inflater.inflate(R.layout.menuitem, null);
			TextView title = (TextView) itemView.findViewById(R.id.TextItemId);
			title.setText(titleTexts);
			ImageView image = (ImageView) itemView
					.findViewById(R.id.ImageItemId);
			image.setImageResource(mImageIds);
			image.setScaleType(ImageView.ScaleType.FIT_CENTER);
			return itemView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return itemViews.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return itemViews[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null)
				return itemViews[position];
			return convertView;
		}
	}
	
	/** 
	     * �ػ�ķ�������Ҫ�ֻ���rootȨ�� 
	     */   
	    private void shutdown() {   
	        try {   
	            Process process = Runtime.getRuntime().exec("su");   
	            DataOutputStream out = new DataOutputStream(   
	                    process.getOutputStream());   
	            out.writeBytes("reboot -p\n");   
	            out.writeBytes("exit\n");   
	            out.flush();   
	        } catch (IOException e) {   
	            e.printStackTrace();   
	        }   
	    }   

}
