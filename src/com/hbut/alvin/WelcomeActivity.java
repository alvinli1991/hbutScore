package com.hbut.alvin;

import java.io.File;

import com.hbut.alvin.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		PackageManager pm = getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo("com.hbut.alvin", 0);
			TextView versionNum = (TextView) findViewById(R.id.versionNum);
			versionNum.setText("Version" + pi.versionName);
		} catch (NameNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Intent intent = new Intent(WelcomeActivity.this,
						LoginActivity.class);
				startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 3000);
	}
	
	public void createCacheDir(){
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			File cacheDir = Environment.getExternalStorageDirectory();
			String path = cacheDir.getPath()+"/hbutscore";
			File nPath = new File(path);	
			File nPath1 = new File(nPath+"/person");
			File nPath2 = new File(nPath+"/class");
			if(!nPath.exists()){
				nPath.mkdirs();
			}
			if(!nPath1.exists()){
				nPath1.mkdirs();
			}
			if(!nPath2.exists()){
				nPath2.mkdirs();
			}
			
		}
	}

}