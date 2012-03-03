package com.hbut.alvin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;




import com.hbut.util.HtmlParser;
import com.hbut.util.PersonInf;
import com.hbut.util.XmlReader;
import com.hbut.alvin.R;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends DownLoadActivity {

	PersonInf pi;
	String pwd;
	String id;
	EditText idFld;
	EditText pwdFld;
	final static int CHECK = 1;
	final static int SERVERERROR = 2;
	final static int DATAERROR = 3;
	HbutApp myApp;
	boolean hasPiFile = false;
	boolean hasClsFile = false;
	Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		idFld = (EditText) findViewById(R.id.idFld);
		pwdFld = (EditText) findViewById(R.id.pswFld);
		myApp = (HbutApp) getApplicationContext();

		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case CHECK:
					Toast.makeText(LoginActivity.this, "验证信息...",
							Toast.LENGTH_LONG).show();
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
					Toast.makeText(LoginActivity.this, "数据获取解析失败",
							Toast.LENGTH_SHORT);
					break;
				}

			}

		};
	}

	public void onEnterClicked(View enterBtn) {
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
		new Thread() {
			public void run() {

				if (hasPiFile) {
					try {
						Log.v("file", String.valueOf(hasPiFile));
						InputStream inputStream = openFileInput(id + ".xml");
						pi = XmlReader.pIXmlParser(inputStream);
						handler.sendMessage(handler.obtainMessage(CHECK));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						handler.sendMessage(handler.obtainMessage(DATAERROR));
					}

				} else {
					String doc = getPsInfoFileByID(id);
					if(doc == null){
						handler.sendMessage(handler.obtainMessage(SERVERERROR));
						return ;
					}
					pi = HtmlParser.parserPsInfo(doc);
					if (pi == null) {	
						handler.sendMessage(handler.obtainMessage(DATAERROR));
						return;
					} else {
						pi.setID(id);
						handler.sendMessage(handler.obtainMessage(CHECK));
					}
				}

			}

		}.start();

	}

	public void onExitClicked(View exitBtn) {
		LoginActivity.this.finish();
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

}
