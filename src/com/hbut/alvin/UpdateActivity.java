package com.hbut.alvin;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.hbut.util.HtmlParser;
import com.hbut.util.PersonInf;
import com.hbut.util.PersonSbj;
import com.hbut.util.XmlWriter;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class UpdateActivity extends DownLoadActivity implements ViewFactory{

	protected static final int DOWNLOAD = 0;
	protected static final int SERVERERROR = 1;
	protected static final int ANALYSE = 2;
	protected static final int PATHERROR = 3;
	protected static final int NEXT = 4;
	protected static final int SAVEING = 5;
	protected static final int SAMENUM = 6;
	protected static final int FINISH =7;
	final static int CONNECTERROR = 8;
	
	ProgressBar circle ;
	TextView noticeText;
	TextSwitcher stateText;
	Handler handler;
	HbutApp myapp;
	Thread updateThread;
	ArrayList<PersonSbj> psSbjList = null;
	PersonInf pi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		myapp = (HbutApp) getApplicationContext();
		pi = myapp.getPsi();
		circle = (ProgressBar) findViewById(R.id.circle);
		noticeText = (TextView) findViewById(R.id.notice);
		stateText = (TextSwitcher) findViewById(R.id.statenotice);
		stateText.setFactory(this);
		
		Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
		stateText.setInAnimation(in);
		stateText.setOutAnimation(out);
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case DOWNLOAD:
					stateText.setText("下载数据……");
					break;
				case ANALYSE:
					stateText.setText("分析数据……");
					break;
				case SAVEING:
					stateText.setText("保存数据……");
					break;
				case SERVERERROR:
					stateText.setText("连接服务器失败");
					break;
				case PATHERROR:
					stateText.setText("数据无法保存到本地");
					break;
				case FINISH:
					stateText.setText("个人成绩更新完毕");
					break;
				case SAMENUM:
					Toast.makeText(getApplicationContext(), "数据已经是最新", Toast.LENGTH_LONG).show();
					break;
				case CONNECTERROR:
					Toast.makeText(getApplicationContext(), "无网络连接", Toast.LENGTH_LONG).show();
					finish();
					break;
				case NEXT:
					finish();
					break;
				}
			}
		};
		
		updateThread = new Thread("updateThread"){

			@Override
			public void run() {
					if(hasNetWork()==false){
						handler.sendMessage(handler.obtainMessage(CONNECTERROR));
						return;
					}
					//get count
					String countDoc = getSbjCountFileByID(myapp.getPsi().getID());
					if(countDoc == null){
						return;
					}
					int count = HtmlParser.parserSbjCount(countDoc);
					if(count == myapp.getPsi().getSbjCount()){
						handler.sendMessage(handler.obtainMessage(SAMENUM));
						handler.sendMessage(handler.obtainMessage(NEXT));
						return;
					}
					
					// download
					handler.sendMessage(handler.obtainMessage(DOWNLOAD));
					String ownGradeDoc = getOwnGradeFileByID(pi.getID());
					if (ownGradeDoc == null) {
						handler.sendMessage(handler
								.obtainMessage(SERVERERROR));
						return;
					}

					// analyse
					
					handler.sendMessage(handler.obtainMessage(ANALYSE));
					psSbjList = HtmlParser.parserPsSbj(ownGradeDoc);
					if (psSbjList == null) {
						return;
					}

					myapp.setpSbjList(psSbjList);

					// save
					String pGradeXml = XmlWriter.writePGradeXml(psSbjList,
							pi);
					if (pGradeXml == null)
						return;
					if (!XmlWriter.hasPath()) {
						handler.sendMessage(handler
								.obtainMessage(PATHERROR));
						handler.sendMessage(handler.obtainMessage(NEXT));
						return;
					} else {
						handler.sendMessage(handler.obtainMessage(SAVEING));
						OutputStream outStream;
						try {
							outStream = openFileOutput(pi.getID() + ".xml",
									MODE_PRIVATE);
							OutputStreamWriter outStreamWriter = new OutputStreamWriter(
									outStream, "GBK");
							outStreamWriter.write(pGradeXml);
							outStreamWriter.close();
							outStream.close();
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
						
					}
					
					
					myapp.setClsDownloadEnd(false);
					Intent serviceIntent = new Intent(UpdateActivity.this,ClsService.class);
					startService(serviceIntent);
					handler.sendMessage(handler.obtainMessage(FINISH));
					handler.sendMessage(handler.obtainMessage(NEXT));
					
				
				
			}	
		};
	

	}

	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		updateThread.start();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}


	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		TextView t = new TextView(this);
		t.setTextSize(30);
		return t;
	}

	
}
