package com.example.usb_host_new_try;

import java.util.Random;
import java.util.concurrent.locks.Condition;

import com.ftdi.j2xx.D2xxManager;
import com.ftdi.j2xx.FT_Device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends Activity {
	int DevCount = -1;
	Context MiscFragment = this;
	D2xxManager ftdid2xx;
	FT_Device ftDevice = null;
	boolean switching = true;
	Random random = new Random();
	int player1 = 100000, player2 = 100000;
	TextView tv1;
	TextView tv2;
	Button b ;
	VideoView videoView;
	long x;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try {
			ftdid2xx = D2xxManager.getInstance(this);
		} catch (D2xxManager.D2xxException ex) {
			ex.printStackTrace();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		filter.setPriority(500);
		// this.registerReceiver(mUsbReceiver, filter);

		tv1 = (TextView) findViewById(R.id.textView1);
//		tv2 = (TextView) findViewById(R.id.textView2);
		videoView = (VideoView) findViewById(R.id.videoView1);

		tv1.setText("Start Game");
		final Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);
		b= (Button) findViewById(R.id.button1);
		
		b.setOnClickListener(new OnClickListener() {
			public void onClick(final View v) {
				v.startAnimation(animRotate);
				String s = "";
				int m = random.nextInt(6) + 1;


				final Uri videoPath = Uri.parse("android.resource://"
						+ getPackageName() + "/raw/dicefinal" + m);
				videoView.setVideoURI(videoPath);
				videoView.requestFocus();
				videoView.start();
				s = s + m;
//				tv2.setText(s);
				if (DevCount <= 0)
					ConnectFunction();
				if (switching == true) {
					tv1.setText("Player 1");
					;
					if (DevCount > 0)
						loopbackWriteRead(m);
					switching = false;
				} else {
					tv1.setText("Player 2");
					;
					if (DevCount > 0)
						loopbackWriteRead(m + 10);
					switching = true;
				}

				}

			});
	}

	public void ConnectFunction() {
		int openIndex = 0;

		if (DevCount > 0)
			return;

		DevCount = ftdid2xx.createDeviceInfoList(MiscFragment);

		if (DevCount > 0) {
			ftDevice = ftdid2xx.openByIndex(MiscFragment, openIndex);

			if (ftDevice == null) {
				Toast.makeText(MiscFragment, "ftDev == null", Toast.LENGTH_LONG)
						.show();
				return;
			}

			if (true == ftDevice.isOpen()) {
				ftDevice.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
				ftDevice.setBaudRate(9600);
				ftDevice.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8,
						D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);
				ftDevice.setFlowControl(D2xxManager.FT_FLOW_NONE, (byte) 0x00,
						(byte) 0x00);
				ftDevice.setLatencyTimer((byte) 16);
				ftDevice.purge((byte) (D2xxManager.FT_PURGE_TX | D2xxManager.FT_PURGE_RX));

				Toast.makeText(MiscFragment,
						"devCount:" + DevCount + " open index:" + openIndex,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(MiscFragment, "Need to get permission!",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.e("j2xx", "DevCount <= 0");
		}
	}

	// --------------------------- loopback
	// -------------------------------------------------//
	public void loopbackWriteRead(int m) {

		byte[] OutData = { (byte) m };
		ftDevice.write(OutData);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
