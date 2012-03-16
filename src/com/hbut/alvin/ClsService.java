package com.hbut.alvin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Map;

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
import com.hbut.util.ClsStuSbj;
import com.hbut.util.HtmlParser;
import com.hbut.util.PersonInf;
import com.hbut.util.UriUtil;
import com.hbut.util.XmlReader;
import com.hbut.util.XmlWriter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;

/*
 * a service used to download class grades file ,parse it and 
 * store it in local SD card 
 * */
public class ClsService extends Service {

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	Thread downloadThread;
	HbutApp myapp;
	Map<String, List<ClsStuSbj>> clsMap = null;
	String clsID = null;
	boolean hasClsFile = false;
	Message StartMsg = null;
	final static int START = 0;
	final static int DOWNLOADERROR = 1;
	final static int DATAERROR = 2;
	final static int PATHERROR = 3;
	final static int END = 4;
	final static int FINISH = 5;
	final static int CONNECTERROR = 6;
	
	final class ServiceHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case START:
				downloadThread.start();
				break;
			case DOWNLOADERROR:
				Toast.makeText(ClsService.this, "����ʧ�ܣ��޷���ʾ�༶�ɼ�",
						Toast.LENGTH_SHORT).show();
				mServiceHandler.sendMessage(mServiceHandler
						.obtainMessage(END));
				break;
			case DATAERROR:
				Toast.makeText(ClsService.this, "���ݷ���ʧ�ܣ��޷���ʾ�༶����",
						Toast.LENGTH_SHORT).show();
				break;
			case PATHERROR:
				Toast.makeText(ClsService.this, "SD���޷����أ������޷�����",
						Toast.LENGTH_SHORT).show();
				break;
			case END:
				Log.v("clsservice", "end");
				Intent intent = new Intent(ClsService.this, VersionCheckService.class);
				startService(intent);
				stopSelf(StartMsg.arg1);
				break;
			case FINISH:
				myapp.setClsDownloadEnd(true);
				Toast.makeText(ClsService.this, "���Բ鿴�༶������",
						Toast.LENGTH_LONG).show();
				break;
			case CONNECTERROR:
				Toast.makeText(ClsService.this, "����������",
						Toast.LENGTH_LONG).show();
				mServiceHandler.sendMessage(mServiceHandler
						.obtainMessage(END));
				break;
			}

		}

		public ServiceHandler(Looper looper) {
			super(looper);
			// TODO Auto-generated constructor stub
		}

	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		HandlerThread thread = new HandlerThread("clsServiceThread",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		myapp = (HbutApp) getApplicationContext();
		clsID = PersonInf.getCidByID(myapp.getPsi().getID());
		hasClsFile = hasFile(clsID);

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);

		downloadThread = new Thread("clsDownloadThread") {

			@Override
			public void run() {
				if (hasClsFile) {
					InputStream inputStream;
					try {
						inputStream = openFileInput(clsID + ".xml");
						clsMap = XmlReader.cGradeXmlParser(inputStream);
						myapp.setClsSbj(clsMap);
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(FINISH));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else {
					if (hasNetWork() == false) {
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(CONNECTERROR));
						return;
					}
					// download
					String clsGradeDoc = getClsGradeFileByName(myapp.getPsi()
							.getCls());
					if (clsGradeDoc == null) {
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(DOWNLOADERROR));
						return;
					}
					// analyse
					clsMap = HtmlParser.parserClsSbj(clsGradeDoc);
					if (clsMap == null) {
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(DATAERROR));
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(END));
						return;
					}

					myapp.setClsSbj(clsMap);
					mServiceHandler.sendMessage(mServiceHandler
							.obtainMessage(FINISH));
					// save
					String cGradeXml = XmlWriter.writeCGradeXml(clsMap, clsID);
					if (cGradeXml == null) {
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(DATAERROR));
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(END));
						return;
					}
					if (!XmlWriter.hasPath()) {
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(DATAERROR));
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(END));
						return;
					}
					OutputStream outStream;
					try {
						outStream = openFileOutput(clsID + ".xml", MODE_PRIVATE);
						OutputStreamWriter outStreamWriter = new OutputStreamWriter(
								outStream, "GBK");
						outStreamWriter.write(cGradeXml);
						outStreamWriter.close();
						outStream.close();
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				mServiceHandler.sendMessage(mServiceHandler
						.obtainMessage(END));
			}

		};
		mServiceHandler.sendMessage(mServiceHandler
				.obtainMessage(START));
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		StartMsg = mServiceHandler.obtainMessage();
		StartMsg.arg1 = startId;
		Log.v("serviceID", Integer.toString(startId));
		return START_NOT_STICKY;

	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasNetWork(){
		ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = conMgr.getActiveNetworkInfo();
		if(net == null)
			return false;
		else
			return true;
	}
	public String getClsGradeFileByName(String clsName) {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
		HttpConnectionParams.setSoTimeout(httpParams, 3000);
		HttpClient httpClient = new DefaultHttpClient();

		String pwdUri = UriUtil.getRealUri(URIContainer.classGrade,
				UriUtil.gbkUriEncode(clsName));
		HttpGet httpget = new HttpGet(pwdUri);
		String text = null;
		HttpResponse response;
		try {
			Log.v("download", "begin");
			response = httpClient.execute(httpget);
			if(response.getStatusLine().getStatusCode() != 200)
				return null;
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
			Log.v("download", "finish");
			text = sb.toString();
			br.close();
			return text;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("error", "download1");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			httpget.abort();
			Log.v("error", "download2");
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

	}

	public boolean hasFile(String cID) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String path = getApplicationContext().getFilesDir()
					.getAbsolutePath();
			File file = new File(path + "/" + cID + ".xml");
			if (file.exists())
				return true;
			else
				return false;
		} else
			return false;

	}
}
