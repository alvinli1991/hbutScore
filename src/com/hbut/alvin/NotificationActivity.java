package com.hbut.alvin;

import com.hbut.alvin.R;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationActivity extends Activity {

	NotificationManager mnofityMgr;
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
		setContentView(R.layout.notification);
		notification = (TextView) findViewById(R.id.notify);
		mnofityMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}
	

}
