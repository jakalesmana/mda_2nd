package com.dyned.mydyned.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import android.content.Context;
import android.os.AsyncTask;

import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.utils.AppUtil;
import com.dyned.mydyned.utils.PreferencesUtil;

public class FileUploaderTask extends AsyncTask<String, Integer, String> {

	private ArrayList<NameValuePair> pairs;
	
	protected PreferencesUtil userPref;
	private InternetConnectionListener listener;
	private File file;
	private Context context;
	
	public FileUploaderTask(Context context, InternetConnectionListener listener, File file) {
		this.listener = listener;
		this.file = file;
		this.context = context;
		userPref = PreferencesUtil.getInstance(context);
	}

	@Override
	protected void onPreExecute() {
		listener.onStart();
		super.onPreExecute();
	}
	
	public void addPair(String key, String value){
		if (pairs == null) {
			pairs = new ArrayList<NameValuePair>();
		}
		if (value!=null) {
			pairs.add(new BasicNameValuePair(key, value));
		}		
	}
	
	@Override
	protected String doInBackground(String... strings) {
		String str = new String();
		try {			
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpPost postRequest = new HttpPost(strings[0]);
			if(null != pairs)postRequest.setEntity(new UrlEncodedFormEntity(pairs));
			
			postRequest.addHeader("X-API-KEY", URLAddress.API_KEY);
			postRequest.addHeader("X-APP-VERSION ", AppUtil.getVersionName(context));
			postRequest.addHeader("X-APP-NAME ", AppUtil.getApplicationName(context));
			postRequest.addHeader("X-DEVICE-MODEL", AppUtil.getDeviceName());
			postRequest.addHeader("X-DEVICE-OS", AppUtil.getOsversion());
			
			postRequest.addHeader("X-APP-KEYS", PreferencesUtil.getInstance(context).getAppKey());
			
			MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			ContentBody cbFile = new FileBody(file, "image/jpeg");
			reqEntity.addPart("userfile", cbFile);
			postRequest.setEntity(reqEntity);
			HttpResponse response = httpClient.execute(postRequest);
			BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
			String sResponse;
			StringBuilder s = new StringBuilder();

			while ((sResponse = reader.readLine()) != null) {
				s = s.append(sResponse);
			}
			str = s.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		listener.onDone(result);
		super.onPostExecute(result);
	}

}
