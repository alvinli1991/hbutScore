package com.hbut.alvin;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
import com.hbut.util.UriUtil;
import com.hbut.alvin.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	PersonInf pi;
	String pwd;
	String id;
	EditText idFld;
	EditText pwdFld;
	final static int CHECK = 1;
	final static int SERVERERROR = 2;
	final static int DATAERROR = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		idFld = (EditText) findViewById(R.id.idFld);
		pwdFld = (EditText) findViewById(R.id.pswFld);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what)
			{
			case CHECK:
				Toast.makeText(LoginActivity.this, "验证信息...",
						Toast.LENGTH_LONG).show();
				if(pi==null)
				{
					Toast.makeText(LoginActivity.this, "帐号,密码错误!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (pi.getPwd().equals(pwd)) {
					Bundle mBundle = new Bundle();
					mBundle.putString("name", pi.getName());
					mBundle.putString("pwd", pi.getPwd());
					mBundle.putString("cls", pi.getCls());
					mBundle.putString("id", pi.getID());
					Intent intent = new Intent(LoginActivity.this,
							EnteringActivity.class);
					intent.putExtras(mBundle);
					startActivity(intent);
					//finish();
				} else {
					Toast.makeText(LoginActivity.this, "密码错误!!!",
							Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case SERVERERROR:
				Toast.makeText(LoginActivity.this, "服务器连接失败", Toast.LENGTH_SHORT)
				.show();
				break;
			case DATAERROR:
				Toast.makeText(LoginActivity.this, "数据解析失败", Toast.LENGTH_SHORT);
				break;
			}
			
		}

	};

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
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				pi = HtmlParser.parserPsInfo(getPsInfoFileByID(id));
				if(pi==null){
					handler.sendMessage(handler.obtainMessage(DATAERROR));
					return;
				}
					
				pi.setID(id);
				handler.sendMessage(handler.obtainMessage(CHECK));
			}
			
		}.start();
		
		


	}

	public void onExitClicked(View exitBtn) {
		LoginActivity.this.finish();
	}

	public String getPsInfoFileByID(String ID) {

		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
		HttpConnectionParams.setSoTimeout(httpParams, 3000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		String pwdUri = UriUtil.getRealUri(URIContainer.personInf, ID);

		HttpGet httpget = new HttpGet(pwdUri);

		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			
			InputStreamReader isr = null;
			BufferedReader br= null;
			StringBuffer sb = new StringBuffer();
			if (entity != null) {
				InputStream instream = entity.getContent();
				isr = new InputStreamReader(instream,"GBK");
				br = new BufferedReader(isr);
				String temp = null;
				while((temp=br.readLine())!= null)
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

}
