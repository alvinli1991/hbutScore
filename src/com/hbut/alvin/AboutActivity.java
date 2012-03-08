package com.hbut.alvin;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		aboutBkg = (ImageView) findViewById(R.id.aboutBkg);
	}

	ImageView aboutBkg;
	
}
