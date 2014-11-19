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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyned.mydyned.R;
import com.dyned.mydyned.fragment.HomeFragment;
import com.dyned.mydyned.model.HomeItem;
import com.dyned.mydyned.utils.AppUtil;
import com.dyned.mydyned.utils.ImageUtil;

public class HomeGridAdapter extends BaseAdapter {

	private Activity act;
	private LayoutInflater li;
	private List<HomeItem> list;
	private int width;
	
	public HomeGridAdapter(Activity act, List<HomeItem> list) {
		li = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list = list;
		this.act = act;
		int orientation = act.getResources().getConfiguration().orientation;
		if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
			width = (AppUtil.GetScreenWidth(act) / 4 ) - 10;
		} else {
			width = (AppUtil.GetScreenWidth(act) / 3 ) - 10;
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
		public String url;
		ImageView image, bg;
		public TextView name;
		int position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = li.inflate(R.layout.home_grid_item, null);
			holder = new ViewHolder();
			holder.layout = (RelativeLayout)convertView.findViewById(R.id.layoutItem);
			holder.name = (TextView) convertView.findViewById(R.id.txtItem);
			holder.image = (ImageView) convertView.findViewById(R.id.imgItem);
			holder.bg = (ImageView) convertView.findViewById(R.id.imgBg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		HomeItem item = list.get(position);
		String name = item.getTitle();
		
		holder.url = item.getUrl();
		holder.position = position;
		holder.layout.setBackgroundColor(item.getColor());
		holder.name.setText(name);
		holder.image.setImageResource(ImageUtil.getDrawableResourceId(act, item.getImageRes()));
		holder.image.getLayoutParams().height = width;
		holder.bg.getLayoutParams().height = width;
		
		if (item.getTitle().compareTo(HomeFragment.SOCIAL_MEDIA) == 0) {
			holder.bg.setImageResource(R.drawable.bg_social);
			holder.bg.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.GONE);
		} else if (item.getTitle().compareTo(HomeFragment.NEWS) == 0) {
			holder.bg.setImageResource(R.drawable.bg_news);
			holder.bg.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.GONE);
		} else if (item.getTitle().compareTo(HomeFragment.LAUNCH_DYNED) == 0) {
			holder.bg.setImageResource(R.drawable.bg_grey_diagonal);
			holder.bg.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.VISIBLE);
		} else {
			holder.image.setVisibility(View.VISIBLE);
			holder.bg.setVisibility(View.GONE);
		}
		
		return convertView;
	}
}
