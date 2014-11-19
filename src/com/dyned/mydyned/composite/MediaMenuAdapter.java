package com.dyned.mydyned.composite;

import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.MediaMenu;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.dyned.mydyned.utils.ImageUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MediaMenuAdapter extends BaseAdapter {

	private LayoutInflater li;
	private List<MediaMenu> list;
	private int width, padding;
	private DisplayImageOptions optionsAvatar;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public MediaMenuAdapter(Activity act, List<MediaMenu> list) {
		li = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		
		optionsAvatar = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_studyguide).showImageForEmptyUri(R.drawable.ic_studyguide)
				.cacheOnDisc().cacheInMemory().build();
		
		InputStream is = act.getResources().openRawResource(R.drawable.ic_studyguide);
		Bitmap bmp = BitmapFactory.decodeStream(is);
		
		padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, act.getResources().getDisplayMetrics());
		width = bmp.getWidth() + (2 * padding);
//		width = bmp.getWidth() * 3 / 4;
		bmp.recycle();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public MediaMenu getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setSelected(int position){
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setSelected(false);
		}
		list.get(position).setSelected(true);
		notifyDataSetChanged();
	}

	public static class ViewHolder {
		public String id;
		public ImageView image;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = li.inflate(R.layout.media_menu_item, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.imgMenu);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		MediaMenu item = list.get(position);
				
		holder.image.getLayoutParams().width = width;
		holder.image.getLayoutParams().height = width;
		holder.image.setPadding(padding, padding, padding, padding);
		
		imageLoader.displayImage(item.getIcon(), holder.image, optionsAvatar, new AnimateFirstDisplayListener(item.getIcon(), holder.image, false, 0, 0));
		
		if (item.isSelected()) {
//			holder.image.setBackgroundColor(0x88bb656b);
			Bitmap bmp = ImageUtil.invertImage(((BitmapDrawable)holder.image.getDrawable()).getBitmap());
			holder.image.setImageBitmap(bmp);
		} else {
//			holder.image.setBackgroundColor(0x00000000);
			imageLoader.displayImage(item.getIcon(), holder.image, optionsAvatar, new AnimateFirstDisplayListener(item.getIcon(), holder.image, false, 0, 0));
		}
		
		return convertView;
	}
}
