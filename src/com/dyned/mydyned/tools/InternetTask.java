package com.dyned.mydyned.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import android.content.Context;
import android.os.AsyncTask;

import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.utils.AppUtil;

public class InternetTask extends AsyncTask<String, Integer, String> {

	public static final String ERROR_INTERNAL = "Server error";
	public static final String ERROR_TIMEOUT = "Connection timeout";
	public static final String ERROR_CONNECT = "Error connecting to internet";
	
	private InternetConnectionListener listener;
	private Context context;
	
	public InternetTask(Context context, InternetConnectionListener listener) {
		this.listener = listener;
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		listener.onStart();
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(String... strings) {
		String str = new String();
		try {
			URL url = new URL(strings[0]);
			
			System.out.println("start access: " + url);
			
			if (strings[0].startsWith("https")) {
				HostnameVerifier hostnameVerifier = new HostnameVerifier() {
				    public boolean verify(String hostname, SSLSession session) {
//				        HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
				        return true;
				    }
				};
				
				HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
				connection.setHostnameVerifier(hostnameVerifier);
//			    // Create the SSL connection
//				SSLContext sc = null;
//				try {
//					sc = SSLContext.getInstance("TLS");
//				    sc.init(null, null, new java.security.SecureRandom());
//				} catch (Exception e) {
//				}
//			    connection.setSSLSocketFactory(sc.getSocketFactory());
			    
//				connection.setRequestProperty("User-Agent",
//		                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
				
				connection.setRequestProperty("X-API-KEY", URLAddress.API_KEY);
				connection.setRequestProperty("X-APP-VERSION ", AppUtil.getVersionName(context));
				connection.setRequestProperty("X-APP-NAME ", AppUtil.getApplicationName(context));
				connection.setRequestProperty("X-DEVICE-MODEL", AppUtil.getDeviceName());
				connection.setRequestProperty("X-DEVICE-OS", AppUtil.getOsversion());
				
				connection.setConnectTimeout(20000);
				connection.setDoInput(true);
				connection.connect();
				int responseCode = connection.getResponseCode();
				System.out.println("response code: " + responseCode);
				switch (responseCode) {
				case HttpURLConnection.HTTP_OK:
					InputStream inputStream = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
					StringBuilder strBuilder = new StringBuilder();
					
					String line = null;
					while ((line = reader.readLine()) != null) {
						strBuilder.append(line + "\n");
					}

					reader.close();
					inputStream.close();

					str = strBuilder.toString();				
					break;

				case HttpURLConnection.HTTP_INTERNAL_ERROR:
					str = "error:" + ERROR_INTERNAL;
					break;
					
				case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
					str = "error:" + ERROR_TIMEOUT;
					break;
				}
			} else {
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();

//				connection.setRequestProperty("User-Agent",
//		                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.11 (KHTML, like Gecko) Chrome/17.0.963.56 Safari/535.11");
				
				connection.setRequestProperty("X-API-KEY", URLAddress.API_KEY);
				connection.setRequestProperty("X-APP-VERSION ", AppUtil.getVersionName(context));
				connection.setRequestProperty("X-APP-NAME ", AppUtil.getApplicationName(context));
				connection.setRequestProperty("X-DEVICE-MODEL", AppUtil.getDeviceName());
				connection.setRequestProperty("X-DEVICE-OS", AppUtil.getOsversion());
				
				connection.setConnectTimeout(15000);
				connection.setDoInput(true);
				connection.connect();
				int responseCode = connection.getResponseCode();
				System.out.println("response code: " + responseCode);
				switch (responseCode) {
				case HttpURLConnection.HTTP_OK:
					InputStream inputStream = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
					StringBuilder strBuilder = new StringBuilder();
					
					String line = null;
					while ((line = reader.readLine()) != null) {
						strBuilder.append(line + "\n");
					}

					reader.close();
					inputStream.close();

					str = strBuilder.toString();				
					break;

				case HttpURLConnection.HTTP_INTERNAL_ERROR:
					str = "error:" + ERROR_INTERNAL;
					break;
					
				case HttpURLConnection.HTTP_GATEWAY_TIMEOUT:
					str = "error:" + ERROR_TIMEOUT;
					break;
				}
			}
		} catch (IOException e) {
			str = "error:" + ERROR_CONNECT;
		} 
		return str;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		if(result.startsWith("error")){
			String errMsg = result.split(":")[1];
			listener.onConnectionError(errMsg);
		} else {
			System.out.println("internet access: " + result);
			listener.onDone(result);
		}
		super.onPostExecute(result);
	}
}