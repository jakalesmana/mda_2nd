package com.dyned.mydyned;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.dyned.mydyned.composite.NewsAdapter;
import com.dyned.mydyned.composite.NewsAdapter.ViewHolder;
import com.dyned.mydyned.constant.URLAddress;
import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.model.News;
import com.dyned.mydyned.tools.InternetConnectionListener;
import com.dyned.mydyned.tools.PostInternetTask;

/**
author: jakalesmana
 */

public class NewsActivity extends BaseActivity {

	private Handler handler = new Handler();
	private ProgressDialog dialog;
	private NewsAdapter adapter;
	private GridView gvNews;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		initView();
	}
	
	@SuppressWarnings("unchecked")
	private void initView() {
		setContentView(R.layout.activity_news);
		setTitle(HomeFragment.NEWS);
		List<News> listNews = (ArrayList<News>)getIntent().getSerializableExtra("list");
		gvNews = (GridView)findViewById(R.id.gvNews);
		adapter = new NewsAdapter(this, listNews);
		gvNews.setAdapter(adapter);
		
		gvNews.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				ViewHolder holder = ((ViewHolder)view.getTag());
				retrieveNewsDetail(holder.id);
			}
		});
	}

	private void retrieveNewsDetail(String id) {
		PostInternetTask postTask = new PostInternetTask(this, new InternetConnectionListener() {
			public void onStart() {
				handler.post(new Runnable() {
					public void run() {
				    	dialog = ProgressDialog.show(NewsActivity.this, "", "Loading..");				
					}
				});
			}
			
			public void onDone(String str) {
				try {
					System.out.println("response: " + str);
					News news = News.ParseNewsDetail(str);
					Intent i = new Intent(NewsActivity.this, NewsDetailActivity.class);
					i.putExtra("news", news);
					startActivity(i);
					dialog.dismiss();
				} catch (Exception e) {
				}
			}

			@Override
			public void onConnectionError(String message) {
				dialog.dismiss();
				Toast.makeText(NewsActivity.this, message + ", try again later.", Toast.LENGTH_SHORT).show();
			}
		});
		postTask.addPair("id", id);
		postTask.execute(URLAddress.NEWS_DETAIL_URL);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initView();
	}

}
