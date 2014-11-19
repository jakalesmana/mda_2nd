package com.dyned.mydyned.composite;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.model.App;
import com.dyned.mydyned.utils.AnimateFirstDisplayListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class AppAdapter extends BaseAdapter {

	private LayoutInflater li;
	private List<App> list;
	private Context ctx;
	
	private DisplayImageOptions optionsIcon = new DisplayImageOptions.Builder()
	.showStubImage(R.drawable.ic_launch_dyned)
	.showImageForEmptyUri(R.drawable.ic_launch_dyned).cacheOnDisc().build();
	
	public AppAdapter(Context context, List<App> list) {
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.ctx = context;
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
		ImageView imgIcon;
		TextView title;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = li.inflate(R.layout.dialog_app_picker_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.txtTitle);
			holder.imgIcon = (ImageView) convertView.findViewById(R.id.imgApp);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		App app = list.get(position);
		
		holder.title.setText(app.getAppName());
		
		if (app.getDrawableIcon() != null) {
			holder.imgIcon.setImageDrawable(app.getDrawableIcon());
		} else {
//			int resID = ctx.getResources().getIdentifier(app.getAppIcon(), "drawable", ctx.getPackageName());
//			holder.imgIcon.setImageResource(resID);
			
			Bitmap bmp = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.ic_launch_dyned);
			holder.imgIcon.getLayoutParams().width = bmp.getWidth();
			holder.imgIcon.getLayoutParams().height = bmp.getWidth();
			ImageLoader imageLoader = ImageLoader.getInstance();
			imageLoader.displayImage(app.getAppIcon(), holder.imgIcon,
			optionsIcon, new AnimateFirstDisplayListener(app.getAppIcon(),holder.imgIcon, false, 0, 0));
		}
		
		return convertView;
	}
}
