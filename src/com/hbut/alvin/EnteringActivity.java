package com.hbut.alvin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.hbut.httpDownload.URIContainer;
import com.hbut.util.HtmlParser;
import com.hbut.util.PersonCombine;
import com.hbut.util.PersonInf;
import com.hbut.util.PersonSbj;
import com.hbut.util.UriUtil;
import com.hbut.util.XmlReader;
import com.hbut.util.XmlWriter;
import com.hbut.alvin.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class EnteringActivity extends DownLoadActivity {

	PersonInf pi;
	TextView processState;
	ImageView flowImg;
	ProgressBar progressBar;
	Handler handler;
	ArrayList<PersonSbj> psSbjList = null;
	HbutApp myApp;
	final static int DOWNLOAD = 1;
	final static int ANALYSE = 2;
	final static int SAVEING = 3;

	final static int SERVERERROR = 4;
	final static int PATHERROR = 5;
	
	final static int READDATA = 6;
	final static int NEXT = 7;
	boolean isRunning = false;
	boolean hasPiFile = false;
	boolean hasClsFile = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entering);
		myApp = (HbutApp) getApplicationContext();
		pi = myApp.getPsi();
		processState = (TextView) findViewById(R.id.prcsState);
		flowImg = (ImageView) findViewById(R.id.flowImg);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setMax(90); 
		Bundle myBundle = getIntent().getExtras();
		hasPiFile = myBundle.getBoolean("hasPiFile");
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case DOWNLOAD:
					progressBar.incrementProgressBy(30);
					processState.setText("下载数据……");
					break;
				case ANALYSE:
					progressBar.incrementProgressBy(30);
					processState.setText("分析数据……");
					break;
				case SAVEING:
					processState.setText("存储数据……");
					break;
				case SERVERERROR:
					Toast.makeText(EnteringActivity.this, "服务器连接失败",
							Toast.LENGTH_SHORT).show();
					finish();
					break;
				case PATHERROR:
					processState.setText("无SD卡,无法储存数据……");
					break;
				case NEXT:
					progressBar.incrementProgressBy(30);
					isRunning = false;
					Intent intent = new Intent(EnteringActivity.this,
							ShowActivity.class);
					startActivity(intent);
					finish();
					break;
				case READDATA:
					processState.setText("读取数据……");
					for(int i = 0;i<4;i++)
						progressBar.incrementProgressBy(15);
					break;
				}
			}

		};
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Thread downloadThread = new Thread() {
			@Override
			public void run() {
				while (isRunning) {
					if (hasPiFile) {
						handler.sendMessage(handler.obtainMessage(READDATA));
						Log.v("file", String.valueOf(hasPiFile));
						InputStream inputStream;
						try {
							inputStream = openFileInput(pi.getID() + ".xml");
							PersonCombine pc = XmlReader
									.pGradeXmlParser(inputStream);
							myApp.setpSbjList(pc.getpSbjList());
							handler.sendMessage(handler.obtainMessage(NEXT));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						// download
						handler.sendMessage(handler.obtainMessage(DOWNLOAD));
						String ownGradeDoc = getOwnGradeFileByID(pi.getID());
						if (ownGradeDoc == null){
							handler.sendMessage(handler.obtainMessage(SERVERERROR));
							return;
						}
							

						// analyse
						handler.sendMessage(handler.obtainMessage(ANALYSE));
						psSbjList = HtmlParser.parserPsSbj(ownGradeDoc);
						if (psSbjList == null){
							return;
						}
							

						myApp.setpSbjList(psSbjList);

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
							Log.v("pxml", pGradeXml);
							OutputStream outStream;
							try {
								outStream = openFileOutput(pi.getID() + ".xml",
										MODE_APPEND);
								OutputStreamWriter outStreamWriter = new OutputStreamWriter(
										outStream, "GBK");
								outStreamWriter.write(pGradeXml);
								outStreamWriter.close();
								outStream.close();
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
								Log.v("streamerror", e.getLocalizedMessage());
							}
							handler.sendMessage(handler.obtainMessage(NEXT));
						}
					}

				}

			}

		};
		isRunning = true;
		downloadThread.start();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		isRunning = false;
		Log.v("Thread", "end");
	}


}
