package com.hbut.alvin;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.hbut.util.HtmlParser;
import com.hbut.util.PersonInf;
import com.hbut.util.VersionInf;
import com.hbut.util.XmlReader;
import com.hbut.util.XmlWriter;
import com.hbut.alvin.R;
import com.hbut.httpDownload.URIContainer;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends DownLoadActivity implements OnTouchListener {
	
	PackageManager pm ;
	PackageInfo pkgi;
	//
	VersionInf remoteVi;
	PersonInf pi;
	String pwd;
	String id;
	AutoCompleteTextView idFld;
	EditText pwdFld;
	// //
	Button enterBtn;
	Button enterContent;
	ImageButton bkgBtn;
	// //
	Button outContent;
	Button outBtn;
	ImageButton bkgBtn1;
	//
	ImageButton permissionBtn;
	//
	final static int CHECK = 1;
	final static int SERVERERROR = 2;
	final static int DATAERROR = 3;
	final static int CONNECTERROR = 4;
	final static int DOUBLEERROR = 5;
	final static int PERMISSIONNO = 6;
	final static int PERMISSIONCHECK=7;
	final static int PERMISSIONYES = 8;
	final static int CHECKNOTIFY = 9;
	HbutApp myApp;
	boolean hasPiFile = false;
	Handler handler;
	Thread loginThread;
	boolean isRunning = false;
	String[] prepareIDList = null;
	String allow = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		pm = getPackageManager();
		try {
			pkgi = pm.getPackageInfo("com.hbut.alvin", 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent allowIntent = getIntent();
		allow = allowIntent.getStringExtra("allow");

		idFld = (AutoCompleteTextView) findViewById(R.id.idFld);
		pwdFld = (EditText) findViewById(R.id.pswFld);

		enterBtn = (Button) findViewById(R.id.enterBtn);
		enterContent = (Button) findViewById(R.id.enterContent);
		bkgBtn = (ImageButton) findViewById(R.id.bkgBtn);

		outContent = (Button) findViewById(R.id.outContent);
		outBtn = (Button) findViewById(R.id.outBtn);
		bkgBtn1 = (ImageButton) findViewById(R.id.bkgBtn1);

		permissionBtn = (ImageButton) findViewById(R.id.permission);

		enterBtn.setOnTouchListener(this);
		enterContent.setOnTouchListener(this);
		bkgBtn.setOnTouchListener(this);

		outBtn.setOnTouchListener(this);
		outContent.setOnTouchListener(this);
		bkgBtn1.setOnTouchListener(this);
		myApp = (HbutApp) getApplicationContext();
		loginThread = getNewThread();
		prepareIDList = getExistID();

		if (allow.equalsIgnoreCase("true")) {
			permissionBtn.setVisibility(View.INVISIBLE);
			permissionBtn.setClickable(false);
		} else {
			enterBtn.setVisibility(View.INVISIBLE);
			enterContent.setVisibility(View.INVISIBLE);
			bkgBtn.setVisibility(View.INVISIBLE);
			enterBtn.setClickable(false);
			enterContent.setClickable(false);
			bkgBtn.setClickable(false);
		}
		if (prepareIDList != null) {
			idFld.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_dropdown_item,
					prepareIDList));
		}
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case CHECK:
					myApp.setClsDownloadEnd(false);
					if (pi == null) {
						Toast.makeText(LoginActivity.this, "帐号,密码错误!",
								Toast.LENGTH_SHORT).show();
						return;
					}
					if (pi.getPwd().equals(pwd)) {

						myApp.setPsi(pi);

						Bundle myBundle = new Bundle();
						myBundle.putBoolean("hasPiFile", hasPiFile);
						Intent intent = new Intent(LoginActivity.this,
								EnteringActivity.class);
						intent.putExtras(myBundle);
						startActivity(intent);

						Intent serviceIntent = new Intent(LoginActivity.this,
								ClsService.class);
						startService(serviceIntent);
					} else {
						Toast.makeText(LoginActivity.this, "密码错误!!!",
								Toast.LENGTH_SHORT).show();
						return;
					}
					break;
				case SERVERERROR:
					Toast.makeText(LoginActivity.this, "服务器连接失败",
							Toast.LENGTH_SHORT).show();
					break;
				case DATAERROR:
					Toast.makeText(LoginActivity.this, "你是我们学校的吗？",
							Toast.LENGTH_SHORT).show();
					break;
				case CONNECTERROR:
					Toast.makeText(LoginActivity.this, "无网络连接",
							Toast.LENGTH_SHORT).show();
					break;
				case DOUBLEERROR:
					Toast.makeText(LoginActivity.this, "劲酒虽好，可不要贪杯哦!",
							Toast.LENGTH_SHORT).show();
					break;
				case PERMISSIONNO:
					Toast.makeText(LoginActivity.this, "app暂时停止服务",
							Toast.LENGTH_SHORT).show();
					Toast.makeText(LoginActivity.this, "请到湖工大在线查看作者说明",
							Toast.LENGTH_LONG).show();
					Toast.makeText(LoginActivity.this, "恢复服务后请点击按钮更新app状态",
							Toast.LENGTH_LONG).show();
					break;
				case PERMISSIONCHECK:
					Toast.makeText(LoginActivity.this, "更新状态...",
							Toast.LENGTH_SHORT).show();
					break;
				case PERMISSIONYES:
					permissionBtn.setVisibility(View.INVISIBLE);
					permissionBtn.setClickable(false);

					enterBtn.setVisibility(View.VISIBLE);
					enterContent.setVisibility(View.VISIBLE);
					bkgBtn.setVisibility(View.VISIBLE);
					enterBtn.setClickable(true);
					enterContent.setClickable(true);
					bkgBtn.setClickable(true);
					break;
				case CHECKNOTIFY:
					Toast.makeText(LoginActivity.this, "验证中……",
							Toast.LENGTH_LONG).show();
					break;
					
				}

			}

		};

	}

	public void onEnterClicked(View enterBtn) {
		handler.sendMessage(handler.obtainMessage(CHECKNOTIFY));
		id = idFld.getText().toString();
		if (id == null || id.equals("")) {
			Toast.makeText(LoginActivity.this, "学号不能为空!!!", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (!PersonInf.checkID(id)) {
			Toast.makeText(LoginActivity.this, "学号格式不对额!!!", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		pwd = pwdFld.getText().toString();
		if (pwd == null || pwd.equals("")) {
			Toast.makeText(LoginActivity.this, "密码不能为空!!!", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		hasPiFile = hasFile(id);
		if (loginThread.getState() == Thread.State.RUNNABLE) {
			handler.sendMessage(handler.obtainMessage(DOUBLEERROR));
			return;
		}
		loginThread = getNewThread();
		isRunning = true;
		loginThread.start();

	}

	public Thread getNewThread() {
		return new Thread() {
			public void run() {
				while (isRunning) {
					if (hasPiFile) {
						try {
							Log.v("file", String.valueOf(hasPiFile));
							InputStream inputStream = openFileInput(id + ".xml");
							pi = XmlReader.pIXmlParser(inputStream);
							handler.sendMessage(handler.obtainMessage(CHECK));
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							handler.sendMessage(handler
									.obtainMessage(DATAERROR));
						}

					} else {

						if (hasNetWork() == false) {
							handler.sendMessage(handler
									.obtainMessage(CONNECTERROR));
							return;
						}
						String doc = getPsInfoFileByID(id);

						if (doc == null) {
							handler.sendMessage(handler
									.obtainMessage(SERVERERROR));
							return;
						}
						pi = HtmlParser.parserPsInfo(doc);
						if (pi == null) {
							handler.sendMessage(handler
									.obtainMessage(DATAERROR));
							return;
						} else {
							String sbjCountDoc = getSbjCountFileByID(id);
							int sbjCount = HtmlParser
									.parserSbjCount(sbjCountDoc);
							pi.setSbjCount(sbjCount);
							pi.setID(id);
							handler.sendMessage(handler.obtainMessage(CHECK));
						}
					}
					isRunning = false;
				}

			}

		};
	}

	public void onExitClicked(View exitBtn) {
		LoginActivity.this.finish();
	}

	public void onPermissionClicked(View pByn) {
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				try {

					handler.sendMessage(handler.obtainMessage(PERMISSIONCHECK));
					// download
					String versionFile = getVersionFile();
					Log.v("versionfile", versionFile);
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
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				} finally {
					if(remoteVi.getAllow().equalsIgnoreCase("false")){
						handler.sendMessage(handler.obtainMessage(PERMISSIONNO));
					}
					else{
						if(pkgi.versionName.compareToIgnoreCase(remoteVi.getVersion())>=0)//pi.versionName.compareToIgnoreCase(localVi.getVersion())>=0
							
							handler.sendMessage(handler.obtainMessage(PERMISSIONYES));
						else
							handler.sendMessage(handler.obtainMessage(PERMISSIONNO));
					}

				}

			}

		}.start();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (!allow.equalsIgnoreCase("true"))
			handler.sendMessage(handler.obtainMessage(PERMISSIONNO));
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		prepareIDList = getExistID();
		if (prepareIDList != null) {
			idFld.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_dropdown_item_1line, prepareIDList));
		}
	}

	public boolean hasFile(String id) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String path = getApplicationContext().getFilesDir()
					.getAbsolutePath();
			File file = new File(path + "/" + id + ".xml");
			if (file.exists())
				return true;
			else
				return false;
		} else
			return false;

	}

	class FileFilter implements FilenameFilter {
		private Pattern pattern;

		public FileFilter() {
			pattern = Pattern.compile("[a-zA-z0-9]{9,11}\\.xml");
		}

		@Override
		public boolean accept(File dir, String filename) {
			// TODO Auto-generated method stub
			return pattern.matcher(filename).matches();
		}

	}

	public String[] getExistID() {
		String[] fileList;
		String[] idList = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			String path = getApplicationContext().getFilesDir()
					.getAbsolutePath();
			File contextDir = new File(path);
			fileList = contextDir.list(new FileFilter());
			idList = new String[fileList.length];
			for (int i = 0; i < fileList.length; i++) {
				idList[i] = fileList[i].split("\\.")[0];
			}
			return idList;
		} else {
			return null;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int id = v.getId();
		if (id == R.id.enterBtn | id == R.id.enterContent | id == R.id.bkgBtn) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				enterBtn.setBackgroundResource(R.drawable.enter_glow_ic);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				enterBtn.setBackgroundResource(R.drawable.enter_ic);
			}
		}
		if (id == R.id.outBtn | id == R.id.outContent | id == R.id.bkgBtn1) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				outBtn.setBackgroundResource(R.drawable.back_glow_ic);
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				outBtn.setBackgroundResource(R.drawable.back_ic);
			}
		}
		return false;
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

}
