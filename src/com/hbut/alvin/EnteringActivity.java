package com.hbut.alvin;

import java.io.BufferedReader;
import java.io.File;
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
import com.hbut.util.PersonInf;
import com.hbut.util.PersonSbj;
import com.hbut.util.UriUtil;
import com.hbut.util.XmlWriter;
import com.hbut.alvin.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class EnteringActivity extends Activity {

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
	final static int NEXT = 6;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.entering);
		myApp = (HbutApp) getApplicationContext();
		Bundle bundle = getIntent().getExtras();
		pi = new PersonInf();
		pi.setCls(bundle.getString("cls"));
		pi.setName(bundle.getString("name"));
		pi.setPwd(bundle.getString("pwd"));
		pi.setID(bundle.getString("id"));
		myApp.setPs(pi);
		processState = (TextView) findViewById(R.id.prcsState);
		flowImg = (ImageView) findViewById(R.id.flowImg);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setMax(90);
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
					break;
				case PATHERROR:
					processState.setText("无SD卡……");
					break;
				case NEXT:
					progressBar.incrementProgressBy(30);
					Intent intent = new Intent(EnteringActivity.this,
							ShowActivity.class);
					startActivity(intent);
					finish();
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
				// download
				handler.sendMessage(handler.obtainMessage(DOWNLOAD));
				String ownGradeDoc = getOwnGradeFileByID(pi.getID());
				if (ownGradeDoc == null)
					return;

				// analyse
				handler.sendMessage(handler.obtainMessage(ANALYSE));
				psSbjList = HtmlParser.parserPsSbj(ownGradeDoc);
				if (psSbjList == null)
					return;
				myApp.setpSbjList(psSbjList);

				// save
				String pGradeXml = XmlWriter.writePGradeXml(psSbjList, pi);
				if (!XmlWriter.hasPath()) {
					handler.sendMessage(handler.obtainMessage(PATHERROR));
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

		};
		downloadThread.start();
	}

	public String getOwnGradeFileByID(String ID) {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
		HttpConnectionParams.setSoTimeout(httpParams, 3000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);

		String pwdUri = UriUtil.getRealUri(URIContainer.personGrade, ID);
		HttpGet httpget = new HttpGet(pwdUri);
		String text = null;
		HttpResponse response;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			InputStreamReader isr = null;
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			if (entity != null) {
				InputStream instream = entity.getContent();
				isr = new InputStreamReader(instream, "GBK");
				br = new BufferedReader(isr);
				String temp = null;
				while ((temp = br.readLine()) != null)
					sb.append(temp);
			}
			text = sb.toString();
			br.close();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			handler.sendMessage(handler.obtainMessage(SERVERERROR));
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block

			e.printStackTrace();
			httpget.abort();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return text;
	}

	public String getClsGradeFileByName(String clsName) {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
		HttpConnectionParams.setSoTimeout(httpParams, 3000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);

		String pwdUri = UriUtil.getRealUri(URIContainer.classGrade,
				UriUtil.gbkUriEncode(clsName));
		HttpGet httpget = new HttpGet(pwdUri);
		String text = null;
		HttpResponse response;
		try {
			response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			InputStreamReader isr = null;
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			if (entity != null) {
				InputStream instream = entity.getContent();
				isr = new InputStreamReader(instream, "GBK");
				br = new BufferedReader(isr);
				String temp = null;
				while ((temp = br.readLine()) != null)
					sb.append(temp);
			}
			text = sb.toString();
			br.close();

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			handler.sendMessage(handler.obtainMessage(SERVERERROR));
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			handler.sendMessage(handler.obtainMessage(SERVERERROR));
			e.printStackTrace();
			httpget.abort();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return text;
	}

	public boolean hasFile(String id) {
		String path = getApplicationContext().getFilesDir().getAbsolutePath();
		File file = new File(path + id);
		if (file.exists())
			return true;
		else
			return false;
	}
}
