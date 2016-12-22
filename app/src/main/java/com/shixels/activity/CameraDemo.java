package com.shixels.activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.shixels.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class CameraDemo extends Activity {

	public Button btn1=null;
    public ImageView iamge1=null;
    private final int camera = 1;
    String imageFilePath="";
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camerademo);
        btn1=(Button)findViewById(R.id.btnclean);
        iamge1=(ImageView)findViewById(R.id.imageView1);
        
        if(btn1!=null)
        {
        	btn1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					openCamera();
				}
			});
        }

		openCamera();
	}
	
	private void openCamera()
	{
		imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mypicture.jpg";  
        File imageFile = new File(imageFilePath);  
        Uri imageFileUri = Uri.fromFile(imageFile);  
        Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
        i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);  
        startActivityForResult(i, camera); 	
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (camera == requestCode) {
				String sdStatus = Environment.getExternalStorageState();
				if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // ºÏ≤‚sd «∑Òø…”√
					Log.i("TestFile",
							"SD card is not avaiable/writeable right now.");
					return;
				}			
				
				Bitmap bitmap = null;
                try {
                FileInputStream fis = new FileInputStream(imageFilePath);
                bitmap = BitmapFactory.decodeStream(fis);
                } catch (FileNotFoundException e) {
                e.printStackTrace();
                }
                iamge1.setImageBitmap(bitmap);
			}

		}

	}
	
	
}