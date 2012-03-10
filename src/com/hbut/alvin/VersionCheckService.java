package com.hbut.alvin;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
import com.hbut.util.VersionInf;
import com.hbut.util.XmlReader;
import com.hbut.util.XmlWriter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

public class VersionCheckService extends Service {

	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	Message StartMsg = null;
	Thread checkThread;
	VersionInf localVi;
	VersionInf remoteVi;
	VersionInf newVi;
	Notification notification;
	NotificationManager mnofityMgr;
	HbutApp myapp ;
	final static int UPDATE_ID = 1;
	final static int END = 0;
	final static int NOTIFY = 1;
	final static int START = 2;

	final class ServiceHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case END:
				Log.v("versionservice", "end");
				stopSelf(StartMsg.arg1);
				break;
			case NOTIFY:
				Log.v("notify", "trigger");
				nofity();
				mServiceHandler.sendMessage(mServiceHandler.obtainMessage(END));
				break;
			case START:
				Log.v("versionservice", "start");
				checkThread.start();
				break;
			}
		}

		private ServiceHandler(Looper looper) {
			super(looper);
			// TODO Auto-generated constructor stub
		}

	}

	public void nofity() {

		notification = new Notification(R.drawable.logo_ic, "update",
				System.currentTimeMillis());
		Context context = getApplicationContext();
		Intent notificationIntent = new Intent(VersionCheckService.this,
				NotificationActivity.class);
		// attach note and version
		notificationIntent.putExtra("version", newVi.getVersion());
		myapp.setAppvi(newVi);
		//notificationIntent.putExtra("note", newVi.getElement());	
		//
		PendingIntent contentIntent = PendingIntent.getActivity(
				VersionCheckService.this, 0, notificationIntent, 0);
		notification.setLatestEventInfo(VersionCheckService.this, "hbutscore",
				"a new version is coming", contentIntent);
		mnofityMgr.notify(UPDATE_ID, notification);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		myapp = (HbutApp) getApplicationContext();
		newVi = new VersionInf();
		HandlerThread thread = new HandlerThread("versionServiceThread",
				Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		mnofityMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		checkThread = new Thread("checkThread") {
			@Override
			public void run() {
				try {
					if (hasNetWork() == false) {
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(END));
						return;
					}
					PackageManager pm = getPackageManager();
					PackageInfo pi = pm.getPackageInfo("com.hbut.alvin", 0);
					String appVerison = pi.versionName;

					if (hasFile()) {
						InputStream inputStream;
						inputStream = openFileInput("version.xml");
						localVi = XmlReader.paserVersionXml(inputStream);

						if (!localVi.getVersion()
								.equalsIgnoreCase(appVerison)) {
							// notify user
							//newVi.setVersion(localVi.getVersion());
							newVi=localVi;
							//newVi.setElement(localVi.getElement());
							mServiceHandler.sendMessage(mServiceHandler
									.obtainMessage(NOTIFY));
							return;
						}

						Calendar now = Calendar.getInstance();
						int nowDays = now.get(Calendar.DAY_OF_YEAR);
						Calendar fileDate = Calendar.getInstance();
						fileDate.set(localVi.getYear(), localVi.getMonth(),
								localVi.getDay());
						int oldDays = fileDate.get(Calendar.DAY_OF_YEAR);
						if (localVi.getYear() == now.get(Calendar.YEAR)
								&& (nowDays - oldDays) < 2) {
							mServiceHandler.sendMessage(mServiceHandler
									.obtainMessage(END));
							return;
						}

					}
					// download
					String versionFile = getVersionFile();
					if (versionFile == null)
						return;
					// paser
					ByteArrayInputStream in = new ByteArrayInputStream(
							versionFile.getBytes());
					remoteVi = XmlReader.paserVersionXml(in);
					// save
					String newVersionXml = XmlWriter.writeVersionXml(
							remoteVi.getVersion(), remoteVi.getElement(),
							remoteVi.getAllow());
					Log.v("versionXML", newVersionXml);
					OutputStream outStream;
					outStream = openFileOutput("version.xml", MODE_PRIVATE);
					OutputStreamWriter outStreamWriter = new OutputStreamWriter(
							outStream, "UTF-8");
					outStreamWriter.write(newVersionXml);
					outStreamWriter.close();
					outStream.close();
					// compare
					if (!appVerison.equalsIgnoreCase(remoteVi.getVersion())) {
						// notify user
						//newVi.setVersion(remoteVi.getVersion());
						
						newVi = remoteVi;
						//newVi.setElement(remoteVi.getElement());
						mServiceHandler.sendMessage(mServiceHandler
								.obtainMessage(NOTIFY));
						return;
					}

					mServiceHandler.sendMessage(mServiceHandler
							.obtainMessage(END));
				} catch (FileNotFoundException e) {
					// TODO: handle exception
					e.printStackTrace();
					mServiceHandler.sendMessage(mServiceHandler
							.obtainMessage(END));
					return;
				} catch (NameNotFoundException e) {
					e.printStackTrace();
					mServiceHandler.sendMessage(mServiceHandler
							.obtainMessage(END));
					return;
				} catch (Exception e) {
					e.printStackTrace();
					mServiceHandler.sendMessage(mServiceHandler
							.obtainMessage(END));
					return;
				}

			}

		};

		mServiceHandler.sendMessage(mServiceHandler.obtainMessage(START));
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

	public boolean hasNetWork() {
		ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = conMgr.getActiveNetworkInfo();
		if (net == null)
			return false;
		else
			return true;
	}

	public String getVersionFile() {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
		HttpConnectionParams.setSoTimeout(httpParams, 3000);
		HttpClient httpClient = new DefaultHttpClient();
		String versionUri = URIContainer.versionCheck;
		HttpGet httpget = new HttpGet(versionUri);
		String text = null;
		HttpResponse response;
		try {
			Log.v("versiondownload", "begin");
			response = httpClient.execute(httpget);
			if (response.getStatusLine().getStatusCode() != 200)
				return null;
			HttpEntity entity = response.getEntity();
			InputStreamReader isr = null;
			BufferedReader br = null;
			StringBuffer sb = new StringBuffer();
			if (entity == null)
				return null;
			InputStream instream = entity.getContent();
			isr = new InputStreamReader(instream, "UTF-8");
			br = new BufferedReader(isr);
			String temp = null;
			while ((temp = br.readLine()) != null)
				sb.append(temp);

			Log.v("versiondownload", "finish");
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
