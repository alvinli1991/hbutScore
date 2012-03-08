package com.hbut.alvin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class OutButton extends RelativeLayout {

	ImageButton bkgBtn1;
	Button outBtn;
	Button outContent;

	public OutButton(Context context) {
		this(context, null);
	}

	public OutButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.outbtn, this, true);
		bkgBtn1 = (ImageButton) findViewById(R.id.bkgBtn1);
		outBtn = (Button) findViewById(R.id.outBtn);
		outContent = (Button) findViewById(R.id.outContent);
	}

}
