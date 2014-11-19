package com.dyned.mydyned.tools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageDownloader extends AsyncTask<Void, Integer, Void> {

	private String url;
	private int progress;
	private Bitmap bmp;
	private ImageLoaderListener listener;

	public ImageDownloader(String url, Context c, ImageLoaderListener listener) {
		this.url = url;
		this.listener = listener;
	}

	public interface ImageLoaderListener {
		void onDownloadStart();
		void onImageDownloaded(Bitmap bmp);
		void onDownloadError();
	}

	@Override
	protected void onPreExecute() {
		if (listener != null) {
			listener.onDownloadStart();
		}
		progress = 0;
		super.onPreExecute();
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		bmp = getBitmapFromURL(url);
		while (progress < 100) {
			progress += 1;
			publishProgress(progress);
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(Void result) {

		if (listener != null) {
			listener.onImageDownloaded(bmp);
		}

		super.onPostExecute(result);
	}

	private Bitmap getBitmapFromURL(String link) {
		try {
			URL url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);

			return myBitmap;

		} catch (IOException e) {
			if (listener != null) {
				listener.onDownloadError();
			}
			e.printStackTrace();
			return null;
		}
	}

}
