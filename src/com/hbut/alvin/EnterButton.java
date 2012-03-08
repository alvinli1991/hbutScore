package com.hbut.alvin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class EnterButton extends RelativeLayout {

	ImageButton bkgBtn;
	Button enterBtn;
	Button enterContent;
	public EnterButton(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}

	public EnterButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.enterbtn,	 this, true);
		bkgBtn= (ImageButton) findViewById(R.id.backBtn);
		enterBtn = (Button) findViewById(R.id.enterBtn);
		enterContent = (Button) findViewById(R.id.enterContent);
		// TODO Auto-generated constructor stub
	}

	

}
