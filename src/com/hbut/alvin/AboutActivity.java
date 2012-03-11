package com.hbut.alvin;

import android.app.ListActivity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class AboutActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.about);

			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo("com.hbut.alvin", 0);
			String version = "Version:"+ pi.versionName;
			String[] about = {version,"≥Ã–Ú‘≥:AlvinLi", "lxlx1991@gmail.com",
					"…Ëº∆‘≥:YuKi", "2010yujin@gmail.com" };
			setListAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, about));
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
