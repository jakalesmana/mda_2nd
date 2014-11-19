package com.dyned.mydyned.composite;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.News;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.dyned.mydyned.utils.AppUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsAdapter extends BaseAdapter {

	private LayoutInflater li;
	private List<?> list;
	private DisplayImageOptions optionsAvatar;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private int width, height;
	private Activity act;
	
	public NewsAdapter(Activity act, List<?> list) {
		li = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.act = act;
		optionsAvatar = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_grey).showImageForEmptyUri(R.drawable.bg_grey)
				.cacheOnDisc().cacheInMemory().build();
		
		if (act.getResources().getBoolean(R.bool.isTablet)) {
			handleTablet();
		} else {
			handlePhone();
		}
	}

	private void handlePhone() {
		int orientation = act.getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			width = (AppUtil.GetScreenWidth(act) / 3 ) - 30;
			height = width;
		} else {
			width = (AppUtil.GetScreenWidth(act) / 2 ) - 20;
			height = width * 4 / 3;
		}
	}

	private void handleTablet() {
		int orientation = act.getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			width = (AppUtil.GetScreenWidth(act) / 4 ) - 40;
			height = width;
		} else {
			width = (AppUtil.GetScreenWidth(act) / 3 ) - 30;
			height = width * 4 / 3;
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public String id;
		ImageView image;
		TextView name;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = li.inflate(R.layout.news_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.image = (ImageView) convertView.findViewById(R.id.imgContent);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		News news = ((News)list.get(position));
		holder.id = news.getId();
		
		String name = news.getTitle();
		
		holder.position = position;
		holder.name.setText(name);
		
		holder.image.getLayoutParams().width = width;
		holder.image.getLayoutParams().height = height;
		holder.name.getLayoutParams().width = width;
		imageLoader.displayImage(news.getImage(), holder.image, optionsAvatar, new AnimateFirstDisplayListener(news.getImage(), holder.image, false, 0, 0));
		
		return convertView;
	}
}
