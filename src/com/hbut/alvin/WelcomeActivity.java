package com.hbut.alvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import com.hbut.alvin.R;
import com.hbut.util.VersionInf;
import com.hbut.util.XmlReader;
import com.hbut.util.XmlWriter;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

public class WelcomeActivity extends Activity {

	PackageInfo pi;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		PackageManager pm = getPackageManager();
		try {
			pi = pm.getPackageInfo("com.hbut.alvin", 0);
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
				String allowState = "true";
				try {
					if (!hasFile()) {
						String versionXml = XmlWriter
								.writeFirstVersionXml(pi.versionName);
						OutputStream outStream;
						outStream = openFileOutput("version.xml", MODE_PRIVATE);
						OutputStreamWriter outStreamWriter = new OutputStreamWriter(
								outStream, "UTF-8");
						outStreamWriter.write(versionXml);
						outStreamWriter.close();
						outStream.close();
						allowState = "true";
					} else {
						InputStream inputStream;
						inputStream = openFileInput("version.xml");
						VersionInf localVi = XmlReader
								.paserVersionXml(inputStream);
						if (localVi.getAllow().equalsIgnoreCase("true"))
							allowState = "true";
						else
							allowState = "false";
					}

				} catch (FileNotFoundException e) {
					// TODO: handle exception
					allowState = "true";
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					allowState = "true";
					e.printStackTrace();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					allowState = "true";
					e.printStackTrace();
				} finally {
					// start new activity
					Intent intent = new Intent(WelcomeActivity.this,
							LoginActivity.class);
					intent.putExtra("allow", allowState);
					startActivity(intent);
					WelcomeActivity.this.finish();
				}

			}
		}, 3000);
	}

	public void createCacheDir() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File cacheDir = Environment.getExternalStorageDirectory();
			String path = cacheDir.getPath() + "/hbutscore";
			File nPath = new File(path);
			File nPath1 = new File(nPath + "/person");
			File nPath2 = new File(nPath + "/class");
			if (!nPath.exists()) {
				nPath.mkdirs();
			}
			if (!nPath1.exists()) {
				nPath1.mkdirs();
			}
			if (!nPath2.exists()) {
				nPath2.mkdirs();
			}

		}
	}

	public boolean hasFile() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String path = getApplicationContext().getFilesDir()
					.getAbsolutePath();
			File file = new File(path + "/" + "version.xml");
			if (file.exists())
				return true;
			else
				return false;
		} else
			return false;

	}

}