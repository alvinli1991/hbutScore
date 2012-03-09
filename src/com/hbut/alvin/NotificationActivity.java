package com.hbut.alvin;

import com.hbut.alvin.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends Activity {

	NotificationManager mnofityMgr;
	HbutApp myapp;
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mnofityMgr.cancel(VersionCheckService.UPDATE_ID);
	}
	TextView notification;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		myapp = (HbutApp) getApplicationContext();
		setContentView(R.layout.notification);
		notification = (TextView) findViewById(R.id.notify);
		mnofityMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		//test
		Intent intent = getIntent();
		String version = intent.getStringExtra("version");
		String element[] = myapp.getAppvi().getElement();
		notification.setText(element[0]);
	}
	

}
