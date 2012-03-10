package com.hbut.alvin;


import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class AboutActivity extends ListActivity {

	String[] about = { "≥Ã–Ú‘≥:AlvinLi", "email:lxlx1991@gmail.com", "…Ëº∆‘≥:YuKi",
			"email:2010yujin@gmail.com" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,about));
	}

}
