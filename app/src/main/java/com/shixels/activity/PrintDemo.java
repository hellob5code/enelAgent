package com.shixels.activity;

import java.io.UnsupportedEncodingException;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.shixels.R;
import com.shixels.helper.printer.BarcodeCreater;
import com.shixels.helper.printer.PrintService;
import com.shixels.helper.printer.PrinterClass;
import com.shixels.printer.PrinterClassSerialPort;

public class PrintDemo extends Activity {
	public static PrinterClassSerialPort printerClass = null;
	private static final int REQUEST_EX = 1;
	protected static final String TAG = "PrintDemo";

	boolean isPrint = true;

	private Button btnUnicode;// Print by Unicode

	private EditText et_input = null;

	private Button btnPrint = null;

	private Button btnOpenPic = null;

	private Button btnPrintPic = null;

	private ImageView iv = null;

	private String picPath = "";// ��ͼƬ�����·��
	private Bitmap btMap = null;// ����ͼƬ

	private Button btnQrCode = null;
	private Button btnBarCode = null;

	private Button btnWordToPic = null;

	private Button btnSendCMD = null;

	String thread = "readThread";
	String text = "打印测试数据\r\nabcdefghijklmnopqrstuvw";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.printdemo);

		et_input = (EditText) findViewById(R.id.editText1);
		btnUnicode = (Button) findViewById(R.id.btnUnicode);
		btnPrint = (Button) findViewById(R.id.btnPrint);

		et_input.setText(text);

		btnOpenPic = (Button) findViewById(R.id.btnOpenPic);
		btnPrintPic = (Button) findViewById(R.id.btnPrintPic);

		iv = (ImageView) findViewById(R.id.iv_test);

		btnQrCode = (Button) findViewById(R.id.btnQrCode);
		btnBarCode = (Button) findViewById(R.id.btnBarCode);

		btnWordToPic = (Button) findViewById(R.id.btnWordToPic);

		btnSendCMD = (Button) findViewById(R.id.btnSendCMD);

		if (btnQrCode != null) {
			btnQrCode.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					String message = "http://www.google.com";
					String str = et_input.getText().toString();
					if (str != null & str != "") {
						message = str;
					}
					if (message.length() > 0) {
						try {
							message = new String(message.getBytes("utf8"));
						} catch (UnsupportedEncodingException e) {
							Log.e(TAG, e.getMessage());
						}
						btMap = BarcodeCreater.encode2dAsBitmap(message, 384,
								384, 2);
						// BarcodeCreater.saveBitmap2file(btMap, "mypic1.JPEG");
						iv.setImageBitmap(btMap);

					}
				}
			});
		}
		if (btnWordToPic != null) {
			btnWordToPic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String str = et_input.getText().toString();
					btMap = Bitmap.createBitmap(384,
							et_input.getLineCount() * 25, Config.ARGB_8888);
					Canvas canvas = new Canvas(btMap);
					canvas.drawColor(Color.WHITE);
					TextPaint textPaint = new TextPaint();
					textPaint.setStyle(Paint.Style.FILL);
					textPaint.setColor(Color.BLACK);
					textPaint.setTextSize(25.0F);
					StaticLayout layout = new StaticLayout(str, textPaint,
							btMap.getWidth(), Alignment.ALIGN_NORMAL,
							(float) 1.0, (float) 0.0, true);

					layout.draw(canvas);

					iv.setImageBitmap(btMap);

				}
			});
		}

		if (btnBarCode != null) {
			btnBarCode.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String message = "123456789";
					String str = et_input.getText().toString().trim();
					if (str != null & str != "") {
						message = str;
					}
					if (message.getBytes().length > message.length()) {
						Toast.makeText(
								PrintDemo.this,
								PrintDemo.this.getResources().getString(
										R.string.str_cannotcreatebar), 2000)
								.show();
						return;
					}
					if (message.length() > 0) {

						try {
							btMap = BarcodeCreater.creatBarcode(PrintDemo.this,
									message, 384, 80, true, 1);
							iv.setImageBitmap(btMap);
						} catch (Exception ex) {
							Toast.makeText(PrintDemo.this, ex.getMessage(),
									2000).show();
						}
					}
				}
			});
			if (btnPrint != null) {
				btnPrint.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						String str = et_input.getText().toString();
						try {
							 printerClass.printText(str);

						} catch (Exception e) {
							Log.e(TAG, e.getMessage());
						}

					}
				});
			}
			if (btnUnicode != null) {
				btnUnicode.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						String str = et_input.getText().toString();
						try {
							printerClass.printUnicode(str);
						} catch (Exception e) {
							Log.e(TAG, e.getMessage());
						}
					}
				});
			}
			if (btnOpenPic != null) {
				btnOpenPic.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(
								Intent.ACTION_PICK,
								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(intent, REQUEST_EX);
					}
				});
			}
			if (btnPrintPic != null) {
				btnPrintPic.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (btMap != null) {
							printerClass.printImage(btMap);
							/*
							 * Message msgMessage = hanler.obtainMessage();
							 * msgMessage.what = 0;
							 * hanler.sendMessage(msgMessage);
							 */
						}
					}
				});
			}

			if (btnSendCMD != null) {
				btnSendCMD.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent();
						intent.setClass(PrintDemo.this, PrintCmdActivity.class);
						startActivity(intent);
					}
				});
			}
		}
		Handler mhandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case PrinterClass.MESSAGE_READ:
					byte[] readBuf = (byte[]) msg.obj;
					Log.i(TAG, "readBuf:" + readBuf[0]);
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
							Toast.makeText(getApplicationContext(), "80mm",
									Toast.LENGTH_SHORT).show();
						} else if (readMessage.contains("580"))// 58mm paper
						{
							PrintService.imageWidth = 48;
							Toast.makeText(getApplicationContext(), "58mm",
									Toast.LENGTH_SHORT).show();
						} else {

						}
					}
					break;
				case PrinterClass.MESSAGE_STATE_CHANGE:// 6��l��״
					switch (msg.arg1) {
					case PrinterClass.STATE_CONNECTED:// �Ѿ�l��
						break;
					case PrinterClass.STATE_CONNECTING:// ����l��
						Toast.makeText(getApplicationContext(),
								"STATE_CONNECTING", Toast.LENGTH_SHORT).show();
						break;
					case PrinterClass.STATE_LISTEN:
					case PrinterClass.STATE_NONE:
						break;
					case PrinterClass.SUCCESS_CONNECT:
						printerClass.write(new byte[] { 0x1b, 0x2b });// ����ӡ���ͺ�
						Toast.makeText(getApplicationContext(),
								"SUCCESS_CONNECT", Toast.LENGTH_SHORT).show();
						break;
					case PrinterClass.FAILED_CONNECT:
						Toast.makeText(getApplicationContext(),
								"FAILED_CONNECT", Toast.LENGTH_SHORT).show();

						break;
					case PrinterClass.LOSE_CONNECT:
						Toast.makeText(getApplicationContext(), "LOSE_CONNECT",
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
		printerClass.open(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		printerClass.close(this);
	}

	private Handler hanler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				btnPrintPic.setEnabled(true);
				btnOpenPic.setEnabled(true);
				btnBarCode.setEnabled(true);
				btnWordToPic.setEnabled(true);
				btnQrCode.setEnabled(true);
				btnPrint.setEnabled(true);
				break;

			default:
				break;
			}
		}
	};

	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
		Bitmap BitmapOrg = bitmap;
		int width = BitmapOrg.getWidth();
		int height = BitmapOrg.getHeight();
		int newWidth = w;
		int newHeight = h;

		if (width >= newWidth) {
			float scaleWidth = ((float) newWidth) / width;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleWidth);
			Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
					height, matrix, true);
			return resizedBitmap;
		} else {

			Bitmap bitmap2 = Bitmap.createBitmap(newWidth, newHeight,
					bitmap.getConfig());
			Canvas canvas = new Canvas(bitmap2);
			canvas.drawColor(Color.WHITE);

			canvas.drawBitmap(BitmapOrg, (newWidth - width) / 2, 0, null);

			return bitmap2;

		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_EX && resultCode == RESULT_OK
				&& null != data) {
			Uri selectedImage = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			picPath = picturePath;
			iv.setImageURI(selectedImage);
			btMap = BitmapFactory.decodeFile(picPath);
			if (btMap.getHeight() > 384) {
				btMap = BitmapFactory.decodeFile(picPath);
				iv.setImageBitmap(resizeImage(btMap, 384, 384));

			}
			cursor.close();
		}

	}

	static byte[] string2Unicode(String s) {
		try {
			byte[] bytes = s.getBytes("unicode");
			byte[] bt = new byte[bytes.length - 2];
			for (int i = 2, j = 0; i < bytes.length - 1; i += 2, j += 2) {
				bt[j] = (byte) (bytes[i + 1] & 0xff);
				bt[j + 1] = (byte) (bytes[i] & 0xff);
			}
			return bt;
		} catch (Exception e) {
			try {
				byte[] bt = s.getBytes("GBK");
				return bt;
			} catch (UnsupportedEncodingException e1) {
				Log.e(TAG, e.getMessage());
				return null;
			}
		}
	}

	private void ShowMsg(String msg) {
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

}
