package com.dyned.mydyned.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.util.Log;

import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.utils.AppUtil;

public class PostInternetTask extends InternetTask {

	private ArrayList<NameValuePair> pairs;
	private Context context;
	
	public PostInternetTask(Context context, InternetConnectionListener listener) {
		super(context, listener);
		this.context = context;
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
		String url = strings[0];
		if (url.startsWith("http")) {
			return handleHttp(url);
		} else if(url.startsWith("https")){
			return handleHttps(url);
		}
		return "";
	}
	
	private String handleHttps(String url){
		String str = new String();

		try {
			HttpParams httpParameters = new BasicHttpParams();

			HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
			
			HttpClient httpclient = new DefaultHttpClient();
			
			SchemeRegistry registry = new SchemeRegistry();
			SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
			socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
			registry.register(new Scheme("https", socketFactory, 443));
			SingleClientConnManager mgr = new SingleClientConnManager(httpclient.getParams(), registry);
			DefaultHttpClient httpClient = new DefaultHttpClient(mgr, httpclient.getParams());

			// Set verifier     
			HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
			HttpPost httppost = new HttpPost(url);
			
			if(null != pairs)httppost.setEntity(new UrlEncodedFormEntity(pairs));
			
			int timeoutConnection = 120000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutConnection);
			httppost.setParams(httpParameters);
			httppost.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httppost.addHeader("X-API-KEY", URLAddress.API_KEY);
			httppost.addHeader("X-APP-VERSION ", AppUtil.getVersionName(context));
			httppost.addHeader("X-APP-NAME ", AppUtil.getApplicationName(context));
			httppost.addHeader("X-DEVICE-MODEL", AppUtil.getDeviceName());
			httppost.addHeader("X-DEVICE-OS", AppUtil.getOsversion());
			
			HttpResponse response = httpClient.execute(httppost);

			InputStream is = response.getEntity().getContent();
//			int length = (int) response.getEntity().getContentLength();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
			StringBuilder sb = new StringBuilder();
	        sb.append(reader.readLine() + "\n");
	        String line="0";
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        str = sb.toString().trim();
		        
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("Internet Task", "string response: " + str);
		return str;
	}
	
	private String handleHttp(String url){
		String str = new String();

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(url);
			
			if(null != pairs)httppost.setEntity(new UrlEncodedFormEntity(pairs));
			
			int timeoutConnection = 120000;
			httppost.addHeader("Content-Type",
					"application/x-www-form-urlencoded");
			httppost.addHeader("X-API-KEY", URLAddress.API_KEY);
			httppost.addHeader("X-APP-VERSION ", AppUtil.getVersionName(context));
			httppost.addHeader("X-APP-NAME ", AppUtil.getApplicationName(context));
			httppost.addHeader("X-DEVICE-MODEL", AppUtil.getDeviceName());
			httppost.addHeader("X-DEVICE-OS", AppUtil.getOsversion());
			
			HttpResponse response = httpclient.execute(httppost);

			InputStream is = response.getEntity().getContent();
//			int length = (int) response.getEntity().getContentLength();
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
			StringBuilder sb = new StringBuilder();
	        sb.append(reader.readLine() + "\n");
	        String line="0";
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        str = sb.toString().trim();
		        
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.d("Internet Task", "string response: " + str);
		return str;
	}
	
	private HttpClient createHttpClient()
	{
	    HttpParams params = new BasicHttpParams();
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
	    HttpProtocolParams.setUseExpectContinue(params, true);

	    SchemeRegistry schReg = new SchemeRegistry();
	    	    
	    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
	    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

	    return new DefaultHttpClient(conMgr, params);
	}
	
//	private String stringEncoder(ArrayList<NameValuePair> pairs) {
//		String str = "";
//		try {
//			for (int i = 0; i < pairs.size(); i++) {
//				BasicNameValuePair pair = (BasicNameValuePair) pairs.get(i);
//				String key = pair.getName();
//				String value = pair.getValue();
//				if (i > 0) {
//					str += "&";
//				}
//				str += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return str;
//	}
}