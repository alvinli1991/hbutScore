package com.hbut.alvin;

import java.io.BufferedReader;
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
import com.hbut.util.UriUtil;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/*
 * the father activity of all activities which have the 
 * download requirement
 * */
public class DownLoadActivity extends Activity {

	/*
	 * check if Internet is available
	 * */
	public boolean hasNetWork(){
		ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo net = conMgr.getActiveNetworkInfo();
		if(net == null)
			return false;
		else
			return true;
	}
	
	/*
	 * download the file contains the number of  courses that all your classmates choose
	 * @param id student id
	 * */
	public String getSbjCountFileByID(String id){
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 4000);
		HttpConnectionParams.setSoTimeout(httpParams, 3000);
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		String pwdUri = UriUtil.getRealUri(URIContainer.clsSbjCount, id);
		HttpGet httpget = new HttpGet(pwdUri);

		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpget);
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
			text = sb.toString();
			br.close();
			return text;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			httpget.abort();
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	/*
	 * download the file contains personal information 
	 * @param id student id
	 * */
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
			text = sb.toString();
			br.close();
			return text;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			httpget.abort();
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

	}

	/*
	 * download the file contains individual grades 
	 * @param id student id
	 * */
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
			text = sb.toString();
			br.close();
			return text;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			httpget.abort();
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

	}

	/*
	 * download the file contains class grades 
	 * @param clsName classname
	 * */	
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
			text = sb.toString();
			br.close();
			return text;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			httpget.abort();
			return null;
		} finally {
			httpClient.getConnectionManager().shutdown();
		}

	}
}
