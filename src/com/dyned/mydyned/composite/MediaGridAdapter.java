package com.dyned.mydyned.composite;

import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.MediaItem;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.dyned.mydyned.utils.AppUtil;
import com.dyned.mydyned.utils.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MediaGridAdapter extends BaseAdapter {

	private Activity act;
	private LayoutInflater li;
	private List<MediaItem> list;
	private int width;
	private DisplayImageOptions optionsAvatar;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public MediaGridAdapter(Activity act, List<MediaItem> list) {
		li = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.act = act;
		
		optionsAvatar = new DisplayImageOptions.Builder().showStubImage(R.drawable.bg_transparent).showImageForEmptyUri(R.drawable.bg_transparent)
				.cacheOnDisc().build();
		
		int orientation = act.getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			width = ((AppUtil.GetScreenWidth(act)) * 7 / 8) / 4 ;
		} else {
			width = ((AppUtil.GetScreenWidth(act)) * 7 / 8) / 2 ;
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
		public RelativeLayout layout;
		public String id;
		public int type;
		public String url;
		ImageView image;
		ImageView thumbnail;
		ImageView imgPdf;
		public TextView name;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = li.inflate(R.layout.media_grid_item, null);
			holder = new ViewHolder();
			holder.thumbnail = (ImageView)convertView.findViewById(R.id.imgThumbnail);
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.layoutItem);
			holder.name = (TextView) convertView.findViewById(R.id.txtItem);
			holder.image = (ImageView) convertView.findViewById(R.id.imgItem);
			holder.imgPdf = (ImageView) convertView.findViewById(R.id.imgPdf);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		MediaItem item = list.get(position);
		String name = item.getTitle();
		
		holder.id = item.getId();
		holder.type = item.getDocumentType();
		holder.url = item.getUrl();
		holder.position = position;
		holder.layout.setBackgroundColor(getRandomColor());
		holder.name.setText(name);
		holder.image.setImageResource(ImageUtil.getDrawableResourceId(act, item.getImageRes()));
//		holder.image.getLayoutParams().height = width;
		
		holder.thumbnail.getLayoutParams().width = width;
		holder.thumbnail.getLayoutParams().height = width * 4 / 5;
		imageLoader.displayImage(item.getThumbnail(), holder.thumbnail, optionsAvatar, new AnimateFirstDisplayListener(item.getThumbnail(), holder.thumbnail, false, 0, 0));
		
		if (holder.type != 0) {
			if (holder.type == MediaItem.FILE_PDF) {
				holder.imgPdf.setVisibility(View.VISIBLE);
				holder.image.setVisibility(View.GONE);
			} else {
				holder.imgPdf.setVisibility(View.GONE);
				holder.image.setVisibility(View.VISIBLE);
			}
		}
		
		return convertView;
	}
	
	private int getRandomColor(){
		Random color = new Random();
		return Color.rgb(color.nextInt(256),color.nextInt(256),color.nextInt(256));
	}
}
